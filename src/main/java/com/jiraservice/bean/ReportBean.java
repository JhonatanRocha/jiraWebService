package com.jiraservice.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.joda.time.DateTime;

import com.jiraservice.dao.DAO;
import com.jiraservice.dao.JiraProjectDAO;
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
	//private List<BarChartModel> animatedBarChart;

	/**
	 * This method is executed every time
	 * the page its refreshed.
	 */
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
	
	public Company[] getCompanies(){
        return Company.values();  
	}
	
	/*public List<BarChartModel> getAnimatedBarChart() {
		return animatedBarChart;
	}

	public void setAnimatedBarChart(List<BarChartModel> animatedBarChart) {
		this.animatedBarChart = animatedBarChart;
	}
	
	private void createAnimatedModels() {
         
        animatedModel2 = initBarModel();
        animatedModel2.setTitle("Bar Charts");
        animatedModel2.setAnimate(true);
        animatedModel2.setLegendPosition("ne");
        yAxis = animatedModel2.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(200);
    }

	private BarChartModel initBarModel() {
		
		return null;
	}*/

	public void searchIssues() {
		System.out.println("Buscando Atividades... " + new DateTime().toString());

		if(!this.issueKey.isEmpty()) {
			this.atividades = new ArrayList<JiraIssue>();
			this.atividades.add(this.jiraServices.getJiraIssue(this.issueKey));
			try {
				new DAO(this.jiraServices.getAllResources()).insert(this.projetos);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Algum erro ocorreu, na transação com o Banco de dados."));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Digite a chave da Atividade."));
		}
	}
	
	/**
	 * This method search the
	 * projects from JIRA
	 */
	public void searchProjects() {
		long startTime = System.currentTimeMillis();

		try {
			if(this.projectKey.isEmpty() && !this.selectedCompany.name().equals("NENHUM")) {
				if(this.dataInicial == null && this.dataFinal == null) {
					searchProjectsByClient();
				}else{
					searchProjectsBetweenDates();
				}
			}else if(this.projectKey.isEmpty() && this.selectedCompany.name().equals("NENHUM")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Selecione um Cliente ou Digite a Chave do Projeto"));
			}else{
				searchProjectByKey();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Algum erro ocorreu, verifique a pesquisa realizada."));
		}
		long endTime   = System.currentTimeMillis();
		System.out.println("Execução demorou: " + (endTime - startTime)/1000 + " segundos.");
	}

	private void searchProjectByKey() {
		this.projetos = new ArrayList<JiraProject>();
		this.projetos.add(this.jiraServices.getJiraProject(this.projectKey));
		
		if(this.projetos.size() > 0){
			try {
				new DAO(this.jiraServices.getAllResources()).insert(this.projetos);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Algum erro ocorreu, na transação com o Banco de dados."));
			}
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Projeto encontrado!"));
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Não foi achado nenhum projeto."));
		}
	}

	private void searchProjectsBetweenDates() throws InterruptedException,
			ExecutionException {
		if(this.dataFinal.after(this.dataInicial)){
			this.projetos = this.jiraServices.getProjectsBetweenDates(this.selectedCompany.name(), new DateTime(this.dataInicial),new DateTime(this.dataFinal));
			
			if(this.projetos.size() > 0){
				new DAO(this.jiraServices.getAllResources()).insert(this.projetos);
				/*JiraProjectDAO projectDAO = new JiraProjectDAO();
				JiraIssueDAO jiraIssueDAO = new JiraIssueDAO();
				JiraTimesheetDAO jiraTimesheetDAO = new JiraTimesheetDAO();

				for (JiraProject jiraProject : this.projetos) {
					for (JiraIssue jiraIssue : jiraProject.getAtividades()) {
						for(JiraTimesheet jiraTimesheet : jiraIssue.getTimesheets()){
							jiraTimesheetDAO.insert(jiraTimesheet);
						}
						jiraIssueDAO.insert(jiraIssue);
					}
					projectDAO.insert(jiraProject);
				}*/
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Foram achados: " + this.projetos.size() + " projetos."));
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Não foi achado nenhum projeto."));
			}
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("A Data inicial deve ser antes da Data final."));
		}
	}

	private void searchProjectsByClient() throws InterruptedException,
			ExecutionException {
		this.projetos = this.jiraServices.getAllProjetosByCliente(this.selectedCompany.name());
		
		if(this.projetos.size() > 0){
			new DAO(this.jiraServices.getAllResources()).insert(this.projetos);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Foram achados: " + this.projetos.size() + " projetos."));
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Não foi achado nenhum projeto."));
		}
	}
}
