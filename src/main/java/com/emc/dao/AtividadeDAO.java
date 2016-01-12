package com.emc.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.emc.model.Atividade;

/**
 * 
 * @author Jhonatan Rocha
 *
 */
public class AtividadeDAO {

	/**
	 * 
	 * @param atividade
	 */
	public void insert(Atividade atividade) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		em.getTransaction().begin();
		em.persist(atividade);
		em.getTransaction().commit();
		em.close();
		factory.close();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Atividade> listAll(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		CriteriaBuilder builder = em.getCriteriaBuilder();
	    CriteriaQuery<Atividade> query = builder.createQuery(Atividade.class);
	    Root<Atividade> root = query.from(Atividade.class);
	    query.select(root);

	    return em.createQuery(query).getResultList();
	}
	
	/**
	 * 
	 * @param atividade
	 * 
	 */
	public void update(Atividade atividade) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		atividade = em.find(Atividade.class, atividade.getId()); //(Classe, ID)
		em.getTransaction().begin();
		em.getTransaction().commit();
		em.close();
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Atividade getById(Long id){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		return em.find(Atividade.class, id); //(Classe, ID)
	}
}
