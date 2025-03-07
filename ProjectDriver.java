//Final Project
//Group members:  Brendan Durante, Joshua Herrera, Justin Stefanski, Enoch Kho

// Import statements
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.*;
import java.nio.file.*;

public class ProjectDriver {
    static Scanner scanner = new Scanner(System.in); // Single global scanner for input
    static ArrayList<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        try {
            mainMenu();
        } finally {
            scanner.close(); // Ensure the scanner is closed when the program exits
        }
    }

    public static void mainMenu() {
        while (true) {
            System.out.println("---------------------------------------------------\n");
            System.out.println("Main Menu\n");
            System.out.println("1 : Student Management");
            System.out.println("2 : Course Management");
            System.out.println("0 : Exit");
            System.out.print("Enter your selection: ");

            try {
                if (scanner.ioException() != null) {
                    throw scanner.ioException();
                }
            } catch (IOException e) {
                System.out.println("Input error: " + e.getMessage());
                scanner = new Scanner(System.in);
            }

            String input = scanner.nextLine();

            int userInput;
            try {
                userInput = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (userInput) {
                case 1:
                    studentManagement();
                    break;
                case 2:
                    courseManagement();
                    break;
                case 0:
                    System.out.println("Take Care!");
                    return;
                default:
                    System.out.println("Invalid option selected. Please try again.");
                    break;
            }
        }
    }

    public static void studentManagement() {
        while (true) {
            System.out.println("Student Management Selected");
            System.out.println("Student Management Menu:\n");

            System.out.println("Choose one of:\n");
            System.out.println("A - Search and add a student");
            System.out.println("B - Delete a Student");
            System.out.println("C - Print Fee Invoice");
            System.out.println("D - Print List of Students");
            System.out.println("X - Back to Main Menu");

            System.out.print("Enter your selection: ");
            String variable = scanner.nextLine();

            switch (variable.toUpperCase()) {
                case "A":
                    searchAddStudent();
                    break;
                case "B":
                    deleteStudent();
                    break;
                case "C":
                    printFeeInvoice();
                    break;
                case "D":
                    printAllStudents();
                    break;
                case "X":
                    return;
                default:
                    System.out.println("Invalid option selected");
                    break;
            }
        }
    }

    public static void searchAddStudent() {
        System.out.println("Enter Student's ID: ");
        String studentID = scanner.nextLine();

        if (isStudentIDExists(studentID)) {
            System.out.println("Student with ID " + studentID + " already exists.");
        } else {
            System.out.println("Student Type (PHD, MS, or UNDERGRAD): ");
            String studentType = scanner.nextLine();

            System.out.println("Enter Student Name: ");
            String studentName = scanner.nextLine();

            System.out.println("Enter Student GPA: ");
            double studentGPA;
            while (true) {
                try {
                    studentGPA = Double.parseDouble(scanner.nextLine()); // Use parseDouble on nextLine to handle
                                                                         // exceptions better
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid GPA. Please enter a valid number.");
                }
            }

            List<Course> courses = new ArrayList<>();

            switch (studentType.toUpperCase()) {
                case "PHD":
                    System.out.println("Enter Advisor's Name: ");
                    String advisor = scanner.nextLine();

                    System.out.println("Enter Research Subject: ");
                    String researchSubject = scanner.nextLine();

                    System.out.println("Enter the number of labs supervised: ");
                    int labsSupervised;
                    while (true) {
                        try {
                            labsSupervised = Integer.parseInt(scanner.nextLine());
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input for the number of labs. Please enter a valid number.");
                        }
                    }

                    students.add(new PhdStudent(studentName, studentID, courses, advisor, researchSubject,
                            studentGPA, labsSupervised));
                    break;
                case "MS":
                    students.add(new MsStudent(studentName, studentID, courses, studentName, studentName, studentGPA));
                    break;
                case "UNDERGRAD":
                    students.add(new UndergraduateStudent(studentName, studentID, courses, studentGPA, false));
                    break;
                default:
                    System.out.println("Invalid student type entered.");
                    break;
            }
            System.out.println("Student added successfully!");
        }
    }

    public static boolean isStudentIDExists(String studentID) {
        for (Student student : students) {
            if (student.getId().equals(studentID)) {
                return true;
            }
        }
        return false;
    }

    public static void deleteStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Student's ID to delete: ");
        String studentID = scanner.nextLine();
        boolean removed = false;

        Iterator<Student> iterator = students.iterator();
        while (iterator.hasNext()) {
            Student student = iterator.next();
            if (student.getId().equals(studentID)) {
                iterator.remove();
                removed = true;
                break;
            }
        }

        if (removed) {
            System.out.println("Student with ID " + studentID + " deleted successfully.");
        } else {
            System.out.println("No student found with ID " + studentID + ".");
        }
    }

    public static void printFeeInvoice() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Student's ID to print fee invoice: ");
        String studentID = scanner.nextLine();
        boolean found = false;

        for (Student student : students) {
            if (student.getId().equals(studentID)) {
                student.printInvoice();
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("No student found with ID " + studentID + ".");
        }
    }

    public static void printAllStudents() {
        System.out.println("PhD Students");
        System.out.println("------------");
        for (Student student : students) {
            if (student instanceof PhdStudent) {
                System.out.println(student.getName());
            }
        }

        System.out.println("\nMS Students");
        System.out.println("------------");
        for (Student student : students) {
            if (student instanceof MsStudent) {
                System.out.println(student.getName());
            }
        }

        System.out.println("\nUndergraduate Students");
        System.out.println("------------");
        for (Student student : students) {
            if (student instanceof UndergraduateStudent) {
                System.out.println(student.getName());
            }
        }
    }

    // Beginning of CourseManagement Method
    public static void courseManagement() {
        // Implementation of course management
        Scanner newInput = new Scanner(System.in);
        System.out.println("Course Management Selected");
        System.out.println("Course Management Menu:\n");
        System.out.println("Choose one of:\n");
        System.out.println("\nA - Search for a class or lab using the class/lab number");
        System.out.println("\nB - delete a class");
        System.out.println("\nC - Add a lab to a class");
        System.out.println("\nX - Back to main menu");
        String thisInput = newInput.nextLine();

        switch (thisInput.toUpperCase()) {
            case "A":
                searchByNumber();
                break;
            case "B":
                deleteClass();
                break;
            case "C":
                addLabToClass();
                break;
            case "X":
                mainMenu();
                break;
            default:
                System.out.println("Invalid Option selected");
        }
    }

    public static void searchByNumber() {
        System.out.println("Enter the Class/Lab Number: ");
        String labNum = scanner.nextLine().trim();

        File file = new File("lec.txt");
        Scanner fileScanner = null;
        try {
            fileScanner = new Scanner(file);

            boolean found = false; // checks for class/lab number
            String output = "";
            boolean hasLab = false; // checks if it's a lecture with a lab

            // reads file
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] details = line.split(",");

                // check if the first element is the labNum
                if (details[0].trim().equals(labNum)) {
                    found = true;
                    output = line;

                    // checks for lab
                    if (details.length > 6 && details[6].trim().equalsIgnoreCase("YES")) {
                        hasLab = true;
                    }
                    break;
                }
            }

            if (found) {
                System.out.println("Found entry: " + output);
                if (hasLab) {
                    System.out.println("Labs:");
                    while (fileScanner.hasNextLine()) {
                        String labLine = fileScanner.nextLine();
                        if (labLine.split(",").length < 5) { // assuming lab entries have fewer fields
                            System.out.println(labLine);
                        } else {
                            break;
                        }
                    }
                }
            } else {
                System.out.println("No class or lab found with number: " + labNum);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } finally {
            if (fileScanner != null) {
                fileScanner.close();
            }
        }
    }

    public static void deleteClass() {
        System.out.println("Enter the Class Number to delete: ");
        String classNumber = scanner.nextLine().trim();
        List<String> linesToKeep = new ArrayList<>();
        boolean found = false;

        try {
            File file = new File("lec.txt");
            Scanner fileScanner = new Scanner(file);
            boolean deleteMode = false;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] details = line.split(",");

                if (details[0].trim().equals(classNumber)) {
                    found = true;
                    if (details.length > 6 && details[6].trim().equalsIgnoreCase("YES")) {
                        deleteMode = true;
                    }
                    continue;
                }

                if (deleteMode && details.length < 5) {
                    continue;
                } else {
                    deleteMode = false;
                }

                linesToKeep.add(line);
            }

            fileScanner.close();

            if (found) {
                PrintWriter writer = new PrintWriter(file);
                for (String keepLine : linesToKeep) {
                    writer.println(keepLine);
                }
                writer.close();
                System.out.println("Class and any associated labs successfully deleted.");
            } else {
                System.out.println("No class found with the number: " + classNumber);
            }
        } catch (FileNotFoundException e) {
            System.out.println("The file could not be found.");
        }
    }

    public static void addLabToClass() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter the Class Number of the class to add labs to: ");
            String classNumberToFind = scanner.nextLine();

            Path filePath = Paths.get("lec.txt");
            List<String> fileContent = new ArrayList<>(Files.readAllLines(filePath));
            List<String> updatedContent = new ArrayList<>();

            boolean classFound = false;
            for (String line : fileContent) {
                if (line.startsWith(classNumberToFind + ",")) {
                    System.out.println("Class found: " + line);
                    classFound = true;
                    updatedContent.add(line); // adds class to first line

                    if (line.split(",")[6].equalsIgnoreCase("YES")) {
                        boolean moreLabs;
                        do {
                            System.out.print("Enter Lab Class Number: ");
                            String labClassNumber = scanner.nextLine();

                            System.out.print("Enter Lab Location: ");
                            String labLocation = scanner.nextLine();

                            updatedContent.add(labClassNumber + "," + labLocation);

                            System.out.print("Are there more labs? (Yes/No): ");
                            moreLabs = "Yes".equalsIgnoreCase(scanner.nextLine());
                        } while (moreLabs);
                    } else {
                        System.out.println("This class does not support labs or is not marked for labs.");
                    }
                } else {
                    updatedContent.add(line);
                }
            }

            if (!classFound) {
                System.out.println("Class Number not found. Please check and try again.");
            } else {
                Files.write(filePath, updatedContent, StandardOpenOption.TRUNCATE_EXISTING);
                System.out.println("Lab(s) added successfully to 'lec.txt'.");
            }
        } catch (Exception e) {
            System.err.println("An error occurred while modifying the file: " + e.getMessage());
        }
    }
}

