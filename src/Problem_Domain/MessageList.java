package Problem_Domain;

import java.util.Vector;

public class MessageList implements java.io.Serializable {
	private static final long serialVersionUID = 1634569638766300862L;
	private Vector<Message> list;

	/* p@ 리스트도 메세지 출 구현되는 거 봐가면서 스트링 형태 필요하면 수정하면 될 것 같아요 */

	public MessageList() {
		list = new Vector<Message>();
	}

	/**
	 * 메세지를 추가할 때 사용. 메세지를 목록에 추가한 뒤 추가작업은 하지 않음.
	 * 
	 * @param m
	 *            추가할 메세지
	 */
	synchronized public void add(Message m) {
		list.add(m);
	}

	/**
	 * 기한이 지난 메세지를 삭제함.
	 */
	synchronized public void checkOutofDate() {
		for (int i = 0; i < list.size(); ++i) {
			if (list.elementAt(i).isExpired())
				list.remove(i);
		}
	}

	/**
	 * 리스트를 문자열로 변환해줌
	 * 
	 * @return 문자열화 된 리스트
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
