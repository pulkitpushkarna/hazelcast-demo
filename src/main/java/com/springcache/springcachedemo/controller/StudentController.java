package com.springcache.springcachedemo.controller;

import com.springcache.springcachedemo.entity.Student;
import com.springcache.springcachedemo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.logging.Logger;

@RestController
public class StudentController {
    @Autowired
    StudentRepository studentRepository;

    Logger logger = Logger.getLogger(StudentController.class.getName());

    @GetMapping("/students")
    public Iterable<Student> getStudents(){
        return studentRepository.findAll();
    }

    @GetMapping("/students/{id}")
    @Cacheable(cacheNames = "student",key = "#id")
    public Student getStudent(@PathVariable Long id){
        logger.info("Fetching data for student id ::"+id);
        Optional<Student> studentOptional = studentRepository.findById(id);
        if(studentOptional.isPresent()){
            return studentOptional.get();
        }else{
            throw new RuntimeException("Student Not found");
        }
    }

    @PostMapping("/students")
    public Student postStudent(@RequestBody Student student){
        return studentRepository.save(student);
    }

    @PutMapping("/students")
    @CachePut(cacheNames = "student",key = "#student.id")
    public Student putStudent(@RequestBody Student student){
        logger.info("Updating student data for student id ::"+student.getId());
        Optional<Student> studentOptional= studentRepository.findById(student.getId());
        if(studentOptional.isPresent()){
            return studentRepository.save(student);
        }else{
            throw new RuntimeException("Student Not found");
        }
    }

    @DeleteMapping("/students/{id}")
    @CacheEvict(cacheNames = "student",key = "#id")
    public Student deleteStudent(@PathVariable Long id){
        logger.info("Deleting data for student id ::"+id);
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if(optionalStudent.isPresent()){
            studentRepository.delete(optionalStudent.get());
            return optionalStudent.get();
        }else{
            throw new RuntimeException("Student Not found");
        }
    }
}

