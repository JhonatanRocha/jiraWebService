package com.emc.test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.Test;

import com.emc.model.Projeto;

/**
 * 
 * @author Jhonatan Rocha
 *
 */
public class ProjetoDAOTest {


	@Test
	public void insert() {

	}
	
	public List<Projeto> listAll(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		CriteriaBuilder builder = em.getCriteriaBuilder();
	    CriteriaQuery<Projeto> query = builder.createQuery(Projeto.class);
	    Root<Projeto> root = query.from(Projeto.class);
	    query.select(root);

	    return em.createQuery(query).getResultList();
	}
	
	public void update(Projeto projeto) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		projeto = em.find(Projeto.class, projeto.getId()); //(Classe, ID)
		em.getTransaction().begin();
		em.getTransaction().commit();
		em.close();
	}

	public Projeto getById(Long id){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jiraextraction");
		EntityManager em = factory.createEntityManager();
		
		return em.find(Projeto.class, id); //(Classe, ID)
	}
}