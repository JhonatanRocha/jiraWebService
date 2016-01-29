package com.jiraservice.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
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
import com.jiraservice.model.JiraTimesheet;


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
	private List<JiraResource> recursos;
	private Iterable<BasicProject> allBasicProjects;
    
	public JiraServices(){
		
		this.jiraUtil = new JiraUtil();
		this.restClient = jiraUtil.createClient();
		try {
			this.recursos = getAllResources();
			this.allBasicProjects = this.restClient.getProjectClient().getAllProjects().get();
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
    /**
     * This method close
     * the Web Service Client
     * Connection.
     * 
     * @throws IOException
     */
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
	
	/**
	 * This method returns
     * all the Projects from
     * specific client, using 
     * date interval.
     * (the client is a 
     * prefix name of the
     * project name)
	 * 
	 * @param cliente
	 * @param initialDate
	 * @param finalDate
	 * @return List of JiraProject
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public List<JiraProject> getProjectsBetweenDates(String cliente, DateTime initialDate, DateTime finalDate) throws InterruptedException, ExecutionException {
		List<JiraProject> projects = new ArrayList<JiraProject>();
		for (JiraProject project : getallProjects(cliente)) {
			
			String jql = "project = " + project.getKey();
			SearchRestClient client = this.restClient.getSearchClient();
			SearchResult results = client.searchJql(jql, 2000, 0, null).claim();
			List<Issue> issues = (List<Issue>) results.getIssues();
			Collections.sort(issues, new ComparatorIssue());
			if (validateProjectDate(issues.get(0).getCreationDate(), initialDate, finalDate)) {
				List<JiraIssue> jiraIssues = getAtividades(results, cliente, this.recursos);
				project.setIssues(jiraIssues);
				projects.add(project);
			}	
		}
		return projects;
	}
	
    /**
     * This method get all
     * the Issues from Jira.
     * 
     * @param SearchResult results
     * @param String client
     * @param List<JiraResource> resources
     * @return List of JiraIssue
     */
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
				String creator = "";
				int remainingTime = (issue.getTimeTracking().getRemainingEstimateMinutes() == null ? 0 : issue.getTimeTracking().getRemainingEstimateMinutes()) / 60;
				//IssueField sprintField = issue.getFieldByName("Sprint");
				
				Object workrate = issue.getField("workratio").getValue();
				JSONObject jsonIssueCreator = (JSONObject) issue.getField("creator").getValue();
				try {
					creator = jsonIssueCreator.get("name").toString();
				} catch (JSONException e) {
					throw new RuntimeException(e);
				}
				/*int i = 0;
				for (IssueField issueField : fields) {
					Object value = issueField.getValue();
					System.out.println("campo: " + i);
					i++;
				}*/
				JiraIssue jiraIssue = new JiraIssue(
						issue.getKey(),
						issue.getSummary(),
						issue.getIssueType().getName(),
						issue.getCreationDate().toDate(),
						issue.getAssignee().getName(),
						tempoEstimado,
						getExecutedHourTotal(worklogs),
						remainingTime,
						issue.getStatus().getName(), 
						Long.valueOf(workrate.toString()).longValue(),
						creator);
				jiraIssues.add(jiraIssue);
			}
		}
		return jiraIssues;
	}

    /**
     * This method returns
     * all the Projects from
     * specific client
     * (the client is a 
     * prefix name of the
     * project name)
     * 
     * @param String client
     * @return List of JiraProject
     * @throws InterruptedException
     * @throws ExecutionException
     */
	public List<JiraProject> getAllProjetosByCliente(String client) throws InterruptedException, ExecutionException {
		List<JiraProject> projects = new ArrayList<JiraProject>();
		for (JiraProject project : getallProjects(client)) {
			
			String jql = "project = " + project.getKey();
			SearchRestClient searchRestClient = this.restClient.getSearchClient();
			SearchResult results = searchRestClient.searchJql(jql, 2000, 0, null).claim();
			List<Issue> issues = (List<Issue>) results.getIssues();
			Collections.sort(issues, new ComparatorIssue());
			
			List<JiraIssue> jiraIssues = getAtividades(results, client, this.recursos);
			project.setIssues(jiraIssues);
			projects.add(project);
						
		}
		return projects;
	}

	/**
	 * This method returns all the User
	 * from Jira(in this specific case
	 * we insert all the user on this
	 * Project)
	 * 
	 * @return List of JiraResource
	 * @throws InterruptedException
	 * @throws ExecutionException
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
	 * This method gets all
	 * projects from JIRA
	 * using the filter
	 * (All projects name has a
	 * prefix in their name,
	 * this prefix refers a client)
	 * 
	 * @param String client
	 * @return List of JiraProject;
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
    public List<JiraProject> getallProjects(String filter) throws InterruptedException, ExecutionException{
    	List<JiraProject> projects = new ArrayList<JiraProject>();
    	for (BasicProject basicProject : this.allBasicProjects) {
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
    * This method returns all Issues
    * from a single Project.
    *  
    * @param projectKey
    * @return List of Issue from JIRA
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
	 * This method returns all the User
	 * from Jira(in this specific case
	 * we insert all the user on this
	 * Project)
	 * 
	 * @param projectKey
	 * @return List of Users from JIRA
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
    public List<User> getAllUsers(String projectKey) throws InterruptedException, ExecutionException{
    	List<Issue> issues = getAllIssuesFromProject(projectKey);
    	List<User> projectUsers = new ArrayList<User>();
    	
    	for (Issue issue : issues) {
			if(issue.getSummary().contains("Sem Demanda")){
				projectUsers.add(issue.getAssignee());
			}
		}
    	return projectUsers;
    }
    
    /**
     * This method return the total
     * time spent from Issue
     * 
     * @param List of Worklog from JIRA
     * @return total time spent
     */
    public Integer getExecutedHourTotal(List<Worklog> worklogs){
		Integer total = 0;
		for (Worklog worklog : worklogs) {
			total += worklog.getMinutesSpent();
		}
		
		return total / 60;
	}
    
    /**
     * This method validate the DateTime
     * from Project.
     * 
     * @param DateTime from Project
     * @param DateTime initialDate
     * @param DateTime finalDate
     * @return boolean value (true/false)
     */
    public boolean validateProjectDate(DateTime projectDate, DateTime initialDate, DateTime finalDate){
		
		return (projectDate.isAfter(initialDate) && projectDate.isBefore(finalDate))
				|| projectDate.equals(initialDate)
				|| projectDate.equals(finalDate);
	}

	public JiraProject getJiraProject(String projectKey) {
		
		return null;
	}
	
	public List<JiraProject> getJiraProjectBetweenDates(
			String projectKey, DateTime initialDate, DateTime finalDate) {
		
		return null;
	}
	
	public JiraIssue getJiraIssue(String issueKey) {
		JiraIssue jiraIssue = null;
		Issue issue = this.restClient.getIssueClient().getIssue(issueKey).claim();
		
		List<Worklog> worklogs = (List<Worklog>) issue.getWorklogs();
		Integer tempoEstimado = (issue.getTimeTracking().getOriginalEstimateMinutes() == null ? 0 : issue.getTimeTracking().getOriginalEstimateMinutes())  / 60;

		if(issue.getAssignee() == null || issue.getIssueType().getName() == null){
			//System.out.println(issue.getKey());
		}else{
			String creator = "";
			int remainingTime = (issue.getTimeTracking().getRemainingEstimateMinutes() == null ? 0 : issue.getTimeTracking().getRemainingEstimateMinutes()) / 60;
			//IssueField sprintField = issue.getFieldByName("Sprint");
			
			Object workrate = issue.getField("workratio").getValue();
			JSONObject jsonIssueCreator = (JSONObject) issue.getField("creator").getValue();
			try {
				creator = jsonIssueCreator.get("name").toString();
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			/*int i = 0;
			for (IssueField issueField : fields) {
				Object value = issueField.getValue();
				System.out.println("campo: " + i);
				i++;
			}*/
			List<JiraTimesheet> worklogList = new ArrayList<JiraTimesheet>();
			
			jiraIssue = new JiraIssue(
					issue.getKey(),
					issue.getSummary(),
					issue.getIssueType().getName(),
					issue.getCreationDate().toDate(),
					issue.getAssignee().getName(),
					tempoEstimado,
					getExecutedHourTotal(worklogs),
					remainingTime,
					issue.getStatus().getName(), 
					Long.valueOf(workrate.toString()).longValue(),
					creator);
			
			for (Worklog worklog : worklogs) {

				worklogList.add(new JiraTimesheet(issue.getKey(), 
													issue.getSummary(), 
													worklog.getUpdateDate().toDate(), 
													worklog.getUpdateAuthor().getDisplayName(),
													worklog.getMinutesSpent(),
													worklog.getComment()));
			}
		}
		
		return jiraIssue;
	}

	public List<JiraIssue> getJiraIssueBetweenDates(String issueKey,
			DateTime dateTime, DateTime dateTime2) {

		return null;
	}
}