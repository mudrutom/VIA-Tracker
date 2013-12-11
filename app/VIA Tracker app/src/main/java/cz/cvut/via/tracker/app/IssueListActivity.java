package cz.cvut.via.tracker.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import cz.cvut.via.tracker.app.model.User;


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
public class IssueListActivity extends AbstractActivity implements IssueListFragment.Callbacks, ListView.OnItemClickListener {

	/**
	 * Whether or not the activity is in two-pane mode,
	 * i.e. running on a tablet device.
	 */
	private boolean twoPane;

	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;

	private Long issueId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_issue_list);
		setTitle(R.string.title_issue_list);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// set a custom shadow that overlays the main content when the drawer opens
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		// set up the drawer's list view with items and click listener
		final String[] drawerItems = new String[] {
				getString(R.string.title_issue_list),
				getString(R.string.title_user_list),
				getString(R.string.account_edit)
		};
		drawerList = (ListView) findViewById(R.id.navigation_drawer);
		drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, drawerItems));
		drawerList.setOnItemClickListener(this);

		if (getActionBar() != null) {
			// enable ActionBar app icon to behave as action to toggle the drawer
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);
		}

		// ActionBarDrawerToggle ties together the the proper interactions between the sliding drawer and the action bar app icon
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);
		drawerLayout.setDrawerListener(drawerToggle);

		if (findViewById(R.id.container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			twoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((IssueListFragment) getSupportFragmentManager().findFragmentById(R.id.issue_list)).setActivateOnItemClick(true);
		}
	}

	@Override
	protected void onPostCreate(final Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		drawerList.setItemChecked(0, true);
	}

	@Override
	public void onConfigurationChanged(final Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles.
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.issue_list_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
			case R.id.menu_edit:
				if (issueId != null) {
					lunchModifyActivity(issueId);
				}
				return true;
			case R.id.menu_create:
				lunchModifyActivity(null);
				return true;
			case R.id.menu_refresh:
				refreshList();
				return true;
			case R.id.menu_logout:
				lunchLoginActivity();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Callback method from {@link IssueListFragment.Callbacks}
	 * indicating that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(Long id) {
		issueId = id;

		if (twoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			final Bundle arguments = new Bundle();
			arguments.putLong(IssueDetailFragment.ARG_ISSUE_ID, id);
			final IssueDetailFragment fragment = new IssueDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			final Intent detailIntent = new Intent(this, IssueDetailActivity.class);
			detailIntent.putExtra(IssueDetailFragment.ARG_ISSUE_ID, id);
			startActivity(detailIntent);
		}
	}

	/**
	 * The click listener for ListView in the navigation drawer.
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = null;
		switch (position) {
			case 0:
				// already selected
				break;
			case 1:
				intent = new Intent(this, UserListActivity.class);
				break;
			case 2:
				final User currentUser = getAppContext().getCurrentUser();
				intent = new Intent(this, UserModifyActivity.class);
				intent.putExtra(UserModifyFragment.ARG_USER_ID, currentUser.getIdUser());
		}

		if (intent != null) {
			startActivity(intent);
			finish();
		}
		drawerLayout.closeDrawers();
	}

	private void lunchModifyActivity(Long id) {
		if (twoPane) {
			final IssueModifyFragment fragment = new IssueModifyFragment();
			if (id != null) {
				final Bundle arguments = new Bundle();
				arguments.putLong(IssueModifyFragment.ARG_ISSUE_ID, id);
				fragment.setArguments(arguments);
			}
			getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
		} else {
			final Intent intent = new Intent(this, IssueModifyActivity.class);
			if (id != null) {
				intent.putExtra(IssueModifyFragment.ARG_ISSUE_ID, id);
			}
			startActivity(intent);
		}
	}

	private void refreshList() {
		((IssueListFragment) getSupportFragmentManager().findFragmentById(R.id.issue_list)).reloadIssues();
	}
}
