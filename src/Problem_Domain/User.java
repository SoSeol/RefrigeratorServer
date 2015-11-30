package Problem_Domain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;

public class User implements java.io.Serializable
{
	private static final long serialVersionUID = -6717215299669248946L;
	MessageDigest hasher;		//메세지 암호화 위한 변수
	private String name;
	private String ID;
	private byte[] PW;
	
	
	/**
	 * 사용자 생성자
	 * @param newName 사용자 이름
	 * @param newID 사용자 ID
	 * @param newPW 사용자 비밀번호
	 * @param prev 
	 */
	protected User(String newName, String newID, String newPW)
	{
		try
		{
			// 암호화 초기화.
			// 자세한 사용방법은 https://docs.oracle.com/javase/8/docs/api/java/security/MessageDigest.html 참고 바람.
			hasher = MessageDigest.getInstance("SHA-256");
			name = newName;
			ID = newID;
			PW = hasher.digest(newPW.getBytes());
		}
		catch (NoSuchAlgorithmException e)	{ }
	}
	
	/**
	 * 새로 바꿀 비밀번호 문자열을 받아서 저장.<p>
	 * 비밀번호를 바꿀 떄 길이 체크는 하지 않음
	 * @param newPW 새 비밀번호 문자열
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
	 * 음식 등록
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
	 * 음식 삭제
	 * @param idx 삭제하고자 하는 음식 인덱스
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
	 * 푸드 리스트 출력
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
	 * 고칠 내용 종류, 리스트 위치, 고칠 내용을 받아 해당 음식 정보 수정
	 * @param act 종류.  FreezerCooler / Location / Quantity / Weight
	 * @param idx 해당 음식 위치
	 * @param str 고칠 내용
	 */	
}
