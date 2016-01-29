package com.jiraservice.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.joda.time.DateTime;

import com.atlassian.jira.rest.client.api.domain.Status;

/**
 * 
 * @author Jhonatan Rocha
 *
 */

@Entity
public class JiraIssue {

	
	@Id
	@Column
	private String keyMap; /*Chave da Atividade*/
	
	@Column
	private long id; /*ID do Projeto*/
	
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
	private Integer originalEstimate; /*Estimativa da Atividade*/
	
	@Column
	private Integer timeSpent; /*Total de horas lançadas*/
	
	@Column
	private Integer remainingEstimate; /*Tempo faltante*/
	
	@Column
	private long workRatio; /*% de Trabalho efetivo*/
	
	@Column
	private String sprint; /*Qual a sprint*/
	
	@Column
	private String creator; /*Recurso que criou a atividade*/
	
	@Column
	private String progress; /*Progresso da atividade*/
	
	@Column
	private Date dueDate; /*Datas Prevista finalização*/

	private List<JiraTimesheet> worklogs;
	
	public JiraIssue() {

	}
	
	public JiraIssue(String keyMap, String summary, String issueType,
			Date created, String resolution, Date resolved, Date updated,
			String assigned, String status, Integer originalEstimate,
			Integer timeSpent, Integer remainingEstimate, long workRatio,
			String sprint, String creator, String progress, Date dueDate) {
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

	public JiraIssue(String key, String summary, String issueType,
			Date creationDate, String assigned, Integer tempoEstimado,
			Integer executedHourTotal,Integer remaningTime, String status, long workratio, String creator) {
		
		this.keyMap = key;
		this.summary = summary;
		this.issueType = issueType;
		this.created = creationDate;
		this.assigned = assigned;
		this.originalEstimate = tempoEstimado;
		this.timeSpent = executedHourTotal;
		this.remainingEstimate = remaningTime;
		this.status = status;
		this.workRatio = workratio;
		this.creator = creator;
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getKeyMap() {
		return keyMap;
	}

	public void setKeyMap(String key) {
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

	public Integer getOriginalEstimate() {
		return originalEstimate;
	}

	public Integer getTimeSpent() {
		return timeSpent;
	}
	
	public void setTimeSpent(Integer timeSpent) {
		this.timeSpent = timeSpent;
	}
	
	public void setOriginalEstimate(Integer originalEstimate) {
		this.originalEstimate = originalEstimate;
	}

	public Integer getRemainingEstimate() {
		return remainingEstimate;
	}

	public void setRemainingEstimate(Integer remainingEstimate) {
		this.remainingEstimate = remainingEstimate;
	}

	public long getWorkRatio() {
		return workRatio;
	}

	public void setWorkRatio(long workRatio) {
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

	public List<JiraTimesheet> getWorklogs() {
		return worklogs;
	}

	public void setWorklogs(List<JiraTimesheet> worklogs) {
		this.worklogs = worklogs;
	}
}
