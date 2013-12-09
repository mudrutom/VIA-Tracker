package cz.cvut.via.tracker.app;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

public abstract class AbstractActivity extends FragmentActivity {

	public AppContext getAppContext() {
		return (AppContext) super.getApplication();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (getAppContext().getCurrentUser() == null) {
			// user needs to login
			lunchLoginActivity();
		}
	}

	protected void lunchLoginActivity() {
		startActivity(new Intent(this, LoginActivity.class));
	}
}
