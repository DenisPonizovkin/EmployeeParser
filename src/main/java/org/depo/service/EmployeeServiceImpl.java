package org.depo.service;

import org.depo.model.Employee;
import org.depo.model.Unit;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

    private List<Unit> units = new ArrayList<Unit>();

    private EmployeeServiceImpl() { }

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

    public int findUnitIdByName(String name) {
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

    public void addUnit(Unit unit) {
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

    @Override
    public Employee findEmployeeByPosition(String position) {
        Employee res = null;

        for (Unit u: units) {
            res = u.findEmployeeByPosition(position);
            if (res != null) {
                break;
            }
        }

        return res;
    }
}
