package Problem_Domain;

public class Administrator extends User {
	private static final long serialVersionUID = 3097568847164762841L;

	public Administrator(String newName, String newID, String newPW) {
		super(newName, newID, newPW);
	}

	public String searchUser(UserList list) {
		return list.showList();
	}

	/**
	 * get index and delete user
	 * 
	 * @param index
	 *            ��ġ
	 * @param uList
	 *            ���� ����Ʈ
	 * @return �����ϸ� true, �����ϸ� false
	 */
	public boolean deleteUser(int index, UserList uList, MessageList mList) {
		return uList.updateList(UpdateUserAction.DELETE,
				uList.elementAt(index), this.getName(), mList);
	}

	/**
	 * user modify
	 * 
	 * @param type
	 *            PW / Name ����
	 * @param id
	 *            �����ϰ��� �ϴ� ���� id
	 * @param editData
	 *            ������ ������ ����
	 */
	public boolean modifyUser(UserEditType type, int idx, String editData,
			UserList uList, MessageList mList) {
		User targetUser = uList.elementAt(idx);
		switch (type) {
		case PW:
			targetUser.setPW(editData);
			break;
		case Name:
			targetUser.setName(editData);
			break;
		default:
			System.err.println("Unknown type\n");
			break;
		}
		return uList.updateList(UpdateUserAction.EDIT, targetUser,
				this.getName(), mList);
	}

	/**
	 * user register and print message
	 * 
	 * @param prev
	 *            usertype(administrator,normal)
	 * @param name
	 * @param id
	 * @param pw
	 */
	public boolean registerUser(UserPrevilege prev, String name, String id,
			String pw, UserList uList, MessageList mList) {
		User newUser = null;
		if (uList.checkID(id) != null)
			return false;

		switch (prev) {
		case Administrator:
			newUser = new Administrator(name, id, pw);
			break;
		case Normal:
			newUser = new NormalUser(name, id, pw);
			break;
		}

		return uList.updateList(UpdateUserAction.REGISTER, newUser,
				this.getName(), mList);
	}

	/**
	 * delete message due
	 */
	public void deleteOldMessages(MessageList mList) {
		mList.checkOutofDate();
	}

	public String toString() {
		return "Administrator, ID : " + getID() + ", Name : " + getName();
	}
}
