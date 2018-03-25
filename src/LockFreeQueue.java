import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


public class LockFreeQueue 
{
	AtomicReference<LockItem> head, tail;
	AtomicInteger currID;
	
	public LockFreeQueue() 
	{
		LockItem dummy = new LockItem( -1 );
		head = new AtomicReference<LockItem>( dummy );
		tail = new AtomicReference<LockItem>( dummy ); //dummy tail
		
		currID = new AtomicInteger( 0 );
	}
	
	public void enq() 
	{
		while( true )
		{
			LockItem last = tail.get();
			LockItem next = last.next.get();
			if( last == tail.get() ) //If empty queue
			{
				if ( next == null ) //if tail hasnt been editted already, edit it
				{
					LockItem newIt 	= new LockItem( currID.getAndIncrement() );
					if ( last.next.compareAndSet( next, newIt ) )
						tail.compareAndSet( last, newIt );
					newIt.setEnqTime( System.currentTimeMillis() );
					return;
				}
			}
			else
			{
				tail.compareAndSet( last, next );
			}
		}
	}

	public LockItem deq() 
	{		
		while( true )
		{
			LockItem first 	= head.get();
			LockItem last 	= tail.get();
			LockItem next	= first.next.get();
			
			if( first == head.get() )
			{
				if( first == last )
				{
					if( next == null )
					{
						continue;
					}
					tail.compareAndSet(last , next);
				}
				else
				{
					LockItem it = next;
					if( head.compareAndSet( first, next ) )
					{
						it.setDeqTime( System.currentTimeMillis() );
						return it;
					}
				}
			}
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
		
		LockFreeQueue blockingQueue = new LockFreeQueue();
		
		EnqLockFreeThread[] enqThreads = new EnqLockFreeThread[ p ];
		DeqLockFreeThread[] deqThreads = new DeqLockFreeThread[ q ];

		long time = System.currentTimeMillis();
		for ( int i = 0; i < p; i++ ) 
		{
			enqThreads[ i ] = new EnqLockFreeThread( blockingQueue );
			enqThreads[ i ].start();
		}
		
		for ( int i = 0; i < q; i++ )
		{
			deqThreads[ i ] = new DeqLockFreeThread( blockingQueue, n );
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
		System.out.println( System.currentTimeMillis() - time );
		/*
		
		ArrayList<Operation> operations = new ArrayList<Operation>();
		for ( int i = 0; i < q; i++ )
		{
			for( LockItem item : deqThreads[ i ].deqItems )
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
		*/
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
