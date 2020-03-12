package com.apptime.auth.config;

import com.apptime.auth.model.Task;
import com.apptime.auth.model.TaskState;

public  class  TaskStateMachine  {



    public static void CREATE(Task task){
        if(task.getState() == null)
            task.setState(TaskState.CREATED);
    }
    public static void START(Task task) {
        TaskState current = task.getState();
        switch (current) {
            case CREATED:
                task.setState(TaskState.CREATED);
        }
    }

    public static void PAUSE(Task task) {
        TaskState current = task.getState();
        switch (current) {
            case CREATED:
                task.setState(TaskState.PAUSED);
        }
    }

    public static void COMPLETE(Task task) {
        TaskState current = task.getState();
        switch (current) {
            case PAUSED:
            case ACTIVE:
                task.setState(TaskState.COMPLETED);
        }
    }




}
