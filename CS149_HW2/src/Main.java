import java.util.ArrayList;
import java.util.PriorityQueue;

public class Main
{
    private static final int TRIALRUNS = 5;
    private static final int MAX_PROCESSES = 20;
    private static final int NUM_ALGORITHMS = 11;
    
    public static void main(String[] args) throws CloneNotSupportedException 
    {                
        // Create a Statistics for each scheduling algorithm
        Statistics fcfs = new FCFS();
        //Statistics pfcfs = new PreemptiveFirstComeFirstServedNoAging();
        
        // nonpreemptive fcfs with priority is the same as nonpreemptive HPF
        //Statistics nfcfs = new NonpreemptiveHighestPriorityFirstNoAging(); 
        //Statistics nhpf = new NonpreemptiveHighestPriorityFirstNoAging();
        
        // round robin + priority is the same as preemptive HPF
       // Statistics phpf = new PreemptiveHighestPriorityFirstNoAging();
        //Statistics rrna = new PreemptiveHighestPriorityFirstNoAging();
        
       // Statistics sjf = new ShortestJobFirst();
       // Statistics sjfna = new ShortestJobFirstNoAging();
      //  Statistics rr = new RoundRobin();
      //  Statistics srt = new ShortestRemainingTime();
      //  Statistics srtna = new ShortestRemainingTimeNoAging();

        // Hold duplicated process queues for each algorithm to use
        PriorityQueue<Process>[] q = new PriorityQueue[NUM_ALGORITHMS + 1];
        q = (PriorityQueue<Process>[]) q;
        
        // Test each scheduling algorithm TRIALRUNS times
        for (int i = 0; i < TRIALRUNS; i++)
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
          //  sjf.schedule(q[1]);
            
            System.out.println("\nShortest Remaining Time: TRIAL_" + (i+1));
         //   srt.schedule(q[2]);
            
            System.out.println("\nRound Robin: TRIAL_" + (i+1));
            //   rr.schedule(q[3]); 
            
            System.out.println("\nNon-Preemptive Highest Priority First: TRIAL_" + (i+1));
         //   nhpf.schedule(q[4]);
            
            System.out.println("\nPreemptive Highest Priority First: TRIAL_" + (i+1));
         //   phpf.schedule(q[5]);
            
            System.out.println("Extra Credit Algorithms (Added Priority Without Aging)");
            
            System.out.println("\nNon-Preemptive First Come First Served (Same algorithm as Non-preemptive HPF): TRIAL_" + (i+1));
         //   nfcfs.schedule(q[6]);
            
            System.out.println("\nPreemptive First Come First Served (Same algorithm as Preemptive HPF without RR: TRIAL_" + (i+1));
         //   pfcfs.schedule(q[7]);
            
            System.out.println("\nRound Robin (Same algorithm as Preemptive HPF): TRIAL_" + (i+1));
        //    rrna.schedule(q[8]);
            
            System.out.println("\nShortest Remaining Time: TRIAL_" + (i+1));
        //    srtna.schedule(q[9]);
            
            System.out.println("\nShortest Job First: TRIAL_" + (i+1));
          //  sjfna.schedule(q[10]);                   
            
            System.out.println("\nNon-Preemptive and Preemptive Highest Priority First");
            System.out.println("    See above -- same algorithms as non-extra credit versions\n");
        }
        System.out.println("Average Statistics");
        
        System.out.println("Non-Extra Credit Algorithms");
        System.out.println("\nFirst Come First Served");
        fcfs.printAvgStats();

        System.out.println("\nShortest Job First");
    //    sjf.printAvgStats();

        System.out.println("\nShortest Remaining Time");
    //    srt.printAvgStats();

        System.out.println("\nNon-Preemptive Highest Priority First");
     //   nhpf.printAvgStats();

        System.out.println("\nPreemptive Highest Priority First  (RR switching between same priority processes)");
     //   phpf.printAvgStats();

        System.out.println("\nRound Robin");
     //   rr.printAvgStats();            

        System.out.println("\nExtra Credit Algorithms (Added Priority Without Aging)");
       
        System.out.println("Non-Preemptive First Come First Served (Same algorithm as Non-preemptive HPF)");
     //   nfcfs.printAvgStats();

        System.out.println("\nPreemptive First Come First Served (Same algorithm as Preemptive HPF without RR");
    //    pfcfs.printAvgStats();

        System.out.println("\nRound Robin (Same algorithm as Preemptive HPF)");
     //   rrna.printAvgStats();

        System.out.println("\nShortest Remaining Time");
     //   srtna.printAvgStats();

        System.out.println("\nShortest Job First");
    //    sjfna.printAvgStats();                   

        System.out.println("\nNon-Preemptive and Preemptive Highest Priority First");
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
