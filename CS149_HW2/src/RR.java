import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class RR extends Statistics 
{    
    @Override
    public Queue<Process> schedule(PriorityQueue<Process> queue) 
    {
        int startTime, finishTime = 0;
        Process p, scheduled, remaining;
        Statistics.Stats stats = this.getStats();
           
        Map<Character, Integer> startTimes = new HashMap<>();        
        Map<Character, Integer> finishTimes = new HashMap<>();
        
        Queue<Process> readyQueue = new LinkedList<>();        
        Queue<Process> waitingQueue = new LinkedList<>();
        Queue<Process> scheduledQueue = new LinkedList<>();     
        
        while (!queue.isEmpty() || !readyQueue.isEmpty() || !waitingQueue.isEmpty())
        {
            // add processes that have arrived by now to the ready queue
            while (!queue.isEmpty() && queue.peek().getArrivalTime() <= finishTime)
                readyQueue.add(queue.poll());
            
            // choose which process to schedule next - Ready > Q > Waiting
            if (!readyQueue.isEmpty())
                p = readyQueue.poll();
            else if (!queue.isEmpty() && waitingQueue.isEmpty())
                p = queue.poll();
            else
                p = waitingQueue.poll(); 
            
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
