package cz.cvut.via.tracker.app.dao;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import cz.cvut.via.tracker.app.model.User;

/**
 * User Database-Access-Object
 */
public class UserDAO {

	private final String userUrl;

	public UserDAO(String userUrl) {
		this.userUrl = userUrl;
	}

	public List<User> getAllUsers() {
		final ResponseEntity<? extends User[]> users = getRestTemplate().getForEntity(userUrl, User[].class);
		return (users.getStatusCode().value() != 200) ? null : Arrays.asList(users.getBody());
	}

	public User getUser(Long id) {
		ResponseEntity<User> user = getRestTemplate().getForEntity(userUrl + "/{id}", User.class, id);
		return (user.getStatusCode().value() != 200) ? null : user.getBody();
	}

	public boolean saveUser(User user) {
		return (user.getIdUser() == null) ? createUser(user) : updateUser(user);
	}

	public boolean createUser(User user) {
		final ResponseEntity<Void> response = getRestTemplate().postForEntity(userUrl, user, Void.class);
		return response.getStatusCode().value() == 201;
	}

	public boolean updateUser(User user) {
		getRestTemplate().put(userUrl + "/{id}", user, user.getIdUser());
		return true;
	}

	private RestTemplate getRestTemplate() {
		final RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		return restTemplate;
	}
}
