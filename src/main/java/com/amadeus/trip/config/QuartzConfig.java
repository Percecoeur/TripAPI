package com.amadeus.trip.config;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

@Data
@Log4j2
@Configuration
@ConfigurationProperties(prefix = "quartz")
public class QuartzConfig {

  private Map<String, String> properties;

  @Autowired
  ApplicationContext applicationContext;

  @Bean
  public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
    SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
    schedulerFactory.setQuartzProperties(quartzProperties());
    schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
    schedulerFactory.setAutoStartup(true);
    schedulerFactory.setApplicationContextSchedulerContextKey("applicationContext");

    return schedulerFactory;
  }

  public Properties quartzProperties() {
    Properties quartzProperties = new Properties();
    quartzProperties.putAll(properties);
    return quartzProperties;
  }

}
