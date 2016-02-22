import java.util.ArrayList;
import java.util.PriorityQueue;

public class Main
{
    private static final int TRIAL_RUNS = 5;
    private static final int MAX_PROCESSES = 20;
    private static final int NUM_ALGORITHMS = 6;
    
    public static void main(String[] args) throws CloneNotSupportedException 
    {                
        //for each scheduling algorithm
        Statistics fcfs = new FCFS();
        Statistics sjf = new SJF();
        Statistics srt = new SRT();
        Statistics rr = new RR();
        Statistics nphp = new NPHP();
        Statistics phpf = new PHPF();

        PriorityQueue<Process>[] q = new PriorityQueue[NUM_ALGORITHMS + 1];
        q = (PriorityQueue<Process>[]) q;
        
        // Test each scheduling algorithm TRIAL_RUNS times
        for (int i = 0; i < TRIAL_RUNS; i++)
        {
            System.out.format("Scheduling Process Trial %d:\n", i + 1);
 
            //generate a new process queue for this testing round then duplicate it
            q[0] = ProcessGenerator.generate(MAX_PROCESSES);
            for (int j = 1; j < NUM_ALGORITHMS + 1; j++)
                q[j] = copyQueue(q[0]);
            
            // Print the process list by ascending arrival time   
            while (!q[NUM_ALGORITHMS].isEmpty())
                System.out.println(q[NUM_ALGORITHMS].poll());
                        
            // Run each scheduling algorithm and show the results
            
            System.out.println("\nAlgorithms");
            System.out.println("\nFirst Come First Served: TRIAL_" + (i+1));
            fcfs.schedule(q[0]);
            
            System.out.println("\nShortest Job First: TRIAL_" + (i+1));
            sjf.schedule(q[1]);
            
            System.out.println("\nShortest Remaining Time: TRIAL_" + (i+1));
            srt.schedule(q[2]);
            
            System.out.println("\nRound Robin: TRIAL_" + (i+1));
            rr.schedule(q[3]); 
            
            System.out.println("\nNon-Preemptive Highest Priority First: TRIAL_" + (i+1));
            nphp.schedule(q[4]);
            
            System.out.println("\nPreemptive Highest Priority First: TRIAL_" + (i+1));
            phpf.schedule(q[5]);
            
           
        }
        System.out.println("Average Statistics");
        
        System.out.println("Non-Extra Credit Algorithms");
        System.out.println("\nFirst Come First Served");
        fcfs.printAvgStats();

        System.out.println("\nShortest Job First");
        sjf.printAvgStats();

        System.out.println("\nShortest Remaining Time");
        srt.printAvgStats();
        
        System.out.println("\nRound Robin");
        rr.printAvgStats(); 

        System.out.println("\nNon-Preemptive Highest Priority First");
        nphp.printAvgStats();

        System.out.println("\nPreemptive Highest Priority First  (RR switching between same priority processes)");
        phpf.printAvgStats();

         }
    
    private static PriorityQueue<Process> copyQueue(PriorityQueue<Process> q) throws CloneNotSupportedException
    {        
        PriorityQueue<Process> qcopy = new PriorityQueue<>();
        ArrayList<Process> qoriginal = new ArrayList<>();
        while (!q.isEmpty())
        {
            Process p = q.poll();
            qcopy.add((Process) p.clone());
            qoriginal.add(p);
        }
        q.addAll(qoriginal);
        return qcopy;
    }
}
