package com.jiraservice.utility;

import java.util.Comparator;

import com.atlassian.jira.rest.client.api.domain.Issue;

public class ComparatorIssue implements Comparator<Issue> {
	public int compare(Issue a, Issue b) {
        return a.getId().compareTo(b.getId());
    }
}
