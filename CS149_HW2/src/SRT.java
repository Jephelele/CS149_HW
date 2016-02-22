import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SRT extends Statistics
{
	@Override
    public Queue<Process> schedule(PriorityQueue<Process> q) 
    {
        int finishTime = 0;
        int startTime;
        Process process;
        Process queued;
        Process remaining;
        Statistics.Stats stats = this.getStats();
        Queue<Process> Queue = new LinkedList<>();
        
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
                    if (p1.getBurstTime() == p2.getBurstTime())
                        return p1.getArrivalTime() <= p2.getArrivalTime() ? -1 : 1;
                    else
                        return p1.getBurstTime() < p2.getBurstTime() ? -1 : 1;
                }            
            });
        
        // Queue processes that are waiting to run by priority and remaining time
        PriorityQueue<Process> waitingQueue = new PriorityQueue<>(10, 
            new Comparator()
            {
                @Override
                public int compare(Object o1, Object o2) 
                {
                    Process p1 = (Process) o1;
                    Process p2 = (Process) o2;
                    if (p1.getBurstTime() == p2.getBurstTime())
                        return p1.getArrivalTime() <= p2.getArrivalTime() ? -1 : 1;
                    else
                        return p1.getBurstTime() < p2.getBurstTime() ? -1 : 1;
                }            
            });
        
        while (!q.isEmpty() || !readyQueue.isEmpty() || !waitingQueue.isEmpty())
        {
            // add processes that have arrived by now to the ready queue
            while (!q.isEmpty() && q.peek().getArrivalTime() <= finishTime)
                readyQueue.add(q.poll());
            
            // Get the process with the shortest remaining time that can start now
            // Break ties Waiting > Ready > Q to prioritize already running process 
            if (readyQueue.isEmpty())
                process = (waitingQueue.isEmpty()) ? q.poll() : waitingQueue.poll();
            else if (waitingQueue.isEmpty())
                process = readyQueue.poll();
            else
                process = (readyQueue.peek().getBurstTime() < waitingQueue.peek().getBurstTime()) 
                  ? readyQueue.poll()
                  : waitingQueue.poll();
            
            // Assign p one time slice for now
            startTime = Math.max((int) Math.ceil(process.getArrivalTime()), finishTime);
            finishTime = startTime + 1;
            
            // Record some stats if we haven't seen this process before
            if (!startTimes.containsKey(process.getName()))
            {
                if (startTime > 100)
                    break;
                startTimes.put(process.getName(), startTime);
                stats.addWaitTime(startTime - process.getArrivalTime());
                stats.addResponseTime(startTime - process.getArrivalTime() + 1);
            }
            else // add the wait time this process was in waitingQueue
                stats.addWaitTime(startTime - finishTimes.get(process.getName()));
            
            // split p into a second process with n-1 time slices and add to waiting queue
            if (process.getBurstTime() > 1)
            {
                try 
                {
                    remaining = (Process) process.clone();
                    remaining.setBurstTime(remaining.getBurstTime() - 1);
                    waitingQueue.add(remaining);
                    finishTimes.put(remaining.getName(), finishTime);
                } 
                catch (CloneNotSupportedException ex) 
                {
                    ex.getStackTrace();
                }
            }
            else // this process finished so record turnaround time
            {
                stats.addTurnaroundTime(finishTime - startTimes.get(process.getName()));
                stats.addProcess();
            }            
            // Create a new process with the calculated start time and add to a new queue
            queued = new Process();
            queued.setBurstTime(1);
            queued.setStartTime(startTime);
            queued.setName(process.getName());
            Queue.add(queued);            
        }        
        stats.addQuanta(finishTime); // Add the total quanta to finish all jobs
        printChart(Queue);
        printAvgStats();
        
        return Queue;
    }
}
