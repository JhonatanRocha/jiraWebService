package com.jiraservice.utility;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.User;

public class JiraServices {

	/*
	 * 
	 *URI para estudos:
	 *https://github.com/vigneshsakthi/JIRA_API/blob/master/Test/src/main/java/TestPro/Test/App.java
	 *
	 *
	 * URI para consultar uma atividade:
	 * https://emcconsulting.atlassian.net/rest/api/latest/issue/GFBD-223
	 *
	 *URL test de autenticação
	 * curl -D- -u jhonatan.rocha:d24m02j -X GET -H "Content-Type: application/json" https://emcconsulting.atlassian.net/rest/api/latest/issue/GFBD-223
	 * 
	 * curl -u jhonatan.rocha:d24m02j -H "Content-Type: application/json" https://emcconsulting.atlassian.net/rest/api/latest/project
	 * 
	 * Dados do usuário
	 * curl -u jhonatan.rocha:d24m02j -H "Content-Type: application/json" https://emcconsulting.atlassian.net/rest/api/2/myself
	 * */
	
	private JiraRestClient restClient;
	private JiraUtil jiraUtil;
    
	public JiraServices(){
		
		this.jiraUtil = new JiraUtil();
		try {
			this.restClient = jiraUtil.createClient();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
    
    
    /**
     * 
     * @param filter
     * @return list of Projects from JIRA
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public List<Project> getallProjects(String filter) throws InterruptedException, ExecutionException{
    	Iterable<BasicProject> allBasicProjects = this.restClient.getProjectClient().getAllProjects().get();
    	List<Project> projects = new ArrayList<Project>();
    	String projectKey = "";
    	for (BasicProject basicProject : allBasicProjects) {
			if(basicProject.getName().length() >= 5){
				if(basicProject.getName().substring(2, 5).equals(filter)){
					projectKey = basicProject.getKey();
					Project project = getSingleProjectFromKey(projectKey);
					projects.add(project);
				}
			}
		}
    	return projects;
    }
    
    /**
     * 
     * @param projectKey
     * @return list of Issues from Single Project from JIRA
     */
    public List<Issue> getAllIssuesFromProject(String projectKey){
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
    
    /**
     * 
     * @param projectKey
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public Project getSingleProjectFromKey(String projectKey) throws InterruptedException, ExecutionException{
    	Project project = this.restClient.getProjectClient().getProject(projectKey).get();
    	return project;
    }
    
    public List<User> getAllUsersFromProject(String projectKey) throws InterruptedException, ExecutionException{
    	Project project = this.restClient.getProjectClient().getProject(projectKey).get();
    	List<Issue> issues = getAllIssuesFromProject("GFBD");
    	
    	for (Issue issue : issues) {
			issue.getSummary();
		}
    	return null;
    }
}