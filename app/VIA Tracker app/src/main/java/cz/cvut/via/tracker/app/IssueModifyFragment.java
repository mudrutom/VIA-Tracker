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

public class IssueModifyFragment extends Fragment {

	public static final String ARG_ISSUE_ID = "idIssue";

	private IssueDAO dao;

	private Issue issue;

	private TextView issueModify;

	public IssueModifyFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final String url = getString(R.string.base_url) + getString(R.string.issue_url);
		dao = new IssueDAO(url);

		final Bundle arguments = getArguments();
		if (arguments != null && arguments.containsKey(ARG_ISSUE_ID)) {
			final long id = arguments.getLong(ARG_ISSUE_ID);
			new AsyncTask<Long, Void, Issue>() {
				@Override
				protected Issue doInBackground(Long... id) {
					return dao.getIssue(id[0]);
				}

				@Override
				protected void onPostExecute(Issue result) {
					issue = result;
					if (issueModify != null) {
						issueModify.setText((issue == null) ? "" : issue.toString());
					}
				}
			}.execute(id);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_issue_modify, container, false);

		if (rootView != null) {
			issueModify = (TextView) rootView.findViewById(R.id.issue_modify);
			issueModify.setText((issue == null) ? "" : issue.toString());
			if (getArguments() == null || !getArguments().containsKey(ARG_ISSUE_ID)) {
				issueModify.setText("NEW issue...");
			}
		}

		return rootView;
	}
}
