package com.jiraservice.utility;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Project;

public class testMain {
	
	private static JiraServices jiraAPI;

	 public static void main(String args[]) throws IOException,URISyntaxException, InterruptedException, ExecutionException {
		jiraAPI = new JiraServices();

		List<Project> getallProjects = jiraAPI.getallProjects("TIM");
		List<Issue> allIssuesFromProject = jiraAPI.getAllIssuesFromProject("GFBD");
		
		//LISTA OS PROJETOS DE ACORDO COM O FILTRO
		for (Issue issue : allIssuesFromProject) {
			System.out.println("[USUARIO] " + issue.getAssignee().getName());
			System.out.println("[SUMARIO]" + issue.getSummary());
			System.out.println("[DESCRICAO] " + issue.getDescription());
		}
		
		//LISTA OS PROJETOS DE ACORDO COM O FILTRO
		/*for (Project project : getallProjects) {
			System.out.println("[KEY]" + project.getKey());
			System.out.println("[NOME]" + project.getName());
			System.out.println("[DESCRICAO]" + project.getDescription());
		}*/
	 }
}