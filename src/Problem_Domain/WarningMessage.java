package Problem_Domain;

public class WarningMessage extends Message {
	private static final long serialVersionUID = 3407002089938496042L;

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
	 *            public WarningMessage(String detail, String created, Calendar
	 *            until) { super(detail, created, until); }
	 */

	/**
	 * Ư�� �������ڸ� �������� ���� ��� ����� ������
	 * 
	 * @param detail
	 *            �޼��� ����
	 * @param created
	 *            �޼��� ������
	 */
	public WarningMessage(String detail, String created) {
		super(detail, created);
	}

	/**
	 * WarningMessage �޼��� ���ڿ��� ��ȯ ��ȯ ����� "[���� ����/������] Warning : '�޼��� ����'"
	 */
	@Override
	public String toString() {
		// String[] splitted = super.toString().split(" ");
		// return splitted[0] + "Warning : " + splitted[1];
		return "[WARNING@" + getcreatedBy() + "] " + getMessageDetail() + " ("
				+ getSdf().format(getCreatedDate().getTime()) + ")";
	}

}
