import java.util.concurrent.atomic.AtomicReference;

public class LockItem
{
	int id;
	long enqTime, deqTime;
	
	public AtomicReference<LockItem> next;
	
	LockItem( int _id )
	{
		id = _id;
		next = new AtomicReference<LockItem>( null );
	}

	public int getId() 
	{
		return id;
	}

	public long getEnqTime() 
	{
		return enqTime;
	}

	public void setEnqTime( long enqTime )  
	{
		this.enqTime = enqTime;
	}

	public long getDeqTime() 
	{
		return deqTime;
	}

	public void setDeqTime( long deqTime )
{
		this.deqTime = deqTime;
	}
}
