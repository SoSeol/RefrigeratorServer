package Problem_Domain;

import java.util.Vector;

public class UserList implements java.io.Serializable
{
	private static final long serialVersionUID = 2999421213097939473L;
	private Vector<User> list;
	
	/**
	 * 사용자 리스트를 새로 생성하고 기본 계정 <p>
	 * 이름과 ID가 <code>Administrator</code>이고
	 * 비밀번호가 <code>admin123</code>인 기본 계쩡을 넣어놓음
	 */
	public UserList()
	{
		list = new Vector<User>();
		// 비어있으면 로그인 자체를 못하므로 기본 사용자를 리스트에 추가
		list.add(new Administrator("Administrator", "Administrator", "admin123"));
	}	

	/**
	 * 리스트를 문자열로 변환해줌
	 * @return 문자열화 된 리스트
	 */
	synchronized public String showList()
	{
		StringBuffer buf = new StringBuffer();
		for(int i = 0; i < list.size(); ++i)
			buf.append((i + 1) + " : " + list.elementAt(i).toString() + "\n");
		return buf.toString();
	}
	
	/**
	 * 리스트에 해당하는 아이디가 있으면 User 객체를 넘겨주고 없으면 null을 넘겨줌
	 * @param ID 찾고자 하는 ID
	 * @return 찾은 경우 해당 사용자, 찾지 못한 경우 null
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
	 * 리스트에서 해당하는 아이디를 찾아 유저 존재시 가진 유저의 인덱스 ,그렇지 않다면 -1 을 넘겨준다. 
	 * @param ID
	 * @return idx값
	 */
	synchronized public User elementAt(int idx)
	{
		return list.elementAt(idx);
	}
	
	/**
	 * 음식 리스트를 업데이트하고 성공하면 true, 실패하면 false를 반환한다
	 * @param act EDIT/DELETE/REGISTER
	 * @param usr 업데이트하는 사용자
	 * @param operatorName 업데이트를 수행중인 관리자명
	 * @param mList 메세지 리스트
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
	 * 업데이트 메세지 생성 후 메세지 목록에 추가
	 * @param t 업데이트 메세지 종류
	 * @param tgtUserName 업데이트 해당하는 사용자 이름
	 * @param operatorName 업데이트를 하는 관리자 이름
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