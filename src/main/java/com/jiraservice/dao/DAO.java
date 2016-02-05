package com.jiraservice.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

import com.jiraservice.model.JiraIssue;
import com.jiraservice.model.JiraProject;
import com.jiraservice.model.JiraResource;
import com.jiraservice.model.JiraTimesheet;

public class DAO {

	private List<JiraResource> resource;

	public List<JiraResource> getResource() {
		return resource;
	}
	
	public void setResource(List<JiraResource> resource) {
		this.resource = resource;
	}
	
	public DAO() {
		
	}
	
	public DAO(List<JiraResource> resource) {
		this.resource = resource;
	}
	
	public void insert(List<JiraProject> projects) {

		// consegue a entity manager
		EntityManager em = new JPAUtil().getEntityManager();

		// abre transacao
		em.getTransaction().begin();

		// persiste o objeto
		for(JiraProject project : projects) {
			for (JiraIssue jiraIssue : project.getAtividades()) {
				for(JiraTimesheet jiraTimesheet : jiraIssue.getTimesheets()) {
					for(JiraResource resource : this.resource){
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
		em.close();
	}
	
	/*
	public void insert(T t) {

		EntityManager em = new JPAUtil().getEntityManager();

		em.getTransaction().begin();

		em.persist(t);

		em.getTransaction().commit();

		em.close();
	}*/

	private void persistAllDataList(List<JiraProject> projects, EntityManager em) {
		for(JiraProject project : projects) {
			for (JiraIssue jiraIssue : project.getAtividades()) {
				for(JiraTimesheet jiraTimesheet : jiraIssue.getTimesheets()) {
					for(JiraResource resource : this.resource){
						em.persist(resource);
					}
					em.persist(jiraTimesheet);
				}
				em.persist(jiraIssue);
			}
			em.persist(project);
		}
	}
}
