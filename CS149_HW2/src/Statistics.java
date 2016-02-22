import java.util.PriorityQueue;
import java.util.Queue;

public abstract class Statistics {
	
	    private Stats stats = new Stats();
	    
	    /* Keep track of various time and throughput statistics */ 
	    public class Stats 
	    {
	        private int turnaroundTime;
	        private int waitingTime;
	        private int responseTime;
	        private int numProcess;
	        private int quanta;
	        
	        public double getAvgTurnaroundTime()
	        {
	            return turnaroundTime / (double) numProcess;
	        }
	        
	        public double getAvgWaitingTime()
	        {
	            return waitingTime / (double) numProcess;
	        }
	        
	        public double getAvgResponseTime()
	        {
	            return responseTime / (double) numProcess;
	        }
	       
	        public double getAvgThroughput()
	        {
	            return 100 * numProcess / (double) quanta;
	        }
	        
	        public void addWaitTime(double time)
	        {
	            waitingTime += time;
	        }
	        
	        public void addResponseTime(double time)
	        {
	            responseTime += time;
	            }
	        
	        public void addTurnaroundTime(double time)
	        {
	            turnaroundTime += time;
	        }
	        
	        public void addProcess()
	        {
	            numProcess++;
	        }
	        
	        public void addQuanta(int quantaCount)
	        {
	            quanta += quantaCount;
	        }
	        
	    }
	    
	    public Stats getStats() { return this.stats; }
	    
	    /**
	     * Print out the average statistics for the given scheduling algorithm
	     */
	    public void printAvgStats()
	    {
	        System.out.format("Turnaround Time: %f\n", stats.getAvgTurnaroundTime());
	        System.out.format("Waiting Time: %f\n", stats.getAvgWaitingTime());
	        System.out.format("Response Time: %f\n", stats.getAvgResponseTime());
	        System.out.format("Throughput: %f\n", stats.getAvgThroughput());
	    }
	    
	    /**
	     * Print a "time chart" of the results, e.g. ABCDABCD...
	     * @param results A list of Processes that have been scheduled
	     */
	    public static void printChart(Queue<Process> q)
	    {
	        int quanta = 0;
	        for (Process p : q)
	        {
	            while (quanta++ < p.getStartTime()) // show idle time
	                System.out.print("-");
	            quanta = (int) Math.floor(p.getStartTime() + p.getBurstTime());
	            
	            for (int i = 0; i < p.getBurstTime(); ++i)
	                System.out.print(p.getName());
	        }
	        System.out.println();
	    }
	    /**
	     * Go through the process queue (sorted by arrival time) 
	     * and create a new process queue using the selected scheduling algorithm
	     * @return A scheduled process queue
	     */
	    public abstract Queue<Process> schedule(PriorityQueue<Process> queue);
	}

