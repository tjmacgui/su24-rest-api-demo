package com.csc340.restapidemo;

import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RestApiController {

    Map<Integer, Student> studentDatabase = new HashMap<>();
    private static final String FILE_PATH = "students.txt";

    public RestApiController() {
        loadStudents();
    }
    /**
     * List all students.
     *
     * @return the list of students.
     */
    @GetMapping("students/all")
    public Object getAllStudents() {
        if (studentDatabase.isEmpty()) {
            studentDatabase.put(1, new Student(1, "sample1", "csc", 3.86));
        }
        return studentDatabase.values();
    }

    /**
     * Get one student by Id
     *
     * @param id the unique student id.
     * @return the student.
     */
    @GetMapping("students/{id}")
    public Student getStudentById(@PathVariable int id) {
        return studentDatabase.get(id);
    }


    /**
     * Create a new Student entry.
     *
     * @param student the new Student
     * @return the List of Students.
     */
    @PostMapping("students/create")
    public Object createStudent(@RequestBody Student student) {
        studentDatabase.put(student.getId(), student);
        saveStudents();
        return studentDatabase.values();
    }

    /**
     * Delete a Student by id
     *
     * @param id the id of student to be deleted.
     * @return the List of Students.
     */
    @DeleteMapping("students/delete/{id}")
    public Object deleteStudent(@PathVariable int id) {
        studentDatabase.remove(id);
        saveStudents();
        return studentDatabase.values();
    }

    private void loadStudents() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Student student = Student.fromString(line);
                    studentDatabase.put(student.getId(), student);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveStudents() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Student student : studentDatabase.values()) {
                writer.write(student.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

