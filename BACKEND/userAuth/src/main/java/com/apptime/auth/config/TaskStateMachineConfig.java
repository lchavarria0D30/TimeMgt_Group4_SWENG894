package com.apptime.auth.config;
import com.apptime.auth.model.TaskEvents;
import com.apptime.auth.model.TaskState;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@EnableStateMachineFactory
@Configuration
public class TaskStateMachineConfig extends StateMachineConfigurerAdapter<TaskState, TaskEvents> {



    @Override
    public void  configure(StateMachineStateConfigurer<TaskState,TaskEvents> states) throws Exception {
    states.withStates().initial(TaskState.CREATED)
            .states(EnumSet.allOf(TaskState.class))
            .end(TaskState.COMPLETED);
    }
    @Override
    public void configure(StateMachineTransitionConfigurer<TaskState,TaskEvents> transitions) throws Exception {
    transitions.withExternal()
            .source(TaskState.CREATED).target(TaskState.CREATED).event(TaskEvents.CREATE)
            .and()
            .withExternal().source(TaskState.CREATED).target(TaskState.ACTIVE).event(TaskEvents.START)
            .and()
            .withExternal().source(TaskState.ACTIVE).target(TaskState.COMPLETED).event(TaskEvents.COMPLETE)
            .and()
            .withExternal().source(TaskState.ACTIVE).target(TaskState.PAUSED).event(TaskEvents.PAUSE)
            .and()
            .withExternal().source(TaskState.PAUSED).target(TaskState.ACTIVE).event(TaskEvents.START)
            .and()
            .withExternal().source(TaskState.PAUSED).target(TaskState.COMPLETED).event(TaskEvents.COMPLETE);

    }

}
