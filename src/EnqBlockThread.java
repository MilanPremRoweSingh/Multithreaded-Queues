import java.util.Random;

public class EnqBlockThread extends Thread 
{
	BlockingQueue queue;
	boolean go = true;
	
	EnqBlockThread( BlockingQueue _queue )
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
			queue.createAndEnq();
		}
	} 
	
	public void flipGo()
	{
		go = !go;
	}
	
}
