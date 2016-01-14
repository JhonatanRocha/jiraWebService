package com.emc.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import com.emc.dao.AtividadeDAO;
import com.emc.model.Atividade;

/**
 * 
 * @author Jhonatan Rocha
 *
 */
public class AtividadeDAOTest {

	private static final long ID = 1l;
	private static final String KEY = "key";
	private static final String SUMMARY = "Issue do JIra";
	private static final String ISSUETYPE = "bug";
	private static final Date DATA_CORRENTE = DateTime.now().toDate();
	private static final String RESOLUTION = "resolucao aleatoria";
	private static final String ASSIGNED = "ciclano";
	private static final String STATUS = "aberto";
	private static final String WORKRATIO = "8%";
	private static final String ORIGINAL_ESTIMATED = "8 horas";
	private static final String SPRINT = "Quarto";
	private static final String PROGRESS = "Desenvolvendo";
	private static final String CREATOR = "fulano";
		
	Atividade atividade = new Atividade(ID, KEY, SUMMARY, ISSUETYPE, DATA_CORRENTE, 
										RESOLUTION, DATA_CORRENTE, DATA_CORRENTE, 
										ASSIGNED, STATUS, ORIGINAL_ESTIMATED,
										DATA_CORRENTE, DATA_CORRENTE, WORKRATIO, 
										SPRINT, CREATOR, PROGRESS, DATA_CORRENTE);
	
	AtividadeDAO atividadeDAO = new AtividadeDAO();
	
	@Test
	public void insert() {
		atividadeDAO.insert(atividade);
		Atividade persistedAtividade = atividadeDAO.getById(ID);
		atividadeDAO.removeById(ID);
		
		assertEquals(atividade.getId(), persistedAtividade.getId());
		assertEquals(atividade.getKey() , persistedAtividade.getKey());
		assertEquals(atividade.getSummary() , persistedAtividade.getSummary());
		assertEquals(atividade.getIssueType() , persistedAtividade.getIssueType());
		//assertEquals(atividade.getCreated() , persistedAtividade.getCreated());
		assertEquals(atividade.getResolution() , persistedAtividade.getResolution());
		//assertEquals(atividade.getResolved() , persistedAtividade.getResolved());
		//assertEquals(atividade.getUpdated() , persistedAtividade.getUpdated());
		assertEquals(atividade.getAssigned() , persistedAtividade.getAssigned());
		assertEquals(atividade.getStatus() , persistedAtividade.getStatus());
		assertEquals(atividade.getOriginalEstimate() , persistedAtividade.getOriginalEstimate());
		//assertEquals(atividade.getTimeSpent() , persistedAtividade.getTimeSpent());
		//assertEquals(atividade.getRemainingEstimate() , persistedAtividade.getRemainingEstimate());
		assertEquals(atividade.getWorkRatio() , persistedAtividade.getWorkRatio());
		assertEquals(atividade.getSprint() , persistedAtividade.getSprint());
		assertEquals(atividade.getCreator() , persistedAtividade.getCreator());
		assertEquals(atividade.getProgress() , persistedAtividade.getProgress());
		//assertEquals(atividade.getDueDate() , persistedAtividade.getDueDate());
	}
	
	@Test
	public void listAll(){
		
		atividadeDAO.insert(atividade);
		List<Atividade> listAtividade = atividadeDAO.listAll();
		atividadeDAO.removeById(atividade.getId());
		
		if(listAtividade.size() == 1) {
			for (Atividade persistedAtividade : listAtividade) {
				assertEquals(atividade.getId(), persistedAtividade.getId());
				assertEquals(atividade.getKey() , persistedAtividade.getKey());
				assertEquals(atividade.getSummary() , persistedAtividade.getSummary());
				assertEquals(atividade.getIssueType() , persistedAtividade.getIssueType());
				//assertEquals(atividade.getCreated() , persistedAtividade.getCreated());
				assertEquals(atividade.getResolution() , persistedAtividade.getResolution());
				//assertEquals(atividade.getResolved() , persistedAtividade.getResolved());
				//assertEquals(atividade.getUpdated() , persistedAtividade.getUpdated());
				assertEquals(atividade.getAssigned() , persistedAtividade.getAssigned());
				assertEquals(atividade.getStatus() , persistedAtividade.getStatus());
				assertEquals(atividade.getOriginalEstimate() , persistedAtividade.getOriginalEstimate());
				//assertEquals(atividade.getTimeSpent() , persistedAtividade.getTimeSpent());
				//assertEquals(atividade.getRemainingEstimate() , persistedAtividade.getRemainingEstimate());
				assertEquals(atividade.getWorkRatio() , persistedAtividade.getWorkRatio());
				assertEquals(atividade.getSprint() , persistedAtividade.getSprint());
				assertEquals(atividade.getCreator() , persistedAtividade.getCreator());
				assertEquals(atividade.getProgress() , persistedAtividade.getProgress());
				//assertEquals(atividade.getDueDate() , persistedAtividade.getDueDate());
			}
		}
	}
	
