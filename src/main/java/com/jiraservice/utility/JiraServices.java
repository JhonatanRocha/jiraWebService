package com.jiraservice.utility;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Project;

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
     * @return list of Project from JIRA
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
					Project project = this.restClient.getProjectClient().getProject(projectKey).get();
					projects.add(project);
				}
			}
		}
    	return projects;
    }
    
   public List<Issue> getAllIssues(){
	   
	   return null;   
   }
}