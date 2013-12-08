package cz.cvut.via.tracker.app.model;

import cz.cvut.via.tracker.app.R;

public enum IssueState {

	open(1, R.string.issueState_open),
	inProgress(2, R.string.issueState_inProgress),
	fixed(3, R.string.issueState_fixed),
	wontFix(4, R.string.issueState_wontFix);

	public final int num;
	public final int nameRes;

	private IssueState(int num, int nameRes) {
		this.num = num;
		this.nameRes = nameRes;
	}

	public static IssueState valueOf(int num) {
		switch (num) {
			case 1: return open;
			case 2: return inProgress;
			case 3: return fixed;
			case 4: return wontFix;
			default: throw new IllegalArgumentException("unknown num=" + num);
		}
	}
}
