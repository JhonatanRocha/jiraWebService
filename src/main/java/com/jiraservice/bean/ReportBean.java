package com.jiraservice.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.joda.time.DateTime;

import com.jiraservice.dao.DAO;
import com.jiraservice.model.JiraProject;
import com.jiraservice.utility.Company;
import com.jiraservice.utility.JiraServices;
import com.jiraservice.utility.SearchBuilder;

/**
 * <P>
 * <B>Description :</B><BR>
 * Bean from the system containing all the data from the project, issue and worklog from Atlassian JIRA.
 * </P>
 * 
 * @author <a href="mailto:jcristianrocha@gmail.com">Jhonatan Rocha</a>
 */

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
	private String selectedResource;
	
	@ManagedProperty("#{login}")
	private LoginBean login;

	@PostConstruct
	public void init() {
		this.jiraServices = new JiraServices(login.getUser(), login.getPwd());
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

	public LoginBean getLogin() {
		return login;
	}

	public void setLogin(LoginBean login) {
		this.login = login;
	}

	public String getSelectedResource() {
		return selectedResource;
	}

	public void setSelectedResource(String selectedResource) {
		this.selectedResource = selectedResource;
	}
	
	/**
	 * The Action Button from the jira.xhtml form will call this method.
	 */
	public void searchProjects() {
		long startTime = System.currentTimeMillis();

		try {
			
			if(this.dataInicial != null && this.dataFinal != null &&  !this.dataFinal.after(this.dataInicial))
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("A Data inicial deve ser antes da Data final."));
			
			String jql = new SearchBuilder(this.projectKey, 
											this.issueKey, 
											this.selectedResource, 
											this.dataInicial, 
											this.dataFinal).build();
			
			if(!jql.isEmpty()) {	
				this.projetos = this.jiraServices.searchProjectUsingJQL(jql ,
						!this.selectedCompany.isEmpty() ? this.selectedCompany.toUpperCase() : "BR",
								(this.dataInicial != null ? new DateTime(this.dataInicial) : null), 
								(this.dataFinal != null ? new DateTime(this.dataFinal) : null),
								this.selectedResource);
			} else
				this.projetos = this.jiraServices.getProjectsByClientPrefix(!this.selectedCompany.isEmpty() ? this.selectedCompany.toUpperCase() : "BR");
			
			if (this.projetos.size() > 0) {
				try {
					new DAO(jiraServices.getAllResources()).insert(this.projetos);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Pesquisa Realizada com Sucesso!"));
				} catch (Exception e) {
					e.printStackTrace();
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
															"Erro na transação com o Banco de dados.", 
															"Verifique se o banco de dados se encontra de pé ou a consistencia dos dados de pesquisa.");
					FacesContext.getCurrentInstance().addMessage(null, message);
				}
			} else { 
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, 
													"Nenhum projeto foi encontrado.", 
													"A pesquisa não retornou nenhum resultado.");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
													"Erro! Verifique se os dados da pesquisa estão corretos.", 
													"Verifique se os dados nos campos de pesquisa estão corretos.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		long endTime   = System.currentTimeMillis();
		System.out.println("Execução demorou: " + (endTime - startTime)/1000 + " segundos.");
	}
}
