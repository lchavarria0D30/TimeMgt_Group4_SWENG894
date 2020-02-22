Last login: Fri Feb 21 22:42:28 on ttys002
%                                                                                                                                                      python2018@Bashiirs-MacBook-Pro TimeMgt_Group4_SWENG894 % git status
On branch TestMaster
Your branch is up to date with 'origin/TestMaster'.

Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git restore <file>..." to discard changes in working directory)
	modified:   BACKEND/userAuth/src/main/java/com/apptime/auth/controller/TaskManager.java
	modified:   BACKEND/userAuth/src/main/java/com/apptime/auth/model/Task.java
	modified:   FRONTEND/newFrontEnd/.DS_Store
	modified:   FRONTEND/newFrontEnd/apTime/src/app/tasks/tasks.component.html
	modified:   FRONTEND/newFrontEnd/apTime/src/app/tasks/tasks.component.ts

Untracked files:
  (use "git add <file>..." to include in what will be committed)
	BACKEND/workspace/

no changes added to commit (use "git add" and/or "git commit -a")
python2018@Bashiirs-MacBook-Pro TimeMgt_Group4_SWENG894 % git stash
Saved working directory and index state WIP on TestMaster: 12333310 Merge pull request #18 from lchavarria0D30/Yanisse
python2018@Bashiirs-MacBook-Pro TimeMgt_Group4_SWENG894 % git checkout Bashiir
Branch 'Bashiir' set up to track remote branch 'Bashiir' from 'origin'.
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
        private LocalDateTime scheduledstart;
        @Column(nullable = true)
          private LocalDateTime duration;
          @Column(nullable = true)
          private LocalDateTime scheduledEndTime;
        private String userName;
        private String Description;

          public LocalDateTime getScheduledstart() {
                return scheduledstart;
-- INSERT --package com.apptime.auth.model;


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
	private LocalDateTime scheduledstart;
	@Column(nullable = true)
	  private LocalDateTime duration;
	  @Column(nullable = true)
	  private LocalDateTime scheduledEndTime;
	private String userName;
	private String Description; 
	
	  public LocalDateTime getScheduledstart() {
		return scheduledstart;
	}
	public void setScheduledstart(LocalDateTime scheduledstart) {
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

