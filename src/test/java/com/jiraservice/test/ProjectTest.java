package com.jiraservice.test;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;

import com.jiraservice.model.JiraProject;


/**
 * 
 * @author Jhonatan Rocha
 *
 */

public class ProjectTest {

	@Test
	public void setAttributes(){
		
		Date currentDate = DateTime.now().toDate();
		
		JiraProject project = new JiraProject(1l, "Projeto Piloto", currentDate);
		
		assertEquals(1l, project.getId());
		assertEquals("Projeto Piloto", project.getProject());
		assertEquals(currentDate, project.getDataCreate());
	}
}
