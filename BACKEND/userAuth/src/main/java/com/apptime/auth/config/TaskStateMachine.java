package com.apptime.auth.config;

import com.apptime.auth.model.Task;
import com.apptime.auth.model.TaskState;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

public  class  TaskStateMachine  {



    public static void CREATE(Task task){
        if(task.getState() == null)
            task.setState(TaskState.CREATED);
    }
    public static void START(Task task) {
        TaskState current = task.getState();
        switch (current) {
            case CREATED:
            case PAUSED:
                task.setState(TaskState.ACTIVE);
        }
    }

    public static void PAUSE(Task task) {
        TaskState current = task.getState();
        Date now = new Date();
        switch (current) {
            case ACTIVE:
                Duration d = Duration.ofMillis(now.getTime() - task.getStart().getTime());
                task.setState(TaskState.PAUSED);
                task.setDuration(d);
        }
    }

    public static void COMPLETE(Task task) {
        TaskState current = task.getState();
        Date now = new Date();
        switch (current) {
            case PAUSED:
                task.setState(TaskState.COMPLETED);
                break;
            case ACTIVE:
                Duration d = Duration.ofMillis(now.getTime() - task.getStart().getTime());
                task.setState(TaskState.COMPLETED);
                task.setDuration(d);

        }
    }




}
