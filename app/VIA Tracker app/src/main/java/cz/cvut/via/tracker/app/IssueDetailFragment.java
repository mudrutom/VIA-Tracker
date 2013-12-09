package cz.cvut.via.tracker.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.cvut.via.tracker.app.dao.IssueDAO;
import cz.cvut.via.tracker.app.model.Issue;

/**
 * A fragment representing a single Issue detail screen.
 * This fragment is either contained in a {@link IssueListActivity}
 * in two-pane mode (on tablets) or a {@link IssueDetailActivity}
 * on handsets.
 */
public class IssueDetailFragment extends Fragment {

	/**
	 * The fragment argument representing the item ID that
	 * this fragment represents.
	 */
	public static final String ARG_ISSUE_ID = "idIssue";

	private IssueDAO dao;

	private Long id = null;

	private Issue issue;

	private TextView issueId, issueTitle, issueDescription, issuePriority, issueCreatedByUser, issueState, issueTimestamp;

	private AsyncTask<Long, Void, Issue> loadTask;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public IssueDetailFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the issue ID specified by the fragment arguments.
		final Bundle arguments = getArguments();
		id = (arguments != null && arguments.containsKey(ARG_ISSUE_ID)) ? arguments.getLong(ARG_ISSUE_ID) : null;

		final String url = getString(R.string.base_url) + getString(R.string.issue_url);
		dao = new IssueDAO(url);

		loadTask = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_issue_detail, container, false);

		// Show the issue content as text in a TextView.
		if (rootView != null) {
			issueId = (TextView) rootView.findViewById(R.id.issue_id);
			issueTitle = (TextView) rootView.findViewById(R.id.issue_title);
			issueDescription = (TextView) rootView.findViewById(R.id.issue_description);
			issuePriority = (TextView) rootView.findViewById(R.id.issue_priority);
			issueCreatedByUser = (TextView) rootView.findViewById(R.id.issue_createdByUser);
			issueState = (TextView) rootView.findViewById(R.id.issue_state);
			issueTimestamp = (TextView) rootView.findViewById(R.id.issue_timestamp);
			bindIssueValues();
		}

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		loadIssue();
	}

	@Override
	public void onPause() {
		super.onPause();

		if (loadTask != null) {
			loadTask.cancel(true);
			loadTask = null;
		}
	}

	private void loadIssue() {
		if (loadTask != null) {
			loadTask.cancel(true);
		}
		loadTask = new AsyncTask<Long, Void, Issue>() {
			@Override
			protected Issue doInBackground(Long... ids) {
				return dao.getIssue(ids[0]);
			}

			@Override
			protected void onPostExecute(Issue result) {
				issue = result;
				bindIssueValues();
			}
		};
		loadTask.execute(id);
	}

	private void bindIssueValues() {
		if (issue != null && issueId != null) {
			issueId.setText(String.valueOf(issue.getIdIssue()));
			issueTitle.setText(String.valueOf(issue.getTitle()));
			issueDescription.setText(String.valueOf(issue.getDescription()));
			issuePriority.setText(String.valueOf(issue.getPriority()));
			issueCreatedByUser.setText(String.valueOf(issue.getCreatedByUser()));
			issueState.setText(String.valueOf(issue.getState()));
			issueTimestamp.setText(String.valueOf(issue.getTimestamp()));
		}
	}
}
