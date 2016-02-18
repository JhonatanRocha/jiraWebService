package com.jiraservice.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

import com.jiraservice.model.JiraIssue;
import com.jiraservice.model.JiraProject;
import com.jiraservice.model.JiraResource;
import com.jiraservice.model.JiraTimesheet;

public class DAO {
	
	List<JiraResource> resources;
	
	public DAO(List<JiraResource> resources) {
		this.resources =  resources;
	}
	
	public void insert(List<JiraProject> projects) {

		// consegue a entity manager
		JPAUtil jpaUtil = new JPAUtil();
		EntityManager em = jpaUtil.getEntityManager();

		// abre transacao
		em.getTransaction().begin();

		// persiste o objeto
		for(JiraProject project : projects) {
			for (JiraIssue jiraIssue : project.getAtividades()) {
				for(JiraTimesheet jiraTimesheet : jiraIssue.getTimesheets()) {
					for(JiraResource resource : this.resources){
						em.persist(resource);
					}
					em.persist(jiraTimesheet);
				}
				em.persist(jiraIssue);
			}
			em.persist(project);
		}

		// commita a transacao
		em.getTransaction().commit();

		// fecha a entity manager
		jpaUtil.close(em);
	}
}
