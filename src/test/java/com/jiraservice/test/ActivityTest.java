package com.jiraservice.test;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;

import com.jiraservice.model.Activity;

/**
 * 
 * @author Jhonatan Rocha
 *
 */

public class ActivityTest {
	
	@Test
	public void setAttributes(){
		
		Date dataAtual = DateTime.now().toDate();
		
		Activity activity = new Activity(1l, "key", "Issue do JIra", "bug", dataAtual, "resolucao aleatoria",
											dataAtual, dataAtual, "ciclano", "aberto", "8 horas",
											dataAtual, dataAtual, "8%", "Quarto", "fulano", "Desenvolvendo", dataAtual);
		
		assertEquals(1l, activity.getId());
		assertEquals("key", activity.getKey());
		assertEquals("Issue do JIra", activity.getSummary());
		assertEquals("bug", activity.getIssueType());
		assertEquals(dataAtual, activity.getCreated());
		assertEquals("resolucao aleatoria", activity.getResolution());
		assertEquals(dataAtual, activity.getResolved());
		assertEquals(dataAtual, activity.getUpdated());
		assertEquals("ciclano", activity.getAssigned());
		assertEquals("aberto", activity.getStatus());
		assertEquals("8 horas", activity.getOriginalEstimate());
		assertEquals(dataAtual, activity.getTimeSpent());
		assertEquals(dataAtual, activity.getRemainingEstimate());
		assertEquals("8%", activity.getWorkRatio());
		assertEquals("Quarto", activity.getSprint());
		assertEquals("fulano", activity.getCreator());
		assertEquals("Desenvolvendo", activity.getProgress());
		assertEquals(dataAtual, activity.getDueDate());
	}
}
