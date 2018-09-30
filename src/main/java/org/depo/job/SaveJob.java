package org.depo.job;

import org.apache.log4j.Logger;
import org.depo.model.Employee;
import org.depo.model.EmployeeList;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

@Component
public class SaveJob implements Job {

    private final static Logger LOGGER = Logger.getLogger(SaveJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOGGER.debug("Save employees data");
        Marshaller marshaller = null;
        try {
            JAXBContext jc = JAXBContext.newInstance(EmployeeList.class);
            marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(EmployeeList.getInstance(), new File("/home/denis/tmp/qqq"));
        } catch (JAXBException e) {
           LOGGER.error(e.getMessage());
        }
    }
}
