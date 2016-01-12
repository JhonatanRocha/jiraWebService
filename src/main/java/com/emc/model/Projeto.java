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
public class Projeto {

	@Id
	private long id; /*ID do Projeto*/
	
	@Column
	private String project; /*Descrição do Projeto*/
	
	@Column
	private Date dataCreate; /*Data da Criação*/

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public Date getDataCreate() {
		return dataCreate;
	}

	public void setDataCreate(Date dataCreate) {
		this.dataCreate = dataCreate;
	}
}
