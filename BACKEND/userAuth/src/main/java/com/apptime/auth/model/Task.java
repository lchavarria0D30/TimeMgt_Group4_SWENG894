package com.apptime.auth.model;


import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
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
	  //@JsonFormat(pattern="MM-dd-yyyy HH:mm:ss")
	//private Date schedule ; 
	private Date scheduledstart;
	@Column(nullable = true)
	  private LocalDateTime duration;
	  @Column(nullable = true)
	  private LocalDateTime scheduledend;
	private String userName;
	private String Description; 
	
	  public Date getScheduledstart() {
		return scheduledstart;
	}
	public void setScheduledstart(Date scheduledstart) {
		this.scheduledstart = scheduledstart;
	}
	
	public Task() {
		
	}
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


	public Task(String string, Date date, int i) {
		
	}

	public LocalDateTime getDuration() {
		return duration;
	}
	public void setDuration(LocalDateTime duration) {
		this.duration = duration;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}



	

}

