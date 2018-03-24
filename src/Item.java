import java.time.LocalDateTime;

public class Item 
{
	int id;
	LocalDateTime enqTime, deqTime;
	
	public Item next;
	
	Item( int _id )
	{
		id = _id;
		next = null;
	}

	public int getId() 
	{
		return id;
	}

	public LocalDateTime getEnqTime() 
	{
		return enqTime;
	}

	public void setEnqTime( LocalDateTime enqTime )  
	{
		this.enqTime = enqTime;
	}

	public LocalDateTime getDeqTime() 
	{
		return deqTime;
	}

	public void setDeqTime( LocalDateTime deqTime )
{
		this.deqTime = deqTime;
	}
}
