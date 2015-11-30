package Problem_Domain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;

public class User implements java.io.Serializable
{
	private static final long serialVersionUID = -6717215299669248946L;
	MessageDigest hasher;		//�޼��� ��ȣȭ ���� ����
	private String name;
	private String ID;
	private byte[] PW;
	
	
	/**
	 * ����� ������
	 * @param newName ����� �̸�
	 * @param newID ����� ID
	 * @param newPW ����� ��й�ȣ
	 * @param prev 
	 */
	protected User(String newName, String newID, String newPW)
	{
		try
		{
			// ��ȣȭ �ʱ�ȭ.
			// �ڼ��� ������� https://docs.oracle.com/javase/8/docs/api/java/security/MessageDigest.html ���� �ٶ�.
			hasher = MessageDigest.getInstance("SHA-256");
			name = newName;
			ID = newID;
			PW = hasher.digest(newPW.getBytes());
		}
		catch (NoSuchAlgorithmException e)	{ }
	}
	
	/**
	 * ���� �ٲ� ��й�ȣ ���ڿ��� �޾Ƽ� ����.<p>
	 * ��й�ȣ�� �ٲ� �� ���� üũ�� ���� ����
	 * @param newPW �� ��й�ȣ ���ڿ�
	 */
	public void changePassword(String newPW) { PW = hasher.digest(newPW.getBytes()); }
	public void setName(String name){	this.name = name;}
	public void setID(String ID){ this.ID = ID;}
	public void setPW(String PW){
		this.PW = hasher.digest(PW.getBytes());
	}
	public String getName() { return name; }
	public String getID() { return ID; }
	public boolean checkPassword(String inputPW) {
		return Arrays.equals(PW, hasher.digest(inputPW.getBytes()));
	}
	
	
	@Override
	public String toString()
	{
		return name+" ("+ID+")";		
	}
	
	/**
	 * ���� ���
	 * @param newName
	 * @param newQuantity
	 * @param newWeight
	 * @param newCalories
	 * @param bFreezer
	 * @param row
	 * @param col
	 * @param newExpirationDate
	 */
	
	public boolean registerFood(String newName, int newQuantity, int newWeight, int newCalories, boolean bFreezer, String newfloor, Calendar newExpirationDate, FoodList fList, MessageList mList)
	{
		if(newName == null || newfloor == null) return false;
		Food newfood = new Food(newName, newQuantity, newWeight, newCalories, bFreezer, newfloor , newExpirationDate);
		if(newfood.isProhibited(fList))
		{
			WarningMessage msg = new WarningMessage(name + "tried to put in forbidden item \"" + newfood.getName() + '\"', "System");
			mList.add(msg);
			return false;
		}
		
		return fList.updateList(UpdateUserAction.REGISTER, null, newfood, this.getName(), mList);
	}
	
	
	/**
	 * ���� ����
	 * @param idx �����ϰ��� �ϴ� ���� �ε���
	 */
	public boolean deleteFood(int idx, FoodList fList, MessageList mList)
	{
		return fList.updateList(UpdateUserAction.REGISTER, null, fList.elementAt(idx), this.getName(), mList);
	}
	
	public boolean modifyFood(FoodEditType type, int idx, String editData, FoodList fList, MessageList mList)
	{
		Food modify_food = fList.elementAt(idx);
		if(editData == null) return false;
		switch(type)
		{
			case FreezerCooler:
				switch(editData)
				{
				case "Freezer":
					if(!modify_food.getFreezeType()) modify_food.setFreezeType(!modify_food.getFreezeType());
					break;
				case "Cooler":
					if(modify_food.getFreezeType()) modify_food.setFreezeType(!modify_food.getFreezeType());
					break;
				}
				break;
			case Location:
				modify_food.setFloor(editData);
				break;
			case Quantity:
				modify_food.setQuantity(Integer.parseInt(editData));
				break;
			case Weight:
				modify_food.setWeight(Integer.parseInt(editData));
				break;
			case Calories:
				modify_food.setCalories(Integer.parseInt(editData));
		}
		return fList.updateList(UpdateUserAction.EDIT, type, modify_food, this.getName(), mList);
	}
	
	/**
	 * Ǫ�� ����Ʈ ���
	 */
	public String searchItem(FoodList fList)
	{
		return fList.showList();
	}
	
	public String searchItem(String str, FoodList fList)
	{
		return fList.searchList(str);
	}
	
	/**
	 * ��ĥ ���� ����, ����Ʈ ��ġ, ��ĥ ������ �޾� �ش� ���� ���� ����
	 * @param act ����.  FreezerCooler / Location / Quantity / Weight
	 * @param idx �ش� ���� ��ġ
	 * @param str ��ĥ ����
	 */	
}
