package org.depo.controllers;

import org.apache.log4j.Logger;
import org.depo.model.*;
import org.depo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EditorController {

    private final static Logger LOGGER = Logger.getLogger(EditorController.class);
    String error = "";

    @Autowired
    EmployeeService employeeService;

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String userIface(Model model, @ModelAttribute("processInfo") ProcessInfo info, RedirectAttributes ra) {

        String out = "";

        error = validate(info);
        if (error.isEmpty()) {
            out = process(model, info);
        }

        if (out.isEmpty()) {
            out = "redirect:/Veterok/EmployeeList";
        }

        ra.addAttribute("error", error);
        return out;
    }

    private String process(Model model, ProcessInfo info) {
        String out = "";
        if (info.getAction().equals("add")) {
            out = add(model, info);
        } else if (info.getAction().equals("delete")) {
            error = delete(info);
        } else {
            boolean isUid = !info.getUid().isEmpty();
            boolean isEid = !info.getEid().isEmpty();
            if (isUid && isEid) {
                out = editEmployee(model, info);
            } else if (isEid) {
                out = editEmployee(model, info);
            } else if (isUid) {
                out = editUnit(model, info);
            }
        }
        return out;
    }

    private String editEmployee(Model model, ProcessInfo info) {
        String out = "";
        Employee e = null;
        if (info.getUid().isEmpty()) {
            e = employeeService.findEmployeeById(Integer.parseInt(info.getEid()));
        } else {
            e = employeeService.findEmployeeByIdAndUnitName(info.getUid(), Integer.parseInt(info.getEid()));
        }
        employeeService.findEmployeeById(Integer.parseInt(info.getEid()));
        if (e == null) {
            error = "No such employee <" + info.getEid() + ">";
        } else {
            int id = Integer.parseInt(info.getEid());
            EmployeeForm ef = new EmployeeForm();
            ef.setOldId(id);
            ef.setUnitName(info.getUid());
            ef.setEmployee(e);
            model.addAttribute("employeeForm", ef);
            out = "employee_form";
        }
        return out;
    }

    private String editUnit(Model model, ProcessInfo info) {
        String out = "";
        if (info.getUid().equals("Руководство")) {
            error = "You cant edit unit <Руководство>";
        } else {
            Unit u = employeeService.findUnitByName(info.getUid());
            if (u == null) {
                error = "No such unit <" + info.getUid() + ">";
            } else {
                UnitForm uf = new UnitForm();
                uf.setUnit(u);
                uf.setOldName(u.getName());
                model.addAttribute("unitForm", uf);
                out = "unit_form";
            }
        }
        return out;
    }

    private String add(Model model, ProcessInfo info) {
        String out = "";
        if (!info.getUid().isEmpty() && !info.getEid().isEmpty()) {
            out = addEmployee(model, info);
        } else if (!info.getUid().isEmpty()) {
            out = addUnit(model, info);
        } else {
            error = "Set employee id or both unit id and employee id";
        }
        return out;
    }

    private String addUnit(Model model, ProcessInfo info) {
        String out = "";
        if (employeeService.findUnitByName(info.getUid()) == null) {
            Unit u = new Unit();
            u.setName(info.getUid());
            employeeService.addUnit(u);
        } else {
            error = "Unit with this name exists already";
        }
        return out;
    }

    private String addEmployee(Model model, ProcessInfo info) {
        String out = "";
        if (employeeService.findUnitByName(info.getUid()) == null) {
            error = "No such unit";
        } else {
            boolean ok = true;
            if (info.getUid().equals("Генеральный директор")) {
                Unit u = employeeService.findUnitByName(info.getUid());
                if (u == null) {
                    error = "No such employee <" + info.getEid() + ">";
                    ok = false;
                } else {
                    Employee e = employeeService.findEmployeeByPosition("Генеральный директор");
                    if (e != null) {
                        error = "The position of the Генеральный директор may be in a single copy";
                        ok = false;
                    }
                }
            }
            if (ok) {
                Employee e = employeeService.findEmployeeById(Integer.parseInt(info.getEid()));
                ok = true;
                if (e != null) {
                    for (Unit u: employeeService.findUnitOfEmployee(e)) {
                       if (u.getName().equals(info.getUid())) {
                           error = "Employee with this id exists already in the unit";
                           ok = false;
                           break;
                       }
                    }
                }
                if (ok) {
                    // Employee with this id exists in the other unit
                    EmployeeForm ef = new EmployeeForm();
                    e = new Employee();
                    e.setId(Integer.parseInt(info.getEid()));
                    ef.setEmployee(e);
                    ef.setOldId(-1);
                    ef.setUnitName(info.getUid());
                    model.addAttribute("employeeForm", ef);
                    out = "employee_form";
                }
            }
        }
        return out;
    }

    private String validate(ProcessInfo info) {
        boolean isUid = !info.getUid().isEmpty();
        boolean isEid = !info.getEid().isEmpty();
        String error = "";
        if (!isUid && !isEid) {
            error = "Set at least one id";
        }
        try {
            if (isEid) {
                Integer.parseInt(info.getEid());
            }
        } catch (Exception e) {
            error = "Set valid integer ID";
        }
        return error;
    }

    private String delete(ProcessInfo info) {
        String error = "";
        if (!info.getUid().isEmpty() && !info.getEid().isEmpty()) {
            error = deleteEmployee(info);
        } else if (info.getEid().isEmpty()) {
            error = deleteUnit(info);
        } else {
            error = deleteEmployee(info);
        }
        return error;
    }

    private String deleteEmployee(ProcessInfo info) {
        String error = "";
        Employee e = employeeService.findEmployeeById(Integer.parseInt(info.getEid()));
        if (e == null) {
            error = "No such employee <" + info.getEid() + ">";
        } else {
            if (e.getPosition().equals("Генеральный директор")) {
                error = "You cant delete employee <Генеральный директор>";
            } else {
                if (!info.getUid().isEmpty() && !info.getEid().isEmpty()) {
                    employeeService.removeEmployeeOfUnit(e, employeeService.findUnitByName(info.getUid()));
                } else {
                    employeeService.removeEmployee(e);
                }
            }
        }

        return error;
    }

    private String deleteUnit(ProcessInfo info) {
        String error = "";
        if (info.getUid().equals("Руководство")) {
            error = "You cant delete unit <Руководство>";
        } else {
            Unit u = employeeService.findUnitByName(info.getUid());
            if (u == null) {
                error = "No such unit <" + info.getUid() + ">";
            } else if (u.getEmployees().size() > 0) {
                error = "Unit is not empty";
            } else {
                employeeService.removeUnit(u);
            }
        }
        return error;
    }

    @RequestMapping(value = "process_unit", method = RequestMethod.POST)
    public String processUnit(Model model, @ModelAttribute("unitForm") UnitForm uf, RedirectAttributes ra) {
        try {
            if (uf.getOldName().isEmpty()) {
                employeeService.addUnit(uf.getUnit());
            } else {
                employeeService.replaceUnit(uf.getUnit(), uf.getOldName());
            }
        } catch (Exception e) {
            ra.addAttribute("error", e.getMessage());
        }
        return "redirect:/Veterok/EmployeeList";
    }

    @RequestMapping(value = "process_employee", method = RequestMethod.POST)
    public String processEmployee(Model model, @ModelAttribute("employeeForm") EmployeeForm ef, RedirectAttributes ra) {
        try {
            if (ef.getOldId() == -1) {
                employeeService.addEmployee(ef.getEmployee(), ef.getUnitName());
            } else {
                //TODO: validate
                employeeService.replaceEmployee(ef.getEmployee(), ef.getOldId());
            }
        } catch (Exception e) {
            ra.addAttribute("error", e.getMessage());
        }
        return "redirect:/Veterok/EmployeeList";
    }
}
