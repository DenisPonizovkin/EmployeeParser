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
            res = u.findUnitByName(name);
            if (res != null) {
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
           for (Unit subu: u.getUnits()) {
              subu.addEmployee(unitName, employee);
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

    @Override
    public List<Unit> findUnitOfEmployee(Employee e) {
        List<Unit> res = new ArrayList<>();
        for (Unit u: units) {
            for (Unit subu: u.getUnits()) {
               if (subu.findEmployeeById(e.getId()) != null) {
                   res.add(subu);
               }
            }
        }
        for (Unit u: units) {
            if (u.findEmployeeById(e.getId()) != null) {
                res.add(u);
            }
        }
        return res;
    }

    @Override
    public Employee findEmployeeByIdAndUnitName(String uid, int i) {
        Employee res = null;
        for (Unit u: units) {
            Employee e = u.findEmployeeById(i);
            if (e != null) {
                if (u.getName().equals(uid)) {
                    res = e;
                    break;
                }
            }
            e = u.findEmployeeByIdAndUnitName(uid, i);
            if (e != null) {
                break;
            }
        }
        return res;
    }

    @Override
    public boolean removeEmployeeOfUnit(Employee e, Unit u) {
        List<Unit> list = findUnitOfEmployee(e);
        boolean ok = false;
        for (Unit u_: list) {
           if (u_.getName().equals(u.getName())) {
              u_.removeEmployee(e);
              ok = true;
              break;
           }
        }
        return ok;
    }
}
