package cz.cvut.via.tracker.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


/**
 * An activity representing a list of Issues. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link IssueDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.<br/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link IssueListFragment} and the item details
 * (if present) is a {@link IssueDetailFragment}.<br/>
 * This activity also implements the required
 * {@link IssueListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class IssueListActivity extends FragmentActivity implements IssueListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode,
	 * i.e. running on a tablet device.
	 */
	private boolean twoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_issue_list);

		if (findViewById(R.id.issue_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			twoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((IssueListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.issue_list))
					.setActivateOnItemClick(true);
		}
	}

	/**
	 * Callback method from {@link IssueListFragment.Callbacks}
	 * indicating that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(Long id) {
		if (twoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			final Bundle arguments = new Bundle();
			arguments.putLong(IssueDetailFragment.ARG_ITEM_ID, id);
			final IssueDetailFragment fragment = new IssueDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.issue_detail_container, fragment)
					.commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			final Intent detailIntent = new Intent(this, IssueDetailActivity.class);
			detailIntent.putExtra(IssueDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}
