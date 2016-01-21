package com.jiraservice.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import com.jiraservice.dao.JiraResourceDAO;
import com.jiraservice.model.JiraResource;

/**
 * 
 * @author Jhonatan Rocha
 *
 */
public class ResourceDAOTest {

	private static final long ID = 1l;
	private static final String USER_NAME = "Fulano";
	private static final String FULL_NAME = "Fulano da Silva";
	private static final Date DATA_CORRENTE = DateTime.now().toDate();
	private static final String STATUS = "Open";
	
	JiraResource resource = new JiraResource(ID, USER_NAME, FULL_NAME, DATA_CORRENTE, DATA_CORRENTE, STATUS);
	
	JiraResourceDAO resourceDAO = new JiraResourceDAO();
	
	@Test
	public void insert() {
		resourceDAO.insert(resource);
		JiraResource persistedResource = resourceDAO.getById(ID);
		resourceDAO.removeById(ID);
		
		assertEquals(resource.getId(), persistedResource.getId());
		assertEquals(resource.getUserName(), persistedResource.getUserName());
		assertEquals(resource.getFullName(), persistedResource.getFullName());
		//assertEquals(recurso.getDataInicio(), persistedRecurso.getDataInicio());
		//assertEquals(recurso.getDataFinal(), persistedRecurso.getDataFinal());
		assertEquals(resource.getStatus(), persistedResource.getStatus());
	}
	
	@Test
	public void listAll(){
		resourceDAO.insert(resource);
		List<JiraResource> listResource = resourceDAO.listAll();
		resourceDAO.removeById(ID);
		
		if(listResource.size() == 1){
			for (JiraResource persistedResource : listResource) {
				assertEquals(resource.getId(), persistedResource.getId());
				assertEquals(resource.getUserName(), persistedResource.getUserName());
				assertEquals(resource.getFullName(), persistedResource.getFullName());
				//assertEquals(recurso.getDataInicio(), persistedRecurso.getDataInicio());
				//assertEquals(recurso.getDataFinal(), persistedRecurso.getDataFinal());
				assertEquals(resource.getStatus(), persistedResource.getStatus());							
			}
		}
	}
	
	@Test
	public void update() {
		String newStatus = "Closed";
		
		resourceDAO.insert(resource);
		resource.setStatus(newStatus);
		resourceDAO.update(resource);
		JiraResource persistedResource = resourceDAO.getById(ID);
		resourceDAO.removeById(ID);
		
		assertEquals(resource.getId(), persistedResource.getId());
		assertEquals(resource.getUserName(), persistedResource.getUserName());
		assertEquals(resource.getFullName(), persistedResource.getFullName());
		//assertEquals(recurso.getDataInicio(), persistedRecurso.getDataInicio());
		//assertEquals(recurso.getDataFinal(), persistedRecurso.getDataFinal());
		assertNotSame(resource.getStatus(), persistedResource.getStatus());
	}
	
	@Test
	public void removeById() {
		resourceDAO.insert(resource);
		resourceDAO.removeById(ID);
		JiraResource persistedResource = resourceDAO.getById(ID);
		
		assertTrue(persistedResource == null);
	}
	
	@Test
	public void getById(){
		resourceDAO.insert(resource);
		JiraResource persistedResource = resourceDAO.getById(ID);
		resourceDAO.removeById(ID);
		
		assertEquals(resource.getId(), persistedResource.getId());
		assertEquals(resource.getUserName(), persistedResource.getUserName());
		assertEquals(resource.getFullName(), persistedResource.getFullName());
		//assertEquals(recurso.getDataInicio(), persistedRecurso.getDataInicio());
		//assertEquals(recurso.getDataFinal(), persistedRecurso.getDataFinal());
		assertEquals(resource.getStatus(), persistedResource.getStatus());
	}
}
