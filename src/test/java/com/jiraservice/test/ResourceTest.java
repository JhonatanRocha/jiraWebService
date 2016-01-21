package com.jiraservice.test;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;

import com.jiraservice.model.JiraResource;


/**
 * 
 * @author Jhonatan Rocha
 *
 */

public class ResourceTest {

	@Test
	public void setAttributes(){
		
		Date currentDate = DateTime.now().toDate();
		
		JiraResource resource = new JiraResource(1l, "fulano", "fulano da silva", currentDate, currentDate, "alocado");
		
		assertEquals(1l, resource.getId());
		assertEquals("fulano", resource.getUserName());
		assertEquals("fulano da silva", resource.getFullName());
		assertEquals(currentDate, resource.getDataInicio());
		assertEquals(currentDate, resource.getDataFinal());
		assertEquals("alocado", resource.getStatus());
	}
}
