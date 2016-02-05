package com.jiraservice.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.jiraservice.model.JiraTimesheet;

/**
 * 
 * @author Jhonatan Rocha
 *
 */
public class JiraTimesheetDAO {

	/**
	 * 
	 * @param timesheet
	 */
	public void insert(JiraTimesheet timesheet) {
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
	public List<JiraTimesheet> listAll(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		CriteriaBuilder builder = em.getCriteriaBuilder();
	    CriteriaQuery<JiraTimesheet> query = builder.createQuery(JiraTimesheet.class);
	    Root<JiraTimesheet> root = query.from(JiraTimesheet.class);
	    query.select(root);

	    return em.createQuery(query).getResultList();
	}
	
	/**
	 * 
	 * @param timesheet
	 */
	public void update(JiraTimesheet timesheet) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		timesheet = em.find(JiraTimesheet.class, timesheet.getId()); //(Classe, ID)
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
		
		JiraTimesheet timesheet = em.find(JiraTimesheet.class, id); //(Classe, ID)
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
	public JiraTimesheet getById(Long id){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		return em.find(JiraTimesheet.class, id); //(Classe, ID)
	}
}
