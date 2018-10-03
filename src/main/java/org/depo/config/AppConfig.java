package org.depo.config;

import org.apache.log4j.Logger;
import org.depo.model.EmployeeParser;
import org.depo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.text.ParseException;

@Configuration
public class AppConfig {

    private final static Logger LOGGER = Logger.getLogger(AppConfig.class);

    private final EmployeeParser ep = new EmployeeParser();

    @Autowired
    EmployeeService employeeService;

    @PostConstruct
    public void init() throws ParseException {
        try {
            ep.parse(employeeService);
      } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
