package cz.cvut.via.tracker.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import cz.cvut.via.tracker.app.dao.UserDAO;
import cz.cvut.via.tracker.app.model.User;

public class UserListFragment extends ListFragment {

	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	private Callbacks callbacks;

	private int position = ListView.INVALID_POSITION;

	private UserDAO dao;

	private List<User> users;

	private AsyncTask<Void, Void, List<User>> reloadTask;

	public UserListFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final String url = getString(R.string.base_url) + getString(R.string.user_url);
		dao = new UserDAO(url);

		reloadTask = null;
	}

	@Override
	public void onResume() {
		super.onResume();
		reloadUsers();
	}

	@Override
	public void onPause() {
		super.onPause();

		if (reloadTask != null) {
			reloadTask.cancel(true);
			reloadTask = null;
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}
		callbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		callbacks = null;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);

		if (users != null && users.size() > position) {
			final Long userId = users.get(position).getIdUser();
			callbacks.onItemSelected(userId);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (position != ListView.INVALID_POSITION) {
			outState.putInt(STATE_ACTIVATED_POSITION, position);
		}
	}

	public void setActivateOnItemClick(boolean activateOnItemClick) {
		getListView().setChoiceMode(activateOnItemClick
				? ListView.CHOICE_MODE_SINGLE
				: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(this.position, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		this.position = position;
	}

	public void reloadUsers() {
		if (reloadTask != null) {
			reloadTask.cancel(true);
		}

		reloadTask = new AsyncTask<Void, Void, List<User>>() {
			@Override
			protected List<User> doInBackground(Void... v) {
				return dao.getAllUsers();
			}

			@Override
			protected void onPostExecute(List<User> result) {
				if (result == null) return;

				users = result;
				setListAdapter(new ArrayAdapter<User>(getActivity(),
						android.R.layout.simple_list_item_activated_1,
						android.R.id.text1,
						users));
				if (position != ListView.INVALID_POSITION) {
					getListView().setItemChecked(position, true);
				}
			}
		};
		reloadTask.execute();
	}

	public interface Callbacks {
		public void onItemSelected(Long id);
	}
}
