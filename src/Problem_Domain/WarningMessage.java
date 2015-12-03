package Problem_Domain;

import java.util.Calendar;

public class WarningMessage extends Message {
	private static final long serialVersionUID = 3407002089938496042L;

	public WarningMessage(String detail, String created, Calendar until) {
		super(detail, created, until);
	}

	public WarningMessage(String detail, String created) {
		super(detail, created);
	}

	@Override
	public String toString() {
		return "[WARNING@" + getcreatedBy() + "] " + getMessageDetail() + " ("
				+ getSDF().format(getCreatedDate().getTime()) + ")";
	}

}
