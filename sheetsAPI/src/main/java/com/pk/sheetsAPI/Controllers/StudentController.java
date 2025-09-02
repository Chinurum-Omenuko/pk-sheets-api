package com.pk.sheetsAPI.Controllers;

import com.pk.sheetsAPI.Models.Student;
import com.pk.sheetsAPI.Services.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class StudentController {
    StudentService _studentService;
    public StudentController(StudentService studentService){
        this._studentService = studentService;
    }

    @GetMapping(path = "/students/{studentName}")
    public Student getStudentByName(@PathVariable String studentName) throws Exception {
        return _studentService.getStudentByName(studentName);
    }

    @GetMapping(path = "/students")
    public List<Student> getStudents() throws Exception {
        return _studentService.getStudents();
    }


    @PostMapping("/students")
    public String addStudent(@RequestBody Student newStudent) throws Exception {
        return _studentService.addStudent(newStudent);
    }

    @PutMapping(path = "/students/{studentName}")
    public void updateStudent(@RequestBody Student updatedStudent) throws Exception{
        _studentService.updateStudent(updatedStudent);
    }


}
