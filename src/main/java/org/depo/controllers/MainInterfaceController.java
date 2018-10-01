package org.depo.controllers;

import org.depo.model.EmployeeParser;
import org.depo.service.EmployeeService;
import org.depo.model.ProcessInfo;
import org.depo.model.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;

@Controller
public class MainInterfaceController {

    //TODO: service for working with DAO.

    private final static Logger LOGGER = Logger.getLogger(MainInterfaceController.class);

    @Autowired
    EmployeeService employeeService;

    private final EmployeeParser ep = new EmployeeParser();

    @RequestMapping(value = "Veterok/EmployeeList", method = RequestMethod.GET)
    public String userIface(Model model, @ModelAttribute("error") String error) throws JAXBException, ParserConfigurationException, TransformerException {

        //ep.parse();
        String out = "";
        for (Unit u: employeeService.getUnits()) {
            out += u;
        }
        out = out.replace("\n", "<br/>");
        out = out.replace("\t", "&emsp;");
        model.addAttribute("list", out);
        model.addAttribute("processInfo", new ProcessInfo());
        if (!error.isEmpty()) {
            model.addAttribute("error", error);
        }
        return "list";
    }

}
