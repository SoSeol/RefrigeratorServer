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
		prohibitedList.add("����");
		prohibitedList.add("����ȭ��");
		prohibitedList.add("����");
		prohibitedList.add("�ʷ���");
		prohibitedList.add("�븶��");
		prohibitedList.add("û�갡��");
		list = new Vector<Food>();
	}

	public ArrayList<String> getProhibitedList() {
		return prohibitedList;
	}

	/**
	 * ����Ʈ�� ���ڿ��� ��ȯ����
	 * 
	 * @return ���ڿ�ȭ �� ����Ʈ
	 */
	/* p@ ���� ����Ʈ ��¹�Ŀ��� �ε��� ������ ���� �ǰ� ��Ź����� */
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

	/* p@ system�̶�� �ϴ� �ͺ��� refrigerator�����, manager ���� �ܾ�� �ٲٸ� �������� �ؿ� */
	synchronized public void checkProhibited(MessageList mList) {
		for (Food tmp : list) {
			if (tmp.isProhibited(this))
				createWarningMessage(WarningMessageType.ForbiddenFood, tmp, "Refrigerator", mList);
		}
	}

	/*
	 * p@ �ε����� ���� �̸����� ���� �˻�, ���� �޼��� �����߽��ϴ�. list.elementat���� �ε������� �ߘ��ϸ� ���
	 * ó������;; ó������ �����?
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
	 * ������� �����ų� 3�� ���Ϸ� ���Ҵ��� Ȯ���� �� ��� �޼��� ����.
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
	 * p@ if�� ��¦ �ٵ�� �� �޼��� �ܿ��� ��� ����ġ ���� ���ؼ� default ���̽��� �ý��ۿ����޼��� �־�ξ����ϴ�.(���α׷�
	 * �������� ���ؼ�)
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
			System.err.println("Unknown Action\n"); /* p@ ������Ʈ �׼� ���� */
			break;
		}
		return bSuccess;
	}

	/**
	 * ������Ʈ �޼��� ���� �� �޼��� ��Ͽ� �߰�
	 * 
	 * @param t
	 *            ������Ʈ �޼��� ����
	 * @param tgtFoodName
	 *            ������Ʈ �ش��ϴ� ���� �̸�
	 * @param operatorName
	 *            ������Ʈ�� �ϴ� ������ �̸�
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
			System.err.println("Unknown type\n"); /* p@ �޼��� Ÿ�� ���� */
			break;
		}
		messagelist.add(newMessage);
	}

	/**
	 * ���� ���� ������Ʈ �޼��� ���� �� �޼��� ��Ͽ� �߰�
	 * 
	 * @param act
	 *            ������ �׸�
	 * @param tgtFoodName
	 *            ������ ���ĸ�
	 * @param operatorName
	 *            ������ ��� �̸�
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
			System.err.println("Unknown type\n"); /* p@ �޼��� Ÿ�� ���� */
			break;
		}
		messagelist.add(newMessage);
	}

	/**
	 * warning �޼��� ������ �޼��� ��Ͽ� �߰�
	 * 
	 * @param t
	 *            ���׸޼��� ����
	 * @param FoodName
	 *            �˶� ��� ����
	 * @param tgtUserName
	 *            ���� ���� ���
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
			System.err.println("Unknown type\n"); /* p@ �޼��� Ÿ�� ���� */
			break;
		}
		if (newMessage != null)
			list.add(newMessage);
	}

}
