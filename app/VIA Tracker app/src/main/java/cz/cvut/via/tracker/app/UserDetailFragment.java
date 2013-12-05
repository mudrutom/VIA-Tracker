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

	private User user;

	private TextView userId, userFirstName, userLastName, userEmail;

	public UserDetailFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final String url = getString(R.string.base_url) + getString(R.string.user_url);
		dao = new UserDAO(url);

		final Bundle arguments = getArguments();
		if (arguments != null && arguments.containsKey(ARG_USER_ID)) {
			final long id = arguments.getLong(ARG_USER_ID);
			new AsyncTask<Long, Void, User>() {
				@Override
				protected User doInBackground(Long... id) {
					return dao.getUser(id[0]);
				}

				@Override
				protected void onPostExecute(User result) {
					user = result;
					bindUserValues();
				}
			}.execute(id);
		}
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

	private void bindUserValues() {
		if (user != null && userId != null) {
			userId.setText(String.valueOf(user.getIdUser()));
			userFirstName.setText(String.valueOf(user.getFirstName()));
			userLastName.setText(String.valueOf(user.getLastName()));
			userEmail.setText(String.valueOf(user.getEmail()));
		}
	}
}
