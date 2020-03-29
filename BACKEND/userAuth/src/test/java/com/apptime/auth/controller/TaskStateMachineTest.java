package com.apptime.auth.controller;

import com.apptime.auth.config.TaskStateMachine;
import com.apptime.auth.model.Task;
import com.apptime.auth.model.TaskState;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Date;

import static com.apptime.auth.model.TaskState.ACTIVE;
import static com.apptime.auth.model.TaskState.COMPLETED;
import static com.apptime.auth.model.TaskState.CREATED;
import static com.apptime.auth.model.TaskState.PAUSED;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Qi Zhang
 * Unit Test for TaskStateMachine
 * Use Case: TMGP4-47, TMGP4-181, TMGP4-48
 */
@SpringBootTest
public class TaskStateMachineTest {
    @Captor
    ArgumentCaptor<Duration> durationArgumentCaptor;

    @Test
    public void testCreate() {
        Task task = mock(Task.class);
        when(task.getState()).thenReturn(null);
        TaskStateMachine.CREATE(task);
        verify(task, times(1)).setState(eq(CREATED));
        clearInvocations(task);

        when(task.getState()).thenReturn(CREATED);
        TaskStateMachine.CREATE(task);
        verify(task, never()).setState(any(TaskState.class));
        clearInvocations(task);

        when(task.getState()).thenReturn(ACTIVE);
        TaskStateMachine.CREATE(task);
        verify(task, never()).setState(any(TaskState.class));
        clearInvocations(task);

        when(task.getState()).thenReturn(COMPLETED);
        TaskStateMachine.CREATE(task);
        verify(task, never()).setState(any(TaskState.class));
        clearInvocations(task);

        when(task.getState()).thenReturn(PAUSED);
        TaskStateMachine.CREATE(task);
        verify(task, never()).setState(any(TaskState.class));
    }

    @Test
    public void testStart() {
        Task task = mock(Task.class);
        when(task.getState()).thenReturn(null);
        TaskStateMachine.START(task);
        verify(task, never()).setState(any(TaskState.class));
        verify(task, never()).setStart(any(Date.class));
        clearInvocations(task);

        when(task.getState()).thenReturn(CREATED);
        TaskStateMachine.START(task);
        verify(task, times(1)).setState(eq(ACTIVE));
        verify(task, times(1)).setStart(any(Date.class));
        clearInvocations(task);

        when(task.getState()).thenReturn(PAUSED);
        TaskStateMachine.START(task);
        verify(task, times(1)).setState(eq(ACTIVE));
        verify(task, times(1)).setStart(any(Date.class));
        clearInvocations(task);

        when(task.getState()).thenReturn(ACTIVE);
        TaskStateMachine.START(task);
        verify(task, never()).setState(any(TaskState.class));
        verify(task, never()).setStart(any(Date.class));
        clearInvocations(task);

        when(task.getState()).thenReturn(COMPLETED);
        TaskStateMachine.START(task);
        verify(task, never()).setState(any(TaskState.class));
        verify(task, never()).setStart(any(Date.class));
    }

    @Test
    public void testPause() {
        Task task = mock(Task.class);
        when(task.getState()).thenReturn(null);
        TaskStateMachine.PAUSE(task);
        verify(task, never()).setState(any(TaskState.class));
        verify(task, never()).setDuration(any(Duration.class));
        clearInvocations(task);

        when(task.getState()).thenReturn(CREATED);
        TaskStateMachine.PAUSE(task);
        verify(task, never()).setState(any(TaskState.class));
        verify(task, never()).setDuration(any(Duration.class));
        clearInvocations(task);

        when(task.getState()).thenReturn(PAUSED);
        TaskStateMachine.PAUSE(task);
        verify(task, never()).setState(any(TaskState.class));
        verify(task, never()).setDuration(any(Duration.class));
        clearInvocations(task);

        when(task.getState()).thenReturn(ACTIVE);
        Date startTime = new Date(System.currentTimeMillis() - 1000L); // one second ago
        when(task.getStart()).thenReturn(startTime);
        TaskStateMachine.PAUSE(task);
        verify(task, times(1)).setState(eq(PAUSED));
        verify(task, times(1)).setDuration(durationArgumentCaptor.capture());
        assertTrue(durationArgumentCaptor.getValue().toMillis() >= 1000L);
        clearInvocations(task);

        // pause one task with existing duration
        when(task.getState()).thenReturn(ACTIVE);
        startTime = new Date(System.currentTimeMillis() - 1000L); // one second ago
        when(task.getStart()).thenReturn(startTime);
        Duration duration = Duration.ofMillis(2000L); // two seconds duration
        when(task.getDuration()).thenReturn(duration);
        TaskStateMachine.PAUSE(task);
        verify(task, times(1)).setState(eq(PAUSED));
        verify(task, times(1)).setDuration(durationArgumentCaptor.capture());
        assertTrue(durationArgumentCaptor.getValue().toMillis() >= 3000L);
        clearInvocations(task);

        when(task.getState()).thenReturn(COMPLETED);
        TaskStateMachine.PAUSE(task);
        verify(task, never()).setState(any(TaskState.class));
        verify(task, never()).setDuration(any(Duration.class));
    }

    @Test
    public void testComplete() {
        Task task = mock(Task.class);
        when(task.getState()).thenReturn(null);
        TaskStateMachine.COMPLETE(task);
        verify(task, never()).setState(any(TaskState.class));
        verify(task, never()).setDuration(any(Duration.class));
        clearInvocations(task);

        when(task.getState()).thenReturn(CREATED);
        TaskStateMachine.COMPLETE(task);
        verify(task, never()).setState(any(TaskState.class));
        verify(task, never()).setDuration(any(Duration.class));
        clearInvocations(task);

        when(task.getState()).thenReturn(PAUSED);
        TaskStateMachine.COMPLETE(task);
        verify(task, times(1)).setState(eq(COMPLETED));
        verify(task, never()).setDuration(any(Duration.class));
        clearInvocations(task);

        when(task.getState()).thenReturn(ACTIVE);
        Date startTime = new Date(System.currentTimeMillis() - 1000L); // one second ago
        when(task.getStart()).thenReturn(startTime);
        TaskStateMachine.COMPLETE(task);
        verify(task, times(1)).setState(eq(COMPLETED));
        verify(task, times(1)).setDuration(durationArgumentCaptor.capture());
        assertTrue(durationArgumentCaptor.getValue().toMillis() >= 1000L);
        clearInvocations(task);

        // pause one task with existing duration
        when(task.getState()).thenReturn(ACTIVE);
        startTime = new Date(System.currentTimeMillis() - 1000L); // one second ago
        when(task.getStart()).thenReturn(startTime);
        Duration duration = Duration.ofMillis(2000L); // two seconds duration
        when(task.getDuration()).thenReturn(duration);
        TaskStateMachine.COMPLETE(task);
        verify(task, times(1)).setState(eq(COMPLETED));
        verify(task, times(1)).setDuration(durationArgumentCaptor.capture());
        assertTrue(durationArgumentCaptor.getValue().toMillis() >= 3000L);
        clearInvocations(task);

        when(task.getState()).thenReturn(COMPLETED);
        TaskStateMachine.COMPLETE(task);
        verify(task, never()).setState(any(TaskState.class));
        verify(task, never()).setDuration(any(Duration.class));
    }
}
