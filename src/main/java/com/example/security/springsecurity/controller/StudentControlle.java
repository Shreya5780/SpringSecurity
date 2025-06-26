package com.example.security.springsecurity.controller;

import com.example.security.springsecurity.model.StudentModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentControlle {
    List<StudentModel> students = new ArrayList<>(List.of(
            new StudentModel(1, "shreya", 33),
            new StudentModel(2, "het", 55)
    )
    );

    @GetMapping("/student")
    public List<StudentModel> hello() {
        return students;
    }

    @GetMapping("/csrf")
    public CsrfToken csrf(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("/student")
    public StudentModel addStudent(@RequestBody StudentModel student) {
        students.add(student);

        return student;

    }
}
