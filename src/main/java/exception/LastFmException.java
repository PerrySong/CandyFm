package exception;

public class LastFmException extends Exception{
	/*
	 * If the response from lastFm is not OK 200, it will cause thisexception
	 */
	public LastFmException(String msg) {
		super("LastFm connection exception" + msg);
	}
}
