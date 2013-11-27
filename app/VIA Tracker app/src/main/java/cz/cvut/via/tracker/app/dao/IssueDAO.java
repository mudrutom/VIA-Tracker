package cz.cvut.via.tracker.app.dao;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import cz.cvut.via.tracker.app.model.Issue;

/**
 * Issue Database-Access-Object
 */
public class IssueDAO {

	private final String issueUrl;

	public IssueDAO(String issueUrl) {
		this.issueUrl = issueUrl;
	}

	public List<Issue> getAllIssues() {
		final ResponseEntity<? extends Issue[]> issues = getRestTemplate().getForEntity(issueUrl, Issue[].class);
		return (issues.getStatusCode().value() != 200) ? null : Arrays.asList(issues.getBody());
	}

	public Issue getIssue(Long id) {
		ResponseEntity<Issue> issue = getRestTemplate().getForEntity(issueUrl + "/{id}", Issue.class, id);
		return (issue.getStatusCode().value() != 200) ? null : issue.getBody();
	}

	public boolean saveIssue(Issue issue) {
		return (issue.getIdIssue() == null) ? createIssue(issue) : updateIssue(issue);
	}

	public boolean createIssue(Issue issue) {
		final ResponseEntity<Void> response = getRestTemplate().postForEntity(issueUrl, issue, Void.class);
		return response.getStatusCode().value() == 201;
	}

	public boolean updateIssue(Issue issue) {
		getRestTemplate().put(issueUrl + "/{id}", issue, issue.getIdIssue());
		return true;
	}

	private RestTemplate getRestTemplate() {
		final RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		return restTemplate;
	}
}
