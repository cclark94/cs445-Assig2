public class Crate implements Comparable<Crate>
{
	// Keep track of the expiration date, the initial banana
	// count, the current banana count, and the cost of the
	// crate.
	private final int expDate, initCount;
	private int currCount; 
	private final float cost;
	
	public Crate(int expDate, int initCount, float cost)
	{
		this.expDate = expDate;
		this.initCount = initCount;
		currCount = initCount;
		this.cost = cost;
	}
	
	public void setCurrCount(int count)
	{
		currCount = count;
	}

	public int getExpDate()
	{
		return expDate;
	}
	
	public int getCurrCount()
	{
		return currCount;
	}
	
	public float getCost()
	{
		return cost;
	}
	
	public String toString()
	{
		return "Expires:" + expDate + "  Start Count:" + initCount +
				"  Remain:" + currCount + "  Cost:" + cost;
	}
	
	public int compareTo(Crate crate2)
	{
		if ( expDate == crate2.expDate )
			return 0;
		
		else if ( expDate > crate2.expDate )
			return 1;
		
		else
			return -1;
	}
}