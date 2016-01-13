package com.emc.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import com.emc.dao.ProjetoDAO;
import com.emc.model.Projeto;

/**
 * 
 * @author Jhonatan Rocha
 *
 */
public class ProjetoDAOTest {

	private static final long ID = 1l;
	private static final String NOME_PROJETO = "Integração WebService JIRA";
	private static final Date DATA_CORRENTE = DateTime.now().toDate();
	
	Projeto projeto = new Projeto(ID, NOME_PROJETO, DATA_CORRENTE);
	
	ProjetoDAO projetoDAO = new ProjetoDAO();

	@Test
	public void insert() {
		projetoDAO.insert(projeto);
		Projeto persistedProjeto = projetoDAO.getById(ID);
		projetoDAO.removeById(ID);
		
		assertEquals(projeto.getId(), persistedProjeto.getId());
		assertEquals(projeto.getProject(), persistedProjeto.getProject());
		//assertEquals(projeto.getDataCreate(), persistedProjeto.getDataCreate());
	}
	
	@Test
	public void listAll(){
		projetoDAO.insert(projeto);
		List<Projeto> listProjeto = projetoDAO.listAll();
		projetoDAO.removeById(ID);
		
		if(listProjeto.size() == 1){
			for (Projeto persistedProjeto : listProjeto) {
				assertEquals(projeto.getId(), persistedProjeto.getId());
				assertEquals(projeto.getProject(), persistedProjeto.getProject());
				//assertEquals(projeto.getDataCreate(), persistedProjeto.getDataCreate());
			}
		}
	}
	
	@Test
	public void update() {
		String newProjectName = "Project Test";
		
		projetoDAO.insert(projeto);
		projeto.setProject(newProjectName);
		projetoDAO.update(projeto);
		Projeto persistedProjeto = projetoDAO.getById(ID);
		projetoDAO.removeById(ID);
		
		assertEquals(projeto.getId(), persistedProjeto.getId());
		assertNotSame(projeto.getProject(), persistedProjeto.getProject());
		//assertEquals(projeto.getDataCreate(), persistedProjeto.getDataCreate());
	}
	
	@Test
	public void removeById() {
		projetoDAO.insert(projeto);
		projetoDAO.removeById(ID);
		Projeto persistedProjeto = projetoDAO.getById(ID);
		
		assertTrue(persistedProjeto == null);
	}

	@Test
	public void getById(){
		projetoDAO.insert(projeto);
		Projeto persistedProjeto = projetoDAO.getById(ID);
		projetoDAO.removeById(ID);
		
		assertEquals(projeto.getId(), persistedProjeto.getId());
		assertEquals(projeto.getProject(), persistedProjeto.getProject());
		//assertEquals(projeto.getDataCreate(), persistedProjeto.getDataCreate());
	}
}