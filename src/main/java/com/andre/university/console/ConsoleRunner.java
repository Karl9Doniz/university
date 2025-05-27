package com.andre.university.console;

import com.andre.university.model.Degree;
import com.andre.university.service.DepartmentService;
import com.andre.university.service.LectorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ConsoleRunner implements CommandLineRunner {
    private static final String HELP = """
      Available commands:
        • Who is head of department {department_name}
        • Show {department_name} statistics
        • Show the average salary for the department {department_name}
        • Show count of employee for {department_name}
        • Global search by {template}
        • exit
      """;

    private static final Pattern HEAD = Pattern.compile("^Who is head of department (.+)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern STATS = Pattern.compile("^Show (.+) statistics$", Pattern.CASE_INSENSITIVE);
    private static final Pattern AVG = Pattern.compile("^Show the average salary for the department (.+)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern COUNT = Pattern.compile("^Show count of employee for (.+)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern SEARCH = Pattern.compile("^Global search by (.+)$", Pattern.CASE_INSENSITIVE);

    private final DepartmentService deptSvc;
    private final LectorService    lectorSvc;

    public ConsoleRunner(DepartmentService deptSvc, LectorService lectorSvc) {
        this.deptSvc  = deptSvc;
        this.lectorSvc = lectorSvc;
    }

    @Override
    public void run(String... args) {
        System.out.println(HELP);
        Scanner in = new Scanner(System.in);

        loop: while (true) {
            System.out.print("$ ");
            String line = in.nextLine().trim();
            if (line.equalsIgnoreCase("exit")) break;

            Matcher m;
            try {
                if ((m = HEAD.matcher(line)).matches()) {
                    String dept = stripDot(m.group(1));
                    System.out.printf("Head of %s department is %s%n",
                            dept, deptSvc.getHeadOfDepartment(dept));
                }
                else if ((m = STATS.matcher(line)).matches()) {
                    String dept = stripDot(m.group(1));
                    var stats = deptSvc.getStatistics(dept);
                    System.out.printf("assistants - %d%n", stats.get(Degree.ASSISTANT));
                    System.out.printf("associate professors - %d%n", stats.get(Degree.ASSOCIATE_PROFESSOR));
                    System.out.printf("professors - %d%n", stats.get(Degree.PROFESSOR));
                }
                else if ((m = AVG.matcher(line)).matches()) {
                    String dept = stripDot(m.group(1));
                    System.out.printf("The average salary of %s is %.2f%n",
                            dept, deptSvc.getAverageSalary(dept));
                }
                else if ((m = COUNT.matcher(line)).matches()) {
                    String dept = stripDot(m.group(1));
                    System.out.println(deptSvc.getEmployeeCount(dept));
                }
                else if ((m = SEARCH.matcher(line)).matches()) {
                    String tpl = stripDot(m.group(1));
                    System.out.println(String.join(", ", lectorSvc.globalSearch(tpl)));
                }
                else {
                    System.out.println("Unknown command. Type 'exit' to quit.");
                }
            }
            catch (NoSuchElementException e) {
                System.out.println("Department not found: " + e.getMessage());
            }
        }

        in.close();
    }

    private String stripDot(String s) {
        return s.endsWith(".") ? s.substring(0, s.length()-1).trim() : s.trim();
    }
}

