package gwtSql.shared;

import java.io.Serializable;

public class DBException extends Exception implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2264335600307875360L;

	private String message;

	public DBException() {
	}

	public DBException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