// ___________________________________________________________________________________________
// Represents a course or lab
class Course {
    private int crn;
    private String prefix;
    private String name;
    private String level; // Graduate or Undergrad
    private int creditHours;

    // constructors
    public Course(int crn, String prefix, String name, String level, int creditHours) {
        this.crn = crn;
        this.prefix = prefix;
        this.name = name;
        this.level = level;
        this.creditHours = creditHours;
    }

    // getters & Setters
    public int getCrn() {
        return crn;
    }

    public void setCrn(int crn) {
        this.crn = crn;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public double calculateCost() {
        double baseCostPerCredit = 120.25;
        return creditHours * baseCostPerCredit;
    }

}

// ___________________________________________________________________________________________
// Abstract class for a student
abstract class Student {
    private String name;
    private String id;
    private List<Course> courses = new ArrayList<>();
    public double healthAndIdFees = 35.00;
    public boolean inState = false;

    public Student(String name, String id, List<Course> courses) {
        this.name = name;
        this.id = id;
        this.courses = courses;
    }

    abstract public void printInvoice();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}

// ___________________________________________________________________________________________
// UndergraduateStudent class
class UndergraduateStudent extends Student {
    private double gpa;
    private boolean isResident;

    public UndergraduateStudent(String name, String id, List<Course> courses, double gpa, boolean isResident) {
        super(name, id, courses);
        this.gpa = gpa;
        this.isResident = isResident;
    }

