import java.util.Queue;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class FCFS extends Statistics
{
    @Override
    public Queue<Process> schedule(PriorityQueue<Process> queue) 
    {
        int startTime = 0; // the current time slice
        int numProcess = queue.size();
        int endTime = 0;
        Process process;
        Process scheduled;
        Stats stats = this.getStats();
        Queue<Process> scheduledQueue = new LinkedList<>();
        
        for (int i = 0; i < numProcess; i++)
        {
            process = queue.poll(); 
            //startTime always starts after previous endTime
            startTime = Math.max((int) Math.ceil(process.getArrivalTime()), endTime);            
            endTime = (int) (startTime + process.getBurstTime());
            //Pass 100 time slices
            if (startTime > 100) 
                break;
            
            //Update Statistics with Current Process
            stats.addWaitTime(startTime - process.getArrivalTime());
            stats.addTurnaroundTime(endTime - process.getArrivalTime());
            stats.addResponseTime(endTime - startTime);
            stats.addProcess();                      

            scheduled = new Process();
            scheduled.setBurstTime(process.getBurstTime());
            scheduled.setStartTime(startTime);
            scheduled.setName(process.getName());
            scheduledQueue.add(scheduled);            
        }
        stats.addQuanta(endTime); // Add the total quanta to finish all jobs
        printChart(scheduledQueue);
        printAvgStats();
        
        return scheduledQueue;
    }
}
