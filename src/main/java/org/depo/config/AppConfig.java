package org.depo.config;

import org.apache.log4j.Logger;
import org.depo.job.SaveJob;
import org.depo.model.Employee;
import org.depo.model.EmployeeList;
import org.depo.model.EmployeeParser;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import java.text.ParseException;

@Configuration
public class AppConfig {

    private final static Logger LOGGER = Logger.getLogger(AppConfig.class);

    @PostConstruct
    public void init() throws ParseException, SchedulerException {
        EmployeeParser.parse();

        try {
            JobDetailImpl job = new JobDetailImpl();
            job.setName("Employee parser");
            job.setJobClass(SaveJob.class);

            CronTriggerImpl trigger = new CronTriggerImpl();
            trigger.setName("cronTrigger");
            trigger.setCronExpression("0 * * * * ?");

            //Execute the job.
            Scheduler scheduler =
                    new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
