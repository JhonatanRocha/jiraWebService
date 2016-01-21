package com.jiraservice.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import com.jiraservice.dao.JiraIssueDAO;
import com.jiraservice.model.JiraIssue;

/**
 * 
 * @author Jhonatan Rocha
 *
 */
public class TaskDAOTest {

	/*private static final long ID = 1l;
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
		
	JiraIssue activity = new JiraIssue(ID, KEY, SUMMARY, ISSUETYPE, DATA_CORRENTE, 
										RESOLUTION, DATA_CORRENTE, DATA_CORRENTE, 
										ASSIGNED, STATUS, ORIGINAL_ESTIMATED,
										DATA_CORRENTE, DATA_CORRENTE, WORKRATIO, 
										SPRINT, CREATOR, PROGRESS, DATA_CORRENTE);
	
	JiraIssueDAO activityDAO = new JiraIssueDAO();
	
	@Test
	public void insert() {
		activityDAO.insert(activity);
		JiraIssue persistedAtividade = activityDAO.getById(ID);
		activityDAO.removeById(ID);
		
		assertEquals(activity.getId(), persistedAtividade.getId());
		assertEquals(activity.getKey() , persistedAtividade.getKey());
		assertEquals(activity.getSummary() , persistedAtividade.getSummary());
		assertEquals(activity.getIssueType() , persistedAtividade.getIssueType());
		//assertEquals(atividade.getCreated() , persistedAtividade.getCreated());
		assertEquals(activity.getResolution() , persistedAtividade.getResolution());
		//assertEquals(atividade.getResolved() , persistedAtividade.getResolved());
		//assertEquals(atividade.getUpdated() , persistedAtividade.getUpdated());
		assertEquals(activity.getAssigned() , persistedAtividade.getAssigned());
		assertEquals(activity.getStatus() , persistedAtividade.getStatus());
		assertEquals(activity.getOriginalEstimate() , persistedAtividade.getOriginalEstimate());
		//assertEquals(atividade.getTimeSpent() , persistedAtividade.getTimeSpent());
		//assertEquals(atividade.getRemainingEstimate() , persistedAtividade.getRemainingEstimate());
		assertEquals(activity.getWorkRatio() , persistedAtividade.getWorkRatio());
		assertEquals(activity.getSprint() , persistedAtividade.getSprint());
		assertEquals(activity.getCreator() , persistedAtividade.getCreator());
		assertEquals(activity.getProgress() , persistedAtividade.getProgress());
		//assertEquals(atividade.getDueDate() , persistedAtividade.getDueDate());
	}
	
	@Test
	public void listAll(){
		
		activityDAO.insert(activity);
		List<JiraIssue> listActivities = activityDAO.listAll();
		activityDAO.removeById(activity.getId());
		
		if(listActivities.size() == 1) {
			for (JiraIssue persistedActivity : listActivities) {
				assertEquals(activity.getId(), persistedActivity.getId());
				assertEquals(activity.getKey() , persistedActivity.getKey());
				assertEquals(activity.getSummary() , persistedActivity.getSummary());
				assertEquals(activity.getIssueType() , persistedActivity.getIssueType());
				//assertEquals(atividade.getCreated() , persistedAtividade.getCreated());
				assertEquals(activity.getResolution() , persistedActivity.getResolution());
				//assertEquals(atividade.getResolved() , persistedAtividade.getResolved());
				//assertEquals(atividade.getUpdated() , persistedAtividade.getUpdated());
				assertEquals(activity.getAssigned() , persistedActivity.getAssigned());
				assertEquals(activity.getStatus() , persistedActivity.getStatus());
				assertEquals(activity.getOriginalEstimate() , persistedActivity.getOriginalEstimate());
				//assertEquals(atividade.getTimeSpent() , persistedAtividade.getTimeSpent());
				//assertEquals(atividade.getRemainingEstimate() , persistedAtividade.getRemainingEstimate());
				assertEquals(activity.getWorkRatio() , persistedActivity.getWorkRatio());
				assertEquals(activity.getSprint() , persistedActivity.getSprint());
				assertEquals(activity.getCreator() , persistedActivity.getCreator());
				assertEquals(activity.getProgress() , persistedActivity.getProgress());
				//assertEquals(atividade.getDueDate() , persistedAtividade.getDueDate());
			}
		}
	}
	
	@Test
	public void update() {
		String newCreatorName = "User Test";
		
		activityDAO.insert(activity);
		activity.setCreator(newCreatorName);
		activityDAO.update(activity);
		JiraIssue persistedActivity = activityDAO.getById(ID);
		activityDAO.removeById(ID);
		
		assertEquals(activity.getId(), persistedActivity.getId());
		assertEquals(activity.getKey() , persistedActivity.getKey());
		assertEquals(activity.getSummary() , persistedActivity.getSummary());
		assertEquals(activity.getIssueType() , persistedActivity.getIssueType());
		//assertEquals(atividade.getCreated() , persistedAtividade.getCreated());
		assertEquals(activity.getResolution() , persistedActivity.getResolution());
		//assertEquals(atividade.getResolved() , persistedAtividade.getResolved());
		//assertEquals(atividade.getUpdated() , persistedAtividade.getUpdated());
		assertEquals(activity.getAssigned() , persistedActivity.getAssigned());
		assertEquals(activity.getStatus() , persistedActivity.getStatus());
		assertEquals(activity.getOriginalEstimate() , persistedActivity.getOriginalEstimate());
		//assertEquals(atividade.getTimeSpent() , persistedAtividade.getTimeSpent());
		//assertEquals(atividade.getRemainingEstimate() , persistedAtividade.getRemainingEstimate());
		assertEquals(activity.getWorkRatio() , persistedActivity.getWorkRatio());
		assertEquals(activity.getSprint() , persistedActivity.getSprint());
		assertNotSame(activity.getCreator() , persistedActivity.getCreator());
		assertEquals(activity.getProgress() , persistedActivity.getProgress());
		//assertEquals(atividade.getDueDate() , persistedAtividade.getDueDate());
	}
	
	@Test
	public void removeById(){
		activityDAO.insert(activity);
		activityDAO.removeById(ID);
		JiraIssue persistedActivity = activityDAO.getById(ID);
		
		assertTrue(persistedActivity == null);
	}
	
	@Test
	public void getById(){
		activityDAO.insert(activity);
		JiraIssue persistedActivity = activityDAO.getById(ID);
		activityDAO.removeById(ID);
		
		assertEquals(activity.getId(), persistedActivity.getId());
		assertEquals(activity.getKey() , persistedActivity.getKey());
		assertEquals(activity.getSummary() , persistedActivity.getSummary());
		assertEquals(activity.getIssueType() , persistedActivity.getIssueType());
		//assertEquals(atividade.getCreated() , persistedAtividade.getCreated());
		assertEquals(activity.getResolution() , persistedActivity.getResolution());
		//assertEquals(atividade.getResolved() , persistedAtividade.getResolved());
		//assertEquals(atividade.getUpdated() , persistedAtividade.getUpdated());
		assertEquals(activity.getAssigned() , persistedActivity.getAssigned());
		assertEquals(activity.getStatus() , persistedActivity.getStatus());
		assertEquals(activity.getOriginalEstimate() , persistedActivity.getOriginalEstimate());
		//assertEquals(atividade.getTimeSpent() , persistedAtividade.getTimeSpent());
		//assertEquals(atividade.getRemainingEstimate() , persistedAtividade.getRemainingEstimate());
		assertEquals(activity.getWorkRatio() , persistedActivity.getWorkRatio());
		assertEquals(activity.getSprint() , persistedActivity.getSprint());
		assertEquals(activity.getCreator() , persistedActivity.getCreator());
		assertEquals(activity.getProgress() , persistedActivity.getProgress());
		//assertEquals(atividade.getDueDate() , persistedAtividade.getDueDate());
	}*/
}
