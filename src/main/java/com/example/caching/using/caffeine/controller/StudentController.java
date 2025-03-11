package com.example.caching.using.caffeine.controller;

import com.example.caching.using.caffeine.model.Student;
import com.example.caching.using.caffeine.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CacheConfig(cacheNames = "students")
@Slf4j
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Get all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    // Get student by ID (Caches result)
    @GetMapping("/{id}")
    @Cacheable(key = "#id")
    public Student getStudentById(@PathVariable Integer id) {
        log.info("Getting student with id {} from DB", id);
        return studentService.getStudentById(id);
    }

    // Create a new student (Adds to cache)
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    // Update a student (Updates cache)
    @PutMapping("/{id}")
    @CachePut(key = "#id")
    public Student updateStudent(@PathVariable Integer id, @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }

    // Delete a student (Evicts from cache)
    @CacheEvict(key = "#id")
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Integer id) {
        studentService.deleteStudent(id);
    }
}
