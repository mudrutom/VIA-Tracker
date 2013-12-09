package cz.cvut.via.tracker.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.cvut.via.tracker.app.dao.UserDAO;
import cz.cvut.via.tracker.app.model.User;

public class UserDetailFragment extends Fragment {

	public static final String ARG_USER_ID = "idUser";

	private UserDAO dao;

	private Long id = null;

	private User user;

	private TextView userId, userFirstName, userLastName, userEmail;

	private AsyncTask<Long, Void, User> loadTask;

	public UserDetailFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle arguments = getArguments();
		id = (arguments != null && arguments.containsKey(ARG_USER_ID)) ? arguments.getLong(ARG_USER_ID) : null;

		final String url = getString(R.string.base_url) + getString(R.string.user_url);
		dao = new UserDAO(url);

		loadTask = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_user_detail, container, false);

		if (rootView != null) {
			userId = (TextView) rootView.findViewById(R.id.user_id);
			userFirstName = (TextView) rootView.findViewById(R.id.user_firstName);
			userLastName = (TextView) rootView.findViewById(R.id.user_lastName);
			userEmail = (TextView) rootView.findViewById(R.id.user_email);
			bindUserValues();
		}

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		loadUser();
	}

	@Override
	public void onPause() {
		super.onPause();

		if (loadTask != null) {
			loadTask.cancel(true);
			loadTask = null;
		}
	}

	private void loadUser() {
		if (loadTask != null) {
			loadTask.cancel(true);
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
		if (user != null && userId != null) {
			userId.setText(String.valueOf(user.getIdUser()));
			userFirstName.setText(String.valueOf(user.getFirstName()));
			userLastName.setText(String.valueOf(user.getLastName()));
			userEmail.setText(String.valueOf(user.getEmail()));
		}
	}
}
