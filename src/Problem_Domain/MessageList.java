package Problem_Domain;

import java.util.Vector;

public class MessageList implements java.io.Serializable {
	private static final long serialVersionUID = 1634569638766300862L;
	private Vector<Message> list;

	/* p@ ����Ʈ�� �޼��� �� �����Ǵ� �� �����鼭 ��Ʈ�� ���� �ʿ��ϸ� �����ϸ� �� �� ���ƿ� */

	public MessageList() {
		list = new Vector<Message>();
	}

	/**
	 * �޼����� �߰��� �� ���. �޼����� ��Ͽ� �߰��� �� �߰��۾��� ���� ����.
	 * 
	 * @param m
	 *            �߰��� �޼���
	 */
	synchronized public void add(Message m) {
		list.add(m);
	}

	/**
	 * ������ ���� �޼����� ������.
	 */
	synchronized public void checkOutofDate() {
		for (int i = 0; i < list.size(); ++i) {
			if (list.elementAt(i).isExpired())
				list.remove(i);
		}
	}

	/**
	 * ����Ʈ�� ���ڿ��� ��ȯ����
	 * 
	 * @return ���ڿ�ȭ �� ����Ʈ
	 */
	synchronized public String showList() {
		if (list.size() == 0)
			return "No messages";
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < list.size(); ++i)
			// p@ buf.append((i + 1) + " : " + list.elementAt(i).toString() + '\n');
			buf.append(list.elementAt(i).toString() + '\n');
		return buf.toString();
	}

}
