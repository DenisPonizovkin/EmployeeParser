package org.depo.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.parameters.P;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@XmlRootElement(name = "employees")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeeList {

    private List<Unit> units = new ArrayList<Unit>();
    private static EmployeeList instance;

    private EmployeeList() { }

    public static synchronized EmployeeList getInstance(){
        if(instance == null){
            instance = new EmployeeList();
        }
        return instance;
    }

    @XmlElement(name="unit")
    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public List<Unit> unitsList() {
        return units;
    }

    public void addNode(Unit unit) {
        units.add(unit);
    }

    public Unit findUnitByName(String name) {
        Unit res = null;

        for (Unit u: units) {
            if (u.getName().equals(name)) {
                res = u;
                break;
            }
        }

        return res;
    }

    public void removeUnit(Unit u) {
        units.remove(u);
    }

    public Employee findEmployeeById(int i) {
        Employee res = null;

        for (Unit u: units) {
            res = u.findEmployeeById(i);
            if (res != null) {
               break;
            }
        }

        return res;
    }

    public void removeEmployee(Employee e) {
        for (Unit u: units) {
            if (u.removeEmployee(e)) {
                break;
            }
        }
    }

    public void replaceUnit(Unit unit, String oldName) {
        for (Unit u: units) {
            if (u.getName().equals(oldName)) {
                u.setName(unit.getName());
                break;
            }
        }
    }

    private int findUnitIdByName(String name) {
        Unit res = null;

        int i = 0;
        for (Unit u: units) {
            if (u.getName().equals(name)) {
                res = u;
                break;
            }
            i++;
        }

       if (res == null) {
           return -1;
       }
       return i;
    }

    public void add(Unit unit) {
        units.add(unit);
    }

    public void addEmployee(Employee employee, String unitName) {
        for (Unit u: units) {
           if (u.getName().equals(unitName)) {
              u.addEmployee(employee);
              break;
           }
        }
    }

    public void replaceEmployee(Employee e, int oldId) {
        for (Unit u: units) {
           if (u.replaceEmployee(e, oldId)) {
              break;
           }
        }
    }
}
