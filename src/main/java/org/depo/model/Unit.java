package org.depo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Unit {

    @XmlElement(name = "employee")
    private List<Employee> employees = new ArrayList<Employee>();

    private List<Unit> units = new ArrayList<Unit>();

    @XmlElement(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        String out = "UNIT-NAME " + name + "\n";
        for (Employee e: employees) {
           out += "\t" + e + "\n";
        }
        for (Unit u: units) {
           out += "\t" + u.toString().replace("\n", "\n\t") + "\n";
        }
        return out;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }
}
