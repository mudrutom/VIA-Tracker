package cz.cvut.via.tracker.app.dao;

import android.util.Log;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractDAO {

	protected final String url;
	protected final String urlWithId;

	protected AbstractDAO(String url) {
		this.url = url;
		urlWithId = url + "/{id}";
	}

	protected boolean isGetSuccess(ResponseEntity<?> response) {
		return response.getStatusCode().value() == 200;
	}

	protected boolean isPostSuccess(ResponseEntity<?> response) {
		return response.getStatusCode().value() == 201;
	}

	protected void handleException(RestClientException e) {
		Log.e("VIA-Tracker", "RestClientException: " + e.getMessage());
	}

	protected RestTemplate getRestTemplate() {
		final RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		return restTemplate;
	}
}
