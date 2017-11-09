package threadpool;

import java.util.HashMap;
import java.util.Set;

/**
 * A read/write lock that allows multiple readers, disallows multiple writers, and allows a writer to 
 * acquire a read lock while holding the write lock. 
 * 
 * A writer may also aquire a second write lock.
 * 
 * A reader may not upgrade to a write lock.
 * 
 */


//question: What is that means: Only one thread may hold the write lock at any time. That thread may hold multiple write locks.
//question2: My program unstable......

/*
 * Multiple threads may hold the read lock as long as no thread holds the write lock.
Only one thread may hold the write lock at any time. That thread may hold multiple write locks.
A thread holding a write lock may also acquire the read lock.
A thread only releases one lock at a time, therefore a thread may acquire a write lock then acquire a read lock. If this thread releases the write lock it will still hold the read lock.
A thread holding a read lock may not upgrade to the write lock. This is to prevent deadlock.
The try...Lock methods will not block, but rather return true or false to indicate whether the lock was acquired.
The lock methods will block until the lock is acquired. Hint, you should use the wait method.
If an unlock method is called and the calling thread does not hold the lock then an IllegalMonitorStateException will be thrown.
 */

public class ReentrantLock {

	//TODO: Declare data members.	

	/**
	 * Construct a new ReentrantLock.
	 */
	
	public HashMap<Long, Integer> reader;
	private HashMap<Long, Integer> writer;
	
	public ReentrantLock() {
		this.reader = new HashMap<Long, Integer>();
		this.writer = new HashMap<Long, Integer>();
	}

	/**
	 * Returns true if the invoking thread holds a read lock.
	 * @return
	 */
	
	public synchronized boolean hasRead() {
		//Get the current thread id.
		Long currentId = Thread.currentThread().getId();
		//If the currentId is in the reader, then we know that the current thread is reading.
		if(reader.containsKey(currentId)) return true;
		//If reader is not in the reader, then we return false.
		return false;
	}

	/**
	 * Returns true if the invoking thread holds a write lock.
	 * @return
	 */
	
	public synchronized boolean hasWrite() {
		//Get the current thread id.
		Long currentId = Thread.currentThread().getId();
		//If the currentId is in the writer, then current thread is writing.
		if(writer.containsKey(currentId)) return true;
		//If the currentId is not in the writer,return false.
		return false;
	}

	/**
	 * Non-blocking method that attempts to acquire the read lock.
	 * Returns true if successful.
	 * @return
	 */
	
	public synchronized boolean tryLockRead() {
		//Only if no one has the write lock or the writeLock is on the current thread, retrun true.
		Long currentId = Thread.currentThread().getId();
		if(writer.isEmpty() || this.hasWrite()) {
			 //When the method is invoked, we add one key to related thread.
			 if(!reader.containsKey(currentId)) {
				 //If the thread is not in reader, we put it in.
				 reader.put(currentId, 1);
			 } else {
				 reader.put(currentId, reader.get(currentId) + 1);
			 } 
			return true;
		}
		//Otherwise it can not get the read lock, we return false
		return false;
	}

	/**
	 * Non-blocking method that attempts to acquire the write lock.
	 * Returns true if successful.
	 * @return
	 */	
	
	public synchronized boolean tryLockWrite() {
		//If others hold the write lock or read lock, it can not get the writelock.
		//If itself have a readlock, then it can not get the writelock.
		//Otherwise, it can get the writeLock.
		Long currentId = Thread.currentThread().getId();
		//When the method is invoked, we add one key to related thread.
		if(this.hasWrite() || (writer.isEmpty() && reader.isEmpty())) {
			if(!writer.containsKey(currentId)) {
				 writer.put(currentId, 1);
			 } else {
				 writer.put(currentId, writer.get(currentId) + 1);
			 }
			return true;
		}
		return false;
	 }

	 /**
	  * Blocking method that will return only when the read lock has been 
	  * acquired.
	  */	 
	
	 public synchronized void lockRead() {
		 //While current tread can not get the key, we put the thread waiting.
		 while(!this.tryLockRead()) {
			 try {
				 this.wait(); 
			 } catch(InterruptedException e) {
				 System.out.println("lockRead has exception!!!" + e);
			 }
		 } 
	 }

	 /**
	  * Releases the read lock held by the calling thread. Other threads may continue
	  * to hold a read lock.
	  */
	 
	 public synchronized void unlockRead() throws IllegalMonitorStateException {
		 //Unlock an lock, we deduct 1 for the related thread. 
		 Long currentId = Thread.currentThread().getId();
		 //If reader does not have the current thread id, throw exception.
		 if(!reader.containsKey(currentId)) throw new IllegalMonitorStateException();
		 reader.put(currentId, reader.get(currentId) - 1);
		 //If there is no key in the current thread, we move the current tread off from reader.
		 if(reader.get(currentId) == 0) reader.remove(currentId);
		 //We notify all the waiting thread that the lock might be available.
		 this.notifyAll();
	 }

	 /**
	  * Blocking method that will return only when the write lock has been 
	  * acquired.
	  */
	 
	 public synchronized void lockWrite() {
		 //While current tread can not get the key, we put the thread waiting.
		 while(!this.tryLockWrite()) {
			 try {
				 this.wait(); 
			 } catch(InterruptedException e) {
				 System.out.println("lockRead has exception!!!" + e);
			 }
		 } 
	 }

	 /**
	  * Releases the write lock held by the calling thread. The calling thread may continue to hold
	  * a read lock.
	  */
	 
	 public synchronized void unlockWrite() throws IllegalMonitorStateException {
		//Unlock an lock, we deduct 1 for the related thread. 
		 Long currentId = Thread.currentThread().getId();
		 //If the thread id is not in writer, throw exception.
		 if(!writer.containsKey(currentId)) throw new IllegalMonitorStateException();
		 writer.put(currentId, writer.get(currentId) - 1);
		 if(writer.get(currentId) == 0) writer.remove(currentId);
		 //Notify all that lock might be available
		 this.notifyAll();
	 }
	 
}