	@Test
	public void update() {
		String newCreatorName = "User Test";
		
		atividadeDAO.insert(atividade);
		atividade.setCreator(newCreatorName);
		atividadeDAO.update(atividade);
		Atividade persistedAtividade = atividadeDAO.getById(ID);
		atividadeDAO.removeById(ID);
		
		assertEquals(atividade.getId(), persistedAtividade.getId());
		assertEquals(atividade.getKey() , persistedAtividade.getKey());
		assertEquals(atividade.getSummary() , persistedAtividade.getSummary());
		assertEquals(atividade.getIssueType() , persistedAtividade.getIssueType());
		//assertEquals(atividade.getCreated() , persistedAtividade.getCreated());
		assertEquals(atividade.getResolution() , persistedAtividade.getResolution());
		//assertEquals(atividade.getResolved() , persistedAtividade.getResolved());
		//assertEquals(atividade.getUpdated() , persistedAtividade.getUpdated());
		assertEquals(atividade.getAssigned() , persistedAtividade.getAssigned());
		assertEquals(atividade.getStatus() , persistedAtividade.getStatus());
		assertEquals(atividade.getOriginalEstimate() , persistedAtividade.getOriginalEstimate());
		//assertEquals(atividade.getTimeSpent() , persistedAtividade.getTimeSpent());
		//assertEquals(atividade.getRemainingEstimate() , persistedAtividade.getRemainingEstimate());
		assertEquals(atividade.getWorkRatio() , persistedAtividade.getWorkRatio());
		assertEquals(atividade.getSprint() , persistedAtividade.getSprint());
		assertNotSame(atividade.getCreator() , persistedAtividade.getCreator());
		assertEquals(atividade.getProgress() , persistedAtividade.getProgress());
		//assertEquals(atividade.getDueDate() , persistedAtividade.getDueDate());
	}
	
	@Test
	public void removeById(){
		atividadeDAO.insert(atividade);
		atividadeDAO.removeById(ID);
		Atividade persistedAtividade = atividadeDAO.getById(ID);
		
		assertTrue(persistedAtividade == null);
	}
	
	@Test
	public void getById(){
		atividadeDAO.insert(atividade);
		Atividade persistedAtividade = atividadeDAO.getById(ID);
		atividadeDAO.removeById(ID);
		
		assertEquals(atividade.getId(), persistedAtividade.getId());
		assertEquals(atividade.getKey() , persistedAtividade.getKey());
		assertEquals(atividade.getSummary() , persistedAtividade.getSummary());
		assertEquals(atividade.getIssueType() , persistedAtividade.getIssueType());
		//assertEquals(atividade.getCreated() , persistedAtividade.getCreated());
		assertEquals(atividade.getResolution() , persistedAtividade.getResolution());
		//assertEquals(atividade.getResolved() , persistedAtividade.getResolved());
		//assertEquals(atividade.getUpdated() , persistedAtividade.getUpdated());
		assertEquals(atividade.getAssigned() , persistedAtividade.getAssigned());
		assertEquals(atividade.getStatus() , persistedAtividade.getStatus());
		assertEquals(atividade.getOriginalEstimate() , persistedAtividade.getOriginalEstimate());
		//assertEquals(atividade.getTimeSpent() , persistedAtividade.getTimeSpent());
		//assertEquals(atividade.getRemainingEstimate() , persistedAtividade.getRemainingEstimate());
		assertEquals(atividade.getWorkRatio() , persistedAtividade.getWorkRatio());
		assertEquals(atividade.getSprint() , persistedAtividade.getSprint());
		assertEquals(atividade.getCreator() , persistedAtividade.getCreator());
		assertEquals(atividade.getProgress() , persistedAtividade.getProgress());
		//assertEquals(atividade.getDueDate() , persistedAtividade.getDueDate());
	}
}
