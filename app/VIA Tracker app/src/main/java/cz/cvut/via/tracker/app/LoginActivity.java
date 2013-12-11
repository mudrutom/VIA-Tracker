package cz.cvut.via.tracker.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cz.cvut.via.tracker.app.dao.LoginDAO;
import cz.cvut.via.tracker.app.model.User;

public class LoginActivity extends Activity implements View.OnClickListener {

	private LoginDAO dao;

	private EditText loginEmail, loginPassword;
	private CheckBox loginRemember;
	private Button loginSubmit;

	private AsyncTask<String, Void, User> loginTask;

	public AppContext getAppContext() {
		return (AppContext) super.getApplication();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setTitle(R.string.title_login);

		final String url = getString(R.string.base_url) + getString(R.string.login_url);
		dao = new LoginDAO(url);

		loginTask = null;

		loginEmail = (EditText) findViewById(R.id.login_email);
		loginPassword = (EditText) findViewById(R.id.login_password);
		loginRemember = (CheckBox) findViewById(R.id.login_remember);
		loginSubmit = (Button) findViewById(R.id.login_submit);

		loginEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
				// action NEXT
				loginPassword.requestFocus();
				return true;
			}
		});
		loginPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
				// action GO
				onClick(textView);
				return true;
			}
		});
		loginRemember.setEnabled(true);
		loginSubmit.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// perform auto-login if enabled
		final AppContext appContext = getAppContext();
		if (appContext.getCurrentUser() == null && appContext.isRememberUser()) {
			performLogin(appContext.getUsername(), appContext.getPassword());
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (loginTask != null) {
			loginTask.cancel(true);
			loginTask = null;
		}
	}

	@Override
	public void onBackPressed() {
		// back is not allowed
	}

	@Override
	public void onClick(View view) {
		final String email = String.valueOf(loginEmail.getText());
		final String password = String.valueOf(loginPassword.getText());
		performLogin(email, password);
	}

	protected void performLogin(String email, String password) {
		loginSubmit.setEnabled(false);

		if (loginTask != null) {
			loginTask.cancel(true);
		}
		loginTask = new AsyncTask<String, Void, User>() {
			@Override
			protected User doInBackground(String... strings) {
				return dao.login(strings[0], strings[1]);
			}

			@Override
			protected void onPostExecute(User user) {
				if (user != null) {
					loginSuccess(user);
				} else {
					showLoginError();
				}
				loginSubmit.setEnabled(true);
			}
		};
		loginTask.execute(email, password);
	}

	protected void loginSuccess(User user) {
		// save current user
		final AppContext appContext = getAppContext();
		appContext.setRememberUser(loginRemember.isChecked());
		appContext.setCurrentUser(user);

		// finish activity
		finish();
	}

	protected void showLoginError() {
		Toast.makeText(this, R.string.err_login_failed, Toast.LENGTH_LONG).show();
	}
}
