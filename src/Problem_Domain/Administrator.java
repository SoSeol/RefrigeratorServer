package Problem_Domain;

public class Administrator extends User {
	private static final long serialVersionUID = 3097568847164762841L;

	public Administrator(String newName, String newID, String newPW) {
		super(newName, newID, newPW);
	}

	public String searchUser(UserList list) {
		return list.showList();
	}

	/*
	 * p@ id 검색 삭제 대신 인덱스 삭제만 구현하면 될 것 같아요 showlist로 유저 리스트 보일때 '인덱스 | id name ~
	 * ' 형태로 보여서 관리자가 id 삭제 관리할 때 인덱스로 선택하면 간편할 것 같습니다. /** id에 해당하는 유저를 삭제함.
	 * 실패시 false
	 * 
	 * @param id 삭제할 유저 id
	 * 
	 * public boolean deleteUser(String id, UserList uList, MessageList mList) {
	 * User usr = uList.checkID(id); if(usr == null) return false; return
	 * uList.updateList(UpdateUserAction.DELETE, usr, this.getName(), mList); }
	 */

	/**
	 * 위치 받아 해당하는 유저를 삭제함. 실패시 false
	 * 
	 * @param index
	 *            위치
	 * @param uList
	 *            유저 리스트
	 * @return 성공하면 true, 실패하면 false
	 */
	public boolean deleteUser(int index, UserList uList, MessageList mList) {
		return uList.updateList(UpdateUserAction.DELETE, uList.elementAt(index), this.getName(), mList);
	}

	/**
	 * 사용자의 입력을 받아 유저 수정.
	 * 
	 * @param type
	 *            PW / Name 변경
	 * @param id
	 *            수정하고자 하는 유저 id
	 * @param editData
	 *            수정할 데이터 내용
	 */
	public boolean modifyUser(UserEditType type, String id, String editData, UserList uList, MessageList mList) {
		User targetUser = uList.checkID(id);
		switch (type) {
		/*
		 * p@ ID 수정은 제외해야할 것 같습니다. 리스트에 지장이 있을 것 같아요 case ID:
		 * if(uList.checkID(editData) != null || this.getID().compareTo(id) ==
		 * 0) return false; target.setID(editData); break;
		 */
		case PW:
			targetUser.setPW(editData);
			break;
		case Name:
			targetUser.setName(editData);
			break;
		default:
			System.err.println("Unknown type\n"); /* p@ 에딧타입 에러 */
			break;
		}
		return uList.updateList(UpdateUserAction.EDIT, targetUser, this.getName(), mList);
	}

	/**
	 * 유저 등록휴 메세지 출력
	 * 
	 * @param prev
	 *            등록하는 유저 타입
	 * @param name
	 * @param id
	 * @param pw
	 */
	public boolean registerUser(UserPrevilege prev, String name, String id, String pw, UserList uList,
			MessageList mList) {
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

		return uList.updateList(UpdateUserAction.REGISTER, newUser, this.getName(), mList);
	}

	/**
	 * 기한이 다 된 메세지를 삭제함.
	 */
	public void deleteOldMessages(MessageList mList) {
		mList.checkOutofDate();
	}

	public String toString() {
		return "Administrator, ID : " + getID() + ", Name : " + getName();
	}
}
