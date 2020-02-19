package com.apptime.auth.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.JoinColumn;

@Entity
public class Users {

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	/*
	 * public Set<Roles> getRoles() { return roles; } public void
	 * setRoles(Set<Roles> roles) { this.roles = roles; }
	 */

	@Column(nullable = false)
	@Id
	private String username;

//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	//private Set<Roles> roles;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Task> tasks;
	
	@Override
	public String toString() {
		return "Users{" +
				"username='" + username + '\'' +
			//	", roles=" + roles +
				'}';
	}
}
