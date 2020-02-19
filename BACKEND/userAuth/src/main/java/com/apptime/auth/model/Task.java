package com.apptime.auth.model;


import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Entity
public class Task {

	@Id
	@GeneratedValue
	private long id;
	private String name;
	  @JsonFormat(pattern="MM-dd-yyyy HH:mm:ss")
	private LocalDateTime start;
	private String userName;
	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public LocalDateTime getStart() {
		return start;
	}



	public void setStart(LocalDateTime start) {
		this.start = start;
	}


	 
	

	public Task(String string, Date date, int i) {
		
	}



	

}

