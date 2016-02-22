package com.jiraservice.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.joda.time.DateTime;

import com.jiraservice.dao.DAO;
import com.jiraservice.model.JiraIssue;
import com.jiraservice.model.JiraProject;
import com.jiraservice.utility.Company;
import com.jiraservice.utility.JiraServices;

@ViewScoped
@ManagedBean(name = "reportBean")
public class ReportBean implements Serializable {

	private static final long serialVersionUID = -4913874444568383152L;
	private JiraServices jiraServices;
	private Date dataInicial;
	private Date dataFinal;
	private String projectKey;
	private String issueKey;
	private Company companies;
	private String selectedCompany;
	private List<JiraProject> projetos;
	private Date issueInitialDate;
	private Date issueFinalDate;

	@PostConstruct
	public void init() {
		this.jiraServices = new JiraServices();
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
	
	public String getSelectedCompany() {
		return selectedCompany;
	}

	public void setSelectedCompany(String selectedCompany) {
		this.selectedCompany = selectedCompany;
	}

	public Company[] getDefaultCompanies() {
        return Company.values();  
	}

	public void searchIssues() throws Exception {
		System.out.println("Buscando Atividades... " + new DateTime().toString());

		if (!this.issueKey.isEmpty()) {
			this.projetos = new ArrayList<>(); 
			this.projetos.add(this.jiraServices.getProjectFromIssueKey(this.issueKey));
			
			try {
				if(!this.projetos.isEmpty()) {
					new DAO(jiraServices.getAllResources()).insert(this.projetos);
				}
			} catch (Exception e) {
				e.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Algum erro ocorreu, na transação com o Banco de dados."));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Digite a chave da Atividade."));
		}
	}
	
	public void searchProjects() {
		long startTime = System.currentTimeMillis();

		try {
			if(this.projectKey.isEmpty() && !this.selectedCompany.isEmpty()) {
				if (this.dataInicial == null && this.dataFinal == null) {
					searchProjectsByClient();
				} else{
					searchProjectsBetweenDates();
				}
			} else if (this.projectKey.isEmpty() && this.selectedCompany.isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Selecione um Cliente ou Digite a Chave do Projeto"));
			} else if (!this.projectKey.isEmpty() && this.dataInicial != null && this.dataFinal != null) {
				searchProjectByKeyBetweenDates();
			} else {
				searchProjectByKey();
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Algum erro ocorreu, verifique a pesquisa realizada."));
		}
		long endTime   = System.currentTimeMillis();
		System.out.println("Execução demorou: " + (endTime - startTime)/1000 + " segundos.");
	}

	private void searchProjectByKey() throws Exception {
		this.projetos = new ArrayList<JiraProject>();
		this.projetos.add(this.jiraServices.getJiraProject(this.projectKey));
		
		if (this.projetos.size() > 0) {
			try {
				new DAO(jiraServices.getAllResources()).insert(this.projetos);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Algum erro ocorreu, na transação com o Banco de dados."));
			}
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Projeto encontrado!"));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Não foi achado nenhum projeto."));
		}
	}

	private void searchProjectsBetweenDates() throws Exception {
		if(this.dataFinal.after(this.dataInicial)) {
			this.projetos = this.jiraServices.getProjectsBetweenDates(this.selectedCompany.toUpperCase(), new DateTime(this.dataInicial),new DateTime(this.dataFinal));
			
			if (this.projetos.size() > 0) {
				new DAO(jiraServices.getAllResources()).insert(this.projetos);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Foram achados: " + this.projetos.size() + " projetos."));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Não foi achado nenhum projeto."));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("A Data inicial deve ser antes da Data final."));
		}
	}

	private void searchProjectsByClient() throws Exception {
		this.projetos = this.jiraServices.getAllProjetosByCliente(this.selectedCompany.toUpperCase());
		
		if (this.projetos.size() > 0) {
			new DAO(jiraServices.getAllResources()).insert(this.projetos);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Foram achados: " + this.projetos.size() + " projetos."));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Não foi achado nenhum projeto."));
		}
	}
	
	private void searchProjectByKeyBetweenDates() throws Exception {
		if (this.dataFinal.after(this.dataInicial)) {
			this.projetos = new ArrayList<JiraProject>();
			JiraProject project = this.jiraServices.getProjectWorklogBetweenDates(this.projectKey, new DateTime(this.dataInicial),new DateTime(this.dataFinal));
			if (project != null) {
					this.projetos.add(project);
			}
			if(this.projetos.size() > 0) {
				new DAO(jiraServices.getAllResources()).insert(this.projetos);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Foram achados: " + this.projetos.size() + " projetos e seus devidos Worklogs"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nenhum Projeto/Worklog foi encontrado."));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("A Data inicial deve ser antes da Data final."));
		}
	}
}
