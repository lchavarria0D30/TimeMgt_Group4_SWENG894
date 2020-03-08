package com.apptime.auth.model;

/**
 * @author Bashiir Mohamed
 * this class is the model for the user returned to client
 */
public class ClientUser {
	/**
	 *
	 * @param username current authenticated user(principle) username
	 * @param email current authenticated user(principle) email
	 */
	public ClientUser(String username, String email) {
		this.email= email;
		this.username = username;
	}

	public String username;
	public  String email;
	

}

