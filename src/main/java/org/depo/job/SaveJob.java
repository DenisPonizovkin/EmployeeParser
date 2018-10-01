package org.depo.job;

import org.apache.log4j.Logger;
import org.depo.model.Employee;
import org.depo.service.EmployeeService;
import org.depo.service.EmployeeServiceImpl;
import org.depo.model.Unit;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

@Component
public class SaveJob implements Job {

    private final static Logger LOGGER = Logger.getLogger(SaveJob.class);

    @Autowired
    EmployeeService employeeService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOGGER.debug("Save employees data");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            //add elements to Document
            Element rootElement =
                    doc.createElementNS("https://www.journaldev.com/employee", "employees");
            //append root element to document
            doc.appendChild(rootElement);
            appendUnits(doc, rootElement);
            //for output to file, console
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            //for pretty print
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            //write to console or file
            StreamResult console = new StreamResult(System.out);
            StreamResult file = new StreamResult(new File("/home/denis/tmp/test.xml"));

            //write data
            transformer.transform(source, console);
            transformer.transform(source, file);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    private void appendUnits(Document doc, Element rootElement) {
        for (Unit u: employeeService.getUnits()) {
            rootElement.appendChild(appendUnit(doc, rootElement, u));
        }
    }

    private Element appendUnit(Document doc, Element rootElement, Unit u) {
        Element unit = doc.createElement("unit");
        unit.setAttribute("name", u.getName());

        for (Unit subu: u.getUnits()) {
            Node nu = appendUnit(doc, rootElement, subu);
            unit.appendChild(nu);
        }


        for (Employee e: u.getEmployees()) {
            unit.appendChild(getEmployee(doc, e));
            rootElement.appendChild(unit);
        }

        return unit;
    }

    private static Node getEmployee(Document doc, Employee e) {
        Element employee = doc.createElement("employee");

        employee.setAttribute("id", String.valueOf(e.getId()));
        employee.setAttribute("fio", String.valueOf(e.getFio()));
        employee.setAttribute("position", String.valueOf(e.getPosition()));
        employee.setAttribute("email", String.valueOf(e.getEmail()));
        employee.setAttribute("tel", String.valueOf(e.getTel()));
        String tel2 = e.getTel2();
        if (!tel2.isEmpty()) {
            employee.setAttribute("tel2", String.valueOf(e.getTel2()));
        }
        employee.setAttribute("room", String.valueOf(e.getRoom()));

        return employee;
    }
}
