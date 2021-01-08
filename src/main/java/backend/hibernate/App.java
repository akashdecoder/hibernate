package backend.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import backend.hibernate.pojo.Employee;

public class App {
	private static SessionFactory factory;
    public static void main( String[] args ) {
    	try {
    		factory = new Configuration().configure().buildSessionFactory();
    	} catch(Throwable ex) {
    		throw new ExceptionInInitializerError(ex);
    	}
    	
    	App app = new App();
    	
    	Integer empID1 = app.addEmployee("Zara", "Ali", 1000);
    	Integer empID2 = app.addEmployee("Daisy", "Das", 5000);
    	Integer empID3 = app.addEmployee("John", "Paul", 10000);
    	
    	app.listEmployee();
    	System.out.println("\n\n");
    	app.updateEmployee(empID1, 5000);
    	
    	app.deleteEmployee(empID2);
    	
    	app.listEmployee();
    	
    }	
	
	private Integer addEmployee(String fName, String lName, int salary) {
		Session session = factory.openSession();
		Transaction trans = null;
		Integer employeeID = null;
		
		try {
			trans = session.beginTransaction();
			Employee employee = new Employee(fName, lName, salary);
			employeeID = (Integer)session.save(employee);
			trans.commit();
		} catch(HibernateException he) {
			if(trans != null)	trans.rollback();
			he.printStackTrace();
		} finally {
			session.close();
		}
		return employeeID;
	}
	
	private void listEmployee() {
		Session session = factory.openSession();
		Transaction trans = null;
		
		try {
			trans = session.beginTransaction();
			List employees = session.createQuery("from Employee").list();
			for(Iterator iterator = employees.iterator(); iterator.hasNext();) {
				Employee employee = (Employee) iterator.next();
				System.out.println("First Name: " + employee.getFirstName());
				System.out.println("Last Name: " + employee.getLastName());
				System.out.println("Salary: " + employee.getSalary());
			}
			trans.commit();
		} catch(HibernateException he) {
			if(trans != null)	trans.rollback();
			he.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	private void updateEmployee(Integer employeeID, int salary) {
		Session session = factory.openSession();
		Transaction trans = null;
		try {
			trans = session.beginTransaction();
			Employee employee = (Employee)session.get(Employee.class, employeeID);
			employee.setSalary(salary);
			session.update(employee);
			trans.commit();
		} catch(HibernateException he) {
			if(trans != null)	trans.rollback();
			he.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	private void deleteEmployee(Integer employeeID) {
		Session session = factory.openSession();
		Transaction trans = null;
		try {
			trans = session.beginTransaction();
			Employee employee = (Employee)session.get(Employee.class, employeeID);
			session.delete(employee);
			trans.commit();
		} catch(HibernateException he) {
			if(trans != null)	trans.rollback();
			he.printStackTrace();
		} finally {
			session.close();
		}
	}
}
