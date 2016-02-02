package com.jiraservice.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.OneToMany;

/**
 * 
 * @author Jhonatan Rocha
 *
 */

@Entity(name="projeto")
public class JiraProject {

	@Id
	@Column(name="keyid")
	private String key;
	
	@Column
	private long id; /*ID do Projeto*/
	
	@Column
	private String project; /*Descrição do Projeto*/
	
	@Column
	private Date dataCreate; /*Data da Criação*/
	
	@OneToMany
	private List<JiraIssue> atividades;

	public JiraProject() {
		
	}
	
	public JiraProject(long id, String project, Date dataCreate) {
		this.id = id;
		this.project = project;
		this.dataCreate = dataCreate;
	}

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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public List<JiraIssue> getAtividades() {
		return atividades;
	}

	public void setAtividades(List<JiraIssue> atividades) {
		this.atividades = atividades;
	}
}
