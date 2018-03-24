import java.util.Random;

public class EnqThread extends Thread 
{
	ThreadsafeQueue queue;
	boolean go = true;
	
	EnqThread( ThreadsafeQueue _queue )
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
				sleep( rand.nextInt(100) );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			queue.createAndEnq();
		}
	} 
	
	public void flipGo()
	{
		go = !go;
	}
	
}