    @Override
    public void printInvoice() {
        System.out.println("VALENCE COLLEGE");
        System.out.println("ORLANDO FL 10101");
        System.out.println("---------------------");
        System.out.println("Fee Invoice Prepared for Student:");
        System.out.println(getId() + "-" + getName() + " (" + (isResident ? "a FL resident, " : "")
                + "has GPA higher than 3.5 GPA)");
        System.out.println("1 Credit Hour = $120.25");
        System.out.println("CRN\tCR_PREFIX\tCR_HOURS");
        for (Course course : getCourses()) {
            System.out.println(course.getCrn() + "\t" + course.getPrefix() + "\t" + course.getCreditHours() + " $"
                    + String.format("%.2f", course.calculateCost()));
        }
        System.out.println("Health & ID fees $" + String.format("%.2f", +35.00));
        double total = calculateTotal(); // This method needs to be defined
        System.out.println("--------------------------------------");
        System.out.println("TOTAL PAYMENTS $" + String.format("%.2f", total));
    }

    private double calculateTotal() {
        double total = 0;

        for (Course course : getCourses()) {
            total += course.getCreditHours() * 120.25;
        }
        total += 35.00;

        // applys discount based on GPA and total amount condition
        if (gpa >= 3.5 && total > 500) {
            double discount = total * 0.25;
            total -= discount;
        }
        return total;
    }

    public boolean isResident() {
        return isResident;
    }

    public void setResident(boolean isResident) {
        this.isResident = isResident;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }
}

// ___________________________________________________________________________________________
// Abstract class for graduate students
abstract class GraduateStudent extends Student {
    private String advisor;
    private String researchSubject;
    private List<Integer> supervisedLabNum = new ArrayList<>();

    public GraduateStudent(String name, String id, List<Course> courses, String advisor, String researchSubject) {
        super(name, id, courses);
        this.advisor = advisor;
        this.researchSubject = researchSubject;
    }

    public int getNumberOfLabs() {
        return supervisedLabNum.size(); // returns the count of labs directly from the list
    }

    public void addLab(int labNumber) {
        supervisedLabNum.add(labNumber); // adds a lab number to the list
    }

