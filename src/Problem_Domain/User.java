package Problem_Domain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;

public class User implements java.io.Serializable {
	private static final long serialVersionUID = -6717215299669248946L;
	//MessageDigest hasher; // �޼��� ��ȣȭ ���� ����
	private final static String HASH_ALGORITHM = "SHA-256";
	private String name;
	private String ID;
	private byte[] PW;

	/**
	 * ����� ������
	 * 
	 * @param newName
	 *            ����� �̸�
	 * @param newID
	 *            ����� ID
	 * @param newPW
	 *            ����� ��й�ȣ
	 * @param prev
	 */
	protected User(String newName, String newID, String newPW) {
			// ��ȣȭ �ʱ�ȭ.
			// �ڼ��� �������
			// https://docs.oracle.com/javase/8/docs/api/java/security/MessageDigest.html
			// ���� �ٶ�.
			name = newName;
			ID = newID;
			try {
				PW = MessageDigest.getInstance(HASH_ALGORITHM).digest(newPW.getBytes());
			} catch (NoSuchAlgorithmException e) {}
	}

	/**
	 * ���� �ٲ� ��й�ȣ ���ڿ��� �޾Ƽ� ����.
	 * <p>
	 * ��й�ȣ�� �ٲ� �� ���� üũ�� ���� ����
	 * 
	 * @param newPW
	 *            �� ��й�ȣ ���ڿ�
	 */

	public void setName(String name) {
		this.name = name;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public void setPW(String PW) {
		try {
			this.PW = MessageDigest.getInstance(HASH_ALGORITHM).digest(PW.getBytes());
		} catch (NoSuchAlgorithmException e) {}
	}

	public String getName() {
		return name;
	}

	public String getID() {
		return ID;
	}

	public boolean checkPassword(String inputPW) {
		try {
			return Arrays.equals(PW, MessageDigest.getInstance(HASH_ALGORITHM).digest(inputPW.getBytes()));
		} catch (NoSuchAlgorithmException e) { return false; }
	}

	@Override
	public String toString() {
		return name + " (" + ID + ")";
	}

	/**
	 * ���� ���
	 * 
	 * @param newName
	 * @param newQuantity
	 * @param newWeight
	 * @param newCalories
	 * @param bFreezer
	 * @param row
	 * @param col
	 * @param newExpirationDate
	 */

	public boolean registerFood(String newName, int newQuantity, int newWeight, int newCalories, boolean bFreezer,
			String newfloor, Calendar newExpirationDate, String newMemo, FoodList fList, MessageList mList) {
		if (newName == null || newfloor == null)
			return false;
		Food newfood = new Food(newName, newQuantity, newWeight, newCalories, bFreezer, newfloor, newExpirationDate,
				newMemo);
		if (newfood.isProhibited(fList)) {
			WarningMessage msg = new WarningMessage(
					name + "tried to put in forbidden item \"" + newfood.getName() + '\"', "Refrigerator");
			mList.add(msg);
			return false;
		}

		return fList.updateList(UpdateUserAction.REGISTER, null, newfood, this.getName(), mList);
	}

	/**
	 * ���� ����
	 * 
	 * @param idx
	 *            �����ϰ��� �ϴ� ���� �ε���
	 */
	public boolean deleteFood(int idx, FoodList fList, MessageList mList) {
		return fList.updateList(UpdateUserAction.DELETE, null, fList.elementAt(idx), this.getName(), mList);
	}

	public boolean modifyFood(FoodEditType type, int idx, String editData, FoodList fList, MessageList mList) {
		Food modifyFood = fList.elementAt(idx);
		if (editData == null)
			return false;
		switch (type) {
		case FreezerCooler:
			switch (editData) {
			case "Freezer":
				modifyFood.setFreezeType(true);
				break;
			case "Cooler":
				modifyFood.setFreezeType(false);
				break;
			}
			break;
		case Location:
			modifyFood.setFloor(editData);
			break;
		case Quantity:
			modifyFood.setQuantity(Integer.parseInt(editData));
			break;
		case Weight:
			modifyFood.setWeight(Integer.parseInt(editData));
			break;
		case Calories:
			modifyFood.setCalories(Integer.parseInt(editData));
			break;
		case Memo:
			modifyFood.setMemo(editData);
			break;
		default:
			System.err.println("Unknown type\n"); /* p@ ���� Ÿ�� ���� */
			break;
		}
		return fList.updateList(UpdateUserAction.EDIT, type, modifyFood, this.getName(), mList);
	}

	/*
	 * p@ ���� �Ʒ��� �ִ� �͵��� �ʿ���°� ����?
	 *//**
		 * Ǫ�� ����Ʈ ���
		 */
	/*
	 * public String searchItem(FoodList fList) { return fList.showList(); }
	 * 
	 * public String searchItem(String str, FoodList fList) { return
	 * fList.searchList(str); }
	 * 
	 *//**
		 * ��ĥ ���� ����, ����Ʈ ��ġ, ��ĥ ������ �޾� �ش� ���� ���� ����
		 * 
		 * @param act
		 *            ����. FreezerCooler / Location / Quantity / Weight
		 * @param idx
		 *            �ش� ���� ��ġ
		 * @param str
		 *            ��ĥ ����
		 */
}
