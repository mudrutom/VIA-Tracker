package cz.cvut.via.tracker.app.dao;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.List;

import cz.cvut.via.tracker.app.model.Issue;

/**
 * Issue Database-Access-Object
 */
public class IssueDAO extends AbstractDAO {

	public IssueDAO(String issueUrl) {
		super(issueUrl);
	}

	public List<Issue> getAllIssues() {
		ResponseEntity<? extends Issue[]> issues = null;
		try {
			issues = getRestTemplate().getForEntity(url, Issue[].class);
		} catch (RestClientException e) {
			handleException(e);
		}
		return (issues != null && isGetSuccess(issues)) ? Arrays.asList(issues.getBody()) : null;
	}

	public Issue getIssue(Long id) {
		ResponseEntity<Issue> issue = null;
		try {
			issue = getRestTemplate().getForEntity(urlWithId, Issue.class, id);
		} catch (RestClientException e) {
			handleException(e);
		}
		return (issue != null && isGetSuccess(issue)) ? issue.getBody() : null;
	}

	public boolean saveIssue(Issue issue) {
		return (issue.getIdIssue() == null) ? createIssue(issue) : updateIssue(issue);
	}

	public boolean createIssue(Issue issue) {
		ResponseEntity<Void> response = null;
		try {
			response = getRestTemplate().postForEntity(url, issue, Void.class);
		} catch (RestClientException e) {
			handleException(e);
		}
		return response != null && isPostSuccess(response);
	}

	public boolean updateIssue(Issue issue) {
		try {
			getRestTemplate().put(urlWithId, issue, issue.getIdIssue());
		} catch (RestClientException e) {
			handleException(e);
			return false;
		}
		return true;
	}
}
