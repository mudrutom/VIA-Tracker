package cz.cvut.via.tracker.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;

import cz.cvut.via.tracker.app.dao.IssueDAO;
import cz.cvut.via.tracker.app.dao.UserDAO;
import cz.cvut.via.tracker.app.model.Issue;
import cz.cvut.via.tracker.app.model.IssueState;
import cz.cvut.via.tracker.app.model.User;

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
	private UserDAO userDAO;

	private Long id = null;

	private Issue issue;
	private User author;

	private TextView issueTitle, issueDescription, issuePriority, issueCreatedByUser, issueState, issueTimestamp;

	private AsyncTask<Long, Void, Serializable[]> loadTask;

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

		final String userUrl = getString(R.string.base_url) + getString(R.string.user_url);
		userDAO = new UserDAO(userUrl);

		loadTask = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_issue_detail, container, false);

		// Show the issue content as text in a TextView.
		if (rootView != null) {
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
		loadTask = new AsyncTask<Long, Void, Serializable[]>() {
			@Override
			protected Serializable[] doInBackground(Long... ids) {
				final Issue resIssue = dao.getIssue(ids[0]);
				final User resUser = (resIssue == null) ? null : userDAO.getUser(resIssue.getCreatedByUser());
				return new Serializable[] { resIssue, resUser };
			}

			@Override
			protected void onPostExecute(Serializable[] result) {
				issue = (Issue) result[0];
				author = (User) result[1];
				bindIssueValues();
			}
		};
		loadTask.execute(id);
	}

	private void bindIssueValues() {
		if (issue != null && author != null && issueTitle != null) {
			issueTitle.setText(String.valueOf(issue.getTitle()));
			issueDescription.setText(String.valueOf(issue.getDescription()));
			issuePriority.setText(String.valueOf(issue.getPriority()));
			issueCreatedByUser.setText(String.valueOf(author.getName()));
			issueState.setText(IssueState.valueOf(issue.getState()).nameRes);
			issueTimestamp.setText(String.valueOf(issue.getTimestamp()));
		}
	}
}
