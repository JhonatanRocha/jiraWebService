package com.jiraservice.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import com.jiraservice.dao.ProjectDAO;
import com.jiraservice.model.Project;

/**
 * 
 * @author Jhonatan Rocha
 *
 */
public class ProjectDAOTest {

	private static final long ID = 1l;
	private static final String NOME_PROJETO = "Integração WebService JIRA";
	private static final Date DATA_CORRENTE = DateTime.now().toDate();
	
	Project project = new Project(ID, NOME_PROJETO, DATA_CORRENTE);
	
	ProjectDAO projectDAO = new ProjectDAO();

	@Test
	public void insert() {
		projectDAO.insert(project);
		Project persistedProject = projectDAO.getById(ID);
		projectDAO.removeById(ID);
		
		assertEquals(project.getId(), persistedProject.getId());
		assertEquals(project.getProject(), persistedProject.getProject());
		//assertEquals(projeto.getDataCreate(), persistedProjeto.getDataCreate());
	}
	
	@Test
	public void listAll(){
		projectDAO.insert(project);
		List<Project> listProject = projectDAO.listAll();
		projectDAO.removeById(ID);
		
		if(listProject.size() == 1){
			for (Project persistedProject : listProject) {
				assertEquals(project.getId(), persistedProject.getId());
				assertEquals(project.getProject(), persistedProject.getProject());
				//assertEquals(projeto.getDataCreate(), persistedProjeto.getDataCreate());
			}
		}
	}
	
	@Test
	public void update() {
		String newProjectName = "Project Test";
		
		projectDAO.insert(project);
		project.setProject(newProjectName);
		projectDAO.update(project);
		Project persistedProject = projectDAO.getById(ID);
		projectDAO.removeById(ID);
		
		assertEquals(project.getId(), persistedProject.getId());
		assertNotSame(project.getProject(), persistedProject.getProject());
		//assertEquals(projeto.getDataCreate(), persistedProjeto.getDataCreate());
	}
	
	@Test
	public void removeById() {
		projectDAO.insert(project);
		projectDAO.removeById(ID);
		Project persistedProject = projectDAO.getById(ID);
		
		assertTrue(persistedProject == null);
	}

	@Test
	public void getById(){
		projectDAO.insert(project);
		Project persistedProject = projectDAO.getById(ID);
		projectDAO.removeById(ID);
		
		assertEquals(project.getId(), persistedProject.getId());
		assertEquals(project.getProject(), persistedProject.getProject());
		//assertEquals(projeto.getDataCreate(), persistedProjeto.getDataCreate());
	}
}