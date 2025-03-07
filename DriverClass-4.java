//unit5.hw
//Group members:  Joshua Herrera, Justin Stefanski and Enoch Kho

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Course {
    int crn;
    String name;
    int creditHours;
    boolean isGraduateLevel;

    Course(int crn, String name, int creditHours, boolean isGraduateLevel) {
        this.crn = crn;
        this.name = name;
        this.creditHours = creditHours;
        this.isGraduateLevel = isGraduateLevel;
    }
}

class CourseDatabase {
    static Map<Integer, Course> courses = new HashMap<>();

    static {
        courses.put(4587, new Course(4587, "MAT 236", 4, false));
        courses.put(4599, new Course(4599, "COP 220", 3, false));
        courses.put(7587, new Course(7587, "MAT 936", 5, true));
        courses.put(8997, new Course(8997, "GOL 124", 1, true));
        courses.put(9696, new Course(9696, "COP 100", 3, true));
        courses.put(4580, new Course(4580, "MAT 136", 1, false));
        courses.put(2599, new Course(2599, "COP 260", 3, false));
        courses.put(1997, new Course(1997, "CAP 424", 1, false));
        courses.put(5696, new Course(5696, "KOL 110", 2, true));
        courses.put(1599, new Course(1599, "COP 111", 3, false));
        courses.put(6997, new Course(6997, "GOL 109", 1, true));
        courses.put(2696, new Course(2696, "COP 101", 3, false));
        courses.put(5580, new Course(5580, "MAT 636", 2, true));
        courses.put(2099, new Course(2099, "COP 268", 3, false));
        courses.put(4997, new Course(4997, "CAP 427", 1, false));
        courses.put(3696, new Course(3696, "KOL 910", 2, false));
    }

    static Course getCourse(int crn) {
        return courses.getOrDefault(crn, null);
    }
}

abstract class Student {
    String name;
    String id;
    List<Course> courses = new ArrayList<>();

    Student(String name, String id, int[] courseCrns) {
        this.name = name;
        this.id = id;
        for (int crn : courseCrns) {
            Course course = CourseDatabase.getCourse(crn);
            if (course != null) {
                this.courses.add(course);
            }
        }
    }

    abstract void printInvoice();
}

class UndergraduateStudent extends Student {
    double gpa;
    boolean isResident;

    UndergraduateStudent(String name, String id, double gpa, boolean isResident, int[] courseCrns) {
        super(name, id, courseCrns);
        this.gpa = gpa;
        this.isResident = isResident;
    }

    @Override
    void printInvoice() {
    	double totalCost = 35.0; // Health & ID fees
        double courseCost = 0.0;
        System.out.println("VALENCE COLLEGE\nORLANDO FL 10101");
        System.out.println("---------------------");
        System.out.println("Fee Invoice Prepared for Student: " + this.id + "-" + this.name);
        System.out.println(isResident ? "(FL resident, GPA: " + gpa + ")" : "(Non-resident, GPA: " + gpa + ")");
        System.out.println("1 Credit Hour = $120.25\n");
        for (Course course : this.courses) {
            double cost = (isResident ? 120.25 : 240.50) * course.creditHours;
            courseCost += cost;
            System.out.printf("%d\t%s\t%d\t\t$%.2f\n", course.crn, course.name, course.creditHours, cost);
        }
        totalCost += courseCost;
        System.out.println("\tHealth & id fees\t$35.00");
        System.out.println("--------------------------------------");
        if (gpa >= 3.5 && totalCost > 500) {
            double discount = totalCost * 0.25;
            System.out.println("\tDiscount\t-$" + String.format("%.2f", discount));
            totalCost -= discount;
        }
        System.out.println("Total Payments: $" + String.format("%.2f", totalCost));
    }
}
    

abstract class GraduateStudent extends Student {
    GraduateStudent(String name, String id, int[] courseCrns) {
        super(name, id, courseCrns);
    }

    @Override
    abstract void printInvoice();
}

class MsStudent extends GraduateStudent {
    MsStudent(String name, String id, int[] courseCrns) {
        super(name, id, courseCrns);
    }

