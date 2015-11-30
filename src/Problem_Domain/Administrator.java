package Problem_Domain;

public class Administrator extends User
{
	private static final long serialVersionUID = 3097568847164762841L;

	public Administrator(String newName, String newID, String newPW)
	{
		super(newName, newID, newPW);
	}
	
	public String searchUser(UserList list)
	{		
		return list.showList();
	}
	
	/**
	 * id�� �ش��ϴ� ������ ������. ���н� false 
	 * @param id ������ ���� id
	 */
	public boolean deleteUser(String id, UserList uList, MessageList mList)
	{
		User usr = uList.checkID(id);
		if(usr == null) return false;
		return uList.updateList(UpdateUserAction.DELETE, usr, this.getName(), mList);
	}
	
	/**
	 * ��ġ �޾� �ش��ϴ� ������ ������. ���н� false
	 * @param index ��ġ
	 * @param uList ���� ����Ʈ
	 * @return �����ϸ� true, �����ϸ� false
	 */
	public boolean deleteUser(int index, UserList uList, MessageList mList)
	{
		return uList.updateList(UpdateUserAction.DELETE, uList.elementAt(index), this.getName(), mList);
	}
	
	/**
	 * ������� �Է��� �޾� ���� ����.
	 * @param type PW / Name ����
	 * @param id �����ϰ��� �ϴ� ���� id
	 * @param editData ������ ������ ����
	 */
	public boolean modifyUser(UserEditType type, String id, String editData, UserList uList, MessageList mList)
	{
		User target = uList.checkID(id);
		switch(type)
		{
		case ID:
			if(uList.checkID(editData) != null || this.getID().compareTo(id) == 0)
				return false;
			target.setID(editData);
			break;
		case PW:
			target.setPW(editData);
			break;
		case Name:
			target.setName(editData);
			break;
		}
		return uList.updateList(UpdateUserAction.EDIT, target, this.getName(), mList);
	}
	
	/**
	 * ���� ����� �޼��� ���
	 * @param prev ����ϴ� ���� Ÿ��
	 * @param name
	 * @param id
	 * @param pw
	 */
	public boolean registerUser(UserPrevilege prev, String name, String id, String pw, UserList uList, MessageList mList)
	{
		User newUser = null;
		if(uList.checkID(id) != null) return false;
		
		switch(prev)
		{
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
	 * ������ �� �� �޼����� ������.
	 */
	public void deleteOldMessages(MessageList mList)
	{
		mList.checkOutofDate();
	}
	
	public String toString()
	{
		return "Administrator, ID : " + getID() + ", Name : " + getName();
	}
}
