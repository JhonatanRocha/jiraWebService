package com.emc.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;

/**
 * 
 * @author Jhonatan Rocha
 *
 */

@Entity
public class Timesheet {

	@Id
	private long id; /*ID do Projeto*/
	
	@Column
	private String keyMap; /*Chave da Atividade*/
	
	@Column
	private String title; /*Descrição da Atividade*/
	
	@Column
	private Date date; /*Data do Lançamento*/
	
	@Column
	private String userName; /*Nome do Recurso*/
	
	@Column
	private Date timeSpent; /*Horas Lançadas*/
	
	@Column
	private String comment; /*Comentários*/

	public Timesheet() {

	}

	public Timesheet(long id, String keyMap, String title, Date date,
			String userName, Date timeSpent, String comment) {
		this.id = id;
		this.keyMap = keyMap;
		this.title = title;
		this.date = date;
		this.userName = userName;
		this.timeSpent = timeSpent;
		this.comment = comment;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getKey() {
		return keyMap;
	}

	public void setKey(String key) {
		this.keyMap = key;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
