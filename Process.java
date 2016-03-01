/**
 * Process class
 *
 * This class represents a process on our hypothetical system. Any information
 * about a single process should be stored here.
 * 
 * @author STHEDE
 *
 */

public class Process 
{
	// The name of the process. Can be set to something, but the current
	// program assigns arbitrary names.
	private String name;
	
	// The starting time of the process - the time that the process arrives
	// at the system to be run.
	private int startTime;
	
	// The ending time of the process - the time that the process completed
	// running on the system.
	private int endTime;
	
	// The first run time of the process - the time that the process was
	// first places on the CPU to be run.
	private int firstRunTime;
	
	// The amount of time left to complete the process - how much time until
	// the process finished.
	private int timeLeft;
	
	// The length of the process - how much time the process originally 
	// required to complete.
	private int length;
	
	// A boolean indicating in the process is finished or not.
	private boolean finished;
	
	
	/**
	 * The constructor for the Process class.
	 * 
	 * @param n The name of the process.
	 * @param s The start time of the process.
	 * @param len The length of the process.
	 */
	public Process( String n, int s, int len )
	{
		name = n;
		startTime = s;
		endTime = -1;       // End time is calculated while running.
		firstRunTime = -1;  // First run time is calculated while running.
		timeLeft = len;     // Initial time left is length of process.
		length = len;
		finished = false;
	}
	
	
	/**
	 * This method is called when the time on the system advances one step.
	 * It should only be called on a process that is currently running on
	 * the CPU.
	 * 
	 * @param time The current time in the system
	 */
	public void tick( int time )
	{
		// One second of running has occurred, so less time is left.
		timeLeft--;
		
		// If there is no time left on the process, then the process
		// is finished, and the end time is the current time.
		if ( timeLeft == 0 )
		{
			finished = true;
			endTime = time;
		}
	}

	
	// Sets the firstRunTime to the time passed as the parameter.
	public void startRunning( int time )
	{
		// Only change it if it has not been changed before.
		if ( firstRunTime == - 1 )
			firstRunTime = time;
	}
	
	
	// Return the name of the process.
	public String getName( )
	{
		return name;
	}
	
	
	// Return the time left of the process.
	public int getTimeLeft( )
	{
		return timeLeft;
	}
	
	
	// Returns whether or not the process is finished.
	public boolean isFinished( )
	{
		return finished;
	}
	
	
	// Returns the starting time of the process.
	public int getStartTime( )
	{
		return startTime;
	}
	
	
	// Returns the end time of the process.
	public int getEndTime( )
	{
		return endTime;
	}
	
	
	// Returns the first time that the process started running.
	public int getFirstRunTime( )
	{
		return firstRunTime;
	}
	
	
	// Returns the original length of the process.
	public int getLength( )
	{
		return length;
	}
}