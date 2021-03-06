package cz.cvut.via.tracker.app;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class IssueModifyActivity extends AbstractActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_issue_modify);

		final Long issueId;
		if (getIntent().hasExtra(IssueModifyFragment.ARG_ISSUE_ID)) {
			issueId = getIntent().getLongExtra(IssueModifyFragment.ARG_ISSUE_ID, 0L);
			setTitle(R.string.issue_edit);
		} else {
			issueId = null;
			setTitle(R.string.issue_create);
		}

		if (getActionBar() != null) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		if (savedInstanceState == null) {
			final IssueModifyFragment fragment = new IssueModifyFragment();
			if (issueId != null) {
				final Bundle arguments = new Bundle();
				arguments.putLong(IssueModifyFragment.ARG_ISSUE_ID, issueId);
				fragment.setArguments(arguments);
			}
			getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.modify_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_save:
				saveIssue();
				return true;
			case R.id.menu_logout:
				lunchLoginActivity();
				return true;
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void saveIssue() {
		((IssueModifyFragment) getSupportFragmentManager().findFragmentById(R.id.container)).saveIssue();
	}
}
