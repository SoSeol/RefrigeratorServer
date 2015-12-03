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
	/* p@ 음식 리스트 출력방식에서 인덱스 넣을지 말지 의견 부탁드려요 */
	synchronized public String showList() {
		if (list.size() == 0)
			return "Refrigerator is empty";
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < list.size(); ++i)
			buf.append((i + 1) + " : " + list.elementAt(i).toString() + '\n');
		return buf.toString();
	}

	synchronized public String searchList(String name) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < list.size(); ++i) {
			if (list.elementAt(i).getName().compareTo(name) == 0)
				buf.append((i + 1) + " : " + list.elementAt(i).toString() + '\n');
		}
		if(buf.length() == 0) return null;
		return buf.toString();
	}

	/* p@ system이라고 하는 것보다 refrigerator라든지, manager 같은 단어로 바꾸면 괜찮을듯 해요 */
	synchronized public void checkProhibited(MessageList mList) {
		for (Food tmp : list) {
			if (tmp.isProhibited(this))
				createWarningMessage(WarningMessageType.ForbiddenFood, tmp, "Refrigerator", mList);
		}
	}

	/*
	 * p@ 인덱스와 음식 이름으로 음식 검색, 선택 메서드 구현했습니다. list.elementat에서 인덱스예외 발샏하면 어떻게
	 * 처리할지;; 처리하지 말까요?
	 */
	synchronized public Food elementAt(int index) {
		try
		{
			return list.elementAt(index);
		}
		catch(ArrayIndexOutOfBoundsException aioobe)
		{
			return null;
		}
	}

	synchronized public Food searchFood(String foodName) {
		for (int i = 0; i < list.size(); ++i) {
			if (list.elementAt(i).getName().compareTo(foodName) == 0)
				return list.elementAt(i);
		}
		return null;
	}

	/**
	 * 유통기한 지났거나 3일 이하로 남았는지 확인한 후 경고 메세지 제작.
	 * <p>
	 */
	synchronized public void checkExpired(MessageList mList) {
		for (Food tmp : list) {
			if (tmp.isExpired())
				createWarningMessage(WarningMessageType.FoodExpired, tmp, "Refrigerator", mList);
			else if (tmp.getLeftDays() <= 3)
				createWarningMessage(WarningMessageType.FoodNearExpiration, tmp, "Refrigerator", mList);
		}
	}
	
	/*
	 * p@ if문 살짝 다듬고 이 메서드 외에도 모든 스위치 문에 대해서 default 케이스에 시스템에러메세지 넣어두었습니다.(프로그램
	 * 안정성을 위해서)
	 */
	synchronized public boolean updateList(UpdateUserAction act, FoodEditType editType, Food food, String operatorName,
			MessageList mList) {
		if(food == null) return false;
		
		int idx;
		boolean bSuccess = false;
		switch (act) {
		case DELETE:
			idx = list.indexOf(food);
			if (idx != -1) {
				list.remove(idx);
				createUpdateMessage(UpdateMessageType.Removal, food.getName(), operatorName, mList);
				bSuccess = true;
			}
			break;
		case EDIT:
			idx = list.indexOf(food);
			if (idx != -1) {
				list.set(idx, food);
				createUpdateMessage(editType, food.getName(), operatorName, mList);
				bSuccess = true;
			}
			break;
		case REGISTER:
			list.add(food);
			createUpdateMessage(UpdateMessageType.Addition, food.getName(), operatorName, mList);
			bSuccess = true;
			break;
		default:
			System.err.println("Unknown Action\n"); /* p@ 업데이트 액션 에러 */
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
	synchronized private void createUpdateMessage(UpdateMessageType type, String tgtFoodName, String operatorName,
			MessageList messagelist) {
		UpdateMessage newMessage = null;
		switch (type) {
		case Addition:
			newMessage = new UpdateMessage("New Food " + tgtFoodName + " stored by " + operatorName, operatorName);
			break;
		case Removal:
			newMessage = new UpdateMessage("Food " + tgtFoodName + " taken by " + operatorName, operatorName);
			break;
		default:
			System.err.println("Unknown type\n"); /* p@ 메세지 타입 에러 */
			break;
		}
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
	synchronized private void createUpdateMessage(FoodEditType type, String tgtFoodName, String operatorName,
			MessageList messagelist) {
		UpdateMessage newMessage = null;
		switch (type) {
		case FreezerCooler:
		case Location:
			newMessage = new UpdateMessage("Food " + tgtFoodName + " was moved by " + operatorName, operatorName);
			break;
		case Weight:
			newMessage = new UpdateMessage("Food " + tgtFoodName + "'s weight was modified by " + operatorName,
					operatorName);
			break;
		case Quantity:
			newMessage = new UpdateMessage("Food " + tgtFoodName + "'s quantity was modified by " + operatorName,
					operatorName);
			break;
		case Calories:
			newMessage = new UpdateMessage("Food " + tgtFoodName + "'s calories were modified by " + operatorName,
					operatorName);
			break;
		case Memo:
			newMessage = new UpdateMessage("Food " + tgtFoodName + "'s memo were modified by " + operatorName,
					operatorName);
			break;
		default:
			System.err.println("Unknown type\n"); /* p@ 메세지 타입 에러 */
			break;
		}
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

	synchronized private void createWarningMessage(WarningMessageType type, Food tgtFood, String tgtUserName,
			MessageList list) {
		WarningMessage newMessage = null;
		switch (type) {
		case FoodExpired:
			newMessage = new WarningMessage(
					"Food expired : Name -> " + tgtFood.getName() + ", Location : "
							+ (tgtFood.getFreezeType() ? "Freezer" : "Cooler") + ", Floor " + tgtFood.getFloor(),
					tgtUserName);
			break;
		case FoodNearExpiration:
			newMessage = new WarningMessage(
					"Food near to expired in " + tgtFood.getLeftDays()
							+ " days : Name -> " + tgtFood.getName() + ", Location : "
							+ (tgtFood.getFreezeType() ? "Freezer" : "Cooler") + ", Floor " + tgtFood.getFloor(),
					tgtUserName);
			break;
		case ForbiddenFood:
			newMessage = new WarningMessage("Prohibited food in refrigerator : " + tgtFood.getName(), tgtUserName);
		default:
			System.err.println("Unknown type\n"); /* p@ 메세지 타입 에러 */
			break;
		}
		if (newMessage != null)
			list.add(newMessage);
	}

}
