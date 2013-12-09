package cz.cvut.via.tracker.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import cz.cvut.via.tracker.app.dao.IssueDAO;
import cz.cvut.via.tracker.app.model.Issue;

public class IssueModifyFragment extends Fragment implements View.OnClickListener {

	public static final String ARG_ISSUE_ID = "idIssue";

	private IssueDAO dao;

	private Long id = null;

	private Issue issue = null;

	private EditText issueTitle, issueDescription;
	private Spinner issueState;
	private SeekBar issuePriority;
	private Button issueSave;

	private AsyncTask<Long, Void, Issue> loadTask;
	private AsyncTask<Issue, Void, Boolean> saveTask;

	public IssueModifyFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle arguments = getArguments();
		id = (arguments != null && arguments.containsKey(ARG_ISSUE_ID)) ? arguments.getLong(ARG_ISSUE_ID) : null;

		final String url = getString(R.string.base_url) + getString(R.string.issue_url);
		dao = new IssueDAO(url);

		loadTask = null;
		saveTask = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_issue_modify, container, false);

		if (rootView != null) {
			issueTitle = (EditText) rootView.findViewById(R.id.issue_title);
			issueDescription = (EditText) rootView.findViewById(R.id.issue_description);
			issueState = (Spinner) rootView.findViewById(R.id.issue_state);
			issuePriority = (SeekBar) rootView.findViewById(R.id.issue_priority);
			issueSave = (Button) rootView.findViewById(R.id.issue_save);

			issueTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
					// action NEXT
					issueDescription.requestFocus();
					return true;
				}
			});
			// TODO issue state spinner
			issuePriority.setMax(5);
			issueSave.setOnClickListener(this);
		}

		bindIssueValues();

		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		loadIssue();
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
		saveIssue();
	}

	private void loadIssue() {
		if (loadTask != null) {
			loadTask.cancel(true);
			loadTask = null;
		}

		if (id == null) {
			// create form
			issue = new Issue();
			return;
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
		if (issue != null && issueTitle != null) {
			issueTitle.setText(String.valueOf(issue.getTitle()));
			issueDescription.setText(String.valueOf(issue.getDescription()));
			issuePriority.setProgress(issue.getPriority());
		}
	}

	private void saveIssue() {
		issueSave.setEnabled(false);

		if (saveTask != null) {
			saveTask.cancel(true);
		}
		collectIssueValues();
		saveTask = new AsyncTask<Issue, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Issue... issues) {
				return dao.saveIssue(issues[0]);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				showSaveResult(result);
				if (result) {
					loadIssue();
				}
				issueSave.setEnabled(true);
			}
		};
		saveTask.execute(issue);
	}

	private void collectIssueValues() {
		issue.setTitle(String.valueOf(issueTitle.getText()));
		issue.setDescription(String.valueOf(issueDescription.getText()));
		issue.setPriority(issuePriority.getProgress());

		if (id == null) {
			// set issue author
			final AppContext appContext = ((AbstractActivity) getActivity()).getAppContext();
			issue.setCreatedByUser(appContext.getCurrentUser().getIdUser());
		}
	}

	private void showSaveResult(boolean success) {
		final int text = success ? R.string.issue_save_success : R.string.err_issue_save_failed;
		Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
	}
}
