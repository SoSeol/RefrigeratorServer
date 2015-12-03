package Problem_Domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Message implements java.io.Serializable {
	private static final long serialVersionUID = 4412316044526006384L;
	private final static byte DEFAULT_EXPIRE_DATE = 3;
	private final static SimpleDateFormat SDF = new SimpleDateFormat(
			"yyyy/MM/dd");
	private String messageDetail;
	private Calendar createdDate;
	private Calendar expiredDate;
	private String createdBy;

	/**
	 * 
	 * 
	 * @param detail
	 *            message content
	 * @param until
	 *            end date
	 * @param created
	 *            writer
	 */
	protected Message(String detail, String created, Calendar until) {
		messageDetail = detail;
		createdDate = Calendar.getInstance();
		expiredDate = until;
		createdBy = created;
	}

	public Message(String detail, String created) {
		this(detail, created, getAfterDay());
	}

	public boolean isExpired() {
		return expiredDate.before(Calendar.getInstance());
	}

	protected String getcreatedBy() {
		return createdBy;
	}

	protected Calendar getCreatedDate() {
		return createdDate;
	}

	protected Calendar getEndDate() {
		return expiredDate;
	}

	protected String getMessageDetail() {
		return messageDetail;
	}

	protected static SimpleDateFormat getSDF() {
		return SDF;
	}

	private static Calendar getAfterDay(int cnt) {
		Calendar date = Calendar.getInstance();
		date.add(Calendar.DATE, cnt);
		return date;
	}

	private static Calendar getAfterDay() {
		return getAfterDay(DEFAULT_EXPIRE_DATE);
	}

	public String toString() {

		return "[MEMO@" + getcreatedBy() + "] " + getMessageDetail() + " ("
				+ SDF.format(getCreatedDate().getTime()) + ')';
	}

}
