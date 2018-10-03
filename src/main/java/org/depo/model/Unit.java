package org.depo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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

    public Employee findEmployeeById(int i) {
        Employee res = null;
        for (Employee e: getEmployees()) {
            if (e.getId() == i) {
                res = e;
                break;
            }
        }
        if (res == null) {
            for (Unit u: units) {
                if (u.getUnits().size() > 0) {
                    res = u.findEmployeeById(i);
                }

                if (res == null) {
                    for (Employee e: u.getEmployees()) {
                        if (e.getId() == i) {
                            res = e;
                            break;
                        }
                    }
                }

                if (res != null) {
                    break;
                }
            }
        }

        return res;
    }

    public boolean removeEmployee(Employee rm) {
        boolean ok = false;
        for (Employee e: getEmployees()) {
            if (e.getId() == rm.getId()) {
                ok = true;
                break;
            }
        }
        if (!ok) {
            for (Unit u: units) {
                if (u.getUnits().size() > 0) {
                    ok = u.removeEmployee(rm);
                }
                if (!ok) {
                    for (Employee e: u.getEmployees()) {
                        if (e.getId() == rm.getId()) {
                            ok = true;
                            break;
                        }
                    }
                    if (ok) {
                        u.removeEmployee_(rm);
                        break;
                    }
                }
            }
        } else {
            removeEmployee_(rm);
        }
        return ok;
    }

    private void removeEmployee_(Employee rm) {
        employees.remove(rm);
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public boolean replaceEmployee(Employee e, int oldId) {
        boolean ok = false;
        int id = 0;
        for (Employee e_: getEmployees()) {
            if (e.getId() == e_.getId()) {
                ok = true;
                break;
            }
            id++;
        }
        if (!ok) {
            for (Unit u: units) {
                if (u.getUnits().size() > 0) {
                    ok = u.replaceEmployee(e, oldId);
                }
                if (!ok) {
                    id = 0;
                    for (Employee e_: u.getEmployees()) {
                        if (e_.getId() == e_.getId()) {
                            ok = true;
                            break;
                        }
                        id++;
                    }
                    if (ok) {
                        replaceEmployee_(e, id);
                    }
                }
            }
        } else {
            replaceEmployee_(e, id);
        }
        return ok;
    }

    private void replaceEmployee_(Employee e, int id) {
        employees.set(id, e);
    }

    public Employee findEmployeeByPosition(String position) {
        Employee res = null;
        for (Employee e: getEmployees()) {
            if (e.getPosition().equals(position)) {
                res = e;
                break;
            }
        }
        if (res == null) {
            for (Unit u: units) {
                if (u.getUnits().size() > 0) {
                    res = u.findEmployeeByPosition(name);
                }

                if (res == null) {
                    for (Employee e: u.getEmployees()) {
                        if (e.getPosition().equals(position)) {
                            res = e;
                            break;
                        }
                    }
                }

                if (res != null) {
                    break;
                }
            }
        }

        return res;
    }

    public Unit findUnitByName(String name) {
        Unit u = null;
        if (getName().equals(name)) {
           u = this;
        } else {
            for (Unit subu: getUnits()) {
                u = subu.findUnitByName(name);
                if (u != null) {
                    break;
                }
            }
        }
        return u;
    }

    public Employee findEmployeeByIdAndUnitName(String uid, int i) {
        Employee res = null;
        if (getName().equals(uid)) {
            res = findEmployeeById(i);
            if (res == null) {
                for (Unit subu: getUnits()) {
                    res = subu.findEmployeeByIdAndUnitName(uid, i);
                    if (res != null) {
                        break;
                    }
                }
            }
        }
        return res;
    }

    public void addEmployee(String unitName, Employee employee) {
        if (unitName.equals(getName())) {
           addEmployee(employee);
        } else {
            for (Unit u: getUnits()) {
               u.addEmployee(unitName, employee);
            }
        }
    }
}
