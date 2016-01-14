package com.emc.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.emc.model.Recurso;

/**
 * 
 * @author Jhonatan Rocha
 *
 */
public class RecursoDAO {

	/**
	 * 
	 * @param recurso
	 */
	public void insert(Recurso recurso) {
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
	public List<Recurso> listAll(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		CriteriaBuilder builder = em.getCriteriaBuilder();
	    CriteriaQuery<Recurso> query = builder.createQuery(Recurso.class);
	    Root<Recurso> root = query.from(Recurso.class);
	    query.select(root);

	    return em.createQuery(query).getResultList();
	}
	
	/**
	 * 
	 * @param recurso
	 */
	public void update(Recurso recurso) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		recurso = em.find(Recurso.class, recurso.getId()); //(Classe, ID)
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
		
		Recurso recurso = em.find(Recurso.class, id); //(Classe, ID)
		em.getTransaction().begin();
		em.remove(recurso);
		em.getTransaction().commit();
		em.close();
		factory.close();
	}
	
	/**
	 * 
	 * @param id
	 * @return Um objeto Recurso
	 */
	public Recurso getById(Long id){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		return em.find(Recurso.class, id); //(Classe, ID)
	}
}
