//unit4.hw
//Group members: Joshua Herrera, Justin Stefanski and Enoch Kho
// 2/12/24

// Imports
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class DriverClass {

	private static College valenceCollege;

	private static String mainMenu() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("\nChoose from the following options:");
		System.out.println("1- Add a new student");
		System.out.println("2- Add/Delete a course");
		System.out.println("3- Search for a student");
		System.out.println("4- Print fee invoice");
		System.out.println("5- Print fee invoice sorted by crn");
		System.out.println("0- Exit program");
		System.out.print("\nEnter your selection: ");
		return scanner.nextLine().trim();
	}

	private static String subMenu(String studentName) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("\nChoose from:");
		System.out.println("A- Add a new course for [" + studentName + "]");
		System.out.println("D- Delete a course from [" + studentName + "]'s schedule");
		System.out.println("C- Cancel operation");
		System.out.print("\nEnter your selection: ");
		return scanner.nextLine().trim();
	}

	public static void main(String[] args) {
		valenceCollege = new College();

		Scanner input = new Scanner(System.in);
		String choose;

		do {
			choose = mainMenu();
			switch (choose) {
			case "1":
				System.out.print("\nEnter the student's id: ");
				int studentId = Integer.parseInt(input.nextLine().trim());
				if (valenceCollege.searchById(studentId)) {
					System.out.println("Sorry, " + studentId + " is already assigned to another student.");
				} else {
					System.out.print("Enter student's name: ");
					String studentName = input.nextLine().trim();
					System.out.print("\nEnter how many courses " + studentName + " is taking? ");
					int numOfCourses = Integer.parseInt(input.nextLine().trim());
					System.out.println("\nEnter the " + numOfCourses + " course numbers ");

					ArrayList<Integer> listOfCrns = new ArrayList<>();
					String[] numbersAsString = input.nextLine().split("\\s+");

					for (String numberAsString : numbersAsString) {
						try {
							int number = Integer.parseInt(numberAsString);
							listOfCrns.add(number);
						} catch (NumberFormatException e) {
							System.out.println("invalid input: " + numberAsString + ". skipping.");
						}
					}

					System.out.print("\nEnter " + studentName + "'s current gpa: ");
					double gpa = Double.parseDouble(input.nextLine().trim());
					Student tmpStudent = new Student(studentName, studentId, gpa, listOfCrns);
					valenceCollege.enrollStudent(tmpStudent);

					if (gpa >= 3.5 && tmpStudent.calculateTotalPaymentWithoutDiscount() > 700) {

						System.out.println("(" + studentName + " is eligible for the 25% discount)");
					} else {
						System.out.println("(" + studentName + " is not eligible for the 25% discount)");
					}

					System.out.println("\nStudent added successfully!");
				}
				break;

			case "2":
				System.out.print("\nEnter the student's id: ");
				int id = Integer.parseInt(input.nextLine().trim());
				if (valenceCollege.searchById(id)) {
					System.out
							.println("\nHere are the courses " + valenceCollege.getStudentNameById(id) + " is taking:");
					valenceCollege.printCourses(id);
					String subChoice;
					do {
						subChoice = subMenu(valenceCollege.getStudentNameById(id));
						switch (subChoice.toUpperCase()) {
						case "A":
							System.out.print("\nEnter course number to add: ");
							int crnToAdd = Integer.parseInt(input.nextLine().trim());
							valenceCollege.addCourse(id, crnToAdd);
							break;

						case "D":
							System.out.print("\nEnter course number to delete: ");
							int crnToDelete = Integer.parseInt(input.nextLine().trim());
							boolean deleted = valenceCollege.deleteCourse(id, crnToDelete);
							if (deleted) {
								System.out.println("[" + crnToDelete + "] is deleted successfully!");
								System.out.print("\nWant to display new invoice? Y/N: ");
								String displayInvoice = input.nextLine().trim();
								if (displayInvoice.equalsIgnoreCase("Y")) {
									valenceCollege.printInvoice(id);
								}
							} else {
								System.out.println("Failed to delete course.");
							}
							break;

						case "C":
							break;

						default:
							System.out.println("Invalid option. Please try again.");
							break;
						}
					} while (!subChoice.equalsIgnoreCase("C"));
				} else {
					System.out.println("\nNo Student found!");
				}
				break;

			case "3":
				System.out.print("\nEnter the student's id: ");
				int searchId = Integer.parseInt(input.nextLine().trim());
				if (valenceCollege.searchById(searchId)) {
					valenceCollege.printInvoice(searchId);
				} else {
					System.out.println("\nNo student found with ID: " + searchId);
				}
				break;

			case "4":
				System.out.print("\nEnter the student's id: ");
				int invoiceId = Integer.parseInt(input.nextLine().trim());
				valenceCollege.printInvoice(invoiceId);
				break;

			case "5":
				System.out.print("\nEnter the student's id: ");
				int sortedInvoiceId = Integer.parseInt(input.nextLine().trim());
				valenceCollege.printSortedInvoice(sortedInvoiceId);
				break;

			case "0":
				System.out.println("\nGoodbye!");
				break;

			default:
				System.out.println("\nInvalid option. Please try again.");
				break;
			}
		} while (!choose.equals("0"));
		input.close();
	}
}

