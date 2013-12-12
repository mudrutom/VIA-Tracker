package cz.cvut.via.tracker.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cz.cvut.via.tracker.app.dao.IssueDAO;
import cz.cvut.via.tracker.app.model.Issue;
import cz.cvut.via.tracker.app.model.IssueState;

/**
 * A list fragment representing a list of Issues. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link IssueDetailFragment}.<br/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class IssueListFragment extends ListFragment {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object.
	 */
	private Callbacks callbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int position = ListView.INVALID_POSITION;

	private IssueDAO dao;

	private List<Issue> issues;
	private IssueState filter = null;

	private AsyncTask<IssueState, Void, List<Issue>> reloadTask;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public IssueListFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final String url = getString(R.string.base_url) + getString(R.string.issue_url);
		dao = new IssueDAO(url);

		reloadTask = null;
	}

	@Override
	public void onResume() {
		super.onResume();
		reloadIssues();
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

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}
		callbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		callbacks = null;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);

		if (issues != null && issues.size() > position) {
			// Notify the active callbacks interface (the activity, if the
			// fragment is attached to one) that an item has been selected.
			final Long issueId = issues.get(position).getIdIssue();
			callbacks.onItemSelected(issueId);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (position != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, position);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
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

	public void setFilter(IssueState state) {
		filter = state;
	}

	public void reloadIssues() {
		if (reloadTask != null) {
			reloadTask.cancel(true);
		}

		reloadTask = new AsyncTask<IssueState, Void, List<Issue>>() {
			@Override
			protected List<Issue> doInBackground(IssueState... states) {
				return dao.getAllIssues(states[0]);
			}

			@Override
			protected void onPostExecute(List<Issue> result) {
				if (result == null) return;

				issues = result;

				setListAdapter(new IssueAdapter(getActivity(), issues));
				if (position != ListView.INVALID_POSITION) {
					getListView().setItemChecked(position, true);
				}
			}
		};
		reloadTask.execute(filter);
	}

	public static class IssueAdapter extends ArrayAdapter<Issue> {

		private final Activity context;
		private final List<Issue> issues;

		public IssueAdapter(Activity context, List<Issue> issues) {
			super(context, R.layout.issue_list_item, issues);
			this.context = context;
			this.issues = issues;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = context.getLayoutInflater().inflate(R.layout.issue_list_item, parent, false);
			}

			if (convertView != null && position < issues.size()) {
				final Issue issue = issues.get(position);
				final IssueState state = IssueState.valueOf(issue.getState());
				((TextView) convertView.findViewById(R.id.issue_title)).setText(String.valueOf(issue.getTitle()));
				final TextView issueState = (TextView) convertView.findViewById(R.id.issue_state);
				issueState.setText(state.nameRes);
				switch (state) {
					case open:
						issueState.setBackgroundResource(R.drawable.bg_state_open);
						break;
					case inProgress:
						issueState.setBackgroundResource(R.drawable.bg_state_inprogress);
						break;
					case fixed:
						issueState.setBackgroundResource(R.drawable.bg_state_fixed);
						break;
					case wontFix:
						issueState.setBackgroundResource(R.drawable.bg_state_wontfix);
						break;
				}
			}

			return convertView;
		}
	}

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(Long id);
	}
}
