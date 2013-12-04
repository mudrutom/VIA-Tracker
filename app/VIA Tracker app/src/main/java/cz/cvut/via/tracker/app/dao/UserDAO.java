package cz.cvut.via.tracker.app.dao;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.List;

import cz.cvut.via.tracker.app.model.User;

/**
 * User Database-Access-Object
 */
public class UserDAO extends AbstractDAO {

	public UserDAO(String userUrl) {
		super(userUrl);
	}

	public List<User> getAllUsers() {
		ResponseEntity<? extends User[]> users = null;
		try {
			users = getRestTemplate().getForEntity(url, User[].class);
		} catch (RestClientException e) {
			handleException(e);
		}
		return (users != null && isGetSuccess(users)) ? Arrays.asList(users.getBody()) : null;
	}

	public User getUser(Long id) {
		ResponseEntity<User> user = null;
		try {
			user = getRestTemplate().getForEntity(urlWithId, User.class, id);
		} catch (RestClientException e) {
			handleException(e);
		}
		return (user != null && isGetSuccess(user)) ? user.getBody() : null;
	}

	public boolean saveUser(User user) {
		return (user.getIdUser() == null) ? createUser(user) : updateUser(user);
	}

	public boolean createUser(User user) {
		ResponseEntity<Void> response = null;
		try {
			response = getRestTemplate().postForEntity(url, user, Void.class);
		} catch (RestClientException e) {
			handleException(e);
		}
		return response != null && isPostSuccess(response);
	}

	public boolean updateUser(User user) {
		try {
			getRestTemplate().put(urlWithId, user, user.getIdUser());
		} catch (RestClientException e) {
			handleException(e);
			return false;
		}
		return true;
	}
}
