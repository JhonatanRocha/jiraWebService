package com.jiraservice.utility;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.joda.time.DateTime;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Issue;
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
    
	public JiraServices(String user, String password) {
		
		try {
			this.bundle = new PropertiesConfiguration("config.properties");
			this.jiraUtil = new JiraUtil(user, password);
			this.restClient = jiraUtil.createClient(this.bundle);
			this.allBasicProjects = this.restClient.getProjectClient().getAllProjects().get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Close the connecton from the REST Client.
	 * @throws IOException.
	 */
	public void closeClient() throws IOException {
		restClient.close();
	}

	/**
	 * Get all the users from specific project.
	 * @param projectKey.
	 * @return List<User>.
	 * @throws InterruptedException.
	 * @throws ExecutionException.
	 */
    public List<User> getAllUsers(String projectKey) throws InterruptedException, ExecutionException {
    	String jql = "project = "+ projectKey;
	   	SearchRestClient client = this.restClient.getSearchClient();
		SearchResult results = client.searchJql(jql, 2000, 0, null).claim();
		List<Issue> issues = (List<Issue>) results.getIssues();
    	List<User> projectUsers = new ArrayList<User>();
    	
    	for (Issue issue : issues) {
			if(issue.getSummary().contains("Sem Demanda"))
				projectUsers.add(issue.getAssignee());
		}
    	return projectUsers;
    }
	
    /**
     * All Resources from current team are registered on project "GFBD".
     * Get all Resources from from project "GFBD".
     * @return List<JiraResource>.
     * @throws InterruptedException.
     * @throws ExecutionException.
     */
	public List<JiraResource> getAllResources() throws InterruptedException, ExecutionException {
		List<JiraResource> resources = new ArrayList<JiraResource>();
		List<User> usersFromJIRA = getAllUsers("GFBD");
		
		for (User user : usersFromJIRA) {
			resources.add(new JiraResource(user.getName(), user.getDisplayName()));
		}
    	return resources;
	}
	
	/**
	 * 
	 * @param issue.
	 * @param initialDate.
	 * @param finalDate.
	 * @param resourceName.
	 * @return List<JiraTimesheet>.
	 * @throws Exception.
	 */
	private List<JiraTimesheet> getTimesheetsFromIssueKeyUsingDate(Issue issue, DateTime initialDate, DateTime finalDate, String resourceName) throws Exception {
		
		List<JiraTimesheet> jiraTimesheets = new ArrayList<>();
        ClientResponse response = this.jiraUtil.requestWorklogREST(issue, this.bundle);
        
        if(response.getStatus() == 200){
        	String outputJson = response.getEntity(String.class);
        	jiraTimesheets = this.jiraUtil.retrieveJiraTimesheetsFromJSON(outputJson, issue, initialDate, finalDate, resourceName);
        	return jiraTimesheets;
        }else
        	return jiraTimesheets;
	}

	/**
	 * Returns the Project from current issue, using date and resource name.
	 * @param issue.
	 * @param initialDate.
	 * @param finalDate.
	 * @param resourceName.
	 * @return JiraIssue.
	 * @throws Exception.
	 */
	public JiraIssue getProjectFromIssueKeyBetweenDates(Issue issue, DateTime initialDate, DateTime finalDate, String resourceName) throws Exception {	
		
		List<JiraTimesheet> worklogList = new ArrayList<>();
		worklogList = getTimesheetsFromIssueKeyUsingDate(issue, initialDate, finalDate, resourceName);
		if(worklogList.size() > 0)
			return this.jiraUtil.getJiraIssueFromDateInterval(issue, worklogList, initialDate, finalDate);
		else
			return null;
	}
	
	/**
	 * All projects on our current Jira, is named like: "BRTIMSINSERTNAMEHERE".
	 * Where "BR" refers to Brazil and "TIM" refers to client name.
	 * Using the client name prefix to retrieve all projects.
	 * @param clientPrefix.
	 * @return List<JiraProject>.
	 * @throws Exception.
	 */
	public List<JiraProject> getProjectsByClientPrefix(String clientPrefix) throws Exception {

		List<JiraProject> projects = new ArrayList<JiraProject>();

		for(BasicProject project : this.allBasicProjects) {
			
			if(clientPrefix.equals("BR") && project.getName().substring(0, 2).startsWith(clientPrefix) || 
					project.getName().substring(2).startsWith(clientPrefix)) {
				String jql = new SearchBuilder(project.getKey(), null, null, null, null).build();
				SearchRestClient searchRestClient = this.restClient.getSearchClient();
				SearchResult results = searchRestClient.searchJql(jql, 10000, 0, null).claim();
				
				if(results.getTotal() > 0) {
					List<JiraIssue> jiraIssues = new ArrayList<JiraIssue>();
					List<Issue> issues = (List<Issue>) results.getIssues();
					Collections.sort(issues, new ComparatorIssue());
					
					for (Issue issue : issues) {
						JiraIssue jiraIssue = getProjectFromIssueKeyBetweenDates(issue, null, null, "");
						
						if(jiraIssue != null) {
							jiraIssues.add(jiraIssue);
							/*JiraIssue[] jiraissues = new JiraIssue[] {jiraIssue};
							JiraProject jiraProject = new JiraProject();
							
							jiraProject.setAtividades(Arrays.asList(jiraissues));
							jiraProject.setProject(issue.getProject().getName());
							jiraProject.setId(issue.getId());
							jiraProject.setKey(issue.getProject().getKey());
							jiraProject.setDataCreate(issue.getCreationDate().toDate());
							projects.add(jiraProject);*/
							
						}
					}
					
					if(!jiraIssues.isEmpty()){
						JiraProject jiraProject = new JiraProject();
						
						jiraProject.setAtividades(jiraIssues);
						jiraProject.setProject(project.getName());
						jiraProject.setId(project.getId());
						jiraProject.setKey(project.getKey());
						jiraProject.setDataCreate(jiraIssues.get(0).getCreated());
						projects.add(jiraProject);
					}
				}
			}
		}
		return projects;
	}
	
	/**
	 * Search Projects using JQL Service, where the JQL is a SQL-like string
	 * to retrieve projects.
	 * @param jql.
	 * @param clientPrefix.
	 * @param initialDate.
	 * @param finalDate.
	 * @param resourceName.
	 * @return List<JiraProject>.
	 * @throws Exception.
	 */
	public List<JiraProject> searchProjectUsingJQL(String jql, String clientPrefix, DateTime initialDate, DateTime finalDate, String resourceName) throws Exception {
		
		SearchRestClient searchRestClient = this.restClient.getSearchClient();
		SearchResult results = searchRestClient.searchJql(jql, 10000, 0, null).claim();
		List<JiraProject> projects = new ArrayList<JiraProject>();
		
		if(results.getTotal() > 0) {
			List<Issue> issues = (List<Issue>) results.getIssues();
			Collections.sort(issues, new ComparatorIssue());
			Map<String, JiraProject> newMap = new HashMap<String, JiraProject>();
			for (Issue issue : issues) {
				
				if((clientPrefix.equals("BR") && issue.getProject().getName().substring(0, 2).startsWith(clientPrefix)) ||
						(issue.getProject().getName().substring(2).startsWith(clientPrefix))) {

					JiraIssue jiraIssue = getProjectFromIssueKeyBetweenDates(issue, this.jiraUtil.removeTime(initialDate),
																				this.jiraUtil.removeTime(finalDate), resourceName);
					
					if(newMap.containsKey(issue.getProject().getKey())) {
						JiraProject jiraProject = newMap.get(issue.getProject().getKey());
						List<JiraIssue> atividades = new ArrayList<JiraIssue>();
						atividades.add(jiraIssue);
						atividades.addAll(jiraProject.getAtividades());
						newMap.put(issue.getProject().getKey(), jiraProject);
						projects = new ArrayList<JiraProject>(newMap.values());
					} else {
						JiraIssue[] jiraissues = new JiraIssue[] {jiraIssue};
						JiraProject project = new JiraProject();
						project.setAtividades(Arrays.asList(jiraissues));
						project.setProject(issue.getProject().getName());
						project.setId(issue.getId());
						project.setKey(issue.getProject().getKey());
						project.setDataCreate(issue.getCreationDate().toDate());
						projects.add(project);
						newMap.put(project.getKey(), project);
					}
				}
			}
		}
		return projects;
	}
}