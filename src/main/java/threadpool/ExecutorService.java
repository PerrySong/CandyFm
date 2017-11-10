package threadpool;

import java.util.LinkedList;
import exception.AddToQueueException;

public class ExecutorService {
/*
 * This class provides threadpool which can limit the number of threads when you rock your program concurrently.
 */
	
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
				e.printStackTrace();
			}
			
		}
	}
	
	private class PoolWorker extends Thread {

		public void run() {
			Runnable r = null;
			while(true) {
				synchronized(queue) {
					// I think if in the block the condition is not satisfied, we can break the loop earlier.
					//Cause inside of the synchronized block, no other thread will change "queue.isEmpty() && !isRunning"'s value
					//So does this thread itself. We can break as early as possible.
					while (queue.isEmpty() && isRunning) {
						try {
							queue.wait();
						} catch (InterruptedException ignored) {
						}
					}
					if(queue.isEmpty() && !isRunning) break;
					r = (Runnable) queue.removeFirst();
				}	
                // If we don't catch RuntimeException, 
                // the pool could leak threads without noticing us.	
				try {
					r.run();
				} catch (RuntimeException e) {
					System.out.println(e.getMessage());
				}				
			}
		}	
	}

}
