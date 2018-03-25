import java.util.ArrayList;
import java.util.Random;

public class DeqBlockThread extends Thread
{
	ArrayList<Item> deqItems;
	BlockingQueue queue;
	int numItems;
	
	DeqBlockThread( BlockingQueue _queue, int _numItems )
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
			Random rand = new Random();
			try {
				sleep( 5 + rand.nextInt(10) );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Item deqIt = queue.deq();
			//deqItems.add( deqIt );
		}
	}
	
}
