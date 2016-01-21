package com.jiraservice.test;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;

import com.jiraservice.model.JiraTimesheet;


/**
 * 
 * @author Jhonatan Rocha
 *
 */

public class TimesheetTest {

	@Test
	public void setAttributes(){
		
		Date dataAtual = DateTime.now().toDate();
		
		JiraTimesheet timesheet = new JiraTimesheet(1l, "chave2", "titulo do proximo do projeto", dataAtual, "fulano", dataAtual, "cometarios legais");
		
		assertEquals(1l, timesheet.getId());
	}
}
