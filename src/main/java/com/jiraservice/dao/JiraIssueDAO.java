package com.jiraservice.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.jiraservice.model.JiraIssue;

/**
 * 
 * @author Jhonatan Rocha
 *
 */
public class JiraIssueDAO {

	/**
	 * 
	 * @param issue
	 */
	public void insert(JiraIssue issue) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		em.getTransaction().begin();
		em.persist(issue);
		em.getTransaction().commit();
		em.close();
		factory.close();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<JiraIssue> listAll(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		CriteriaBuilder builder = em.getCriteriaBuilder();
	    CriteriaQuery<JiraIssue> query = builder.createQuery(JiraIssue.class);
	    Root<JiraIssue> root = query.from(JiraIssue.class);
	    query.select(root);

	    return em.createQuery(query).getResultList();
	}
	
	/**
	 * 
	 * @param issue 
	 * 
	 */
	public void update(JiraIssue issue) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		issue = em.find(JiraIssue.class, issue.getId()); //(Classe, ID)
		em.getTransaction().begin();
		em.getTransaction().commit();
		em.close();
	}
	
	/**
	 * 
	 * @param id
	 */
	public void removeById(Long id){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		JiraIssue issue = em.find(JiraIssue.class, id); //(Classe, ID)
		em.getTransaction().begin();
		em.remove(issue);
		em.getTransaction().commit();
		em.close();
		factory.close();
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public JiraIssue getById(Long id){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		return em.find(JiraIssue.class, id); //(Classe, ID)
	}
}
