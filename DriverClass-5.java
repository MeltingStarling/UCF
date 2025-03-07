//unit9.hw
//Group members:  Joshua Herrera, Justin Stefanski

import java.util.Scanner;

public class DriverClass {
    public static void main(String[] args) {
        LinkedList list = new LinkedList();
        list.addNewEmployee(new Employee("Kim Oz", 1235.5, 3));
        list.addNewEmployee(new Employee("Rim Oz", 8235.5, 1));
        list.addNewEmployee(new Employee("Dane Ali", 3235.5, 0));
        list.addNewEmployee(new Employee("Aidan Jones", 2035.5, 2));
        list.addNewEmployee(new Employee("Nadia Jones", 5035.5, 3));
        list.addNewEmployee(new Employee("Ed Renu", 6035, 2));
        list.addNewEmployee(new Employee("Naadi Jones", 36035.75, 5));
        
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("Choose an option:");
            System.out.println("1. Print all employees");
            System.out.println("2. Add a new employee");
            System.out.println("3. Search for an employee by name");
            System.out.println("4. Find the highest net salary");
            System.out.println("5. Delete an employee by name");
            System.out.println("6. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    list.printAllEmployees();
                    break;
                case 2:
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter salary: ");
                    double salary = scanner.nextDouble();
                    System.out.print("Enter number of dependents: ");
                    int numberOfDependents = scanner.nextInt();
                    list.addNewEmployee(new Employee(name, salary, numberOfDependents));
                    break;
                case 3:
                    System.out.print("Enter name to search: ");
                    String searchName = scanner.nextLine();
                    if (list.searchByName(searchName)) {
                        System.out.println(searchName + " found.");
                    } else {
                        System.out.println(searchName + " not found.");
                    }
                    break;
                case 4:
                    System.out.println("The highest net salary = " + list.highestNetSalary());
                    break;
                case 5:
                    System.out.print("Enter name to delete: ");
                    String deleteName = scanner.nextLine();
                    list.deleteEmployeeByName(deleteName);
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
        scanner.close();
    }
}

class LinkedList {
    Node company;

    public LinkedList() {
        company = null;
    }

    public void printAllEmployees() {
        Node current = company;
        while (current != null) {
            Node innerCurrent = current;
            while (innerCurrent != null) {
                System.out.println(innerCurrent.e);
                innerCurrent = innerCurrent.below;
            }
            current = current.next;
        }
    }

    public void addNewEmployee(Employee e) {
        int id = e.getId();
        Node newNode = new Node(e);
        if (company == null) {
            company = newNode;
            return;
        }
        Node current = company;
        Node prev = null;
        while (current != null) {
            if (current.e.getId() == id) {
                while (current.below != null) {
                    current = current.below;
                }
                current.below = newNode;
                return;
            }
            prev = current;
            current = current.next;
        }
        prev.next = newNode;
    }

    public boolean searchByName(String name) {
        Node current = company;
        while (current != null) {
            Node innerCurrent = current;
            while (innerCurrent != null) {
                if (innerCurrent.e.getName().equals(name)) {
                    return true;
                }
                innerCurrent = innerCurrent.below;
            }
            current = current.next;
        }
        return false;
    }

    public double highestNetSalary() {
        double maxNetSalary = Double.MIN_VALUE;
        Node current = company;
        while (current != null) {
            Node innerCurrent = current;
            while (innerCurrent != null) {
                double netSalary = innerCurrent.e.calculateNetSalary();
                if (netSalary > maxNetSalary) {
                    maxNetSalary = netSalary;
                }
                innerCurrent = innerCurrent.below;
            }
            current = current.next;
        }
        return maxNetSalary;
    }

    public void deleteEmployeeByName(String name) {
        Node current = company;
        Node prev = null;
        while (current != null) {
            Node innerCurrent = current;
            Node innerPrev = null;
            while (innerCurrent != null) {
                if (innerCurrent.e.getName().equals(name)) {
                    if (innerPrev == null) {
                        if (prev == null) {
                            company = current.next;
                        } else {
                            prev.next = current.next;
                        }
                    } else {
                        innerPrev.below = innerCurrent.below;
                    }
                    return;
                }
                innerPrev = innerCurrent;
                innerCurrent = innerCurrent.below;
            }
            prev = current;
            current = current.next;
        }
    }
}

class Employee {
    private String name;
    private int id;
    private int numberOfDependents;
    private double salary;

    public Employee(String name, double salary, int numberOfDependents) {
        this.name = name;
        this.salary = salary;
        this.numberOfDependents = numberOfDependents;
        this.id = calculateId(name.toUpperCase());
    }

    private int calculateId(String name) {
        int sum = 0;
        for (char c : name.toCharArray()) {
            sum += (int) c;
        }
        return sum;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double calculateNetSalary() {
        return salary * 0.91 + (numberOfDependents * 0.01 * salary);
    }

    @Override
    public String toString() {
        return "[" + id + "," + name + "," + calculateNetSalary() + "]";
    }
}

class Node {
    Employee e;
    Node next;
    Node below;

    public Node(Employee e) {
        this.e = e;
        this.next = null;
        this.below = null;
    }
}