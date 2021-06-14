package com.example.demo.controller;


import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.HibernateUtil;
import com.example.demo.entities.Student;
import com.example.demo.service.StudentService;
 
@RestController
@RequestMapping("/students")
public class StudentController 
{
	
	@Autowired
    StudentService service;
	
	@GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> list = service.getAllStudents();
 
        return new ResponseEntity<List<Student>>(list, new HttpHeaders(), HttpStatus.OK);
    }
	
	@GetMapping("/entityLifeCycle")
    public void showEntityLifeCycle() {
         service.showEntityLifeCycle();
 
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable("id") Integer id) 
                                                    throws Exception {
        Student entity = service.getStudentById(id);
 
        return new ResponseEntity<Student>(entity, new HttpHeaders(), HttpStatus.OK);
    }
 
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Student> createOrUpdateStudent(@RequestBody Student student)
                                                    throws Exception {
    	//here student object will be in new state.
    	 Session session = HibernateUtil.getSessionFactory().openSession();
		 session.beginTransaction();
		 
		//here student object will be in new state hence its not in sesion / first level cache.
		 Boolean doesExist = session.contains(student);
		 System.out.println(student.getFirstName() + student.getLastName()+ "exists status : " + doesExist);

		//here student object will be in managed state as u can see query is fired.
    	 Student student2 = session.get(Student.class, new Integer(2));
    	 Boolean doesExistEmp2 = session.contains(student2);
		 System.out.println(student2.getFirstName() + student2.getLastName()+ "exists status : " + doesExistEmp2);
		//after this its proved that student object will be in managed state in the session.

		 //Query is not fired as sssion now has this object with id 2
		 Student student3 = session.get(Student.class, new Integer(2));
    	 Boolean doesExistEmp3 = session.contains(student3);
		 System.out.println(student3.getFirstName() + student3.getLastName()+ "exists status : " + doesExistEmp3);
		
		 
//    	 System.out.println(student2.getFirstName() + student2.getLastName());
        Student updated = service.createOrUpdateStudent(student);
        return new ResponseEntity<Student>(updated, new HttpHeaders(), HttpStatus.OK);
    }
 
    @DeleteMapping("/{id}")
    public HttpStatus deleteStudentById(@PathVariable("id") Integer id) 
                                                    throws Exception {
        service.deleteStudentById(id);
        return HttpStatus.FORBIDDEN;
    }
	
}