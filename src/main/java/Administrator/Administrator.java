package Administrator;

import java.util.HashMap;

import concurrent.ReentrantLock;

public class Administrator {
	/*
	 * This class is for storing administrators Information.
	 */
	private HashMap<String, String> accounts;
	private ReentrantLock rwl;
	
	
	public Administrator() {
		this.accounts = new HashMap<String, String>();
		accounts.put("Perry", "13609629560");
	}
	
	public boolean addAccount(String administrator, String passWord) {
		rwl.lockWrite();
		try {
			if(!this.accounts.containsKey(administrator)) {
				this.accounts.put(administrator, passWord);
				return true;
			} else {
				return false;
			}
		} finally {
			rwl.unlockWrite();
		}
	}
	
	//This method return true if the account and the password is true otherwise return false
	public boolean login(String administrator, String passWord) {
		rwl.lockRead();
		try {
			if(!this.accounts.containsKey(administrator) || this.accounts.get(administrator).equals(passWord)) return false;
			return true;
		} finally {
			rwl.unlockRead();
		}
	}
	
	
}
