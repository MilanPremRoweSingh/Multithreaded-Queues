
public abstract class ThreadsafeQueue 
{
	public int currID = 0;
	
	public abstract void enq( Item i );
	public abstract void createAndEnq();
	public abstract Item deq();
}
