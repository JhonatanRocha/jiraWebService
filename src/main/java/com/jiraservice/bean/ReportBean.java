package com.jiraservice.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.joda.time.DateTime;

import com.jiraservice.model.JiraIssue;
import com.jiraservice.model.JiraProject;
import com.jiraservice.utility.Company;
import com.jiraservice.utility.JiraServices;

@ViewScoped
@ManagedBean(name = "reportBean")
public class ReportBean {

	private JiraServices jiraServices;
	private Date dataInicial;
	private Date dataFinal;
	private String projectOrIssueKey;
	private String projectOrIssueSelection;
	private Company selectedCompany;
	private List<JiraProject> projetos;

	/**
	 * This method is executed every time
	 * the page its refreshed.
	 */
	@PostConstruct
	public void init() {
		this.jiraServices = new JiraServices();
	}
	
	/**
	 * This method search the
	 * projects from JIRA
	 */
	public void searchProjects() {
		System.out.println("Buscando... " + new DateTime().toString());

		if(this.jiraServices == null){
			this.jiraServices = new JiraServices();
		}
		try {
			if(this.dataInicial == null && this.dataFinal == null && 
				this.projectOrIssueKey.equals("") && this.projectOrIssueSelection.equals("")) {
				
					this.projetos = this.jiraServices.getAllProjetosByCliente(this.selectedCompany.name());					
			} else if(this.projectOrIssueKey.equals("") && projectOrIssueSelection != null){
				//this.projetos = this.jiraServices.getPro);
			} else {
				this.projetos = this.jiraServices.getProjectsBetweenDates(this.selectedCompany.name(), new DateTime(this.dataInicial),new DateTime(this.dataFinal));
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
	/*	for (JiraProject projeto : this.projetos) {
			
			TreeNode projetoTree = new DefaultTreeNode(projeto, this.projetosTree);
			for (JiraIssue atividade : projeto.getIssues()) {
				TreeNode atividadeTree = new DefaultTreeNode(atividade, projetoTree);
			}
		}
		System.out.println("buscaProjetos" + new DateTime().toString());*/
	}
	
	public Company[] getCompanies(){
        return Company.values();  
	}
	
	public JiraServices getJiraServices() {
		return jiraServices;
	}
	
	public void setJiraServices(JiraServices jiraServices) {
		this.jiraServices = jiraServices;
	}
	
	public Date getDataInicial() {
		return dataInicial;
	}
	
	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}
	
	public Date getDataFinal() {
		return dataFinal;
	}
	
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	public Company getSelectedCompany() {
		return selectedCompany;
	}
	
	public void setSelectedCompany(Company selectedCompany) {
		this.selectedCompany = selectedCompany;
	}

	public List<JiraProject> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<JiraProject> projetos) {
		this.projetos = projetos;
	}

	public String getProjectOrIssueKey() {
		return projectOrIssueKey;
	}

	public void setProjectOrIssueKey(String projectOrIssueKey) {
		this.projectOrIssueKey = projectOrIssueKey;
	}

	public String getProjectOrIssueSelection() {
		return projectOrIssueSelection;
	}

	public void setProjectOrIssueSelection(String projectOrIssueSelection) {
		this.projectOrIssueSelection = projectOrIssueSelection;
	}
}
