package com.andre.university.console;

import com.andre.university.model.Degree;
import com.andre.university.service.DepartmentService;
import com.andre.university.service.LectorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ConsoleRunner implements CommandLineRunner {
    private static final String HELP = """
      Available commands:
        - Who is head of department {department_name}
        - Show {department_name} statistics
        - Show the average salary for the department {department_name}
        - Show count of employee for {department_name}
        - Global search by {template}
        - help (will print this message)
        - exit
      """;

    private static final Pattern HEAD_PATTERN = Pattern.compile("^Who is head of department (.+)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern STATS_PATTERN = Pattern.compile("^Show (.+) statistics$", Pattern.CASE_INSENSITIVE);
    private static final Pattern AVG_SALARY_PATTERN = Pattern.compile("^Show the average salary for the department (.+)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern COUNT_EMPLOYEE_PATTERN = Pattern.compile("^Show count of employee for (.+)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern GLOBAL_SEARCH_PATTERN = Pattern.compile("^Global search by (.+)$", Pattern.CASE_INSENSITIVE);

    private final DepartmentService departmentService;
    private final LectorService lectorService;
    private final Map<Pattern, Consumer<Matcher>> commandActions;

    public ConsoleRunner(DepartmentService departmentService, LectorService lectorService) {
        this.departmentService = departmentService;
        this.lectorService = lectorService;
        this.commandActions = new LinkedHashMap<>();
        initializeCommandActions();
    }

    private void initializeCommandActions() {
        commandActions.put(HEAD_PATTERN, matcher -> {
            String departmentName = stripDot(matcher.group(1));
            System.out.printf("Head of %s department is %s%n",
                    departmentName, departmentService.getHeadOfDepartment(departmentName));
        });

        commandActions.put(STATS_PATTERN, matcher -> {
            String departmentName = stripDot(matcher.group(1));
            Map<Degree, Long> stats = departmentService.getStatistics(departmentName);
            System.out.printf("assistants - %d%n", stats.getOrDefault(Degree.ASSISTANT, 0L));
            System.out.printf("associate professors - %d%n", stats.getOrDefault(Degree.ASSOCIATE_PROFESSOR, 0L));
            System.out.printf("professors - %d%n", stats.getOrDefault(Degree.PROFESSOR, 0L));
        });

        commandActions.put(AVG_SALARY_PATTERN, matcher -> {
            String departmentName = stripDot(matcher.group(1));
            System.out.printf("The average salary of %s is %.2f%n",
                    departmentName, departmentService.getAverageSalary(departmentName));
        });

        commandActions.put(COUNT_EMPLOYEE_PATTERN, matcher -> {
            String departmentName = stripDot(matcher.group(1));
            System.out.println(departmentService.getEmployeeCount(departmentName));
        });

        commandActions.put(GLOBAL_SEARCH_PATTERN, matcher -> {
            String template = stripDot(matcher.group(1));
            System.out.println(String.join(", ", lectorService.globalSearch(template)));
        });
    }

    @Override
    public void run(String... args) {
        System.out.println(HELP);
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");
            String line = in.nextLine().trim();

            if (line.equalsIgnoreCase("exit")) {
                break;
            }

            if (line.equalsIgnoreCase("help")) {
                System.out.println(HELP);
                continue;
            }

            boolean commandHandled = false;
            for (Map.Entry<Pattern, Consumer<Matcher>> entry : commandActions.entrySet()) {
                Matcher matcher = entry.getKey().matcher(line);
                if (matcher.matches()) {
                    try {
                        entry.getValue().accept(matcher);
                    } catch (NoSuchElementException e) {
                        System.out.println("Department not found or no data for: " + matcher.group(1));
                    } catch (Exception e) {
                        System.err.println("Error occurred: " + e.getMessage());
                    }
                    commandHandled = true;
                    break;
                }
            }

            if (!commandHandled) {
                System.out.println("Unknown command.");
            }
        }

        in.close();
        System.out.println("Exiting app.");
    }

    private String stripDot(String s) {
        if (s == null) {
            return "";
        }
        String trimmed = s.trim();
        return trimmed.endsWith(".") ? trimmed.substring(0, trimmed.length() - 1).trim() : trimmed;
    }
}