package com.apptime.auth.config;

import com.apptime.auth.model.TaskEvents;
import com.apptime.auth.model.TaskState;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskStateMachineConfigTest {
    @Autowired
    StateMachineFactory<TaskState, TaskEvents> factory;
    private StateMachine<TaskState, TaskEvents> stateMachine;

    @Before
    public void setUp() throws Exception {
        stateMachine = factory.getStateMachine();
    }

    @Test
    public void testNewStateMachine(){
        StateMachine<TaskState, TaskEvents> sm = factory.getStateMachine();
            sm.start();
            System.out.println(sm.getState().toString());
            sm.sendEvent(TaskEvents.create);
            System.out.println(sm.getState().toString());
            sm.sendEvent(TaskEvents.start);
            System.out.println(sm.getState().toString());

    }
}
