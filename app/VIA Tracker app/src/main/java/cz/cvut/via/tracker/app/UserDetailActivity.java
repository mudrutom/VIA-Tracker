package cz.cvut.via.tracker.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class UserDetailActivity extends FragmentActivity {

	private Long userId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_detail);
		setTitle(R.string.title_user_detail);

		userId = getIntent().getLongExtra(UserDetailFragment.ARG_USER_ID, 0L);

		if (getActionBar() != null) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		if (savedInstanceState == null) {
			final Bundle arguments = new Bundle();
			arguments.putLong(UserDetailFragment.ARG_USER_ID, userId);
			final UserDetailFragment fragment = new UserDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.user_detail_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_edit:
				final Intent intent = new Intent(this, UserModifyActivity.class);
				intent.putExtra(UserModifyFragment.ARG_USER_ID, userId);
				startActivity(intent);
				finish();
				return true;
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
