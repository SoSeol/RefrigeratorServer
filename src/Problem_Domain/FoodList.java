package Problem_Domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

public class FoodList implements java.io.Serializable {
	private static final long serialVersionUID = 5379206085183801822L;
	private ArrayList<String> prohibitedList;
	private Vector<Food> list;

	public FoodList() {
		prohibitedList = new ArrayList<String>();
		prohibitedList.add("마약");
		prohibitedList.add("마리화나");
		prohibitedList.add("본드");
		prohibitedList.add("필로폰");
		prohibitedList.add("대마초");
		prohibitedList.add("청산가리");
		list = new Vector<Food>();
	}

	public ArrayList<String> getProhibitedList() {
		return prohibitedList;
	}

	/**
	 * 리스트를 문자열로 변환해줌
	 * 
	 * @return 문자열화 된 리스트
	 */
	synchronized public String showList() {
		if(list.size() == 0) return "No foods in refrigerator";
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < list.size(); ++i)
			buf.append((i + 1) + " : " + list.elementAt(i).toString() + '\n');
		return buf.toString();
	}

	synchronized public String searchList(String name) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < list.size(); ++i) {
			if (list.elementAt(i).getName().compareTo(name) == 0)
				buf.append((i + 1) + " : " + list.elementAt(i).toString()
						+ '\n');
		}
		return buf.toString();
	}

	synchronized public void checkProhibited(MessageList mList) {
		for (Food tmp : list) {
			if (tmp.isProhibited(this))
				createWarningMessage(WarningMessageType.ForbiddenFood, tmp,
						"System", mList);
		}
	}
	
	synchronized public Food elementAt(int index)
	{
		return list.elementAt(index);
	}

	/**
	 * 유통기한 지났거나 3일 이하로 남았는지 확인한 후 경고 메세지 제작.
	 * <p>
	 */
	synchronized public void checkExpired(MessageList mList) {
		for (Food tmp : list) {
			if (tmp.isExpired())
				createWarningMessage(WarningMessageType.FoodExpired, tmp,
						"System", mList);
			else if (Calendar.getInstance().compareTo(tmp.getExpirationDate()) <= 3)
				createWarningMessage(WarningMessageType.FoodNearExpiration,
						tmp, "System", mList);
		}
	}

	synchronized public boolean updateList(UpdateUserAction act, FoodEditType editType, Food food, String operatorName, MessageList mList)
	{
		int idx;
		boolean bSuccess = false;
		switch(act)
		{
		case DELETE:
			idx = list.indexOf(food);
			if(idx == -1)
			{
				bSuccess = false;
				break;
			}
			list.remove(idx);
			createUpdateMessage(UpdateMessageType.Removal, food.getName(), operatorName, mList);
			bSuccess = true;
			break;
		case EDIT:
			idx = list.indexOf(food);
			if(idx == -1)
			{
				bSuccess = false;
				break;
			}
			list.set(idx, food);
			createUpdateMessage(editType, food.getName(), operatorName, mList);
			bSuccess = true;
			break;
		case REGISTER:
			list.add(food);
			createUpdateMessage(UpdateMessageType.Addition, food.getName(), operatorName, mList);
			bSuccess = true;
			break;
		}
		return bSuccess;
	}

	/**
	 * 업데이트 메세지 생성 후 메세지 목록에 추가
	 * 
	 * @param t
	 *            업데이트 메세지 종류
	 * @param tgtFoodName
	 *            업데이트 해당하는 음식 이름
	 * @param operatorName
	 *            업데이트를 하는 관리자 이름
	 */
	synchronized private void createUpdateMessage(UpdateMessageType t, String tgtFoodName,
			String operatorName, MessageList messagelist) {
		UpdateMessage newMessage = null;
		switch (t) {
		case Addition:
			newMessage = new UpdateMessage("New Food " + tgtFoodName
					+ " stored by " + operatorName, operatorName);
			break;
		case Removal:
			newMessage = new UpdateMessage("Food " + tgtFoodName + " taken by "
					+ operatorName, operatorName);
			break;
		default:
			break;
		}
		if (null != newMessage)
			messagelist.add(newMessage);
	}

	/**
	 * 수정 관련 업데이트 메세지 생성 후 메세지 목록에 추가
	 * 
	 * @param act
	 *            수정한 항목
	 * @param tgtFoodName
	 *            수정한 음식명
	 * @param operatorName
	 *            수정한 사람 이름
	 */
	synchronized private void createUpdateMessage(FoodEditType type, String tgtFoodName,
			String operatorName, MessageList messagelist) {
		UpdateMessage newMessage = null;
		switch (type) {
		case FreezerCooler:
		case Location:
			newMessage = new UpdateMessage("Food " + tgtFoodName
					+ " was moved by " + operatorName, operatorName);
			break;
		case Weight:
			newMessage = new UpdateMessage("Food " + tgtFoodName
					+ "'s weight was modified by " + operatorName, operatorName);
			break;
		case Quantity:
			newMessage = new UpdateMessage("Food " + tgtFoodName
					+ "'s quantity was modified by " + operatorName,
					operatorName);
			break;
		case Calories:
			newMessage = new UpdateMessage("Food " + tgtFoodName
					+ "'s calories were modified by " + operatorName, operatorName);
		}
		if (null != newMessage)
			messagelist.add(newMessage);
	}

	/**
	 * warning 메세지 생성후 메세지 목록에 추가
	 * 
	 * @param t
	 *            워닝메세지 종류
	 * @param FoodName
	 *            알람 대상 음식
	 * @param tgtUserName
	 *            음식 넣은 사람
	 */
	synchronized private void createWarningMessage(WarningMessageType t, Food tgtFood, String tgtUserName,MessageList list)
	{
		WarningMessage newMessage = null;
		switch(t)
		{
		case FoodExpired:
			newMessage= new WarningMessage("Food expired : Name -> " + tgtFood.getName() +
			         						", Location : " + (tgtFood.getFreezeType()? "Freezer" : "Cooler") + 
			        						", Floor " + tgtFood.getFloor(), tgtUserName);
			break;
		case FoodNearExpiration:
			newMessage = new WarningMessage("Food near to expired in "+ Calendar.getInstance().compareTo(tgtFood.getExpirationDate()) + 
			           						" days : Name -> " + tgtFood.getName() + ", Location : " +
			           						(tgtFood.getFreezeType()? "Freezer" : "Cooler") + ", Floor " + tgtFood.getFloor(), tgtUserName);
			break;
		case ForbiddenFood:
			newMessage = new WarningMessage("Prohibited food in refrigerator : " + tgtFood.getName(), tgtUserName); 
		default: break;
		}
		if(newMessage != null)
			list.add(newMessage);
	}

}
