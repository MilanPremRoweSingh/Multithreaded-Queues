import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class BlockingQueue extends ThreadsafeQueue
{
	Item head, tail;
	
	BlockingQueue()
	{
		head = null;
		tail = null;
		currID = 0;
	}
	
	public void enq( Item i )
	{
		synchronized( this ) 	//not strictly necessary as is always called in a synch-block in this program, 
		{						//but this ensures the lock is held ( more robust, avoid programmer error )
			if (  head ==  null )
			{
				head = i;
			}
			else
			{
				tail.next = i;
			}
			tail = i;
			i.setEnqTime( LocalDateTime.now() );
			this.notify();
		}
	}
	
	public void createAndEnq()
	{
		synchronized( this )
		{
			int id = currID;
			enq( new Item( id ) );
			currID++;
		}
	}
	
	public Item deq()
	{
		synchronized( this )
		{
			while( head == null )
			{
				try 
				{
					this.wait();
				} catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
			Item i = head;
			head = head.next;
			i.setDeqTime( LocalDateTime.now() );
			
			if( head == null )
				tail = null;
			
			return i;
		}
	}
	
	public static void main( String[] args ) throws InterruptedException
	{
		int p,q,n;
		
		try
		{
			p = Integer.parseInt( args[0] );
			q = Integer.parseInt( args[1] );
			n = Integer.parseInt( args[2] );
			
			if ( p < 1 || q < 1 || n < 1 )
			{
				System.out.println( "Invlaid arguments" );
				return;
			}
		}
		catch ( NumberFormatException e )
		{
			System.out.println( "Invalid arguments" );
			return;
		}
		catch ( IndexOutOfBoundsException e )
		{
			System.out.println( "Invalid arguments" );
			return;
		}
		
		BlockingQueue blockingQueue = new BlockingQueue();
		
		EnqThread[] enqThreads = new EnqThread[ p ];
		DeqThread[] deqThreads = new DeqThread[ q ];
		
		for ( int i = 0; i < p; i++ ) 
		{
			enqThreads[ i ] = new EnqThread( blockingQueue );
			enqThreads[ i ].start();
		}
		
		for ( int i = 0; i < q; i++ )
		{
			deqThreads[ i ] = new DeqThread( blockingQueue, n );
			deqThreads[ i ].start();
		}
		
		for ( int i = 0; i < q; i++ )
		{
			deqThreads[ i ].join();
		}
		
		for ( int i = 0; i < p; i++ ) 
		{
			enqThreads[ i ].flipGo();
		}
		
		for ( int i = 0; i < p; i++ ) 
		{
			enqThreads[ i ].join();
		}
		
		ArrayList<Operation> operations = new ArrayList<Operation>();
		for ( int i = 0; i < q; i++ )
		{
			for( Item item : deqThreads[ i ].deqItems )
			{
				operations.add( new Operation( item.getEnqTime(), "enq", item.getId() ) );
				operations.add( new Operation( item.getDeqTime(), "deq", item.getId() ) );
			}
		}
		
		Collections.sort( operations, new Comparator<Operation>() 
		{

			@Override
			public int compare(Operation o1, Operation o2) {
				return o1.time.compareTo( o2.time );
			}
			
		} );
		
		for( Operation op : operations )
		{
			System.out.println( op.toString() );
		}
	}
	
	private static class Operation
	{
		public LocalDateTime time;
		String name;
		int id;
		
		Operation( LocalDateTime _time, String _name, int _id )
		{
			time = _time;
			name = _name;
			id = _id;
		}
		
		public String toString()
		{
			return name + " " + id + " - " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond() +"." + time.getNano()/1000000;
		}
	}
	
}
