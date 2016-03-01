import java.util.ArrayList;
import java.util.Random;

/**
 * The Simulator class, which executes the simulation.
 * @author STHEDE
 *
 */
public class Simulator 
{
	// cpu1 is the CPU of the system.
	private CPU cpu1;
	
	// ready stores the processes that are ready to run and are waiting for
	// the CPU.
	private ArrayList<Process> ready;
	
	// finished stores the processes that have finished execution.
	private ArrayList<Process> finished;
	
	// time stores the current time of the system.
	private int time;
	
	//quantumTime tracks time as well, but can be modified based on the quantum.
	private int quantumTime;
	
	// END_TIME indicates how long the simulation will run.
	private int END_TIME = 300;
	
	// PROC_ARRIVE_PERCENT indicates the percent chance that a new process
	// will arrive to run on the CPU at any given time unit.
	private int PROC_ARRIVE_PERCENT = 50;
	
	// PROC_MIN_LENGTH is the minimum length that a process can be.
	private int PROC_MIN_LENGTH = 6;
	
	// PROC_MAX_LENGTH is the maximum length that a process can be.
	private int PROC_MAX_LENGTH = 15;
	
	// Quantum for the algorithms that need it
	private int TIME_QUANTUM = 2;
	
	// SCHEDULING_ALGORITHM indicates which algorithm the simulation
	// will be using. Options are "FIFO", "SJF", "STCF", "RR", and
	// "MLFQ".
	private String SCHEDULING_ALGORITHM = "STCF";
	
	/**
	 * The constructor for the Simulator class. Everything starts as
	 * a new Object, and time starts at 0.
	 */
	public Simulator( )
	{
		if (SCHEDULING_ALGORITHM.equals("STCF")) {
			TIME_QUANTUM = 1;
		}
		cpu1 = new CPU( );
		ready = new ArrayList<Process>( );
		finished = new ArrayList<Process>( );
		time = 0;
	}

