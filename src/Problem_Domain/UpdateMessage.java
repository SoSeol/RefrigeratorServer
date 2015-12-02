package Problem_Domain;

public class UpdateMessage extends Message {
	private static final long serialVersionUID = 4645469466221212808L;

	/**
	 * 특정 종료일자를 지정할 경우 사용할 생성자
	 * 
	 * @param detail
	 *            메세지 내용
	 * @param created
	 *            메세지 제작자
	 * @param messageUntil
	 *            게시만료일자
	 *
	 *            public UpdateMessage(String detail, String created, Calendar
	 *            messageUntil) { super(detail, created, messageUntil); }
	 */

	/**
	 * 특정 종료일자를 지정하지 않을 경우 사용할 생성자
	 * 
	 * @param detail
	 *            메세지 내용
	 * @param created
	 *            메세지 제작자
	 */
	public UpdateMessage(String detail, String created) {
		super(detail, created);
	}

	@Override
	public String toString() {
		return "[UPDATE@" + getcreatedBy() + "] " + getMessageDetail() + " ("
				+ getSdf().format(getCreatedDate().getTime())+")";
	}
}
