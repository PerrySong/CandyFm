package exception;

public class AddToQueueException extends Exception{
	
	/*
	 *For objects of ExecutorService, when you call shutdown then you call execute, there will be a AddToQueueException.
	 */
	
	public AddToQueueException(String msg) {
		super(msg);
	}
	
}
