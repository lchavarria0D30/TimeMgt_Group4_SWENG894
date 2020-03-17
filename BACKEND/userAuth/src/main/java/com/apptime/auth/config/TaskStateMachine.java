package com.apptime.auth.config;

import com.apptime.auth.model.Task;
import com.apptime.auth.model.TaskState;

import java.time.Duration;
import java.util.Date;

/**
 * @author Bashiir
 * Use Case: TMGP4-47, TMGP4-181, TMGP4-48
 */
public class TaskStateMachine  {

    public static void CREATE(Task task){
        if (task.getState() == null)
            task.setState(TaskState.CREATED);
    }

    public static void START(Task task) {
        TaskState current = task.getState();
        if (current == null) {
            return;
        }
        switch (current) {
            case CREATED:
            case PAUSED:
                task.setState(TaskState.ACTIVE);
                task.setStart(new Date());
        }
    }

    public static void PAUSE(Task task) {
        TaskState current = task.getState();
        if (current == null) {
            return;
        }
        if (current == TaskState.ACTIVE) {
            task.setState(TaskState.PAUSED);
            updateDuration(task);
        }
    }

    public static void COMPLETE(Task task) {
        TaskState current = task.getState();
        if (current == null) {
            return;
        }
        switch (current) {
            case PAUSED:
                task.setState(TaskState.COMPLETED);
                task.setEnd(new Date());
                break;
            case ACTIVE:
                task.setState(TaskState.COMPLETED);
                task.setEnd(new Date());
                updateDuration(task);
        }
    }

    private static void updateDuration(Task task) {
        Date now = new Date();
        Duration duration = Duration.ofMillis(now.getTime() - task.getStart().getTime());
        if (task.getDuration() != null) {
            duration = duration.plus(task.getDuration());
        }
        task.setDuration(duration);
    }
}
