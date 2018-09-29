package org.depo.controllers;

import org.apache.log4j.Logger;
import org.depo.model.Employee;
import org.depo.model.Unit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    private final static Logger LOGGER = Logger.getLogger(MainController.class);

    @RequestMapping(value = "/")
    public String userIface() throws JAXBException {

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
                LOGGER.debug("\n" + unit);
            }
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (SAXException e1) {
            e1.printStackTrace();
        }

        return "index";
    }

    private Unit createUnit(Node root) {
        Unit u = new Unit();
        u.setName(((Element)root).getAttribute("name"));
        //Get employees list
        List<Employee> employees = new ArrayList<Employee>();
        List<Node> unitEmployees = getNodeList("employee", root.getChildNodes());
        LOGGER.debug("" + unitEmployees.size());
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

    private Employee createEmployee(Node root) {
        Employee e = new Employee();
        Element eleroot = (Element)  root;
        e.setId(Integer.parseInt(eleroot.getAttribute("id")));
        e.setEmail(eleroot.getAttribute("id"));
        e.setFio(eleroot.getAttribute("id"));
        e.setPosition(eleroot.getAttribute("id"));
        e.setRoom(eleroot.getAttribute("id"));
        e.setTel(eleroot.getAttribute("id"));
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

    private List<Node> getNodeList(String name, NodeList list) {
        List<Node> nl = new ArrayList<Node>();
        for (int i = 0; i < list.getLength(); i++) {
            Node n = list.item(i);
            if (n.getNodeName().equals(name)) {
                nl.add(n);
            }
        }
        return nl;
    }

    private Node getNode(String name, NodeList list) {
        Node n = null;
        for (int i = 0; i < list.getLength(); i++) {
            n = list.item(i);
            if (n.getNodeName().equals(name)) {
                break;
            }
        }
        return n;
    }

    private void doNodes(Node n) {
        NodeList nodes = n.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n_ = nodes.item(i);
            if (n_ == null) {
                continue;
            }
            doNode(n);
        }
    }

    private void doNode(Node n) {
        switch (n.getNodeType()) {
            case Node.ELEMENT_NODE:
                System.out.println("ELEMENT<" + n.getNodeName() + ">");
                doNodes(n);
                break;
            case Node.TEXT_NODE:
                String text = n.getNodeValue();
                if (text.length() == 0 ||
                        text.equals("\n") || text.equals("\\r")) {
                    break;
                }
                System.out.println("TEXT: " + text);
                break;
            default:
                System.err.println("OTHER NODE " +
                        n.getNodeType() + ": " + n.getClass());
                break;
        }
    }
}
