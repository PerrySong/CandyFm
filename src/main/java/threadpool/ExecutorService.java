package threadpool;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import exception.AddToQueueException;

public class ExecutorService {

	private final int nThreads;
	private final PoolWorker[] threads;
    private volatile LinkedList<Runnable> queue;
    private volatile boolean isRunning;
	
    
    public ExecutorService(int nThreads) {
    
        this.nThreads = nThreads;
        this.queue = new LinkedList<Runnable>();
        this.threads = new PoolWorker[this.nThreads];
        this.isRunning = true;
        
        
        for(int i = 0; i < nThreads; i++) {
        		threads[i] = new PoolWorker();
        		threads[i].start();
        		
        }
        
    }
    
 //Execute the given command in the future some point.
    
    public void execute(Runnable r) throws AddToQueueException {
	    if(isRunning) {
    			synchronized(queue) {
    				queue.addLast(r);
    				queue.notify();
    			}
	    } else {
	    		AddToQueueException a = new AddToQueueException("The queue was shuted down ");
	    		throw a;
	    }
    }
    
    
    
	public void shutdown() {
		this.isRunning = false;
	}
	
	public void awaitTermination() {
		
		synchronized(queue) {
			queue.notifyAll();
		}
		for(PoolWorker thread: this.threads) {

			try {

				thread.join();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	
//	private synchronized Runnable removeFirst() {
//		while(!queue.isEmpty() || isRunning) {
//			synchronized(queue) {
//				while (queue.isEmpty() && isRunning) {
//					try {
//						queue.wait();
//					} catch (InterruptedException ignored) {
//					}
//				}
//				return(Runnable) queue.removeFirst();
//			}
//		return null;	
//	}
	
	private class PoolWorker extends Thread {

		public void run() {
			Runnable r = null;
			while(!queue.isEmpty() || isRunning) {
				synchronized(queue) {
					if(queue.isEmpty() && !isRunning) break;
					while (queue.isEmpty() && isRunning) {
						try {
							queue.wait();
						} catch (InterruptedException ignored) {
						}
					}
					if(!queue.isEmpty()) r = (Runnable) queue.removeFirst();
				}	
                // If we don't catch RuntimeException, 
                // the pool could leak threads	
				if(r != null) {
					try {
						r.run();
					} catch (RuntimeException e) {
					}
				}
			}
		}	
	}	

}
