package cz.cvut.via.tracker.app.dao;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.List;

import cz.cvut.via.tracker.app.model.Comment;

/**
 * Comment Database-Access-Object
 */
public class CommentDAO extends AbstractDAO {

	public CommentDAO(String commentUrl) {
		super(commentUrl);
	}

	public List<Comment> getAllCommentsForIssue(Long issueId) {
		ResponseEntity<? extends Comment[]> comments = null;
		try {
			comments = getRestTemplate().getForEntity(url, Comment[].class, issueId);
		} catch (RestClientException e) {
			handleException(e);
		}
		return (comments != null && isGetSuccess(comments)) ? Arrays.asList(comments.getBody()) : null;
	}

	public boolean createCommentForIssue(Long issueId, Comment comment) {
		ResponseEntity<Void> response = null;
		try {
			response = getRestTemplate().postForEntity(url, comment, Void.class, issueId);
		} catch (RestClientException e) {
			handleException(e);
		}
		return response != null && isPostSuccess(response);
	}
}
