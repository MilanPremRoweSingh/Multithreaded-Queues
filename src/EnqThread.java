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
			queue.createAndEnq();
			Random rand = new Random();
			try {
				sleep(9 + rand.nextInt( 3 ) );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	} 
	
	public void flipGo()
	{
		go = !go;
	}
	
}
