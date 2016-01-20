package com.jiraservice.utility;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.atlassian.jira.rest.client.api.domain.Project;

public class testMain {
	
	private static JiraServices jiraAPI;

	 public static void main(String args[]) throws IOException,URISyntaxException, InterruptedException, ExecutionException {
		jiraAPI = new JiraServices();
		List<Project> getallProjects = jiraAPI.getallProjects("TIM");
		
		for (Project project : getallProjects) {
			System.out.println("[KEY]" + project.getKey());
			System.out.println("[NOME]" + project.getName());
			System.out.println("[DESCRICAO]" + project.getDescription());
		}
	 }
}