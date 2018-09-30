package org.depo.controllers;

import org.apache.log4j.Logger;
import org.depo.model.EmployeeList;
import org.depo.model.ProcessInfo;
import org.depo.model.Unit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.List;

@Controller
public class MainInterfaceController {

    //TODO: service for working with DAO.

    private final static Logger LOGGER = Logger.getLogger(MainInterfaceController.class);

    @RequestMapping(value = "Veterok/EmployeeList", method = RequestMethod.GET)
    public String userIface(Model model, @ModelAttribute("error") String error) throws JAXBException, ParserConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElementNS("", "employees");
        doc.appendChild(root);

        for (Unit u: EmployeeList.getInstance().getUnits()) {
                    Element user = doc.createElement("user");

        user.setAttribute("id", id);
        user.appendChild(createUserElement(doc, "firstname", firstName));
        user.appendChild(createUserElement(doc, "lastname", lastName));
        user.appendChild(createUserElement(doc, "occupation", occupation));
            root.appendChild(u);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transf = transformerFactory.newTransformer();

        transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transf.setOutputProperty(OutputKeys.INDENT, "yes");
        transf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        DOMSource source = new DOMSource(doc);

        File myFile = new File("src/main/resources/users.xml");

        StreamResult console = new StreamResult(System.out);
        StreamResult file = new StreamResult(myFile);

        transf.transform(source, console);
        transf.transform(source, file);

        String out = "";
        for (Unit u: EmployeeList.getInstance().getUnits()) {
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
