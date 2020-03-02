package com.apptime.auth.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Bashiir Current user roles
 * this mapps to auth 2.0 claims.
 */
@Entity
public class Roles {

	@Id
	@GeneratedValue
	private int role_id;
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	private String role;

	@Override
	public String toString() {
		return getRole();
	}
}
