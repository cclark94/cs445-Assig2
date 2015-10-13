import java.util.*;

public class Store
{
	private int clock;
	private Crate counterCrate;
	private LinkedStack<Crate> stack = new LinkedStack<Crate>();
	private LinkedStack<Crate> tempStack = new LinkedStack<Crate>();
	
	// Data for recent shipment
	private int recentCrateCount, recentMoves;
	private float recentBananaCost;
	
	// Overall data (since beginning of simulation)
	private int allCrateCount, allMoves;
	private float allBananaCost;
	
	// Adds all of the Crates in an ArrayList to stack	
	public void shipment(ArrayList<Crate> arr)
	{
		int count = arr.size();
		recentCrateCount = count;
		allCrateCount += count;
		System.out.println("Receiving " + count + " crates of bananas");
		System.out.println();
		
		int shipmentMoves = 0;
		float shipmentCost = 0;
		for (Crate c : arr)
		{
			// receive() returns the number of moves required to add one crate
			shipmentMoves += receive(c);
			shipmentCost += c.getCost();
		}
		
		// clearTempStack() returns number of moves required to move everything
		// in tempStack back to stack
		shipmentMoves += clearTempStack();
		recentMoves = shipmentMoves;
		allMoves += shipmentMoves;
		
		recentBananaCost = shipmentCost;
		allBananaCost += shipmentCost;
	}
	
	// Adds a single crate to stack in the proper location, using tempStack
	// Returns the number of moves required
	public int receive(Crate crate)
	{
		boolean added = false;
		int moves = 0;
		while ( !added )
		{
			if ( stack.isEmpty() || crate.compareTo(stack.peek()) <= 0 )
			{
				if ( tempStack.isEmpty() || crate.compareTo(tempStack.peek()) >= 0 )
				{
					stack.push(crate);
					moves++;
					added = true;
				}
				
				// crate.compareTo(tempStack.peek()) < 0
				// In other words, expiration date is sooner than top of tempStack
				// Move top crate from tempStack to stack and try again
				else
				{
					stack.push(tempStack.pop());
					moves++;
				}
			} // end if
			
			// crate.compareTo(stack.peek()) > 0
			// In other words, expiration date is later than top of stack
			// Move top crate from stack to tempStack and try again
			else
			{
				tempStack.push(stack.pop());
				moves++;
			}
		} // end while
		
		return moves;
	}
	
	// Uses a given number of bananas. Moves a new crate to the counter if necessary,
	// and prints a message if no bananas remain
	public void use(int number)
	{
		System.out.println(number + " bananas needed for order");
		boolean done = false;
		while ( !done )
		{
			// If there isn't a crate on the counter yet or counterCrate is 
			// empty, attempt to move the top crate from the stack to 
			// the counter
			if ( counterCrate == null || counterCrate.getCurrCount() == 0 )
			{
				if ( stack.isEmpty() )
				{
					System.out.println("Store is out of bananas!  The horror!");
					done = true;
				}
				
				else
				{
					counterCrate = stack.pop();
					System.out.println("Getting crate: " + counterCrate.toString()
									   + " from the stack");
				}
			} // end if
			
			else
			{
				// If counterCrate has enough bananas to satisfy the order
				if ( counterCrate.getCurrCount() >= number )
				{
					//System.out.println(counterCrate.getCurrCount() + " bananas in crate before order.");
					counterCrate.setCurrCount(counterCrate.getCurrCount() - number);
					System.out.println(number + " bananas used from current crate");
					done = true;
					//System.out.println("Satisfied order. " + counterCrate.getCurrCount() + " bananas remaining in counterCrate.");
				}
				
				// If counterCrate doesn't have enough bananas, use what's left and
				// update the number of bananas needed
				else
				{
					number = number - counterCrate.getCurrCount(); 
					System.out.println(counterCrate.getCurrCount() + " bananas used from current crate");
					counterCrate.setCurrCount(0);
				}
			} // end else
		} // end while
		
		System.out.println();
	}
	
	// Shows info about both counterCrate and all the crates in stack
	public void display()
	{
		if ( counterCrate != null )
			System.out.println("Current crate: " + counterCrate.toString());
		
		if ( !stack.isEmpty() )
		{
			System.out.println("Stack crates (top to bottom):");
			printStack();			
		}
		
		else
			System.out.println("No crates in stack - please reorder!");
		
		System.out.println();
	}
	
	// Moves the clock a day forward and throws out any expired crates
	public void skip()
	{
		clock++;
		System.out.println("The current day is now Day " + clock);
		if ( counterCrate != null )
		{
			if ( counterCrate.getExpDate() < clock )
			{
				System.out.println("Current crate: " + 
									counterCrate.toString() + " is expired!");
				counterCrate = null;
			}
		}
		if ( !stack.isEmpty() )
		{
			while ( stack.peek().getExpDate() < clock )
			{
				System.out.println("Top crate: " + stack.pop().toString() + 
								   " is expired!");
			}
		}
		System.out.println();
	}
	
	// Prints a financial statement
	public void report()
	{
		System.out.println("Lickety Splits Financial Statement:");
		System.out.println("\tMost Recent Shipment:");
		System.out.println("\t\tCrates: " + recentCrateCount);
		System.out.println("\t\tBanana cost: " + recentBananaCost);
		System.out.println("\t\tLabor (moves): " + recentMoves);
		System.out.println("\t\tLabor cost: " + (float) recentMoves);
		System.out.println("\t\t--------------------");
		System.out.println("\t\tTotal: " + (recentBananaCost + recentMoves));
		System.out.println();
		System.out.println("\tOverall Expenses:");
		System.out.println("\t\tCrates: " + allCrateCount);
		System.out.println("\t\tBanana cost: " + allBananaCost);
		System.out.println("\t\tLabor (moves): " + allMoves);
		System.out.println("\t\tLabor cost: " + (float) allMoves);
		System.out.println("\t\t--------------------");
		System.out.println("\t\tTotal: " + (allBananaCost + allMoves));
		System.out.println();
	}
	
	// Returns the number of moves required to move all crates
	// from tempStack back to stack
	public int clearTempStack()
	{
		int moves = 0;
		while ( !tempStack.isEmpty() )
		{
			stack.push(tempStack.pop());
			moves++;
		}
		return moves;
	}
	
	public void incrementClock()
	{
		clock++;
	}
	
	// Prints info about each crate in a stack from top to bottom
	public void printStack()
	{
		// Add everything from stack to an ArrayList
		// First item in arr is the top of the stack
		ArrayList<Crate> arr = new ArrayList<Crate>();
		while ( !stack.isEmpty() )
		{
			arr.add(stack.pop());
		}
		
		// Print by looping through arr
		for ( Crate cr : arr )
		{
			System.out.println(cr.toString());
		}

		// Add everything from arr back to stack.
		// Need to go from end of arr to beginning to
		// preserve the proper order.
		for ( int i=arr.size()-1; i>=0; i-- )
		{
			stack.push(arr.get(i));
		}
	}
}