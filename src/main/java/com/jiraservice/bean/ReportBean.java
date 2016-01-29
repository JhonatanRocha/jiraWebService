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
	private String projectKey;
	private String issueKey;
	private Company selectedCompany;
	private List<JiraProject> projetos;
	private List<JiraIssue> atividades;
	private Date issueInitialDate;
	private Date issueFinalDate;

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
		System.out.println("Buscando Projetos... " + new DateTime().toString());

		try {
			if(this.projectKey.isEmpty() && !this.selectedCompany.name().isEmpty()) {
				if(this.dataInicial == null && this.dataFinal == null) {
					this.projetos = this.jiraServices.getAllProjetosByCliente(this.selectedCompany.name());
				}else{
					this.projetos = this.jiraServices.getProjectsBetweenDates(this.selectedCompany.name(), new DateTime(this.dataInicial),new DateTime(this.dataFinal));
				}
			}else if(this.projectKey.isEmpty() && this.selectedCompany.name().isEmpty()) {
				//TODO: Solu��o para filtrar projetos atrav�s de texto.
			}else{
				this.projetos.add(this.jiraServices.getJiraProject(this.projectKey));
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	public void searchIssues() {
		System.out.println("Buscando Atividades... " + new DateTime().toString());

		if(this.issueKey.isEmpty()) {
			/*if(this.issueInitialDate == null && this.issueFinalDate == null) {
				//this.atividades = this.jiraServices.;	
			} else {
				//this.atividades = this.jiraServices.getIssuesBetweenDates(this.selectedCompany.name(), new DateTime(this.dataInicial),new DateTime(this.dataFinal));
			}*/
		} else {
			this.atividades = new ArrayList<JiraIssue>();
			this.atividades.add(this.jiraServices.getJiraIssue(this.issueKey));
		}
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

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

	public String getIssueKey() {
		return issueKey;
	}

	public void setIssueKey(String issueKey) {
		this.issueKey = issueKey;
	}

	public List<JiraIssue> getAtividades() {
		return atividades;
	}

	public void setAtividades(List<JiraIssue> atividades) {
		this.atividades = atividades;
	}

	public Date getIssueInitialDate() {
		return issueInitialDate;
	}

	public void setIssueInitialDate(Date issueInitialDate) {
		this.issueInitialDate = issueInitialDate;
	}

	public Date getIssueFinalDate() {
		return issueFinalDate;
	}

	public void setIssueFinalDate(Date issueFinalDate) {
		this.issueFinalDate = issueFinalDate;
	}
}
