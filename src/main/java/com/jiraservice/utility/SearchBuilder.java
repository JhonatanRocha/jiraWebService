package com.jiraservice.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchBuilder {

	private String projectKey;
	private String issueKey;
	private String resourceName;
	private String initialDate;
	private String finalDate;
	
	public SearchBuilder(String projectKey, String issueKey,
			String resourceName, Date initialDate,
			Date finalDate) {

		setProjectKey(projectKey);
		setIssueKey(issueKey);
		setResourceName(resourceName);
		setInitialDate(initialDate);
		setFinalDate(finalDate);
	}
	
	public void setProjectKey(String projectKey) {
		if(projectKey != null && !projectKey.isEmpty())
			this.projectKey = "project= " + projectKey;
		else
			this.projectKey = "";
	}

	public void setIssueKey(String issueKey) {
		if(issueKey != null && !issueKey.isEmpty())
			this.issueKey = "key= " + issueKey;
		else
			this.issueKey = "";
	}

	public void setResourceName(String resourceName) {
		if(resourceName != null && !resourceName.isEmpty())
			this.resourceName =  "worklogAuthor= " + resourceName;
		else
			this.resourceName = "";
	}

	public void setInitialDate(Date initialDate) {
		if(initialDate != null)
			this.initialDate = "worklogDate >= " + "\"" + formatDateddMMyyyy(initialDate) + "\"";
		else
			this.initialDate = "";
	}

	public void setFinalDate(Date finalDate) {
		if(finalDate != null)
			this.finalDate = "worklogDate <= " + "\"" + formatDateddMMyyyy(finalDate) + "\"";
		else
			this.finalDate = "";
	}
	
	public String formatDateddMMyyyy(Date dateToFormat) {
         SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd");
         return dt.format(dateToFormat);
	}
	
	public String add(){
		return " and ";
	}

	public String build(){
		String jql = this.projectKey + (!this.projectKey.isEmpty() ? add() : "") +
					this.issueKey + (!this.issueKey.isEmpty() ? add() : "") +
					this.resourceName + (!this.resourceName.isEmpty() ? add() : "") +
					this.initialDate + (!this.initialDate.isEmpty() ? add() : "") +
					this.finalDate;
		
		if(jql.endsWith(" and "))
			jql = jql.substring(0, jql.length()-5);
		
		return jql;
	}
}
