package cz.cvut.via.tracker.app;

import android.app.Application;
import android.content.SharedPreferences;

import cz.cvut.via.tracker.app.model.User;


public class AppContext extends Application {

	private static final String PRIVATE_FILE = "cz.cvut.via.tracker.app";

	private static final String REMEMBER_USER = "remember_username";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";

	private User currentUser;

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
		if (isRememberUser()) {
			setUsername(currentUser.getEmail());
			setPassword(currentUser.getPw());
		}
	}

	public boolean isRememberUser() {
		return getSharedPreferences().getBoolean(REMEMBER_USER, false);
	}

	public void setRememberUser(boolean rememberUser) {
		final SharedPreferences.Editor editor = getSharedPreferences().edit();
		editor.putBoolean(REMEMBER_USER, rememberUser);
		editor.commit();
	}

	public String getUsername() {
		return getSharedPreferences().getString(USERNAME, null);
	}

	public void setUsername(String username) {
		final SharedPreferences.Editor editor = getSharedPreferences().edit();
		editor.putString(USERNAME, username);
		editor.commit();
	}

	public String getPassword() {
		return getSharedPreferences().getString(PASSWORD, null);
	}

	public void setPassword(String password) {
		final SharedPreferences.Editor editor = getSharedPreferences().edit();
		editor.putString(PASSWORD, password);
		editor.commit();
	}

	protected SharedPreferences getSharedPreferences() {
		return getSharedPreferences(PRIVATE_FILE, MODE_PRIVATE);
	}
}
