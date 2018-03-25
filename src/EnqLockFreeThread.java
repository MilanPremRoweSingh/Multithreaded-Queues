import java.util.Random;

public class EnqLockFreeThread extends Thread
{
	LockFreeQueue queue;
	boolean go = true;
	
	EnqLockFreeThread( LockFreeQueue _queue )
	{
		queue = _queue;
	}
	
	@Override
	public void run() 
	{
		while( go )
		{

			Random rand = new Random();

			try {
				sleep( 5 + rand.nextInt(10) );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			queue.enq();
		}
	} 
	
	public void flipGo()
	{
		go = !go;
	}
	
}
