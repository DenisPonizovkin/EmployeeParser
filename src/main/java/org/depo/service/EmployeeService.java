package org.depo.service;

import org.depo.model.Employee;
import org.depo.model.Unit;

import java.util.List;

public interface EmployeeService {
    public List<Unit> getUnits();
    public void setUnits(List<Unit> units);
    public List<Unit> unitsList();
    public void addNode(Unit unit);
    public Unit findUnitByName(String name);
    public void removeUnit(Unit u);
    public Employee findEmployeeById(int i);
    public void removeEmployee(Employee e);
    public void replaceUnit(Unit unit, String oldName);
    public int findUnitIdByName(String name);
    public void addUnit(Unit unit);
    public void addEmployee(Employee employee, String unitName);
    public void replaceEmployee(Employee e, int oldId);
    Employee findEmployeeByPosition(String генеральный_директор);
    List<Unit> findUnitOfEmployee(Employee e);
    Employee findEmployeeByIdAndUnitName(String uid, int i);
    boolean removeEmployeeOfUnit(Employee e, Unit unitByName);
}
