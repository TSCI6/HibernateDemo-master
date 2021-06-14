package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.HibernateUtil;
import com.example.demo.entities.Student;
import com.example.demo.repositories.StudentRepo;

@Service
public class StudentService {
	
	 @Autowired
	 StudentRepo repository;
	 
	
	 
	 
	 public List<Student> getAllStudents()
	 {
		 Session session = HibernateUtil.getSessionFactory().openSession();
		 session.beginTransaction();

			/*
			 * Student student = session.get(Student.class, new Integer(2));
			 * System.out.println(student.getFirstName() + student.getLastName());
			 */
		 List<Student> studentList = repository.findAll();

			/*
			 * Student student1 = (Student)session.get(Student.class, new Integer(2));
			 * System.out.println(student1.getFirstName() + student1.getLastName());
			 */

		 if(studentList.size() > 0) {
			 return studentList;
		 } else {
			 return new ArrayList<Student>();
		 }
	 }

	    public Student getStudentById(Integer id) throws Exception 
	    {
	        Optional<Student> student = repository.findById(id);
	         
	        if(student.isPresent()) {
	            return student.get();
	        } else {
	            throw new Exception("No student record exist for given id");
	        }
	    }
	     
	    public Student createOrUpdateStudent(Student entity) throws Exception 
	    {
	    	if(entity.getId() == 0) {
	    		//It comes in managed state.
	    		entity = repository.save(entity);
	             
	            return entity;
	    	}
	    	//It comes in managed state.
	        Optional<Student> student = repository.findById(entity.getId());
	         
	        if(student.isPresent()) 
	        {
	            Student newEntity = student.get();
	            newEntity.setEmail(entity.getEmail());
	            newEntity.setFirstName(entity.getFirstName());
	            newEntity.setLastName(entity.getLastName());
	 
	            newEntity = repository.save(newEntity);
	             
	            return newEntity;
	        } else {
	            entity = repository.save(entity);
	             
	            return entity;
	        }
	    } 
	     
	    public void deleteStudentById(Integer id) throws Exception 
	    {
	        Optional<Student> student = repository.findById(id);
	         
	        if(student.isPresent()) 
	        {
	            repository.deleteById(id);
	        } else {
	            throw new Exception("No student record exist for given id");
	        }
	    }

		public void showEntityLifeCycle() {
			
			 Session session = HibernateUtil.getSessionFactory().openSession();
			 session.beginTransaction();

			 Student student = session.get(Student.class, new Integer(2));
			 System.out.println(student.getFirstName() + student.getLastName());
			 
			 Boolean doesExist = session.contains(student);
			 System.out.println(student.getFirstName() + student.getLastName()+ "exists status : " + doesExist);

			 //session cleared, it comes in detached state 
			 session.clear();
			 Boolean doesExistAfterCleared = session.contains(student);
			 System.out.println(student.getFirstName() + student.getLastName()+ "exists status : " + doesExistAfterCleared);

			 //remove object it goes into removed state.
			 session.delete(student);
			 Boolean doesExistAfterRemoved = session.contains(student);
			 System.out.println(student.getFirstName() + student.getLastName()+ "exists status : " + doesExistAfterRemoved);
			 // this is the diff between JPA and hibernate -> hibernate allows us to remove even the detached entities.
		}

}
