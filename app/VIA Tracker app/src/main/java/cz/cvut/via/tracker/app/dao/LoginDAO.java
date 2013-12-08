package cz.cvut.via.tracker.app.dao;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import cz.cvut.via.tracker.app.model.User;

public class LoginDAO extends AbstractDAO {

	protected final String urlForLogin;

	public LoginDAO(String loginUrl) {
		super(loginUrl);
		urlForLogin = url + "?email={email}&pw={password}";
	}

	public User login(String email, String password) {
		ResponseEntity<User> user = null;
		try {
			user = getRestTemplate().getForEntity(urlForLogin, User.class, email, password);
		} catch (RestClientException e) {
			handleException(e);
		}
		return (user != null && isGetSuccess(user)) ? user.getBody() : null;
	}
}
