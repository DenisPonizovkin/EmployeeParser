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
                error = "You can't edit unit and employee simultaneously";
            }
            if (!info.getUid().isEmpty()) {
                out = editUnit(model, info);
            } else {
                out = editEmployee(model, info);
            }
        }
        return out;
    }

    private String editEmployee(Model model, ProcessInfo info) {
        String out = "";
        Employee e = employeeService.findEmployeeById(Integer.parseInt(info.getEid()));
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
        if (!info.getUid().isEmpty() && !info.getUid().isEmpty()) {
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
                EmployeeForm ef = new EmployeeForm();
                ef.setEmployee(new Employee());
                ef.setOldId(-1);
                ef.setUnitName(info.getUid());
                model.addAttribute("employeeForm", ef);
                out = "redirect:/employee_form";
            }
        } else if (!info.getUid().isEmpty()) {
            UnitForm uf = new UnitForm();
            uf.setUnit(new Unit());
            uf.setOldName("");
            model.addAttribute("unitForm", uf);
            out = "redirect:/unit_form";
        } else {
            error = "Set unit id or both unit id and employee id";
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
            Integer.parseInt(info.getEid());
        } catch (Exception e) {
            error = "Set valid integer ID";
        }
        return error;
    }

    private String delete(ProcessInfo info) {
        String error = "";
        if (!info.getUid().isEmpty()) {
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
                employeeService.removeEmployee(e);
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
