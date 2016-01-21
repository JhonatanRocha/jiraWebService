package com.jiraservice.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import com.jiraservice.dao.JiraTimesheetDAO;
import com.jiraservice.model.JiraTimesheet;


/**
 * 
 * @author Jhonatan Rocha
 *
 */
public class TimesheetDAOTest {

	private static final long ID = 1l;
	private static final String KEY = "Test key entry";
	private static final String TITLE = "Um titulo qualquer";
	private static final Date CURRENT_DATE = DateTime.now().toDate();
	private static final String USER_NAME = "Fulano";
	private static final String COMMENT = "Gostei do seu horário";
	
	JiraTimesheet timesheet = new JiraTimesheet(ID, KEY, TITLE, CURRENT_DATE, USER_NAME, CURRENT_DATE, COMMENT);
	
	JiraTimesheetDAO timesheetDAO = new JiraTimesheetDAO();
	
	@Test
	public void insert() {
		timesheetDAO.insert(timesheet);
		JiraTimesheet persistedTimesheet = timesheetDAO.getById(ID);
		timesheetDAO.removeById(ID);
		
		assertEquals(timesheet.getId(), persistedTimesheet.getId());
		assertEquals(timesheet.getKey(), persistedTimesheet.getKey());
		assertEquals(timesheet.getTitle(), persistedTimesheet.getTitle());
		//assertEquals(timesheet.getDate(), persistedTimesheet.getDate());
		assertEquals(timesheet.getUserName(), persistedTimesheet.getUserName());
		//assertEquals(timesheet.getTimeSpent(), persistedTimesheet.getTimeSpent());
		assertEquals(timesheet.getComment(), persistedTimesheet.getComment());
	}
	
	@Test
	public void listAll(){
		timesheetDAO.insert(timesheet);
		List<JiraTimesheet> listTimesheet = timesheetDAO.listAll();
		timesheetDAO.removeById(ID);
		
		if(listTimesheet.size() == 1){
			for (JiraTimesheet persistedTimesheet : listTimesheet) {				
			assertEquals(timesheet.getId(), persistedTimesheet.getId());
			assertEquals(timesheet.getKey(), persistedTimesheet.getKey());
			assertEquals(timesheet.getTitle(), persistedTimesheet.getTitle());
			//assertEquals(timesheet.getDate(), persistedTimesheet.getDate());
			assertEquals(timesheet.getUserName(), persistedTimesheet.getUserName());
			//assertEquals(timesheet.getTimeSpent(), persistedTimesheet.getTimeSpent());
			assertEquals(timesheet.getComment(), persistedTimesheet.getComment());
			}
		}		
	}
		
	
	
	public void update() {
		String newTitle = "Apenas um título!";
		
		timesheetDAO.insert(timesheet);
		timesheet.setTitle(newTitle);
		timesheetDAO.update(timesheet);
		JiraTimesheet persistedTimesheet = timesheetDAO.getById(ID);
		timesheetDAO.removeById(ID);
		
		assertEquals(timesheet.getId(), persistedTimesheet.getId());
		assertEquals(timesheet.getKey(), persistedTimesheet.getKey());
		assertNotSame(timesheet.getTitle(), persistedTimesheet.getTitle());
		//assertEquals(timesheet.getDate(), persistedTimesheet.getDate());
		assertEquals(timesheet.getUserName(), persistedTimesheet.getUserName());
		//assertEquals(timesheet.getTimeSpent(), persistedTimesheet.getTimeSpent());
		assertEquals(timesheet.getComment(), persistedTimesheet.getComment());
	}
	
	@Test
	public void removeById() {
		timesheetDAO.insert(timesheet);
		timesheetDAO.removeById(ID);
		JiraTimesheet persistedTimesheet = timesheetDAO.getById(ID);
		
		assertTrue(persistedTimesheet == null);
	}
	
	@Test
	public void getById(){
		timesheetDAO.insert(timesheet);
		JiraTimesheet persistedTimesheet = timesheetDAO.getById(ID);
		timesheetDAO.removeById(ID);
		
		assertEquals(timesheet.getId(), persistedTimesheet.getId());
		assertEquals(timesheet.getKey(), persistedTimesheet.getKey());
		assertEquals(timesheet.getTitle(), persistedTimesheet.getTitle());
		//assertEquals(timesheet.getDate(), persistedTimesheet.getDate());
		assertEquals(timesheet.getUserName(), persistedTimesheet.getUserName());
		//assertEquals(timesheet.getTimeSpent(), persistedTimesheet.getTimeSpent());
		assertEquals(timesheet.getComment(), persistedTimesheet.getComment());
	}
}
