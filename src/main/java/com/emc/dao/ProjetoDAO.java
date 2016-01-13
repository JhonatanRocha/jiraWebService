package com.emc.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.emc.model.Projeto;

/**
 * 
 * @author Jhonatan Rocha
 *
 */
public class ProjetoDAO {

	/**
	 * 
	 * @param projeto
	 */
	public void insert(Projeto projeto) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		em.getTransaction().begin();
		em.persist(projeto);
		em.getTransaction().commit();
		em.close();
		factory.close();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Projeto> listAll(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		CriteriaBuilder builder = em.getCriteriaBuilder();
	    CriteriaQuery<Projeto> query = builder.createQuery(Projeto.class);
	    Root<Projeto> root = query.from(Projeto.class);
	    query.select(root);

	    return em.createQuery(query).getResultList();
	}
	
	/**
	 * 
	 * @param projeto
	 */
	public void update(Projeto projeto) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		projeto = em.find(Projeto.class, projeto.getId()); //(Classe, ID)
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
		
		Projeto projeto = em.find(Projeto.class, id); //(Classe, ID)
		em.getTransaction().begin();
		em.remove(projeto);
		em.getTransaction().commit();
		em.close();
		factory.close();
	}
	
	/**
	 * 
	 * @param id
	 * @return Um objeto Projeto
	 */
	public Projeto getById(Long id){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		return em.find(Projeto.class, id); //(Classe, ID)
	}
}