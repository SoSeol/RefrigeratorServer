package Problem_Domain;

public class UpdateMessage extends Message {
	private static final long serialVersionUID = 4645469466221212808L;

	/**
	 * Ư�� �������ڸ� ������ ��� ����� ������
	 * 
	 * @param detail
	 *            �޼��� ����
	 * @param created
	 *            �޼��� ������
	 * @param messageUntil
	 *            �Խø�������
	 *
	 *            public UpdateMessage(String detail, String created, Calendar
	 *            messageUntil) { super(detail, created, messageUntil); }
	 */

	/**
	 * Ư�� �������ڸ� �������� ���� ��� ����� ������
	 * 
	 * @param detail
	 *            �޼��� ����
	 * @param created
	 *            �޼��� ������
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
