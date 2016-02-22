package com.jiraservice.utility;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.joda.time.DateTime;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.User;
import com.jiraservice.model.JiraIssue;
import com.jiraservice.model.JiraProject;
import com.jiraservice.model.JiraResource;
import com.jiraservice.model.JiraTimesheet;
import com.sun.jersey.api.client.ClientResponse;


/**
 * <P>
 * <B>Description :</B><BR>
 * Class containing the integration service with Atlassian JIRA.
 * </P>
 * 
 * @author <a href="mailto:jcristianrocha@gmail.com">Jhonatan Rocha</a>
 */
public class JiraServices implements Serializable {
	
	private static final long serialVersionUID = 3072228013239966011L;
	private JiraRestClient restClient;
	private JiraUtil jiraUtil;
	private Iterable<BasicProject> allBasicProjects;
	private Configuration bundle;
    
	public JiraServices() {
		
		try {
			this.bundle = new PropertiesConfiguration("config.properties");
			this.jiraUtil = new JiraUtil();
			this.restClient = jiraUtil.createClient(this.bundle);
			this.allBasicProjects = this.restClient.getProjectClient().getAllProjects().get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void closeClient() throws IOException {
		restClient.close();
	}
	
	public List<JiraProject> getProjectsBetweenDates(String cliente, DateTime initialDate, DateTime finalDate) throws Exception {
		
		List<JiraProject> projects = new ArrayList<JiraProject>();
		
		for (JiraProject project : getallProjects(cliente)) {
			String jql = "project = " + project.getKey();
			SearchRestClient client = this.restClient.getSearchClient();
			SearchResult results = client.searchJql(jql, 5000, 0, null).claim();
			List<Issue> issues = (List<Issue>) results.getIssues();
			Collections.sort(issues, new ComparatorIssue());

			boolean updateFlag = false;
			for (Issue issue : issues) {
				if(this.jiraUtil.isBetweenDate(issue.getUpdateDate(), initialDate, finalDate)) {
					updateFlag = true;
					break;
				}
			}
			
			if (updateFlag) {
				List<JiraIssue> jiraIssues = getAtividadesUsingDateInterval(results, initialDate, finalDate);
				if (jiraIssues.size() > 0) {
					project.setAtividades(jiraIssues);
					project.setDataCreate(issues.get(0).getCreationDate().toDate());
					projects.add(project);
				}
			}	
		}
		return projects;
	}

	public List<JiraProject> getAllProjetosByCliente(String client) throws Exception {
		List<JiraProject> projects = new ArrayList<JiraProject>();
		for (JiraProject project : getallProjects(client)) {
			
			String jql = "project = " + project.getKey();
			SearchRestClient searchRestClient = this.restClient.getSearchClient();
			SearchResult results = searchRestClient.searchJql(jql, 2000, 0, null).claim();
			List<Issue> issues = (List<Issue>) results.getIssues();
			Collections.sort(issues, new ComparatorIssue());
			
			List<JiraIssue> jiraIssues = getAtividadesUsingDateInterval(results, null, null);//getAtividades(results);
			if(jiraIssues.size() > 0) {				
				project.setAtividades(jiraIssues);
				project.setDataCreate(issues.get(0).getCreationDate().toDate());
				projects.add(project);
			}
						
		}
		return projects;
	}

	public List<JiraResource> getAllResources() throws InterruptedException, ExecutionException {
		List<JiraResource> resources = new ArrayList<JiraResource>();
		List<User> usersFromJIRA = getAllUsers("GFBD");
		
		for (User user : usersFromJIRA) {
			resources.add(new JiraResource(user.getName(), user.getDisplayName()));
		}
    	return resources;
	}

    public List<JiraProject> getallProjects(String filter) throws InterruptedException, ExecutionException, ConfigurationException, IOException{
    	List<JiraProject> projects = new ArrayList<JiraProject>();
    	for (BasicProject basicProject : this.allBasicProjects) {
			if(basicProject.getName().length() >= 5) {
				if(basicProject.getName().substring(2).startsWith(filter)) {
					Project project = this.jiraUtil.getProjectFromKey(basicProject.getKey());
					JiraProject jiraproject = new JiraProject();
					jiraproject.setKey(project.getKey());
					jiraproject.setProject(project.getName());
					jiraproject.setId(project.getId());
					projects.add(jiraproject);
				}
			}
		}
    	return projects;
    }
    
   public List<Issue> getAllIssuesFromProject(String projectKey) {
	   	String jql = "project = "+ projectKey;
	   	List<Issue> listIssues = new ArrayList<Issue>();
	   	SearchRestClient client = this.restClient.getSearchClient();
		SearchResult results = client.searchJql(jql, 2000, 0, null).claim();
		List<Issue> issues = (List<Issue>) results.getIssues();
		
		for (Issue issue : issues) {
			listIssues.add(issue);
		}
	   return listIssues; 
   }
    
    public List<User> getAllUsers(String projectKey) throws InterruptedException, ExecutionException {
    	List<Issue> issues = getAllIssuesFromProject(projectKey);
    	List<User> projectUsers = new ArrayList<User>();
    	
    	for (Issue issue : issues) {
			if(issue.getSummary().contains("Sem Demanda")) {
				projectUsers.add(issue.getAssignee());
			}
		}
    	return projectUsers;
    }

	public JiraProject getJiraProject(String projectKey) throws Exception {

		Project project = this.restClient.getProjectClient().getProject(projectKey).claim();
			
		String jql = "project = " + project.getKey();
		SearchRestClient searchRestClient = this.restClient.getSearchClient();
		SearchResult results = searchRestClient.searchJql(jql, 10000, 0, null).claim();
		List<Issue> issues = (List<Issue>) results.getIssues();
		DateTime creationDate = issues.get(0).getCreationDate();
		Collections.sort(issues, new ComparatorIssue());
		
		List<JiraIssue> jiraIssues = getAtividadesUsingDateInterval(results, null, null); //getAtividades(results);
		JiraProject jiraproject = new JiraProject();
		jiraproject.setKey(project.getKey());
		jiraproject.setProject(project.getName());
		jiraproject.setAtividades(jiraIssues);
		jiraproject.setDataCreate(creationDate.toDate());
		
		return jiraproject;
	}
	
	public JiraProject getProjectFromIssueKey(String issueKey) throws Exception {
		JiraProject jiraProject = new JiraProject();
		Issue issue = this.restClient.getIssueClient().getIssue(issueKey).claim();
		
		
		List<JiraTimesheet> worklogList = new ArrayList<>();
		worklogList = getTimesheetsFromIssueKeyUsingDate(issue, null, null);
		JiraIssue jiraIssue = this.jiraUtil.getJiraIssueFromDateInterval(issue, worklogList, null, null);
		
		if(jiraIssue != null) {
			JiraIssue[] jiraissues = new JiraIssue[] {jiraIssue};
			jiraProject.setAtividades(Arrays.asList(jiraissues));
			jiraProject.setProject(issue.getProject().getName());
			jiraProject.setId(issue.getId());
			jiraProject.setKey(issue.getProject().getKey());
			jiraProject.setDataCreate(issue.getCreationDate().toDate());
		}

		return jiraProject;
	}

	public JiraProject getProjectWorklogBetweenDates(String projectKey, DateTime initialDate, DateTime finalDate) throws Exception {

		Project project = this.jiraUtil.getProjectFromKey(projectKey);
		
		String jql = "project = " + project.getKey();
		SearchRestClient searchRestClient = this.restClient.getSearchClient();
		SearchResult results = searchRestClient.searchJql(jql, 2000, 0, null).claim();
		List<Issue> issues = (List<Issue>) results.getIssues();
		DateTime creationDate = issues.get(0).getCreationDate();
		Collections.sort(issues, new ComparatorIssue());
		
		List<JiraIssue> jiraIssues = getAtividadesUsingDateInterval(results, initialDate, finalDate);
		
		if (jiraIssues.size() > 0) {
			
			JiraProject jiraproject = new JiraProject();
			
			jiraproject.setKey(project.getKey());
			jiraproject.setProject(project.getName());
			jiraproject.setAtividades(jiraIssues);
			jiraproject.setDataCreate(creationDate.toDate());		
			
			return jiraproject;
		} else {
			return null;
		}
	}
	
	private List<JiraIssue> getAtividadesUsingDateInterval(SearchResult results, DateTime initialDate, DateTime finalDate) throws Exception {
		
		List<JiraIssue> jiraIssues = new ArrayList<JiraIssue>();
		for (final BasicIssue result : results.getIssues()) {
			
			Issue issue = this.restClient.getIssueClient().getIssue(result.getKey()).claim();
			List<JiraTimesheet> worklogList = new ArrayList<>();
			worklogList = getTimesheetsFromIssueKeyUsingDate(issue, initialDate, finalDate);
			
			if(worklogList.size() > 0) {
				JiraIssue jiraIssue = this.jiraUtil.getJiraIssueFromDateInterval(issue, worklogList, initialDate, finalDate);
				if(jiraIssue != null){
					jiraIssues.add(jiraIssue);
				}
			}
		}
		return jiraIssues;
	}
	
	private List<JiraTimesheet> getTimesheetsFromIssueKeyUsingDate(Issue issue, DateTime initialDate, DateTime finalDate) throws Exception {
		
		List<JiraTimesheet> jiraTimesheets = new ArrayList<>();
        ClientResponse response = this.jiraUtil.requestWorklogREST(issue, this.bundle);
        
        if(response.getStatus() == 200){
        	String outputJson = response.getEntity(String.class);
        	jiraTimesheets = this.jiraUtil.retrieveJiraTimesheetsFromJSON(outputJson, issue, initialDate, finalDate);
        	return jiraTimesheets;
        }else {
        	return jiraTimesheets;
        }
	}
}