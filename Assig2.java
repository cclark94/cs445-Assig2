import java.io.*;
import java.util.*;

public class Assig2
{
	public static void main(String[] args) throws IOException
	{
		Store myStore = new Store();		
		File inFile = new File(args[0]);
		Scanner commands = new Scanner(inFile);
		
		while ( commands.hasNext() )
		{
			String command = commands.nextLine();
			
			if ( command.equals("receive") )
			{
				ArrayList<Crate> arr = new ArrayList<Crate>();
				// Next line says how many crates are being received				
				int count = Integer.parseInt(commands.nextLine());
				
				for ( int i=0; i<count; i++ )
				{				
					String[] line = commands.nextLine().split(" ");
					// line[0] is expiration date
					// line[1] is number of bananas in crate
					// line[2] is cost of crate	
					Crate newCrate = new Crate(Integer.parseInt(line[0]),
											   Integer.parseInt(line[1]),
											   Float.parseFloat(line[2]));
					arr.add(newCrate);
				} // end for
				
				myStore.shipment(arr);
			}
			
			if ( command.equals("use") )
			{
				// Next line says how many bananas will be used
				int number = Integer.parseInt(commands.nextLine());
				myStore.use(number);
			}
			
			if ( command.equals("display") )
				myStore.display();
			
			if ( command.equals("skip") )
				myStore.skip();
			
			if ( command.equals("report") )
				myStore.report();		
		} // end while		
		
		System.out.println("End of Simulation");
	} // end main
}