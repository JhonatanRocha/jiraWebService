package com.emc.test;

import static org.junit.Assert.assertEquals;

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

	private static final String PROGRESS = "Desenvolvendo";
	private static final String CREATOR = "fulano";
	private static final String SPRINT = "Quarto";
	private static final String ORIGINAL_ESTIMATED = "8 horas";
	private static final long ID = 1l;
	private static final String KEY = "key";
	private static final String SUMMARY = "Issue do JIra";
	private static final String ISSUETYPE = "bug";
	private static final Date DATA_CORRENTE = DateTime.now().toDate();
	private static final String RESOLUTION = "resolucao aleatoria";
	private static final String ASSIGNED = "ciclano";
	private static final String STATUS = "aberto";
	private static final String WORKRATIO = "8%";
	
	AtividadeDAO atividadeDAO = new AtividadeDAO();

	
	Atividade atividade = new Atividade(ID, KEY, SUMMARY, ISSUETYPE, DATA_CORRENTE, 
										RESOLUTION, DATA_CORRENTE, DATA_CORRENTE, 
										ASSIGNED, STATUS, ORIGINAL_ESTIMATED,
										DATA_CORRENTE, DATA_CORRENTE, WORKRATIO, 
										SPRINT, CREATOR, PROGRESS, DATA_CORRENTE);
	
	@Test
	public void insert() {
		atividadeDAO.insert(atividade);
		Atividade persistedAtividade = atividadeDAO.getById(1l);
		atividadeDAO.removeById(1l);
		
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
		Atividade atividade2 = new Atividade(2l, KEY, SUMMARY, ISSUETYPE, DATA_CORRENTE, 
				RESOLUTION, DATA_CORRENTE, DATA_CORRENTE, 
				ASSIGNED, STATUS, ORIGINAL_ESTIMATED,
				DATA_CORRENTE, DATA_CORRENTE, WORKRATIO, 
				SPRINT, CREATOR, PROGRESS, DATA_CORRENTE);
		
		long idAtividade = atividade.getId();
		
		atividadeDAO.insert(atividade);
		atividadeDAO.insert(atividade2);
		List<Atividade> listAtividade = atividadeDAO.listAll();
		atividadeDAO.removeById(atividade.getId());
		atividadeDAO.removeById(atividade2.getId());
		
		if(listAtividade.size() == 2) {
			for (Atividade persistedAtividade : listAtividade) {
				assertEquals(idAtividade, persistedAtividade.getId());
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
				idAtividade++;
			}
		}
	}
}
