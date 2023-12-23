package com.exam.sirma.teamemployees.thymeleaf;

import com.exam.sirma.teamemployees.entity.Employee;
import com.exam.sirma.teamemployees.service.EmployeeService;
import com.exam.sirma.teamemployees.util.CalculationUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

import static com.exam.sirma.teamemployees.util.StringConstant.ERROR_FETCHING_EMPLOYEES;

@Controller
@RequestMapping()
public class ModelController {

    private final EmployeeService employeeService;

    public ModelController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // print result in html using Thymeleaf
    @GetMapping("/print-result")
    public String printResult(Model model) {
        List<Employee> allEmployees = employeeService.getAllEmployees();
        String result;

        if (allEmployees == null || allEmployees.isEmpty()) {
            result = ERROR_FETCHING_EMPLOYEES;
        } else {
            result = CalculationUtil.identifyLongestWorkingPair(allEmployees);
        }

        List<String> resultRows = Arrays.asList(result.split(System.lineSeparator()));
        model.addAttribute("resultRows", resultRows);
        return "result-template";
    }
}
