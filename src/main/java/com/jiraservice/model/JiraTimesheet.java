package com.jiraservice.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;

/**
 * 
 * @author Jhonatan Rocha
 *
 */

@Entity(name="timesheet")
public class JiraTimesheet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idTimesheet;
	
	@Column(name="keyid")
	private String key; /*Chave da Atividade*/
	
	@Column
	private long id; /*ID do Projeto*/
	
	@Column
	private String title; /*Descrição da Atividade*/
	
	@Column
	private Date date; /*Data do Lançamento*/
	
	@Column
	private String userName; /*Nome do Recurso*/
	
	@Column
	private Integer timeSpent; /*Horas Lançadas*/
	
	@Column(length = 500)
	private String comment; /*Comentários*/
	
	@Column
	private String formatedTimeSpent;

	public JiraTimesheet(String key, String title, Date date, String userName, Integer timeSpent, String comment) {
		this.key = key;
		this.title = title;
		this.date = date;
		this.userName = userName;
		this.timeSpent = timeSpent;
		this.comment = comment;
		this.formatedTimeSpent = getFormatTimeSpent(timeSpent);
	}

	public long getIdTimesheet() {
		return idTimesheet;
	}

	public void setIdTimesheet(long idTimesheet) {
		this.idTimesheet = idTimesheet;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getTimeSpent() {
		return timeSpent;
	}
	
	public void setTimeSpent(Integer timeSpent) {
		this.timeSpent = timeSpent;
	}
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getFormatedTimeSpent() {
		return formatedTimeSpent;
	}

	public void setFormatedTimeSpent(String formatedTimeSpent) {
		this.formatedTimeSpent = formatedTimeSpent;
	}
	
	public String getFormatTimeSpent(Integer timeSpentMinutes) {
		Integer hours = timeSpentMinutes / 60;
		timeSpentMinutes -= hours * 60;
		String timeSpentString = String.valueOf(hours) + "h " + String.valueOf(timeSpentMinutes) + "m";
		return timeSpentString;
	}
}
