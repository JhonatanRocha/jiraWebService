package com.jiraservice.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.jiraservice.model.Timesheet;

/**
 * 
 * @author Jhonatan Rocha
 *
 */
public class TimesheetDAO {

	/**
	 * 
	 * @param timesheet
	 */
	public void insert(Timesheet timesheet) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		em.getTransaction().begin();
		em.persist(timesheet);
		em.getTransaction().commit();
		em.close();
		factory.close();
	}
	
	/**
	 * 
	 * @return Uma lista de objetos de Timesheet
	 */
	public List<Timesheet> listAll(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		CriteriaBuilder builder = em.getCriteriaBuilder();
	    CriteriaQuery<Timesheet> query = builder.createQuery(Timesheet.class);
	    Root<Timesheet> root = query.from(Timesheet.class);
	    query.select(root);

	    return em.createQuery(query).getResultList();
	}
	
	/**
	 * 
	 * @param timesheet
	 */
	public void update(Timesheet timesheet) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		timesheet = em.find(Timesheet.class, timesheet.getId()); //(Classe, ID)
		em.getTransaction().begin();
		em.getTransaction().commit();
		em.close();
	}
	
	/**
	 * 
	 * @param id
	 */
	public void removeById(Long id) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		Timesheet timesheet = em.find(Timesheet.class, id); //(Classe, ID)
		em.getTransaction().begin();
		em.remove(timesheet);
		em.getTransaction().commit();
		em.close();
		factory.close();
	}
	
	/**
	 * 
	 * @param id
	 * @return Um objeto Timesheet
	 */
	public Timesheet getById(Long id){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		return em.find(Timesheet.class, id); //(Classe, ID)
	}
}