package com.exam.sirma.teamemployees;

import com.exam.sirma.teamemployees.utils.CSVReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TeamEmployeesApplication {

    public static void main(String[] args) {


        CSVReader.read("src/main/resources/datafile.csv");
        SpringApplication.run(TeamEmployeesApplication.class, args);
    }

}
