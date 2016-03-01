import java.util.ArrayList;

/**
 * The CPU class.
 * This class represents a CPU that is running on our system.
 * 
 * @author STHEDE
 */
public class CPU 
{
	// This process is the process that is currently running on the CPU.
	private Process running;
	
	// This variable is true if the CPU is empty (not running a process).
	private boolean empty;
	
	// This variable stores the names of the processes that were run on
	// the CPU over the course of the simulation.
	private ArrayList<String> chart;
	
	
	/**
	 * The constructor for the CPU class. We assume that the CPU is not
	 * running anything initially.
	 */
	public CPU( )
	{
		running = null;
		empty = true;
		chart = new ArrayList<String>( );
	}
	
	
	/**
	 * This method is used to start a process running on the CPU. The process
	 * to start is called p, and the time it is started is time.
	 * 
	 * @param p  The process to start running on the system.
	 * @param time  The current time.
	 */
	public void begin( Process p, int time )
	{
		// If the process p is null, then don't do anything.
		if ( p != null )
		{
			// If the CPU is empty and there is time left to run on process p,
			// then start p running.
			if ( empty && ( p.getTimeLeft( ) > 0 ) )
			{
				running = p;
				running.startRunning( time );
				empty = false;
			}
			// Otherwise, print an appropriate error message.
			else if ( !empty )
			{
				System.out.println( "CPU is currently running another process." );
			}
			else
			{
				System.out.println( "Process is already finished." );
			}
		}
	}
	
	public Process clearCPU() { //begin modifier
		if (!empty) {
			empty = true;
			return running;
		}
		
		System.out.println("CPU is already empty");
		return null;
	}
	
	
	/**
	 * The tick method advances the CPU one time tick. If the process that is
	 * currently running on the CPU finishes, it is returned from the method;
	 * otherwise it will return null.
	 * 
	 * @param time
	 * @return The process that completed; null if no process completed
	 */
	public Process tick( int time )
	{
		// If the CPU is currently running something, do this stuff.
		if ( !empty )
		{
			// Advance the process running on the CPU one time tick.
			running.tick( time );
			
			// Add the name of the process running to the chart.
			chart.add( running.getName( ) );
		
			// If the process is finished now, remove it from the CPU and
			// return it.
			if ( running.isFinished( ) )
			{
				empty = true;
				return running;
			}
			// If the process is not finished, return null.
			else
			{
				return null;
			}
		}
		// If the CPU is not running anything, it is empty, so add
		// null to the chart, then return null.
		chart.add( null );
		return null;
	}
	
	
	/**
	 * Return the value of the empty variable.
	 * @return Whether or not the CPU is empty.
	 */
	public boolean isEmpty( )
	{
		return empty;
	}
	
	
	/**
	 * This method prints the CPU usage chart, 20 time ticks per line. This
	 * code assumes the names of the processes are two characters - if they
	 * are not, the chart will not line up well.
	 */
	public void printChart( )
	{
		int i = 1;
		
		for ( String s : chart )
		{
			// If no process was running, this will print XX on the chart.
			if ( s != null )
				System.out.print( s + " " );
			else
				System.out.print( "XX " );
			
			// After 20 items, move to the next line.
			if ( ( i % 20 ) == 0 )
				System.out.println( );
			
			i++;
		}
		System.out.println( );
	}
}