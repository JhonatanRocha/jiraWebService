package com.jiraservice.utility;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.joda.time.DateTime;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.User;
import com.atlassian.jira.rest.client.api.domain.Worklog;
import com.jiraservice.model.JiraIssue;
import com.jiraservice.model.JiraProject;
import com.jiraservice.model.JiraResource;


/**
 * <P>
 * <B>Description :</B><BR>
 * Class containing the integration service with Atlassian JIRA.
 * </P>
 * 
 * @author <a href="mailto:jcristianrocha@gmail.com">Jhonatan Rocha</a>
 */
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
    
	public void closeClient() throws IOException {
		restClient.close();
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
	
	public List<JiraProject> getProjects(String cliente, DateTime initialDate, DateTime finalDate) throws InterruptedException, ExecutionException {
		List<JiraProject> projects = new ArrayList<JiraProject>();
		List<JiraResource> recursos = getAllResources();
		for (JiraProject project : getallProjects(cliente)) {
			
			String jql = "project = " + project.getKey();
			SearchRestClient client = this.restClient.getSearchClient();
			SearchResult results = client.searchJql(jql, 2000, 0, null).claim();
			List<Issue> issues = (List<Issue>) results.getIssues();
			Collections.sort(issues, new ComparatorIssue());
			
			List<JiraIssue> jiraIssues = getAtividades(results, cliente, recursos);
			project.setIssues(jiraIssues);
			projects.add(project);
						
		}
		return projects;
	}
    
    public List<JiraIssue> getAtividades(SearchResult results, String cliente,
			List<JiraResource> recursos) {
    	
    	List<JiraIssue> jiraIssues = new ArrayList<JiraIssue>();
		for (final BasicIssue result : results.getIssues()) {
			
			Issue issue = this.restClient.getIssueClient().getIssue(result.getKey()).claim();
			List<Worklog> worklogs = (List<Worklog>) issue.getWorklogs();
			Integer tempoEstimado = (issue.getTimeTracking().getOriginalEstimateMinutes() == null ? 0 : issue.getTimeTracking().getOriginalEstimateMinutes())  / 60;

			if(issue.getAssignee() == null || issue.getIssueType().getName() == null){
				//System.out.println(issue.getKey());
			}else{
				JiraIssue jiraIssue = new JiraIssue(
						issue.getKey(),
						issue.getSummary(),
						issue.getIssueType().getName(),
						issue.getCreationDate().toDate(),
						issue.getAssignee().getName(),
						tempoEstimado,
						getExecutedHourTotal(worklogs),
						issue.getStatus().getName());
				jiraIssues.add(jiraIssue);
			}
		}
		return jiraIssues;
	}

	public List<JiraProject> getAllProjetosByCliente(String filter) throws InterruptedException, ExecutionException {
		List<JiraProject> projects = new ArrayList<JiraProject>();
		List<JiraResource> recursos = getAllResources();
		for (JiraProject project : getallProjects(filter)) {
			
			String jql = "project = " + project.getKey();
			SearchRestClient client = this.restClient.getSearchClient();
			SearchResult results = client.searchJql(jql, 2000, 0, null).claim();
			List<Issue> issues = (List<Issue>) results.getIssues();
			Collections.sort(issues, new ComparatorIssue());
			
			List<JiraIssue> jiraIssues = getAtividades(results, filter, recursos);
			project.setIssues(jiraIssues);
			projects.add(project);
						
		}
		return projects;
	}

	public List<JiraResource> getAllResources() throws InterruptedException, ExecutionException {
		List<JiraResource> resources = new ArrayList<JiraResource>();
		List<User> usersFromJIRA = getAllUsersFromProject("GFBD");
		
		for (User user : usersFromJIRA) {
			resources.add(new JiraResource(user.getName(), user.getDisplayName()));
		}
		
    	return resources;
	}

	/**
     * 
     * @param filter
     * @return list of Projects from JIRA
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public List<JiraProject> getallProjects(String filter) throws InterruptedException, ExecutionException{
    	Iterable<BasicProject> allBasicProjects = this.restClient.getProjectClient().getAllProjects().get();
    	List<JiraProject> projects = new ArrayList<JiraProject>();
    	for (BasicProject basicProject : allBasicProjects) {
			if(basicProject.getName().length() >= 5) {
				if(basicProject.getName().substring(2, 5).equals(filter)) {
					Project project = getSingleProjectFromKey(basicProject.getKey());
					JiraProject jiraproject = new JiraProject();
					jiraproject.setKey(project.getKey());
					jiraproject.setProject(project.getName());
					projects.add(jiraproject);
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
    
    
    public List<User> getAllUsersFromProject(String projectKey) throws InterruptedException, ExecutionException{
    	List<Issue> issues = getAllIssuesFromProject("GFBD");
    	List<User> projectUsers = new ArrayList<User>();
    	
    	for (Issue issue : issues) {
			if(issue.getSummary().contains("Sem Demanda")){
				projectUsers.add(issue.getAssignee());
			}
		}
    	return projectUsers;
    }
    
    public Integer getExecutedHourTotal(List<Worklog> worklogs){
		Integer valor = 0;
		for (Worklog worklog : worklogs) {
			valor += worklog.getMinutesSpent();
		}
		
		return valor / 60;
	}
}