    @Override
    void printInvoice() {
    	double totalCost = 35.0; // Health & ID fees
        System.out.println("VALENCE COLLEGE\nORLANDO FL 10101");
        System.out.println("---------------------");
        System.out.println("Fee Invoice Prepared for Student: " + this.id + "-" + this.name);
        System.out.println("1 Credit Hour = $300.00\n");
        for (Course course : this.courses) {
            double cost = 300 * course.creditHours;
            System.out.printf("%d\t%s\t%d\t\t$%.2f\n", course.crn, course.name, course.creditHours, cost);
            totalCost += cost;
        }
        System.out.println("\tHealth & id fees\t$35.00");
        System.out.println("--------------------------------------");
        System.out.println("Total Payments: $" + String.format("%.2f", totalCost));
    }
}
    

class PhdStudent extends GraduateStudent {
    String researchTopic;
    int taCourseCRN; // Course CRN for teaching assistantship

    PhdStudent(String name, String id, String researchTopic, int taCourseCrns) {
    	super(name, id, new int[0]); //PhD students don't take courses so we pass an empty array
        this.researchTopic = researchTopic;
        this.taCourseCRN = taCourseCRN;
    }

    @Override
    void printInvoice() {
    	double totalCost = 700.0 + 35.0; // Research fee + Health & ID fees
        System.out.println("VALENCE COLLEGE\nORLANDO FL 10101");
        System.out.println("---------------------");
        System.out.println("Fee Invoice Prepared for Student: " + this.id + "-" + this.name);
        System.out.println("Research Topic: " + this.researchTopic);
        System.out.println("\tResearch fee\t\t$700.00");
        System.out.println("\tHealth & id fees\t$35.00");
        System.out.println("--------------------------------------");
        System.out.println("Total Payments: $" + String.format("%.2f", totalCost));
    }
}

class College {
    private List<Student> students = new ArrayList<>();

    public void enrollStudent(Student student) {
        this.students.add(student);
    }

    public Student findStudentById(String id) {
        for (Student student : students) {
            if (student.id.equals(id)) {
                return student;
            }
        }
        return null;
    }

    public void printInvoiceForStudent(String id) {
        Student student = findStudentById(id);
        if (student != null) {
            student.printInvoice();
        } else {
            System.out.println("Student with ID " + id + " not found.");
        }
    }
}

public class DriverClass {
    private static College valenceCollege = new College();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n1. Add new student");
            System.out.println("2. Print fee invoice for a student");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    printInvoice();
                    break;
                case 0:
                    System.out.println("Exiting program.");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }
    }

    private static void addStudent() {
        System.out.print("Enter student type (Undergraduate/Ms/Phd): ");
        String type = scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
 
  
        if ("Undergraduate".equalsIgnoreCase(type)) {
            System.out.print("Enter GPA: ");
            double gpa = scanner.nextDouble();
            System.out.print("Is Florida resident? (true/false): ");
            boolean isResident = scanner.nextBoolean();
            scanner.nextLine(); 
            
            int[] courseCrns = {4587, 4599};
            UndergraduateStudent student = new UndergraduateStudent(name, id, gpa, isResident, courseCrns);
            valenceCollege.enrollStudent(student);
            System.out.println("Undergraduate student added successfully.");
        } else if ("Ms".equalsIgnoreCase(type)) {
            System.out.println("Enter number of courses: ");
            int numCourses = scanner.nextInt();
            scanner.nextLine();
            int[] coursesCrns = new int [numCourses];
            System.out.println("Enter course CRNs: ");
            for (int i = 0; i < numCourses; i++) {
            	coursesCrns[i] = scanner.nextInt ();
            }
            scanner.nextLine();
            
            MsStudent student = new MsStudent(name, id, coursesCrns);
            valenceCollege.enrollStudent(student);
            System.out.println("MS student added successfully.");
            
        } else if ("Phd".equalsIgnoreCase(type)) {
            System.out.println("Enter research topic: ");
            String researchTopic = scanner.nextLine();
            System.out.print("Enter course CRN for teaching assistantship: ");
            int crn = scanner.nextInt();
            scanner.nextLine();
            
            PhdStudent student = new PhdStudent(name, id, researchTopic, crn);
            valenceCollege.enrollStudent(student);
            System.out.println("PhD student added successfully.");
        } else {
            System.out.println("Invalid student type entered.");
        }
    }

    private static void printInvoice() {
        System.out.print("Enter student ID for invoice: ");
        String id = scanner.nextLine();
        valenceCollege.printInvoiceForStudent(id);
    }
}