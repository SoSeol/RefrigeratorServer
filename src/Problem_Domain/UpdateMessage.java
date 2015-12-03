package Problem_Domain;

import java.util.Calendar;

public class UpdateMessage extends Message {
	private static final long serialVersionUID = 4645469466221212808L;

	public UpdateMessage(String detail, String created, Calendar messageUntil) {
		super(detail, created, messageUntil);
	}

	public UpdateMessage(String detail, String created) {
		super(detail, created);
	}

	@Override
	public String toString() {
		return "[UPDATE@" + getcreatedBy() + "] " + getMessageDetail() + " ("
				+ getSDF().format(getCreatedDate().getTime()) + ")";
	}
}
