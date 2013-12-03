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

public class UserModifyFragment extends Fragment {

	public static final String ARG_USER_ID = "idUser";

	private UserDAO dao;

	private User user;

	private TextView userModify;

	public UserModifyFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final String url = getString(R.string.base_url) + getString(R.string.user_url);
		dao = new UserDAO(url);

		final Bundle arguments = getArguments();
		if (arguments != null && arguments.containsKey(ARG_USER_ID)) {
			final Long id = arguments.getLong(ARG_USER_ID);
			new AsyncTask<Long, Void, User>() {
				@Override
				protected User doInBackground(Long... id) {
					return dao.getUser(id[0]);
				}

				@Override
				protected void onPostExecute(User result) {
					user = result;
					if (userModify != null) {
						userModify.setText((user == null) ? "" : user.toString());
					}
				}
			}.execute(id);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_user_modify, container, false);

		if (rootView != null) {
			userModify = (TextView) rootView.findViewById(R.id.user_modify);
			userModify.setText((user == null) ? "" : user.toString());
			if (getArguments() == null || !getArguments().containsKey(ARG_USER_ID)) {
				userModify.setText("NEW USER...");
			}
		}

		return rootView;
	}
}
