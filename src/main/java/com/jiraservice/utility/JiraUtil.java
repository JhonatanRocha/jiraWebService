package com.jiraservice.utility;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.User;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

/**
 * <P>
 * <B>Description :</B><BR>
 * Class containing the integration service with Atlassian JIRA.
 * </P>
 * 
 * @author <a href="mailto:jcristianrocha@gmail.com">Jhonatan Rocha</a>
 */
public class JiraUtil {
	
    /**
     * This method create the connection
     * of the Web Service Client from JIRA.
     * 
     * @return JiraRestClient
     * @throws ConfigurationException 
     */
	public JiraRestClient createClient(Configuration config) throws ConfigurationException {
    	
		final AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		config = new PropertiesConfiguration("config.properties");
		JiraRestClient restClient = null;
		try {
			restClient = factory.createWithBasicHttpAuthentication(new URI(config.getString("server")), config.getString("usuario"), config.getString("senha"));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		return restClient;
	}
	
	/**
	 * This method converts Minutes to Hours.
	 * 
	 * @param String minutes
	 * @return
	 */
	public String convertMinuteHour(Integer minutes){
		
		StringBuilder time = new StringBuilder();
		String hours;
		if(minutes != null){
			Integer valueHours = minutes / 60;
			Integer valueMinutes = minutes % 60;
			hours = time.append(String.valueOf(valueHours)).append(":").append(valueMinutes < 10 && valueMinutes > 0 ? "0".concat(String.valueOf(valueMinutes)) : valueMinutes).toString();
		}else{
			hours = "0:0";
		}
		return hours;
	}
	
	/**
	 * This method converts minutes to hours.
	 * 
	 * @param Integer minutes
	 * @return Double doubleHours
	 */
	public Double convertMinuteHourDouble(Integer minutes){
		
		StringBuilder time = new StringBuilder();
		Integer hoursValue = minutes / 60;
		Integer minutesValue = minutes % 60;
		String stringHours = minutes != null ? time.append(String.valueOf(hoursValue)).append(".").append(minutesValue < 10 && minutesValue > 0 ? "0".concat(String.valueOf(minutesValue)) : minutesValue).toString() : "0";
		Double doubleHours =  new Double(stringHours); 		
		return doubleHours;
	}	
	
	/**
	 * This method formats the String date.
	 * 
	 * @param String date
	 * @return String formatedDate
	 */
	public String dateFormate(String date){
			StringBuilder dataFormatada = new StringBuilder();
			return dataFormatada.append(date.subSequence(8,10)).append("/").append(date.subSequence(5,7)).append("/").append(date.subSequence(0,4)).append(" �s ").append(date.subSequence(11,16)).toString();
	}
	
	/**
	 * This method get the
	 * current date with no slash.
	 * 
	 * @return String 
	 */
	 public String getCurrentDateWithNoSlash() {
		    GregorianCalendar gc = new GregorianCalendar();
		    gc.setTime(new java.util.Date());

		    String stringDate = "";
		    int year = gc.get(Calendar.YEAR);
		    int month = gc.get(Calendar.MONTH) + 1;
		    int day = gc.get(Calendar.DAY_OF_MONTH);

		    stringDate += year;
		    if (month < 10) {
		      stringDate += "0";
		    }
		    stringDate += month;
		    if (day < 10) {
		      stringDate += "0";
		    }
		    stringDate += day;

		    return stringDate;
	 }
	 
	 /**
	  * This method get the Current
	  * Hour by the separator.
	  * 
	  * @return String hour
	  */
	 public String getCurrentHourBySeparator() {
		    GregorianCalendar gc = new GregorianCalendar();
		    gc.setTime(new java.util.Date());

		    String hourString = "";
		    int hour = gc.get(Calendar.HOUR_OF_DAY);
		    int minute = gc.get(Calendar.MINUTE);
		    int second = gc.get(Calendar.SECOND);

		    if (hour < 10) {
		      hourString += "0";
		    }
		    hourString += hour;
		    if (minute < 10) {
		      hourString += "0";
		    }
		    hourString += minute;
		    if (second < 10) {
		      hourString += "0";
		    }
		    hourString += second;

		    return hourString;
	 }
	 
	 /**
	  * This method check if the
	  * Date is between two other dates.
	  * 
	  * @param Date initialDate
	  * @param Date finalDate
	  * @param Date dateToCompare
	  * @return boolean (true/false)
	  */
	public static boolean isDateBetween(Date initialDate,Date finalDate, Date dateToCompare) {

		GregorianCalendar initialGC = new GregorianCalendar();
		if (initialDate != null) {
			initialGC.setTime(initialDate);
			initialGC.set(GregorianCalendar.HOUR_OF_DAY, 0);
		}

		GregorianCalendar finalGC = new GregorianCalendar();
		if (finalDate != null) {
			finalGC.setTime(finalDate);
			finalGC.set(GregorianCalendar.HOUR_OF_DAY, 24);
		}

		GregorianCalendar toCompareGC = new GregorianCalendar();
		toCompareGC.setTime(dateToCompare);

		boolean cond1 = initialDate == null || toCompareGC.after(initialGC);
		boolean cond2 = finalDate == null || finalGC.after(toCompareGC);

		return cond1 && cond2;
	}
	
	public boolean isValidUser(String login, String password) throws IOException{
		
		AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		Configuration config = null;
		JiraRestClient restClient = null;
		try {
			config = new PropertiesConfiguration("config.properties");
			restClient = factory.createWithBasicHttpAuthentication(new URI(config.getString("server")), "jhonatan.rocha", "d24m02j");
			User user = restClient.getUserClient().getUser("jhonatan.rocha").get();
			restClient.close();
		} catch (Exception e1) {
			restClient.close();
			if(e1.getCause().toString().contains("401")){
				System.out.println("caiu");
				return false;
			} else {
				e1.printStackTrace();
			}
			//e1.printStackTrace();
			//e1.getCause().toString().contains("401");
		}
		return true;
	}
	
}