	/**
	 * The main function runs the program. This just creates a Simulator
	 * Object, and calls its run method.
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Simulator s = new Simulator( );
		s.run();
		//s.testSuite();
	}
	
	//Run all the requested tests in one big group to avoid recoding after each test
	private void testSuite() {
		String[][] results = null;
		//If algorithm doesn't need a quantum
		if (SCHEDULING_ALGORITHM.equals("FIFO") || SCHEDULING_ALGORITHM.equals("SJF") 
				|| SCHEDULING_ALGORITHM.equals("STCF") || SCHEDULING_ALGORITHM.equals("PS")) {
			
			results = new String[18][4];
			int testing = 0;
			for (PROC_ARRIVE_PERCENT = 20; PROC_ARRIVE_PERCENT < 81; PROC_ARRIVE_PERCENT += 30) {
				System.out.println("% arriving: " + PROC_ARRIVE_PERCENT);
				String parameters = "% arriving: " + PROC_ARRIVE_PERCENT + ", ";
				PROC_MIN_LENGTH = 2;
				PROC_MAX_LENGTH = 3;
				
				for (int i = 0; i < 3; i++) {
					PROC_MIN_LENGTH += i;
					PROC_MAX_LENGTH += ((2 * i) + 2);
					System.out.println("\n Min process length: " + PROC_MIN_LENGTH);
					System.out.println("Max process length: " + PROC_MAX_LENGTH);
					String parametersTwo = parameters + "Process length: " + PROC_MIN_LENGTH + "-" + PROC_MAX_LENGTH + ", ";
					
					for (END_TIME = 100; END_TIME < 251; END_TIME += 150) {
						System.out.println("\n End time: " + END_TIME);
						String parametersThree = parametersTwo + "End time: " + END_TIME;
						ready.clear();
						finished.clear();
						cpu1 = new CPU( );
						time = 0;
						
						results[testing] = this.run();
						results[testing][3] = parametersThree;
						testing++;
					}
				}
			}
		}
		
		//If algorithm DOES need a quantum
		else if (SCHEDULING_ALGORITHM.equals("RR") || SCHEDULING_ALGORITHM.equals("MLFQ")) {
			results = new String[36][4];
			int testing = 0;
			for (PROC_ARRIVE_PERCENT = 20; PROC_ARRIVE_PERCENT < 81; PROC_ARRIVE_PERCENT += 30) {
				System.out.println("% arriving: " + PROC_ARRIVE_PERCENT);
				String parameters = "% arriving: " + PROC_ARRIVE_PERCENT + ", ";
				PROC_MIN_LENGTH = 2;
				PROC_MAX_LENGTH = 3;
				
				for (int i = 0; i < 3; i++) {
					PROC_MIN_LENGTH += i;
					PROC_MAX_LENGTH += ((2 * i) + 2);
					System.out.println("\n Min process length: " + PROC_MIN_LENGTH);
					System.out.println("Max process length: " + PROC_MAX_LENGTH);
					String parametersTwo = parameters + "Process length: " + PROC_MIN_LENGTH + "-" + PROC_MAX_LENGTH + ", ";
					
					for (TIME_QUANTUM = 2; TIME_QUANTUM < 6; TIME_QUANTUM += 3) {
						System.out.println("Time quantum: " + TIME_QUANTUM);
						String parametersThree = parametersTwo + "Quantum: " + TIME_QUANTUM + ", ";
						
						for (END_TIME = 100; END_TIME < 251; END_TIME += 150) {
							System.out.println("\n End time: " + END_TIME);
							String parametersFour = parametersThree + "End time: " + END_TIME;
							ready.clear(); 
							finished.clear();
							cpu1 = new CPU();
							time = 0;
							
							results[testing] = this.run();
							results[testing][3] = parametersFour;
							testing++;
						}
					}
				}
			}
		}
		for (String[] s: results) {
			System.out.println();
			System.out.println("Parameters: " + s[3]);
			System.out.println("# of unfinished processes: " + s[0] + ",      average response time: " + s[1] + ",      average turnaround time: " + s[2]);
		}
	}

	/**
	 * The addProcess starts a new process at the current time.
	 * @param name - The name of the process to add.
	 * @param start - The start time for the process to add.
	 * @param length - The length of the process to add.
	 */
	public void addProcess( String name, int start, int length )
	{
		Process temp = new Process( name, start, length );
		ready.add( temp );
	}
	
	
	/**
	 * Select the next process to run on the CPU. The algorithm is determined
	 * by the value of the SCHEDULING_ALGORITHM variable.
	 * @param ready  The set of processes that are ready to run.
	 * @return  Returns the process selected to run.
	 */
	public Process select(ArrayList<Process> ready) {
		if (ready.size() > 0) {
			if(SCHEDULING_ALGORITHM == "FIFO") {
				Process temp = ready.get(0);
				ready.remove(0);
				return temp;
			}
		
			else if (SCHEDULING_ALGORITHM == "SJF" || SCHEDULING_ALGORITHM == "STCF") {
				Process shortest = ready.get(0);
				int index = 0;
				for (int i = 1; i < ready.size(); i++) {
					if (shortest.getLength() > ready.get(i).getLength()) {
						shortest = ready.get(i);
						index = i;
					}
				}
				ready.remove(index);
				return shortest;
			}
		}
		return null;
	}
	
	
	/**
	 * The run method runs the entire simulation. We assume that this 
	 * method is run immediately after creating the Simulator object.
	 */
	public String[] run( )
	{
		// Various variables needed.
		Process temp;
		Random r = new Random( );
		int procTime;
		char ch1 = 'A', ch2 = 'A';
		char[] n;
		String s;
		
		System.out.println( "Beginning simulation." );
		
		// Loop until the time gets to the end.
		while ( time < END_TIME )
		{
			// See if a new process arrives.
			if ( r.nextInt( 100 ) < PROC_ARRIVE_PERCENT )
			{	
				// Create a new name by incrementing the characters.
				n = new char[2];
				n[0] = ch1;
				n[1] = ch2;
				s = new String( n );
				if ( ch2 == 'Z' )
				{
					ch1++;
					ch2 = 'A';
				}
				else
				{
					ch2++;
				}
				
				// Creates a process with a random length between the minimum and maximum lengths.
				procTime = r.nextInt( PROC_MAX_LENGTH - PROC_MIN_LENGTH + 1 ) + PROC_MIN_LENGTH;
				addProcess( s, time, procTime );
			}

			// Increment the time on the CPU.
			temp = cpu1.tick( time );
			
			// If a job finished, add it to finished.
			if ( temp != null )
			{
				finished.add( temp );
			}
			
			// If there is nothing on the CPU, add something to it.
			if ( cpu1.isEmpty( ) )
			{
				temp = select( ready );
				cpu1.begin( temp, time );
				quantumTime = 0;
			}
			else if (SCHEDULING_ALGORITHM.equals("RR") || SCHEDULING_ALGORITHM.equals("MLFQ") || SCHEDULING_ALGORITHM.equals("STCF")) {
					if( quantumTime % TIME_QUANTUM == 0) {
						ready.add(cpu1.clearCPU());
						temp = select(ready);
						cpu1.begin(temp, time);
					}
					
					quantumTime++;
			}
			
			time++;
		}
		
		// Print the results.
		double[] results = printResults( ready, finished );
		String[] toReturn = {Double.toString(results[0]), Double.toString(results[1]), Double.toString(results[2]), null};
		return toReturn;
	}
	
	
	/**
	 * This method prints the results of the simulation.
	 * @param ready - the list of jobs waiting for the CPU.
	 * @param finished - the list of jobs that finished during the simulation.
	 */
	public double[] printResults( ArrayList<Process> ready, ArrayList<Process> finished )
	{
		// Variables to calculate results.
		int totalResponseTime = 0;
		int totalTurnaroundTime = 0;
		double avgRT, avgTT;
		
		int s, f, fr, r, t, l;
		String n;
		
		// Everything on ready is unfinished. Ignore the job currently running on the CPU.
		System.out.println( "There are " + ready.size() + " unfinished processes." );
		System.out.println( );
		
		// Print results from all finished processes.
		System.out.println( "Name   Arrive   First Run   Finished   Length   Response   Turnaround" );
		for ( Process p : finished )
		{
			n = p.getName( );
			s = p.getStartTime( );
			f = p.getEndTime( );
			fr = p.getFirstRunTime( );
			l = p.getLength( );
			r = fr - s;
			t = f - s;
			
			System.out.format( "%4s   %6d   %9d   %8d   %6d   %8d   %10d%n", n, s, fr, f, l, r, t );
			
			totalResponseTime += r;
			totalTurnaroundTime += t;
		}
		
		// Calculate average response and turnaround times.
		avgRT = (double)totalResponseTime / finished.size( );
		avgTT = (double)totalTurnaroundTime / finished.size( );
		
		System.out.println( );
		System.out.format( "Averages                                        %8.2f   %10.2f%n", avgRT, avgTT );

		// Print out the CPU usage chart.
		System.out.println( );
		cpu1.printChart( );
		
		                  //Unfinished                                     Response                          Turnaround
		double[] results = {(Math.floor(ready.size() * 100) / 100), (Math.floor(avgRT * 100) / 100), (Math.floor(avgTT * 100) / 100)};
		return results;
	}
}