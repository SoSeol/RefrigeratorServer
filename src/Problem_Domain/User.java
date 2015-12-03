package Problem_Domain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;

public class User implements java.io.Serializable {
	private static final long serialVersionUID = -6717215299669248946L;
	private final static String HASH_ALGORITHM = "SHA-256";
	private String name;
	private String ID;
	private byte[] PW;

	/**
	 * 사용자 생성자
	 * 
	 * @param newName
	 *            사용자 이름
	 * @param newID
	 *            사용자 ID
	 * @param newPW
	 *            사용자 비밀번호
	 * @param prev
	 */
	protected User(String newName, String newID, String newPW) {

		name = newName;
		ID = newID;
		try {
			PW = MessageDigest.getInstance(HASH_ALGORITHM).digest(
					newPW.getBytes());
		} catch (NoSuchAlgorithmException e) {
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public void setPW(String PW) {
		try {
			this.PW = MessageDigest.getInstance(HASH_ALGORITHM).digest(
					PW.getBytes());
		} catch (NoSuchAlgorithmException e) {
		}
	}

	public String getName() {
		return name;
	}

	public String getID() {
		return ID;
	}

	public boolean checkPassword(String inputPW) {
		try {
			return Arrays.equals(PW, MessageDigest.getInstance(HASH_ALGORITHM)
					.digest(inputPW.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			return false;
		}
	}

	@Override
	public String toString() {
		return name + " (" + ID + ")";
	}

	public boolean registerFood(String newName, int newQuantity, int newWeight,
			int newCalories, boolean bFreezer, String newfloor,
			Calendar newExpirationDate, String newMemo, FoodList fList,
			MessageList mList) {
		if (newName == null || newfloor == null)
			return false;
		Food newfood = new Food(newName, newQuantity, newWeight, newCalories,
				bFreezer, newfloor, newExpirationDate, newMemo);
		if (newfood.isProhibited(fList)) {
			WarningMessage msg = new WarningMessage(name
					+ "tried to put in forbidden item \"" + newfood.getName()
					+ '\"', "Refrigerator");
			mList.add(msg);
			return false;
		}

		return fList.updateList(UpdateUserAction.REGISTER, null, newfood,
				this.getName(), mList);
	}

	public boolean deleteFood(int idx, FoodList fList, MessageList mList) {
		return fList.updateList(UpdateUserAction.DELETE, null,
				fList.elementAt(idx), this.getName(), mList);
	}

	public boolean modifyFood(FoodEditType type, int idx, String editData,
			FoodList fList, MessageList mList) {
		Food modifyFood = fList.elementAt(idx);
		if (editData == null || modifyFood == null)
			return false;
		switch (type) {
		case FreezerCooler:
			switch (editData) {
			case "1":
				modifyFood.setFreezeType(true);
				break;
			case "2":
				modifyFood.setFreezeType(false);
				break;
			default:
				System.err.println("Unknown type\n");
				return false;
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
			System.err.println("Unknown type\n");
			return false;
		}
		return fList.updateList(UpdateUserAction.EDIT, type, modifyFood,
				this.getName(), mList);
	}

}
