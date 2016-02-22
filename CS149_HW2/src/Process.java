
public class Process implements Comparable,Cloneable
{
	private char name;
	private float arrivalTime;
	private float startTime;
	private float burstTime;
	private int priority;
	
	//Getters for instance variables
	public float getArrivalTime() {
		return arrivalTime;
	}
	public float getBurstTime() {
		return burstTime;
	}
	public char getName() {
		return name;
	}
	public int getPriority() {
		return priority;
	}
	public float getStartTime() {
		return startTime;
	}
	
	//Setters for instance variables
	public void setArrivalTime(float arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public void setBurstTime(float burstTime) {
		this.burstTime = burstTime;
	}
	public void setName(char name) {
		this.name = name;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public void setStartTime(float startTime) {
		this.startTime = startTime;
	}
	
	@Override 
    public Object clone() throws CloneNotSupportedException 
    {
        Process cloned = new Process();
        cloned.name = this.name;
        cloned.arrivalTime = this.arrivalTime;
        cloned.priority = this.priority;
        cloned.burstTime = this.burstTime;
        cloned.startTime = this.startTime;
        return cloned;
    }
	
	@Override
	    /* Implement comparable so that we can put these in a priority queue */
	    public int compareTo(Object o)
	    {
	        Process p = (Process) o;
	        return this.arrivalTime < p.arrivalTime ? -1 : 1;
	    }
	
	 @Override
	    public String toString() 
	    {
	        return String.format(
	                "Process %c\n\tarrivalTime=%f\n\texpectedRunTime=%f\n\tpriority=%d",
	                name, arrivalTime, burstTime, priority);
	    }	
}
