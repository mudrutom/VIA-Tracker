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
	public static final String ARG_ITEM_ID = "idIssue";

	private IssueDAO dao;

	private Issue issue;

	private TextView issueDetail;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public IssueDetailFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final String url = getString(R.string.base_url) + getString(R.string.issue_url);
		dao = new IssueDAO(url);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the issue content specified by the fragment arguments.
			final long id = getArguments().getLong(ARG_ITEM_ID);
			new AsyncTask<Long, Void, Issue>() {
				@Override
				protected Issue doInBackground(Long... id) {
					return dao.getIssue(id[0]);
				}

				@Override
				protected void onPostExecute(Issue result) {
					issue = result;
					if (issueDetail != null) {
						issueDetail.setText((issue == null) ? "" : issue.toString());
					}
				}
			}.execute(id);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_issue_detail, container, false);

		// Show the issue content as text in a TextView.
		if (rootView != null) {
			issueDetail = (TextView) rootView.findViewById(R.id.issue_detail);
			issueDetail.setText((issue == null) ? "" : issue.toString());
		}

		return rootView;
	}
}
