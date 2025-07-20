package hellojava;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        StudentRepository repo = new StudentRepository();

        // adding a new student
        Student student1 = new Student("Elif", "Computer Engineering");
        repo.save(student1);
        System.out.println("New student added: " + student1.getName());

        // finding a student by ID
        Student found = repo.findById(student1.getId());
        System.out.println("Student found by ID: " + found.getName());

        // list all students
        List<Student> allStudents = repo.findAll();
        System.out.println("All students:");
        for (Student s : allStudents) {
            System.out.println(s.getId() + " - " + s.getName() + " (" + s.getDepartment() + ")");
        }

        // updating student info
        found.setDepartment("Software Engineering");
        repo.update(found);
        System.out.println("Student updated: " + found.getName() + " (" + found.getDepartment() + ")");

        // deleting the student
        repo.delete(found.getId());
        System.out.println("Student deleted: " + found.getId());
    }
}
