package com.exam.sirma.teamemployees.config;

import com.exam.sirma.teamemployees.entity.Employee;
import com.exam.sirma.teamemployees.service.EmployeeService;
import com.exam.sirma.teamemployees.service.ProjectParticipationService;
import com.exam.sirma.teamemployees.util.CSVReader;
import com.exam.sirma.teamemployees.util.CalculationUtil;
import com.exam.sirma.teamemployees.util.StringConstant;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitialization {

    private final EmployeeService employeeService;
    private final ProjectParticipationService participationService;
    private final CalculationUtil calculationUtil;

    public DataInitialization(EmployeeService employeeService, ProjectParticipationService participationService, CalculationUtil calculationUtil) {
        this.employeeService = employeeService;
        this.participationService = participationService;
        this.calculationUtil = calculationUtil;
    }

    //init the data from CSV to DB
    @Bean
    public CommandLineRunner initData() {
        return args -> {
            List<Employee> read = CSVReader.read(StringConstant.FILE_PATH, employeeService, participationService);
            employeeService.saveAllEmployees(read);
            //CalculationUtil.identifyLongestWorkingPair(read);
        };
    }
}

