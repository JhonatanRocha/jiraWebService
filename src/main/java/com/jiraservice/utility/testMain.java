package com.jiraservice.utility;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.joda.time.DateTime;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.User;
import com.jiraservice.model.JiraIssue;
import com.jiraservice.model.JiraProject;
import com.jiraservice.model.JiraResource;

public class testMain {
	
	private static JiraServices jiraAPI;

	 public static void main(String args[]) throws IOException,URISyntaxException, InterruptedException, ExecutionException {
		jiraAPI = new JiraServices();

		//List<Project> getallProjects = jiraAPI.getallProjects("TIM");
		//List<Issue> allIssuesFromProject = jiraAPI.getAllIssuesFromProject("GFBD");
		//List<User> allUsersFromProject = jiraAPI.getAllUsersFromProject("GFBD");
		
		//LISTA OS PROJETOS DE ACORDO COM O FILTRO
		/*for (Project project : getallProjects) {
			System.out.println("[KEY]" + project.getKey());
			System.out.println("[NOME]" + project.getName());
			System.out.println("[DESCRICAO]" + project.getDescription());
		}*/
		
		//LISTA OS PROJETOS DE ACORDO COM O FILTRO
		/*for (Issue issue : allIssuesFromProject) {
			System.out.println("[USUARIO] " + issue.getAssignee().getName());
			System.out.println("[SUMARIO]" + issue.getSummary());
			System.out.println("[DESCRICAO] " + issue.getDescription());
		}*/
		
		//LISTA TODOS OS USUARIOS DE UM PROJETO
		/*int i = 1;
		for (User user : allUsersFromProject) {
			System.out.println("[INDEX] " + i);
			System.out.println("[NAME] " + user.getName());
			System.out.println("[DISPLAY NAME] " + user.getDisplayName());
			System.out.println("[EMAIL] " + user.getEmailAddress());
			i++;
		}*/
	/*	
		for (Project project : getallProjects) {
			System.out.println(project.getId());
			System.out.println(project.getKey());
			
			for (Issue issue : jiraAPI.getAllIssuesFromProject(project.getKey())) {
				System.out.println(issue.getKey());
				System.out.println(issue.getSummary());
				System.out.println(issue.getIssueType().getName());
				System.out.println(issue.getCreationDate().toString());
				System.out.println(issue.getResolution().getName());
				System.out.println(issue.getUpdateDate().toString());
				System.out.println(issue.getAssignee().getName());
				System.out.println(issue.getStatus().getName());
				System.out.println(issue.getTimeTracking().getOriginalEstimateMinutes() == null ? 0 : issue.getTimeTracking().getOriginalEstimateMinutes());
				System.out.println(issue.getTimeTracking().getTimeSpentMinutes() == null ? 0 : issue.getTimeTracking().getTimeSpentMinutes());
				System.out.println(issue.getTimeTracking().getRemainingEstimateMinutes() == null ? 0 : issue.getTimeTracking().getTimeSpentMinutes());
				//workRatio
				//sprint
				//Creator
				//progress
				//System.out.println(issue.getDueDate().toString());
				//IssueField field = issue.getField("creator");
				System.out.println("=====================================");
				
			}
		}*/
		
		System.out.println("INICIANDO PROCESSAMENTO DE TODOS OS PROJETOS E SEUS ATIVIDADES E RECURSO");
		long startTime = System.currentTimeMillis();
		/*List<JiraProject> resours = jiraAPI.getAllProjetosByCliente("TIM");
		int i = 0;
		for (JiraProject jiraProject : resours) {
			System.out.println(jiraProject.getKey());
			System.out.println(jiraProject.getProject());
			System.out.println("=== LISTANDO ATIVIDADES ===");
			for (JiraIssue jiraIssue : jiraProject.getIssues()) {
				System.out.println("[ID] " + jiraIssue.getId());
				System.out.println("[SUMMARY] " + jiraIssue.getSummary());
				System.out.println("[ASSIGNED] " + jiraIssue.getAssigned());
				System.out.println("[TYPE] " + jiraIssue.getIssueType());
				System.out.println("[CREATOR] " + jiraIssue.getCreator());
				System.out.println("[KEY] " + jiraIssue.getKey());
				System.out.println("[STATUS] " + jiraIssue.getStatus());
				System.out.println("[ORIGINAL ESTIMATE] " + jiraIssue.getOriginalEstimate());
				System.out.println("[TIME SPENT] " + jiraIssue.getTimeSpent());
			}
		}*/
		
		//TODO: IMPLEMENTAÇÃO COM BUSCA COM INTERVALO DE DATAS!
		List<JiraProject> resours = jiraAPI.getProjectsBetweenDates("TIM", new DateTime(2015, 12, 1, 0, 0, 0, 0), new DateTime(2016, 1, 22, 0, 0, 0, 0));
		int i = 0;
		for (JiraProject jiraProject : resours) {
			System.out.println(jiraProject.getKey());
			System.out.println(jiraProject.getProject());
			System.out.println("=== LISTANDO ATIVIDADES ===");
			for (JiraIssue jiraIssue : jiraProject.getAtividades()) {
				System.out.println("[ID] " + jiraIssue.getId());
				System.out.println("[KEY] " + jiraIssue.getKey());
				System.out.println("[TYPE] " + jiraIssue.getIssueType());
				System.out.println("[SUMMARY] " + jiraIssue.getSummary());
				System.out.println("[STATUS] " + jiraIssue.getStatus());
				System.out.println("[ASSIGNED] " + jiraIssue.getAssigned());
				System.out.println("[ORIGINAL ESTIMATE] " + jiraIssue.getOriginalEstimate());
				System.out.println("[TIME SPENT] " + jiraIssue.getTimeSpent());
				System.out.println("[REMAINING TIME] " + jiraIssue.getRemainingEstimate());
				System.out.println("[CREATOR] " + jiraIssue.getCreator());
				System.out.println("[WORKRATIO] " + jiraIssue.getWorkRatio());
			}
		}
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("PROCESSAMENTO TERMINADO NO TEMPO DE " + totalTime);
		jiraAPI.closeClient();
	 }
}