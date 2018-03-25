import java.util.ArrayList;
import java.util.Random;

public class DeqLockFreeThread extends Thread 
{
	public ArrayList<LockItem> deqItems;
	LockFreeQueue queue;
	boolean go = true;
	int numDeqs;
	
	DeqLockFreeThread( LockFreeQueue _queue, int _numDeqs )
	{
		queue = _queue;
		numDeqs = _numDeqs;
	}
	
	@Override
	public void run() 
	{
		for( int i = 0; i < numDeqs; i++ )
		{

			Random rand = new Random();

			try {
				sleep( 5 + rand.nextInt(10) );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				LockItem deqIt = queue.deq();
				deqItems.add( deqIt );
			} catch (Exception e) {
				i--;
			}
		}
	} 
	
	public void flipGo()
	{
		go = !go;
	}
	
}