package com.jiraservice.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.jiraservice.model.JiraResource;

/**
 * 
 * @author Jhonatan Rocha
 *
 */
public class JiraResourceDAO {

	/**
	 * 
	 * @param recurso
	 */
	public void insert(JiraResource recurso) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		em.getTransaction().begin();
		em.persist(recurso);
		em.getTransaction().commit();
		em.close();
		factory.close();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<JiraResource> listAll(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		CriteriaBuilder builder = em.getCriteriaBuilder();
	    CriteriaQuery<JiraResource> query = builder.createQuery(JiraResource.class);
	    Root<JiraResource> root = query.from(JiraResource.class);
	    query.select(root);

	    return em.createQuery(query).getResultList();
	}
	
	/**
	 * 
	 * @param resource
	 */
	public void update(JiraResource resource) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		resource = em.find(JiraResource.class, resource.getId()); //(Classe, ID)
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
		
		JiraResource resource = em.find(JiraResource.class, id); //(Classe, ID)
		em.getTransaction().begin();
		em.remove(resource);
		em.getTransaction().commit();
		em.close();
		factory.close();
	}
	
	/**
	 * 
	 * @param id
	 * @return Um objeto Recurso
	 */
	public JiraResource getById(Long id){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		return em.find(JiraResource.class, id); //(Classe, ID)
	}
}
