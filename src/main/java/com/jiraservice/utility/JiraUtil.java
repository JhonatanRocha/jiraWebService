package com.jiraservice.utility;

import java.net.URI;
import java.net.URISyntaxException;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

public class JiraUtil {
	
	private String JIRA_HOST = "https://emcconsulting.atlassian.net";
    private String JIRA_USERNAME = "jhonatan.rocha";
    private String JIRA_PASSWORD = "d24m02j";
	
	public JiraRestClient createClient() throws URISyntaxException {
    	
		final AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		final URI jiraServerUri = new URI(JIRA_HOST);
		final JiraRestClient restClient = factory.createWithBasicHttpAuthentication(jiraServerUri, JIRA_USERNAME, JIRA_PASSWORD);
		return restClient;
	}
}