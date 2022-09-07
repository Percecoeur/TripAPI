package com.amadeus.trip.job;

import com.amadeus.trip.utils.Constants;
import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

/**
 * This represents a notification message triggered by the quartz scheduler
 */
@Log4j2
public class QrtzJob implements Job {

  @Override
  public void execute(JobExecutionContext jobExecutionContext) {

    JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
    String message = dataMap.getString(Constants.NOTIF_MESSAGE);

    log.info(message);
  }
}
