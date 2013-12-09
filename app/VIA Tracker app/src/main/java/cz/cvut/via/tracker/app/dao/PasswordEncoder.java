package cz.cvut.via.tracker.app.dao;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoder {

	public final String algorithm;

	public PasswordEncoder(String algorithm) {
		this.algorithm = algorithm;
	}

	public String encodePassword(String password) {
		final MessageDigest digest;
		try {
			digest = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			Log.e("VIA-Tracker", "NoSuchAlgorithmException: " + e.getMessage());
			// fail-back
			return password;
		}
		digest.update(password.getBytes());
		return new String(digest.digest());
	}
}
