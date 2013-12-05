package cz.cvut.via.tracker.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class UserListActivity extends FragmentActivity implements UserListFragment.Callbacks, ListView.OnItemClickListener {

	private boolean twoPane;

	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;

	private Long userId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);
		setTitle(R.string.title_user_list);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		final String[] drawerItems = new String[] {
				getString(R.string.title_issue_list),
				getString(R.string.title_user_list)
		};
		drawerList = (ListView) findViewById(R.id.navigation_drawer);
		drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, drawerItems));
		drawerList.setOnItemClickListener(this);

		if (getActionBar() != null) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);
		}

		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);
		drawerLayout.setDrawerListener(drawerToggle);

		if (findViewById(R.id.container) != null) {
			twoPane = true;

			((UserListFragment) getSupportFragmentManager().findFragmentById(R.id.user_list)).setActivateOnItemClick(true);
		}
	}

	@Override
	protected void onPostCreate(final Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		drawerList.setItemChecked(1, true);
	}

	@Override
	public void onConfigurationChanged(final Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.user_list_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
			case R.id.menu_edit:
				if (userId != null) {
					lunchModifyActivity(userId);
				}
				return true;
			case R.id.menu_create:
				lunchModifyActivity(null);
				return true;
			case R.id.menu_refresh:
				refreshList();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemSelected(Long id) {
		userId = id;

		if (twoPane) {
			final Bundle arguments = new Bundle();
			arguments.putLong(UserDetailFragment.ARG_USER_ID, id);
			final UserDetailFragment fragment = new UserDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
		} else {
			final Intent detailIntent = new Intent(this, UserDetailActivity.class);
			detailIntent.putExtra(UserDetailFragment.ARG_USER_ID, id);
			startActivity(detailIntent);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		Intent intent = null;
		switch (position) {
			case 0:
				intent = new Intent(this, IssueListActivity.class);
				break;
			case 1:
				// already selected
				break;
		}

		if (intent != null) {
			startActivity(intent);
			finish();
		}
		drawerLayout.closeDrawers();
	}

	private void lunchModifyActivity(Long id) {
		if (twoPane) {
			final UserModifyFragment fragment = new UserModifyFragment();
			if (id != null) {
				final Bundle arguments = new Bundle();
				arguments.putLong(UserModifyFragment.ARG_USER_ID, id);
				fragment.setArguments(arguments);
			}
			getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
		} else {
			final Intent intent = new Intent(this, UserModifyActivity.class);
			if (id != null) {
				intent.putExtra(UserModifyFragment.ARG_USER_ID, id);
			}
			startActivity(intent);
		}
	}

	private void refreshList() {
		((UserListFragment) getSupportFragmentManager().findFragmentById(R.id.user_list)).reloadUsers();
	}
}
