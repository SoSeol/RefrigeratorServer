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
	 *            위치
	 * @param uList
	 *            유저 리스트
	 * @return 성공하면 true, 실패하면 false
	 */
	public boolean deleteUser(int index, UserList uList, MessageList mList) {
		return uList.updateList(UpdateUserAction.DELETE,
				uList.elementAt(index), this.getName(), mList);
	}

	/**
	 * user modify
	 * 
	 * @param type
	 *            PW / Name 변경
	 * @param id
	 *            수정하고자 하는 유저 id
	 * @param editData
	 *            수정할 데이터 내용
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
