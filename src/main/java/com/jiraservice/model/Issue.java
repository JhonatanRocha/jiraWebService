package com.jiraservice.model;

import java.util.Date;

import com.google.gson.Gson;

public class Issue {

	private long id; /*ID do Projeto*/
	
	private String keyMap; /*Chave da Atividade*/
	
	private String summary; /*Descrição da Atividade*/
	
	private String issueType; /*Tipo da Atividade*/
	
	private Date created; /*Data de Criação*/
	
	private String resolution; /*Status de Resolução*/
	
	private Date resolved; /*Data da Resolução*/
	
	private Date updated; /*Data da Atualização*/
	
	private String assigned; /*Recurso assignado*/
	
	private String status; /*Status da Atividade*/
	
	private String originalEstimate; /*Estimativa da Atividade*/
	
	private Date timeSpent; /*Total de horas lançadas*/
	
	private Date remainingEstimate; /*Tempo faltante*/
	
	private String workRatio; /*% de Trabalho efetivo*/
	
	private String sprint; /*Qual a sprint*/
	
	private String creator; /*Recurso que criou a atividade*/
	
	private String progress; /*Progresso da atividade*/
	
	private Date dueDate; /*Datas Prevista finalização*/

	
	public Issue() {

	}
	
	public Issue(long id, String keyMap, String summary, String issueType,
			Date created, String resolution, Date resolved, Date updated,
			String assigned, String status, String originalEstimate,
			Date timeSpent, Date remainingEstimate, String workRatio,
			String sprint, String creator, String progress, Date dueDate) {
		this.id = id;
		this.keyMap = keyMap;
		this.summary = summary;
		this.issueType = issueType;
		this.created = created;
		this.resolution = resolution;
		this.resolved = resolved;
		this.updated = updated;
		this.assigned = assigned;
		this.status = status;
		this.originalEstimate = originalEstimate;
		this.timeSpent = timeSpent;
		this.remainingEstimate = remainingEstimate;
		this.workRatio = workRatio;
		this.sprint = sprint;
		this.creator = creator;
		this.progress = progress;
		this.dueDate = dueDate;
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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public Date getResolved() {
		return resolved;
	}

	public void setResolved(Date resolved) {
		this.resolved = resolved;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getAssigned() {
		return assigned;
	}

	public void setAssigned(String assigned) {
		this.assigned = assigned;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOriginalEstimate() {
		return originalEstimate;
	}

	public Date getTimeSpent() {
		return timeSpent;
	}
	
	public void setTimeSpent(Date timeSpent) {
		this.timeSpent = timeSpent;
	}
	
	public void setOriginalEstimate(String originalEstimate) {
		this.originalEstimate = originalEstimate;
	}

	public Date getRemainingEstimate() {
		return remainingEstimate;
	}

	public void setRemainingEstimate(Date remainingEstimate) {
		this.remainingEstimate = remainingEstimate;
	}

	public String getWorkRatio() {
		return workRatio;
	}

	public void setWorkRatio(String workRatio) {
		this.workRatio = workRatio;
	}

	public String getSprint() {
		return sprint;
	}

	public void setSprint(String sprint) {
		this.sprint = sprint;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	public String toJSON() {
		return new Gson().toJson(this);
	}
}
