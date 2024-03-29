import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PHPF extends Statistics
{    
    @Override
    public Queue<Process> schedule(PriorityQueue<Process> q) 
    {
        int finishTime = 0;
        int startTime;
        Process p;
        Process scheduled;
        Process remaining;
        Statistics.Stats stats = this.getStats();
        Queue<Process> scheduledQueue = new LinkedList<>();
        
        // Need to keep track of these to calculate turnaround and wait times
        Map<Character, Integer> startTimes = new HashMap<>();
        Map<Character, Integer> finishTimes = new HashMap<>();
        
        // Queue processes that are ready to run, and order by shortest run time
        // break ties with arrival time so they are readied in the correct order
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(10, 
            new Comparator()
            {
                @Override
                public int compare(Object o1, Object o2) 
                {
                    Process p1 = (Process) o1;
                    Process p2 = (Process) o2;
                    if (p1.getPriority() == p2.getPriority())
                        return p1.getArrivalTime() < p2.getArrivalTime() ? -1 : 1;
                    else
                        return p1.getPriority() < p2.getPriority() ? -1 : 1;
                }            
            });
        
        // Queue processes that are waiting to run by priority ONLY so that they
        // are switched correctly in round robin
        PriorityQueue<Process> waitingQueue = new PriorityQueue<>(10, 
            new Comparator()
            {
                @Override
                public int compare(Object o1, Object o2) 
                {
                    Process p1 = (Process) o1;
                    Process p2 = (Process) o2;
                    return p1.getPriority() < p2.getPriority() ? -1 : 1;
                }            
            });
        
        while (!q.isEmpty() || !readyQueue.isEmpty() || !waitingQueue.isEmpty())
        {
            // add processes that have arrived by now to the ready queue
            while (!q.isEmpty() && q.peek().getArrivalTime() <= finishTime)
                readyQueue.add(q.poll());
            
            // Get the process with the highest priority that can start now
            // Order readyQueue > waitingQueue > q to ensure round robin fairness     
            if (readyQueue.isEmpty())
                p = (waitingQueue.isEmpty()) ? q.poll() : waitingQueue.poll();
            else if (waitingQueue.isEmpty())            
                p = readyQueue.poll();
            else
                p = (readyQueue.peek().getPriority() <= waitingQueue.peek().getPriority())
                  ? readyQueue.poll()
                  : waitingQueue.poll();
            
            // Assign p one time slice for now
            startTime = Math.max((int) Math.ceil(p.getArrivalTime()), finishTime);
            finishTime = startTime + 1;
            
            // Record some stats if we haven't seen this process before
            if (!startTimes.containsKey(p.getName()))
            {
                if (startTime > 100)
                    break;
                startTimes.put(p.getName(), startTime);
                stats.addWaitTime(startTime - p.getArrivalTime());
                stats.addResponseTime(startTime - p.getArrivalTime() + 1);
            }
            else // add the wait time this process was in waitingQueue
                stats.addWaitTime(startTime - finishTimes.get(p.getName()));
            
            // split p into a second process with n-1 time slices and add to waiting queue
            if (p.getBurstTime() > 1)
            {
                try 
                {
                    remaining = (Process) p.clone();
                    remaining.setBurstTime(remaining.getBurstTime() - 1);
                    waitingQueue.add(remaining);
                    finishTimes.put(remaining.getName(), finishTime);
                } 
                catch (CloneNotSupportedException ex) 
                {
                    ex.printStackTrace();
                }
            }
            else // this process finished so record turnaround time
            {
                stats.addTurnaroundTime(finishTime - startTimes.get(p.getName()));
                stats.addProcess();
            }            
            // Create a new process with the calculated start time and add to a new queue
            scheduled = new Process();
            scheduled.setBurstTime(1);
            scheduled.setStartTime(startTime);
            scheduled.setName(p.getName());
            scheduledQueue.add(scheduled);            
        }        
        stats.addQuanta(finishTime); // Add the total quanta to finish all jobs
        printChart(scheduledQueue);
        printAvgStats();
        
        return scheduledQueue;
    }
}
