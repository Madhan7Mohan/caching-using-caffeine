package com.example.caching.using.caffeine.service;

import com.example.caching.using.caffeine.model.Student;
import com.example.caching.using.caffeine.repo.StudentRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Fetch all students (No caching as it's a list and can become stale)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Fetch a student by ID (Caches the result)
    @Cacheable(value = "students", key = "#id")
    public Student getStudentById(int id) {
        System.out.println("Fetching student from DB...");
        return studentRepository.findById(id).orElse(null);
    }

    // Add a new student (Caches the newly added student)
    @CachePut(value = "students", key = "#student.id")
    @Transactional
    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    // Update an existing student (Updates cache)
    @CachePut(value = "students", key = "#id")
    @Transactional
    public Student updateStudent(int id, Student updatedStudent) {
        Optional<Student> existingStudent = studentRepository.findById(id);
        if (existingStudent.isPresent()) {
            Student student = existingStudent.get();
            student.setName(updatedStudent.getName());
            student.setEmail(updatedStudent.getEmail());
            return studentRepository.save(student);
        }
        return null;
    }

    // Delete a student (Removes from cache)
    @CacheEvict(value = "students", key = "#id")
    @Transactional
    public void deleteStudent(int id) {
        studentRepository.deleteById(id);
    }

    // Clear a specific student from the cache
    @CacheEvict(value = "students", key = "#id")
    public void evictStudentFromCache(int id) {
        System.out.println("Cache evicted for student ID: " + id);
    }

    // Clear all cached students
    @CacheEvict(value = "students", allEntries = true)
    public void evictAllCache() {
        System.out.println("Clearing all cache entries...");
    }
}

