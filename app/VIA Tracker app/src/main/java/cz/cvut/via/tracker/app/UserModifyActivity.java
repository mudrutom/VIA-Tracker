package cz.cvut.via.tracker.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class UserModifyActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_modify);

		final Long userId;
		if (getIntent().hasExtra(UserModifyFragment.ARG_USER_ID)) {
			userId = getIntent().getLongExtra(UserModifyFragment.ARG_USER_ID, 0L);
			setTitle(R.string.user_edit);
		} else {
			userId = null;
			setTitle(R.string.user_create);
		}

		if (getActionBar() != null) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		if (savedInstanceState == null) {
			final UserModifyFragment fragment = new UserModifyFragment();
			if (userId != null) {
				final Bundle arguments = new Bundle();
				arguments.putLong(UserModifyFragment.ARG_USER_ID, userId);
				fragment.setArguments(arguments);
			}
			getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
		}
	}
}
