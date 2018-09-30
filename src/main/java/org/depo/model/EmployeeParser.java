package org.depo.model;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeParser {

    private final static Logger LOGGER = Logger.getLogger(EmployeeParser.class);

    public static void parse() {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File("/home/denis/tmp/test.xml"));
            // normalize text representation
            doc.getDocumentElement ().normalize ();

            NodeList root = doc.getChildNodes();
            Node employees = getNode("employees", root);
            //Node units = getNode("unit", employees.getChildNodes());
            //Node employee = getNode("employee", units.getChildNodes());
            List<Node> units = getNodeList("unit", employees.getChildNodes());
            for (Node u: units) {
                Unit unit = createUnit(u);
                EmployeeList.getInstance().addNode(unit);
                LOGGER.debug("\n" + unit);
            }
        } catch (ParserConfigurationException e) {
            LOGGER.error(e.getMessage());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        } catch (SAXException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private static Unit createUnit(Node root) {
        Unit u = new Unit();
        u.setName(((Element)root).getAttribute("name"));
        //Get employees list
        List<Employee> employees = new ArrayList<Employee>();
        List<Node> unitEmployees = getNodeList("employee", root.getChildNodes());
        for (Node e: unitEmployees) {
            employees.add(createEmployee(e));
        }
        u.setEmployees(employees);

        //Get units list
        List<Unit> units = new ArrayList<>();
        List<Node> unitsOfUnits = getNodeList("unit", root.getChildNodes());
        for (Node node: unitsOfUnits) {
            Unit subunit = createUnit(node);
            units.add(subunit);
        }
        u.setUnits(units);
        return u;
    }

    private static Employee createEmployee(Node root) {
        //TODO: validate existence of attribute
        Employee e = new Employee();
        Element eleroot = (Element)  root;
        e.setId(Integer.parseInt(eleroot.getAttribute("id")));
        e.setEmail(eleroot.getAttribute("email"));
        e.setFio(eleroot.getAttribute("fio"));
        e.setPosition(eleroot.getAttribute("position"));
        e.setRoom(eleroot.getAttribute("room"));
        e.setTel(eleroot.getAttribute("tel"));
        try {
            e.setTel2(eleroot.getAttribute("tel2"));
        } catch (Exception ex) {
            LOGGER.debug("TEL2 for employee " + e.getId() + " not presented");
        }
        return e;
    }

    private List<Unit> getUnits(Node root) {
        List<Node> units = getNodeList("unit", root.getChildNodes());
        List<Unit> res = new ArrayList<>();
        if (units.size() > 0) {
            for (Node u: units) {
                List<Node> unitsOfUnits = getNodeList("unit", u.getChildNodes());
            }
        }
        return res;
    }

    private static List<Node> getNodeList(String name, NodeList list) {
        List<Node> nl = new ArrayList<Node>();
        for (int i = 0; i < list.getLength(); i++) {
            Node n = list.item(i);
            if (n.getNodeName().equals(name)) {
                nl.add(n);
            }
        }
        return nl;
    }

    private static Node getNode(String name, NodeList list) {
        Node n = null;
        for (int i = 0; i < list.getLength(); i++) {
            n = list.item(i);
            if (n.getNodeName().equals(name)) {
                break;
            }
        }
        return n;
    }
}
