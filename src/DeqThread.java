import java.util.ArrayList;
import java.util.Random;

public class DeqThread extends Thread
{
	ArrayList<Item> deqItems;
	ThreadsafeQueue queue;
	int numItems;
	
	DeqThread( ThreadsafeQueue _queue, int _numItems )
	{
		numItems = _numItems;
		queue = _queue;
		
		deqItems = new ArrayList<Item>();
	}
	
	@Override
	public void run() 
	{
		for( int i = 0; i < numItems; i++ )
		{
			deqItems.add( queue.deq() );
			System.out.println( deqItems.get( deqItems.size() - 1 ));
			Random rand = new Random();
			try {
				sleep(9 + rand.nextInt( 3 ) );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
