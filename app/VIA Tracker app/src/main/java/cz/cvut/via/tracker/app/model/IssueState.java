package cz.cvut.via.tracker.app.model;

public enum IssueState {

	open(1), inProgress(2), fixed(3), wontFix(4);

	final int num;

	private IssueState(int num) {
		this.num = num;
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
