package Problem_Domain;

import java.util.Vector;

public class UserList implements java.io.Serializable
{
	private static final long serialVersionUID = 2999421213097939473L;
	private Vector<User> list;
	
	/**
	 * ����� ����Ʈ�� ���� �����ϰ� �⺻ ���� <p>
	 * �̸��� ID�� <code>Administrator</code>�̰�
	 * ��й�ȣ�� <code>admin123</code>�� �⺻ ������ �־����
	 */
	public UserList()
	{
		list = new Vector<User>();
		// ��������� �α��� ��ü�� ���ϹǷ� �⺻ ����ڸ� ����Ʈ�� �߰�
		list.add(new Administrator("Administrator", "Administrator", "admin123"));
	}	

	/**
	 * ����Ʈ�� ���ڿ��� ��ȯ����
	 * @return ���ڿ�ȭ �� ����Ʈ
	 */
	synchronized public String showList()
	{
		StringBuffer buf = new StringBuffer();
		for(int i = 0; i < list.size(); ++i)
			buf.append((i + 1) + " : " + list.elementAt(i).toString() + "\n");
		return buf.toString();
	}
	
	/**
	 * ����Ʈ�� �ش��ϴ� ���̵� ������ User ��ü�� �Ѱ��ְ� ������ null�� �Ѱ���
	 * @param ID ã���� �ϴ� ID
	 * @return ã�� ��� �ش� �����, ã�� ���� ��� null
	 */
	synchronized public User checkID(String ID)
	{
		for(int i = 0; i < list.size(); ++i)
		{
			if(list.elementAt(i).getID().compareTo(ID) == 0)
				return list.elementAt(i);
		}
		return null;
	}
	
	/**
	 * ����Ʈ���� �ش��ϴ� ���̵� ã�� ���� ����� ���� ������ �ε��� ,�׷��� �ʴٸ� -1 �� �Ѱ��ش�. 
	 * @param ID
	 * @return idx��
	 */
	synchronized public User elementAt(int idx)
	{
		return list.elementAt(idx);
	}
	
	/**
	 * ���� ����Ʈ�� ������Ʈ�ϰ� �����ϸ� true, �����ϸ� false�� ��ȯ�Ѵ�
	 * @param act EDIT/DELETE/REGISTER
	 * @param usr ������Ʈ�ϴ� �����
	 * @param operatorName ������Ʈ�� �������� �����ڸ�
	 * @param mList �޼��� ����Ʈ
	 * @return true / false
	 */
	synchronized public boolean updateList(UpdateUserAction act, User usr, String operatorName, MessageList mList)
	{
		int idx;
		boolean bSuccess = false;
		switch(act)
		{
		case DELETE:
			idx = list.indexOf(usr);
			if(idx == -1)
			{
				bSuccess = false;
				break;
			}
			list.remove(idx);
			createUpdateMessage(UpdateMessageType.Removal, usr.getName(), operatorName, mList);
			bSuccess = true;
			break;
		case EDIT:
			idx = list.indexOf(usr);
			if(idx == -1)
			{
				bSuccess = false;
				break;
			}
			list.set(idx, usr);
			createUpdateMessage(UpdateMessageType.Modification, usr.getName(), operatorName, mList);
			bSuccess = true;
			break;
		case REGISTER:
			list.add(usr);
			createUpdateMessage(UpdateMessageType.Addition, usr.getName(), operatorName, mList);
			bSuccess = true;
			break;
		}
		return bSuccess;
	}
	
	/**
	 * ������Ʈ �޼��� ���� �� �޼��� ��Ͽ� �߰�
	 * @param t ������Ʈ �޼��� ����
	 * @param tgtUserName ������Ʈ �ش��ϴ� ����� �̸�
	 * @param operatorName ������Ʈ�� �ϴ� ������ �̸�
	 *
	 */	
	synchronized private void createUpdateMessage(UpdateMessageType t, String tgtUserName, String operatorName, MessageList mList)
	{
		UpdateMessage newMessage = null;
		switch(t)
		{
		case Addition:
			newMessage = new UpdateMessage("New User " + tgtUserName + "added by " + operatorName, operatorName);
			break;
		case Modification:
			newMessage = new UpdateMessage("User " + tgtUserName + "modified by " + operatorName, operatorName);
			break;
		case Removal:
			newMessage = new UpdateMessage("User " + tgtUserName + "deleted by " + operatorName, operatorName);
			break;
		default: break;
		}
		mList.add(newMessage);
	}
}