package com.emc.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import com.emc.dao.RecursoDAO;
import com.emc.model.Recurso;

/**
 * 
 * @author Jhonatan Rocha
 *
 */
public class RecursoDAOTest {

	private static final long ID = 1l;
	private static final String USER_NAME = "Fulano";
	private static final String FULL_NAME = "Fulano da Silva";
	private static final Date DATA_CORRENTE = DateTime.now().toDate();
	private static final String STATUS = "Open";
	
	Recurso recurso = new Recurso(ID, USER_NAME, FULL_NAME, DATA_CORRENTE, DATA_CORRENTE, STATUS);
	
	RecursoDAO recursoDAO = new RecursoDAO();
	
	@Test
	public void insert() {
		recursoDAO.insert(recurso);
		Recurso persistedRecurso = recursoDAO.getById(ID);
		recursoDAO.removeById(ID);
		
		assertEquals(recurso.getId(), persistedRecurso.getId());
		assertEquals(recurso.getUserName(), persistedRecurso.getUserName());
		assertEquals(recurso.getFullName(), persistedRecurso.getFullName());
		//assertEquals(recurso.getDataInicio(), persistedRecurso.getDataInicio());
		//assertEquals(recurso.getDataFinal(), persistedRecurso.getDataFinal());
		assertEquals(recurso.getStatus(), persistedRecurso.getStatus());
	}
	
	@Test
	public void listAll(){
		recursoDAO.insert(recurso);
		List<Recurso> listRecurso = recursoDAO.listAll();
		recursoDAO.removeById(ID);
		
		if(listRecurso.size() == 1){
			for (Recurso persistedRecurso : listRecurso) {
				assertEquals(recurso.getId(), persistedRecurso.getId());
				assertEquals(recurso.getUserName(), persistedRecurso.getUserName());
				assertEquals(recurso.getFullName(), persistedRecurso.getFullName());
				//assertEquals(recurso.getDataInicio(), persistedRecurso.getDataInicio());
				//assertEquals(recurso.getDataFinal(), persistedRecurso.getDataFinal());
				assertEquals(recurso.getStatus(), persistedRecurso.getStatus());							
			}
		}
	}
	
	@Test
	public void update() {
		String newStatus = "Closed";
		
		recursoDAO.insert(recurso);
		recurso.setStatus(newStatus);
		recursoDAO.update(recurso);
		Recurso persistedRecurso = recursoDAO.getById(ID);
		recursoDAO.removeById(ID);
		
		assertEquals(recurso.getId(), persistedRecurso.getId());
		assertEquals(recurso.getUserName(), persistedRecurso.getUserName());
		assertEquals(recurso.getFullName(), persistedRecurso.getFullName());
		//assertEquals(recurso.getDataInicio(), persistedRecurso.getDataInicio());
		//assertEquals(recurso.getDataFinal(), persistedRecurso.getDataFinal());
		assertNotSame(recurso.getStatus(), persistedRecurso.getStatus());
	}
	
	@Test
	public void removeById() {
		recursoDAO.insert(recurso);
		recursoDAO.removeById(ID);
		Recurso persistedRecurso = recursoDAO.getById(ID);
		
		assertTrue(persistedRecurso == null);
	}
	
	@Test
	public void getById(){
		recursoDAO.insert(recurso);
		Recurso persistedRecurso = recursoDAO.getById(ID);
		recursoDAO.removeById(ID);
		
		assertEquals(recurso.getId(), persistedRecurso.getId());
		assertEquals(recurso.getUserName(), persistedRecurso.getUserName());
		assertEquals(recurso.getFullName(), persistedRecurso.getFullName());
		//assertEquals(recurso.getDataInicio(), persistedRecurso.getDataInicio());
		//assertEquals(recurso.getDataFinal(), persistedRecurso.getDataFinal());
		assertEquals(recurso.getStatus(), persistedRecurso.getStatus());
	}
}
