package com.remousses.app.tennis;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.remousses.app.tennis.config.taskscheduler.SchedulerConfiguration;
import com.remousses.app.tennis.util.Constant;

@Service
public class TennisScheduler {

	@Autowired
	private ScheduledAnnotationBeanPostProcessor postProcessor;

	@Autowired
	private SchedulerConfiguration schedulerConfiguration;
	
	public String stopSchedule() {
		postProcessor.postProcessBeforeDestruction(schedulerConfiguration, Constant.TENNIS_SCHEDULED_TASKS);
		return String.format("Stop %s", Constant.TENNIS_SCHEDULED_TASKS);
	}

	public String startSchedule() {
		postProcessor.postProcessAfterInitialization(schedulerConfiguration, Constant.TENNIS_SCHEDULED_TASKS);
		return String.format("Start %s", Constant.TENNIS_SCHEDULED_TASKS);
	}

	public String listSchedules() throws JsonProcessingException {
		Set<ScheduledTask> setTasks = postProcessor.getScheduledTasks();
		if (!setTasks.isEmpty()) {
			return setTasks.toString();
		} else {
			return "Currently no scheduler tasks are running";
		}
	}
}
