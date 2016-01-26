package com.jiraservice.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.joda.time.DateTime;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.jiraservice.model.JiraIssue;
import com.jiraservice.model.JiraProject;
import com.jiraservice.utility.Company;
import com.jiraservice.utility.JiraServices;

@ViewScoped
@ManagedBean(name = "reportBean")
public class ReportBean {

	private JiraServices jiraServices;
	private TreeNode projetosTree;
	private Date dataInicial;
	private Date dataFinal;
	private Company selectedCompany;

	@PostConstruct
	public void init() {
		this.jiraServices = new JiraServices();
		this.projetosTree = new DefaultTreeNode(new JiraProject(), null);
	}
	
	public void searchProjects() {
		
		System.out.println("buscaProjetos" + new DateTime().toString());
		this.projetosTree = new DefaultTreeNode(new JiraProject(), null);
		List<JiraProject> projetos = new ArrayList<>();
		if(this.jiraServices == null){
			this.jiraServices = new JiraServices();
		}
		try {
			projetos = this.jiraServices.getProjects(this.selectedCompany.name(), new DateTime(this.dataInicial),new DateTime(this.dataFinal));
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		for (JiraProject projeto : projetos) {
			
			TreeNode projetoTree = new DefaultTreeNode(projeto, this.projetosTree);
			for (JiraIssue atividade : projeto.getIssues()) {
				TreeNode atividadeTree = new DefaultTreeNode(atividade, projetoTree);
			}
		}
		System.out.println("buscaProjetos" + new DateTime().toString());
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
	
	public TreeNode getProjetosTree() {
		return projetosTree;
	}
	
	public void setProjetosTree(TreeNode projetosTree) {
		this.projetosTree = projetosTree;
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
}
