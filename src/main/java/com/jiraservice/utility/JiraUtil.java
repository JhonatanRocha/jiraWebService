package com.jiraservice.utility;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.jiraservice.model.JiraIssue;
import com.jiraservice.model.JiraTimesheet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * <P>
 * <B>Description :</B><BR>
 * Class containing the integration service with Atlassian JIRA.
 * </P>
 * 
 * @author <a href="mailto:jcristianrocha@gmail.com">Jhonatan Rocha</a>
 */
public class JiraUtil implements Serializable {
	
	private static final long serialVersionUID = -3813483976157592850L;

	/**
     * This method create the connection
     * of the Web Service Client from JIRA.
     * 
     * @return JiraRestClient
     * @throws ConfigurationException 
     */
	public JiraRestClient createClient(Configuration config) throws ConfigurationException {
    	
		final AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		JiraRestClient restClient = null;
		try {
			restClient = factory.createWithBasicHttpAuthentication(new URI(config.getString("server")), 
																			config.getString("usuario"), 
																			config.getString("senha"));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		return restClient;
	}
	
	public boolean isBetweenDate(DateTime dateToCompare, DateTime initialDate, DateTime finalDate){
	    DateTime parsedDate = DateTime.parse(DateTimeFormat.forPattern("yyyy-MM-dd").print(dateToCompare));
		return parsedDate.isAfter(initialDate) && parsedDate.isBefore(finalDate) 
				|| parsedDate.equals(initialDate) || parsedDate.equals(finalDate);
	}
	
	public Project getProjectFromKey(String projectKey) throws InterruptedException, ExecutionException, ConfigurationException, IOException {
		JiraRestClient jiraRestClient = createClient(new PropertiesConfiguration("config.properties"));
		Project project = jiraRestClient.getProjectClient().getProject(projectKey).get();
		jiraRestClient.close();
		return project;
	}
	
	public JiraIssue getJiraIssueFromDateInterval(Issue issue, List<JiraTimesheet> worklogList, DateTime initialDate, DateTime finalDate) throws JSONException {
		
		Integer tempoEstimado = (issue.getTimeTracking().getOriginalEstimateMinutes() == null ? 0 : issue.getTimeTracking().getOriginalEstimateMinutes())  / 60;
		if(issue.getAssignee() != null) {

			String assignedUser = issue.getAssignee().getName();
			String issueType = "";
			String resolution = "";
			
			if(issue.getIssueType() != null){
				issueType = issue.getIssueType().getName();
			}
			
			if(issue.getResolution() != null){
				resolution = issue.getResolution().getName();
			}
				
			int remainingTime = (issue.getTimeTracking().getRemainingEstimateMinutes() == null ? 0 : issue.getTimeTracking().getRemainingEstimateMinutes()) / 60;
			
			Object workrate = issue.getField("workratio").getValue();
			JSONObject jsonIssueCreator = (JSONObject) issue.getField("creator").getValue();
			
			String creator = jsonIssueCreator.get("name").toString();

			JiraIssue jiraIssue = null;

			if(initialDate != null && finalDate != null && isBetweenDate(issue.getUpdateDate(), initialDate, finalDate)) {
				
				jiraIssue = new JiraIssue(issue.getKey(), issue.getSummary(), issueType,
										  issue.getCreationDate().toDate(), assignedUser, tempoEstimado, 
										  issue.getTimeTracking().getTimeSpentMinutes(), remainingTime, issue.getStatus().getName(), 
										  Long.valueOf(workrate.toString()).longValue(), creator, resolution,
										  issue.getProject().getId(), issue.getUpdateDate().toDate(), worklogList);
			
				if(issue.getDueDate() != null){					
					jiraIssue.setDueDate(issue.getDueDate().toDate());
				}
				return jiraIssue;
			}else if(initialDate == null && finalDate == null){
				
				jiraIssue = new JiraIssue(issue.getKey(), issue.getSummary(), issueType,
						  issue.getCreationDate().toDate(), assignedUser, tempoEstimado, 
						  issue.getTimeTracking().getTimeSpentMinutes(), remainingTime, issue.getStatus().getName(), 
						  Long.valueOf(workrate.toString()).longValue(), creator, resolution,
						  issue.getProject().getId(), issue.getUpdateDate().toDate(), worklogList);

				if(issue.getDueDate() != null){					
					jiraIssue.setDueDate(issue.getDueDate().toDate());
				}
				return jiraIssue;
			}
		}
		return null;
	}
	
	public ClientResponse requestWorklogREST(Issue issue, Configuration config) throws ConfigurationException {
		String apiRestUrl = config.getString("server") + "/rest/api/latest/issue/"+ issue.getKey() + "/worklog";
		String authUserPass = config.getString("usuario") + ":" + config.getString("senha");
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
	
	public List<JiraTimesheet> retrieveJiraTimesheetsFromJSON(String outputJson, Issue issue, DateTime initialDate, DateTime finalDate) 
			throws JSONException {
		
		List<JiraTimesheet> jiraTimesheets = new ArrayList<>();
    	JSONObject obj = new JSONObject(outputJson);
    	
    	if(obj.has("worklogs")) {
        	JSONArray jsonObjArray = obj.getJSONArray("worklogs");
        	
        	for(int i = 0 ; i < jsonObjArray.length() ; i++) {
        		JSONObject jsonObject = jsonObjArray.getJSONObject(i);
        		DateTime worklogStartedDate = DateTime.parse(jsonObject.getString("started"));
        		
        		if (initialDate != null && finalDate != null && isBetweenDate(worklogStartedDate, initialDate, finalDate)) {
        		
	        		String timeSpentSecond = jsonObject.getString("timeSpentSeconds");
	        		String comment = jsonObject.getString("comment");
	        		JSONObject jsonObjectUpdateAuthor = jsonObject.getJSONObject("updateAuthor");
	        		String author = jsonObjectUpdateAuthor.getString("displayName");
	        		Integer timeSpentMinutes = Integer.parseInt(timeSpentSecond) / 60;
	        	    
	        	    jiraTimesheets.add(new JiraTimesheet(issue.getKey(), 
	        	    									 issue.getSummary(), 
	        	    									 worklogStartedDate.toDate(), 
	        	    									 author, 
	        	    									 timeSpentMinutes, 
	        	    									 comment));
        		} else if(initialDate == null && finalDate == null) {
        			
        			String timeSpentSecond = jsonObject.getString("timeSpentSeconds");
	        		String comment = jsonObject.getString("comment");
	        		JSONObject jsonObjectUpdateAuthor = jsonObject.getJSONObject("updateAuthor");
	        		String author = jsonObjectUpdateAuthor.getString("displayName");
	        		Integer timeSpentMinutes = Integer.parseInt(timeSpentSecond) / 60;
	        	    
	        	    jiraTimesheets.add(new JiraTimesheet(issue.getKey(), 
	        	    									 issue.getSummary(), 
	        	    									 worklogStartedDate.toDate(), 
	        	    									 author, 
	        	    									 timeSpentMinutes, 
	        	    									 comment));
        		}
        	}
    	}	
    	return jiraTimesheets;
	}
	
}