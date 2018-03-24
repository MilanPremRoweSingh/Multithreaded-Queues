import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class BlockingQueue
{
	public int currID = 0;
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
			i.setEnqTime( System.currentTimeMillis() );
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
			i.setDeqTime( System.currentTimeMillis() );
			
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
		
		EnqBlockThread[] enqThreads = new EnqBlockThread[ p ];
		DeqBlockThread[] deqThreads = new DeqBlockThread[ q ];
		
		for ( int i = 0; i < p; i++ ) 
		{
			enqThreads[ i ] = new EnqBlockThread( blockingQueue );
			enqThreads[ i ].start();
		}
		
		for ( int i = 0; i < q; i++ )
		{
			deqThreads[ i ] = new DeqBlockThread( blockingQueue, n );
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
				return ( o1.time < o2.time ) ? -1 : ( o1.time == o2.time ) ? 0 : 1;
			}
			
		} );
		
		for( Operation op : operations )
		{
			System.out.println( op.toString() );
		}
	}
	
	private static class Operation
	{
		public long time;
		String name;
		int id;
		
		Operation( long _time, String _name, int _id )
		{
			time = _time;
			name = _name;
			id = _id;
		}
		
		public String toString()
		{
			return name + " " + id + " - " + time;
		}
	}
	
}
