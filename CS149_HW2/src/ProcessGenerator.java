import java.util.PriorityQueue;
import java.util.Random;

public class ProcessGenerator {
	public static PriorityQueue<Process> generate(int numProcess)
	{
		String letter ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		
		PriorityQueue<Process> queue = new PriorityQueue<>();
		
		Random random = new Random();
		float arrivalTime = 0;
		for(int i = 0; i != numProcess && arrivalTime < 99; i++)
        {		
            Process process = new Process();
            process.setArrivalTime(arrivalTime); 
            process.setBurstTime(random.nextInt(10) + 1); //Runtime [0,10]
            process.setPriority(random.nextInt(4) + 1); //Priorities [0,5]
            process.setName(letter.charAt(i));
            queue.add(process);
            
            arrivalTime += random.nextFloat() * 10;
        }
		return queue;
	}
}
