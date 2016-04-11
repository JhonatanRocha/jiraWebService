package com.jiraservice.utility;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.jiraservice.model.JiraIssue;
import com.jiraservice.model.JiraTimesheet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * <P>
 * <B>Description :</B><BR>
 * Class containing all utility methods from Atlassian JIRA REST API and REST Service.
 * </P>
 * 
 * @author <a href="mailto:jcristianrocha@gmail.com">Jhonatan Rocha</a>
 */
public class JiraUtil implements Serializable {
	
	private static final long serialVersionUID = -3813483976157592850L;

	private String user;
	private String pass;
	
	public JiraUtil(String user, String pass) {
		this.user = user;
		this.pass = pass;
	}
	
	/**
     * This method create the connection
     * of the Web Service Client from JIRA.
     * 
     * @return JiraRestClient.
     * @throws ConfigurationException .
     */
	public JiraRestClient createClient(Configuration config) throws ConfigurationException {
    	
		final AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		JiraRestClient restClient = null;
		try {
			restClient = factory.createWithBasicHttpAuthentication(new URI(config.getString("server")), 
																			this.user, 
																			this.pass);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		return restClient;
	}
	
	/**
	 * Check if date is between two dates.
	 * @param dateToCompare.
	 * @param initialDate.
	 * @param finalDate.
	 * @return true/false.
	 */
	public boolean isBetweenDate(DateTime dateToCompare, DateTime initialDate, DateTime finalDate){
		
		return dateToCompare.isEqual(initialDate) || 
							dateToCompare.isEqual(finalDate) ||
							(initialDate.isBefore(dateToCompare) && finalDate.isAfter(dateToCompare));
	}
	
	/**
	 * Remove and set 00 on hour/min/sec/millis time values.
	 * @param dateTime.
	 * @return DateTime.
	 */
	public DateTime removeTime(DateTime dateTime) {
		if(dateTime != null) {
			String year = String.valueOf(dateTime.getYear());
			String month = String.valueOf(dateTime.getMonthOfYear());
			String day = String.valueOf(dateTime.getDayOfMonth());
			String stringDate = year + "-" + month + "-" + day + "T00:00:00.000-00:00";
			return DateTime.parse(stringDate);
		} else
			return null;
    }
	
	/**
	 * Get the JiraIssue using Date interval.
	 * @param issue.
	 * @param worklogList.
	 * @param initialDate.
	 * @param finalDate.
	 * @return JiraIssue.
	 * @throws JSONException.
	 */
	public JiraIssue getJiraIssueFromDateInterval(Issue issue, List<JiraTimesheet> worklogList, DateTime initialDate, DateTime finalDate) throws JSONException {
		
		String assignedUser = "Não Atribuído";
		Integer tempoEstimado = 0;
		int remainingTime = 0;
		String issueType = "";
		String resolution = "";
		
		if(issue.getAssignee() != null)
			assignedUser = issue.getAssignee().getName();
	
		if(issue.getTimeTracking() != null){
			tempoEstimado = (issue.getTimeTracking().getOriginalEstimateMinutes() == null ? 0 : issue.getTimeTracking().getOriginalEstimateMinutes())  / 60;
			remainingTime = (issue.getTimeTracking().getRemainingEstimateMinutes() == null ? 0 : issue.getTimeTracking().getRemainingEstimateMinutes()) / 60;
		}
		
		if(issue.getIssueType() != null)
			issueType = issue.getIssueType().getName();
		
		if(issue.getResolution() != null)
			resolution = issue.getResolution().getName();
		
		JSONObject progressJsonField = (JSONObject) issue.getField("progress").getValue();
		//int timespent = Integer.parseInt(progressJsonField.get("total").toString()) / 60;
		Long workratio = Long.valueOf(progressJsonField.get("percent").toString());
		
		JSONObject jsonIssueCreator = (JSONObject) issue.getField("creator").getValue();
		String creator = jsonIssueCreator.get("name").toString();

		JiraIssue jiraIssue = null;

		if(initialDate != null && finalDate != null) {
			
			jiraIssue = new JiraIssue(issue.getKey(), issue.getSummary(), issueType,
									  issue.getCreationDate().toDate(), assignedUser, tempoEstimado, 
									  getTimeSpentInterval(worklogList), remainingTime, issue.getStatus().getName(), 
									  workratio, creator, resolution,
									  issue.getProject().getId(), issue.getUpdateDate().toDate(), worklogList);
		
			if(issue.getDueDate() != null)			
				jiraIssue.setDueDate(issue.getDueDate().toDate());
			
			return jiraIssue;
		} else if(initialDate == null && finalDate == null) {
			jiraIssue = new JiraIssue(issue.getKey(), issue.getSummary(), issueType,
					  issue.getCreationDate().toDate(), assignedUser, tempoEstimado, 
					  getTimeSpentInterval(worklogList), remainingTime, issue.getStatus().getName(), 
					  workratio, creator, resolution,
					  issue.getProject().getId(), issue.getUpdateDate().toDate(), worklogList);

			if(issue.getDueDate() != null)				
				jiraIssue.setDueDate(issue.getDueDate().toDate());
			
			return jiraIssue;
		}
		return null;
	}
	
	/**
	 * Get the clientResponse of JIRA REST API from worklog url.
	 * @param issue.
	 * @param config.
	 * @return ClientResponse.
	 * @throws ConfigurationException.
	 */
	public ClientResponse requestWorklogREST(Issue issue, Configuration config) throws ConfigurationException {
		String apiRestUrl = config.getString("server") + "/rest/api/latest/issue/"+ issue.getKey() + "/worklog";
		String authUserPass = this.user + ":" + this.pass;
        String authEncryption = new Base64().encodeToString(authUserPass.getBytes());
 
        Client restClient = Client.create();
        WebResource webResource = restClient.resource(apiRestUrl);
        ClientResponse resp = webResource
						        		.accept("application/json")
						        		.header("Authorization", "Basic " + authEncryption)
						        		.get(ClientResponse.class);
        restClient.destroy();
        return resp;
	}
	
	/**
	 * Parse the outputJson JSON to build JiraTimesheet.
	 * @param outputJson.
	 * @param issue.
	 * @param initialDate.
	 * @param finalDate.
	 * @param resourceName.
	 * @return List<JiraTimesheet>.
	 * @throws JSONException.
	 */
	public List<JiraTimesheet> retrieveJiraTimesheetsFromJSON(String outputJson, Issue issue, 
			DateTime initialDate, DateTime finalDate, String resourceName) throws JSONException {
		
		List<JiraTimesheet> jiraTimesheets = new ArrayList<>();
    	JSONObject obj = new JSONObject(outputJson);
    	
    	if(obj.has("worklogs")) {
        	JSONArray jsonObjArray = obj.getJSONArray("worklogs");
        	
        	for(int i = 0 ; i < jsonObjArray.length() ; i++) {
        		JSONObject jsonObject = jsonObjArray.getJSONObject(i);
        		DateTime worklogStartedDate = removeTime(DateTime.parse(jsonObject.getString("started")));
        		JSONObject jsonObjectUpdateAuthor = jsonObject.getJSONObject("updateAuthor");
        		String author = jsonObjectUpdateAuthor.getString("name");

        		if (initialDate != null && 
        				finalDate != null && 
        				isBetweenDate(worklogStartedDate, initialDate, finalDate) && 
        				resourceName.isEmpty()) {
        		
        			String authorDisplayedName = jsonObjectUpdateAuthor.getString("displayName");
	        		String timeSpentSecond = jsonObject.getString("timeSpentSeconds");
	        		String comment = jsonObject.getString("comment");
	        		String id = jsonObject.getString("id");
        			Integer timeSpentMinutes = Integer.parseInt(timeSpentSecond) / 60;
		        	    
	        	    jiraTimesheets.add(new JiraTimesheet(Long.parseLong(id),issue.getKey(), 
		        	    									 issue.getSummary(), 
		        	    									 worklogStartedDate.toDate(), 
		        	    									 authorDisplayedName, 
		        	    									 timeSpentMinutes, 
		        	    									 comment));		
        		}else if (initialDate != null && 
        					finalDate != null && 
        					isBetweenDate(worklogStartedDate, initialDate, finalDate) && 
        					!resourceName.isEmpty() && author.equals(resourceName)) {
        			
        			String authorDisplayedName = jsonObjectUpdateAuthor.getString("displayName");
	        		String timeSpentSecond = jsonObject.getString("timeSpentSeconds");
	        		String comment = jsonObject.getString("comment");
	        		String id = jsonObject.getString("id");
        			Integer timeSpentMinutes = Integer.parseInt(timeSpentSecond) / 60;
		        	    
	        	    jiraTimesheets.add(new JiraTimesheet(Long.parseLong(id),issue.getKey(), 
		        	    									 issue.getSummary(), 
		        	    									 worklogStartedDate.toDate(), 
		        	    									 authorDisplayedName, 
		        	    									 timeSpentMinutes, 
		        	    									 comment));		
        		} else if(initialDate == null && 
        					finalDate == null && 
        					resourceName.isEmpty()) {
        			
        			String authorDisplayedName = jsonObjectUpdateAuthor.getString("displayName");
        			String timeSpentSecond = jsonObject.getString("timeSpentSeconds");
	        		String comment = jsonObject.getString("comment");
	        		String id = jsonObject.getString("id");
        			Integer timeSpentMinutes = Integer.parseInt(timeSpentSecond) / 60;
		        	    
	        	    jiraTimesheets.add(new JiraTimesheet(Long.parseLong(id),issue.getKey(), 
	        	    									 issue.getSummary(), 
	        	    									 worklogStartedDate.toDate(), 
	        	    									 authorDisplayedName, 
	        	    									 timeSpentMinutes, 
	        	    									 comment));
        		} else if(initialDate == null && 
        					finalDate == null && 
        					!resourceName.isEmpty() && 
        					author.equals(resourceName)) {
        			
        			String authorDisplayedName = jsonObjectUpdateAuthor.getString("displayName");
        			String timeSpentSecond = jsonObject.getString("timeSpentSeconds");
	        		String comment = jsonObject.getString("comment");
	        		String id = jsonObject.getString("id");
        			Integer timeSpentMinutes = Integer.parseInt(timeSpentSecond) / 60;
		        	    
	        	    jiraTimesheets.add(new JiraTimesheet(Long.parseLong(id),issue.getKey(), 
	        	    									 issue.getSummary(), 
	        	    									 worklogStartedDate.toDate(), 
	        	    									 authorDisplayedName, 
	        	    									 timeSpentMinutes, 
	        	    									 comment));
        		}
        	}
    	}	
    	return jiraTimesheets;
	}
	
	/**
	 * Return the sum of all worklogs from single issue.
	 * @param List<JiraTimesheet> worklogs
	 * @return Integer
	 */
	public int getTimeSpentInterval(List<JiraTimesheet> worklogs){
		Integer timeSpent = 0;
		for (JiraTimesheet jiraTimesheet : worklogs) {
			timeSpent += jiraTimesheet.getTimeSpent();
		}
		return timeSpent;
	}
}