    public List<Integer> getSupervisedLabNum() {
        return supervisedLabNum;
    }

    @Override
    public void printInvoice() {
        double baseCostPerCreditHour = 300.00; // defines the base cost per credit hour for graduate students.

        System.out.println("VALENCE COLLEGE");
        System.out.println("ORLANDO FL 10101");
        System.out.println("---------------------");
        System.out.println("Fee Invoice Prepared for Student:");
        System.out.println(getId() + "-" + getName());
        System.out.println("1 Credit Hour = $" + String.format("%.2f", baseCostPerCreditHour));
        System.out.println("CRN\tCR_PREFIX\tCR_HOURS\tCOST");

        double totalCost = 0.00;
        for (Course course : getCourses()) {
            double cost = course.getCreditHours() * baseCostPerCreditHour;
            System.out.println(course.getCrn() + "\t" + course.getPrefix() + "\t" + course.getCreditHours() + "\t$"
                    + String.format("%.2f", cost));
            totalCost += cost;
        }

        double healthAndIdFees = 35.00;
        totalCost += healthAndIdFees;

        System.out.println("Health & id fees $" + String.format("%.2f", healthAndIdFees));
        System.out.println("--------------------------------------");
        System.out.println("TOTAL PAYMENTS $" + String.format("%.2f", totalCost));
    }

    public double calculateTotal() {
        double total = 0;
        int researchFee = 700;

        for (Course course : getCourses()) {
            total += course.getCreditHours() * 300.00;
        }
        total += researchFee;
        total += 35.00;

        return total;
    }

    public String getAdvisor() {
        return advisor;
    }

    public void setAdvisor(String advisor) {
        this.advisor = advisor;
    }

    public String getResearchSubject() {
        return researchSubject;
    }

    public void setResearchSubject(String researchSubject) {
        this.researchSubject = researchSubject;
    }
}

// ___________________________________________________________________________________________
// MS student class
class MsStudent extends GraduateStudent {

    private double gpa;

    public MsStudent(String name, String id, List<Course> courses, String advisor, String researchSubject,
            double gpa) {
        super(name, id, courses, advisor, researchSubject);
        this.gpa = gpa;
    }

    @Override
    public double calculateTotal() {
        double total = 0;
        int researchFee = 700;

        for (Course course : getCourses()) {
            total += course.getCreditHours() * 300.00;
        }
        total += researchFee;
        total += 35.00;

        return total;
    }

    @Override
    public void printInvoice() {
        System.out.println("VALENCE COLLEGE");
        System.out.println("ORLANDO FL 10101");
        System.out.println("---------------------");
        System.out.println("Fee Invoice Prepared for Student:");
        System.out.println(getId() + " " + getName().split(" ")[0] + ".");
        System.out.println("Health & id fees $" + String.format("%.2f", +35.00));
        System.out.println("--------------------------------------");
        System.out.println("Total Payments $" + String.format("%.2f", calculateTotal()));
    }
}

// ___________________________________________________________________________________________
// PhD student class
class PhdStudent extends GraduateStudent {
    private List<Integer> labSupervised = new ArrayList<>();

    public PhdStudent(String studentName, String studentID, List<Course> courses, String advisor,
            String researchSubject, double studentGPA, int labsSupervised) {
        super(studentName, studentID, courses, advisor, researchSubject);
        for (int i = 0; i < labsSupervised; i++) {
            this.labSupervised.add(0);
        }
    }

    public List<Integer> getLabSupervised() {
        return labSupervised;
    }

    public void setLabSupervised(List<Integer> labSupervised) {
        this.labSupervised = labSupervised;
    }

    @Override
    public double calculateTotal() {
        double total = 0;

        // calculate total for courses
        for (Course course : getCourses()) {
            total += course.getCreditHours() * 300.00;
        }

        int researchFee = 700;
        if (getNumberOfLabs() >= 2) {
            researchFee /= 2;
        }
        total += researchFee;

        total += 35.00;

        // adds lab supervise discount
        if (labSupervised.size() >= 3) {
            total = 35.00;
        }

        return total;
    }

    @Override
    public void printInvoice() {
        System.out.println("VALENCE COLLEGE");
        System.out.println("ORLANDO FL 10101");
        System.out.println("---------------------");
        System.out.println("Fee Invoice Prepared for Student:");
        System.out.println(getId() + " " + getName().split(" ")[0] + "’s research subject: "
                + getResearchSubject());
        if (getLabSupervised().size() >= 3) {
            System.out.println(getName().split(" ")[0] + " supervised three labs or more.");
        }
        System.out.println("Health & ID fees $" + String.format("%.2f", +35.00));
        System.out.println("--------------------------------------");
        System.out.println("Total Payments $" + String.format("%.2f", calculateTotal()));
    }

}