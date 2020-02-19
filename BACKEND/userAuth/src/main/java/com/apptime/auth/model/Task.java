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
	  @JsonFormat(pattern="MM-dd-yyyy HH:mm:ss")
	private LocalDateTime start;
	  @Column(nullable = true)
	  private LocalDateTime duration;
	  @Column(nullable = false)
	  private LocalDateTime endTime;
	private String userName;
	private String Description; 
	
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



	public LocalDateTime getStart() {
		return start;
	}



	public void setStart(LocalDateTime start) {
		this.start = start;
	}


	 
	

	public Task(String string, Date date, int i) {
		
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
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

