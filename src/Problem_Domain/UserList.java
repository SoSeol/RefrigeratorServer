package Problem_Domain;

import java.util.Vector;

public class UserList implements java.io.Serializable {
	private static final long serialVersionUID = 2999421213097939473L;
	private Vector<User> list;

	/**
	 * default administrator
	 */
	public UserList() {
		list = new Vector<User>();
		list.add(new Administrator("Administrator", "Administrator", "admin123"));
	}

	synchronized public String showList() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < list.size(); ++i)
			buf.append((i + 1) + " : " + list.elementAt(i).toString() + "\n");
		return buf.toString();
	}

	synchronized public User checkID(String ID) {
		for (int i = 0; i < list.size(); ++i) {
			if (list.elementAt(i).getID().compareTo(ID) == 0)
				return list.elementAt(i);
		}
		return null;
	}

	/**
	 * get userlist index
	 */
	synchronized public User elementAt(int idx) {
		try {
			return list.elementAt(idx);
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			return null;
		}
	}

	synchronized public boolean updateList(UpdateUserAction act, User usr,
			String operatorName, MessageList mList) {
		return updateList(act, usr, operatorName, mList, true);
	}

	synchronized public boolean updateList(UpdateUserAction act, User usr,
			String operatorName, MessageList mList, boolean bMessage) {
		if (usr == null)
			return false;

		int idx;
		boolean bSuccess = false;
		switch (act) {
		case DELETE:
			idx = list.indexOf(usr);
			if (idx != -1) {
				list.remove(idx);
				createUpdateMessage(UpdateMessageType.Removal, usr.getName(),
						operatorName, mList);
				bSuccess = true;
			}
			break;
		case EDIT:
			idx = list.indexOf(usr);
			if (idx != -1) {
				list.set(idx, usr);
				if (bMessage == true)
					createUpdateMessage(UpdateMessageType.Modification,
							usr.getName(), operatorName, mList);
				bSuccess = true;
			}
			break;
		case REGISTER:
			list.add(usr);
			createUpdateMessage(UpdateMessageType.Addition, usr.getName(),
					operatorName, mList);
			bSuccess = true;
			break;
		default:
			System.err.println("Unknown Action\n");
			break;
		}
		return bSuccess;
	}

	synchronized private void createUpdateMessage(UpdateMessageType t,
			String tgtUserName, String operatorName, MessageList mList) {
		UpdateMessage newMessage = null;
		switch (t) {
		case Addition:
			newMessage = new UpdateMessage("New User " + tgtUserName
					+ " added by " + operatorName, operatorName);
			break;
		case Modification:
			newMessage = new UpdateMessage("User " + tgtUserName
					+ " modified by " + operatorName, operatorName);
			break;
		case Removal:
			newMessage = new UpdateMessage("User " + tgtUserName
					+ " deleted by " + operatorName, operatorName);
			break;
		default:
			System.err.println("Unknown type\n");
			break;
		}
		if (newMessage != null)
			mList.add(newMessage);
	}

}