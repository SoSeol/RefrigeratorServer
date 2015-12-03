package Problem_Domain;

import java.util.Vector;

public class MessageList implements java.io.Serializable {
	private static final long serialVersionUID = 1634569638766300862L;
	private Vector<Message> list;

	public MessageList() {
		list = new Vector<Message>();
	}

	synchronized public void add(Message m) {
		for(Message i : list)
		{
			if(i.toString().compareTo(m.toString()) == 0 && m instanceof WarningMessage)
				return;
		}
		list.add(m);
	}
	
	synchronized public void checkOutofDate() {
		for (int i = 0; i < list.size(); ++i) {
			if (list.elementAt(i).isExpired())
				list.remove(i);
		}
	}
	
	synchronized public String showList() {
		if (list.size() == 0)
			return "No messages";
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < list.size(); ++i)			
			buf.append(list.elementAt(i).toString() + '\n');
		return buf.toString();
	}

}