//___________________________________________________________________________________

class College {
	private ArrayList<Student> list;

	public College() {
		this.list = new ArrayList<>();
	}

	public void enrollStudent(Student student) {
		list.add(student);
	}

	public boolean searchById(int studentId) {
		for (Student student : list) {
			if (student.getStudentId() == studentId) {
				return true;
			}
		}
		return false;
	}

	public boolean addCourse(int studentId, int crn) {
		for (Student student : list) {
			if (student.getStudentId() == studentId) {
				student.getListOfCrns().add(crn);
				return true;
			}
		}
		return false;
	}

	public boolean deleteCourse(int studentId, int crn) {
		for (Student student : list) {
			if (student.getStudentId() == studentId) {
				return student.getListOfCrns().remove((Integer) crn);
			}
		}
		return false;
	}

	public void printInvoice(int studentId) {
		for (Student student : list) {
			if (student.getStudentId() == studentId) {
				student.printFeeInvoice();
				return;
			}
		}
		System.out.println("Student with ID " + studentId + " not found.");
	}

	public void printCourses(int studentId) {
		for (Student student : list) {
			if (student.getStudentId() == studentId) {
				student.printCourseDetails();
				return;
			}
		}
		System.out.println("Student with ID " + studentId + " not found.");
	}

	public void printSortedInvoice(int studentId) {
		for (Student student : list) {
			if (student.getStudentId() == studentId) {
				ArrayList<Integer> sortedCrns = new ArrayList<>(student.getListOfCrns());
				Collections.sort(sortedCrns);
				student.setListOfCrns(sortedCrns);
				student.printFeeInvoice();
				return;
			}
		}
		System.out.println("Student with ID " + studentId + " not found.");
	}

	public String getStudentNameById(int studentId) {
		for (Student student : list) {
			if (student.getStudentId() == studentId) {
				return student.getStudentName();
			}
		}
		return null;
	}

}

//_______________________________________________________________________________________________________

class Student {
	private int studentId;
	private String studentName;
	private double gpa;
	private ArrayList<Integer> listOfCrns;

	public Student(String studentName, int studentId, double gpa, ArrayList<Integer> listOfCrns) {
		this.studentId = studentId;
		this.studentName = studentName;
		this.gpa = gpa;
		this.listOfCrns = listOfCrns;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public double getGpa() {
		return gpa;
	}

	public void setGpa(double gpa) {
		this.gpa = gpa;
	}

	public ArrayList<Integer> getListOfCrns() {
		return listOfCrns;
	}

	public void setListOfCrns(ArrayList<Integer> listOfCrns) {
		this.listOfCrns = listOfCrns;
	}

	public double calculateTotalPaymentWithoutDiscount() {
		double totalPayment = 0;
		for (int crn : listOfCrns) {
			totalPayment += getCourseCost(crn);
		}
		totalPayment += 35;

		return totalPayment;
	}

	// printout the feeInvoice
	public void printFeeInvoice() {
		System.out.println("VALENCE COLLEGE\nORLANDO FL 10101");
		System.out.println("---------------------");
		System.out.println("Fee Invoice Prepared for Student: ");
		System.out.println(studentId + "-" + studentName);
		System.out.println();
		System.out.println("1 Credit Hour = $120.25");
		System.out.println();

		double totalPayment = calculateTotalPaymentWithoutDiscount();
		printCourseDetails();
		System.out.println("\n                     Health & id fees $35.00");
		System.out.println("--------------------------------------");

		System.out.println("                              $ " + String.format("%.2f", totalPayment));
		System.out.println("                             -$ " + String.format("%.2f", totalPayment * 0.25));

		System.out.println("                             ----------\n               Total Payments $ "
				+ String.format("%.2f", (totalPayment - 0.25 * totalPayment)));
	}

	// printout specific detail for the course
	public void printCourseDetails() {

		System.out.println("CRN    CR_PREFIX     CR_HOURS");
		for (int crn : listOfCrns) {
			System.out.printf("%-6d %-13s %-13d $%8.2f\n", crn, getCourseName(crn), getCreditHours(crn),
					getCourseCost(crn));
		}
	}

	String getCourseName(int crn) {
		switch (crn) {
		case 4587:
			return "MAT 236";
		case 4599:
			return "COP 220";
		case 8997:
			return "GOL 124";
		case 9696:
			return "COP 100";
		case 4580:
			return "MAT 136";
		case 2599:
			return "COP 260";
		case 1997:
			return "CAP 424";
		case 3696:
			return "KOL 110";
		default:
			return "Unknown";
		}
	}

	int getCreditHours(int crn) {
		switch (crn) {
		case 4587:
			return 4;
		case 4599:
		case 9696:
		case 2599:
			return 3;
		case 8997:
		case 4580:
		case 1997:
			return 1;
		case 3696:
			return 2;
		default:
			return 0;
		}
	}

	double getCourseCost(int crn) {
		return getCreditHours(crn) * 120.25;
	}

}