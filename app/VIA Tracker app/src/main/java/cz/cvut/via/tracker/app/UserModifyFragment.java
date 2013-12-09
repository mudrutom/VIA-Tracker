package cz.cvut.via.tracker.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cz.cvut.via.tracker.app.dao.UserDAO;
import cz.cvut.via.tracker.app.model.User;

public class UserModifyFragment extends Fragment implements View.OnClickListener {

	public static final String ARG_USER_ID = "idUser";

	private UserDAO dao;

	private Long id = null;

	private User user = null;

	private TextView userFirstName, userLastName, userEmail, userPw1, userPw2;
	private Button userSave;

	private AsyncTask<Long, Void, User> loadTask;
	private AsyncTask<User, Void, Boolean> saveTask;

	public UserModifyFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle arguments = getArguments();
		id = (arguments != null && arguments.containsKey(ARG_USER_ID)) ? arguments.getLong(ARG_USER_ID) : null;

		final String url = getString(R.string.base_url) + getString(R.string.user_url);
		dao = new UserDAO(url);

		loadTask = null;
		saveTask = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_user_modify, container, false);

		if (rootView != null) {
			userFirstName = (TextView) rootView.findViewById(R.id.user_firstName);
			userLastName = (TextView) rootView.findViewById(R.id.user_lastName);
			userEmail = (TextView) rootView.findViewById(R.id.user_email);
			userPw1 = (TextView) rootView.findViewById(R.id.user_pw1);
			userPw2 = (TextView) rootView.findViewById(R.id.user_pw2);
			userSave = (Button) rootView.findViewById(R.id.user_save);

			userFirstName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
					// action NEXT
					userLastName.requestFocus();
					return true;
				}
			});
			userLastName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
					// action NEXT
					userEmail.requestFocus();
					return true;
				}
			});
			userEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
					// action NEXT
					userPw1.requestFocus();
					return true;
				}
			});
			userPw1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
					// action NEXT
					userPw2.requestFocus();
					return true;
				}
			});
			userPw2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
					// action NEXT
					saveUser();
					return true;
				}
			});
			userSave.setOnClickListener(this);
		}

		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		loadUser();
	}

	@Override
	public void onPause() {
		super.onPause();

		if (loadTask != null) {
			loadTask.cancel(true);
			loadTask = null;
		}
		if (saveTask != null) {
			saveTask.cancel(true);
			saveTask = null;
		}
	}

	@Override
	public void onClick(View view) {
		saveUser();
	}

	private void loadUser() {
		if (loadTask != null) {
			loadTask.cancel(true);
			loadTask = null;
		}

		if (id == null) {
			// create form
			user = new User();
			return;
		}

		loadTask = new AsyncTask<Long, Void, User>() {
			@Override
			protected User doInBackground(Long... ids) {
				return dao.getUser(ids[0]);
			}

			@Override
			protected void onPostExecute(User result) {
				user = result;
				bindUserValues();
			}
		};
		loadTask.execute(id);
	}

	private void bindUserValues() {
		if (user != null && userFirstName != null) {
			userFirstName.setText(String.valueOf(user.getFirstName()));
			userLastName.setText(String.valueOf(user.getLastName()));
			userEmail.setText(String.valueOf(user.getEmail()));
			userPw1.setText("");
			userPw2.setText("");
		}
	}

	private void saveUser() {
		userSave.setEnabled(false);

		if (saveTask != null) {
			saveTask.cancel(true);
		}

		if (userPw1.getText() == null || !String.valueOf(userPw1.getText()).equals(String.valueOf(userPw2.getText()))) {
			// password mismatch
			Toast.makeText(getActivity(), R.string.err_user_pw_match, Toast.LENGTH_LONG).show();
			return;
		}

		collectUserValues();
		saveTask = new AsyncTask<User, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(User... users) {
				// TODO encode password
				return dao.saveUser(users[0]);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				showSaveResult(result);
				if (result) {
					loadUser();
				}
				userSave.setEnabled(true);
			}
		};
		saveTask.execute(user);
	}

	private void collectUserValues() {
		user.setFirstName(String.valueOf(userFirstName.getText()));
		user.setLastName(String.valueOf(userLastName.getText()));
		user.setEmail(String.valueOf(userEmail.getText()));
		user.setPw(String.valueOf(userPw1.getText()));
	}

	private void showSaveResult(boolean success) {
		final int text = success ? R.string.user_save_success : R.string.err_user_save_failed;
		Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
	}
}
