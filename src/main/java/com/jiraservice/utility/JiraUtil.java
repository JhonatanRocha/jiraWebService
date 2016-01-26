package com.jiraservice.utility;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

public class JiraUtil {
	
	private String JIRA_HOST = "https://emcconsulting.atlassian.net";
    private String JIRA_USERNAME = "jhonatan.rocha";
    private String JIRA_PASSWORD = "d24m02j";
	
	public JiraRestClient createClient() {
    	
		final AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		JiraRestClient restClient = null;
		try {
			restClient = factory.createWithBasicHttpAuthentication(new URI(JIRA_HOST), JIRA_USERNAME, JIRA_PASSWORD);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		return restClient;
	}
	
	public String convertMinuteHour(Integer minutos){
		
		StringBuilder tempo = new StringBuilder();
		String horas;
		if(minutos != null){
			Integer valueHoras = minutos / 60;
			Integer valueMinutos = minutos % 60;
			horas = tempo.append(String.valueOf(valueHoras)).append(":").append(valueMinutos < 10 && valueMinutos > 0 ? "0".concat(String.valueOf(valueMinutos)) : valueMinutos).toString();
		}else{
			horas = "0:0";
		}
		return horas;
	}
	
	public Double convertMinuteHourDouble(Integer minutos){
		
		StringBuilder tempo = new StringBuilder();
		Integer valueHoras = minutos / 60;
		Integer valueMinutos = minutos % 60;
		String horas = minutos != null ? tempo.append(String.valueOf(valueHoras)).append(".").append(valueMinutos < 10 && valueMinutos > 0 ? "0".concat(String.valueOf(valueMinutos)) : valueMinutos).toString() : "0";
		Double valor =  new Double(horas); 		
		return valor;
	}	
	
	public String dateFormate(String data){
			StringBuilder dataFormatada = new StringBuilder();
			return dataFormatada.append(data.subSequence(8,10)).append("/").append(data.subSequence(5,7)).append("/").append(data.subSequence(0,4)).append(" �s ").append(data.subSequence(11,16)).toString();
	}
	
	 public String getCurrentDateWithNuSlash() {
		    GregorianCalendar gc = new GregorianCalendar();
		    gc.setTime(new java.util.Date());

		    String data = "";
		    int ano = gc.get(Calendar.YEAR);
		    int mes = gc.get(Calendar.MONTH) + 1;
		    int dia = gc.get(Calendar.DAY_OF_MONTH);

		    data += ano;
		    if (mes < 10) {
		      data += "0";
		    }
		    data += mes;
		    if (dia < 10) {
		      data += "0";
		    }
		    data += dia;

		    return data;
	 }
	 
	 public String getCurrentHourBySeparator() {
		    GregorianCalendar gc = new GregorianCalendar();
		    gc.setTime(new java.util.Date());

		    String hora = "";
		    int hour = gc.get(Calendar.HOUR_OF_DAY);
		    int minute = gc.get(Calendar.MINUTE);
		    int second = gc.get(Calendar.SECOND);

		    if (hour < 10) {
		      hora += "0";
		    }
		    hora += hour;
		    if (minute < 10) {
		      hora += "0";
		    }
		    hora += minute;
		    if (second < 10) {
		      hora += "0";
		    }
		    hora += second;

		    return hora;
	 }
	 
	public static boolean isDateBetween(Date dataInicio,Date dataFim, Date dataComparar) {

		GregorianCalendar gcInicio = new GregorianCalendar();
		if (dataInicio != null) {
			gcInicio.setTime(dataInicio);
			gcInicio.set(GregorianCalendar.HOUR_OF_DAY, 0);
		}

		GregorianCalendar gcFinal = new GregorianCalendar();
		if (dataFim != null) {
			gcFinal.setTime(dataFim);
			gcFinal.set(GregorianCalendar.HOUR_OF_DAY, 24);
		}

		GregorianCalendar gcComparar = new GregorianCalendar();
		gcComparar.setTime(dataComparar);

		boolean cond1 = dataInicio == null || gcComparar.after(gcInicio);
		boolean cond2 = dataFim == null || gcFinal.after(gcComparar);

		return cond1 && cond2;
	}
	
}