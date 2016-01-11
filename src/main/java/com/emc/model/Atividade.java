package com.emc.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Atividade {

	@Id
	private long id; /*ID do Projeto*/
	
	@Column
	private String keyMap; /*Chave da Atividade*/
	
	@Column
	private String summary; /*Descrição da Atividade*/
	
	@Column
	private String issueType; /*Tipo da Atividade*/
	
	@Column
	private Date created; /*Data de Criação*/
	
	@Column
	private String resolution; /*Status de Resolução*/
	
	@Column
	private Date resolved; /*Data da Resolução*/
	
	@Column
	private Date updated; /*Data da Atualização*/
	
	@Column
	private String assigned; /*Recurso assignado*/
	
	@Column
	private String status; /*Status da Atividade*/
	
	@Column
	private String originalEstimate; /*Estimativa da Atividade*/
	
	@Column
	private Date timeSpent; /*Total de horas lançadas*/
	
	@Column
	private Date remainingEstimate; /*Tempo faltante*/
	
	@Column
	private String workRatio; /*% de Trabalho efetivo*/
	
	@Column
	private String sprint; /*Qual a sprint*/
	
	@Column
	private String creator; /*Recurso que criou a atividade*/
	
	@Column
	private String progress; /*Progresso da atividade*/
	
	@Column
	private Date dueDate; /*Datas Prevista finalização*/

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
}
