package Problem_Domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Food implements java.io.Serializable {
	private static final long serialVersionUID = -3110469111318421458L;
	private String name;
	private int quantity;
	private int weight;
	private int calories;
	private boolean freezeType;
	private String floor;
	private Calendar expirationDate;
	private Calendar insertedDate;
	private String memo;

	/* p@ 젠장!!! 메모가 없었어요ㅜㅜ */

	/**
	 * @param newName
	 *            음식 이름
	 * @param newQuantity
	 *            음식 개수
	 * @param newWeight
	 *            음식 무게
	 * @param newCalories
	 *            개수당 칼로리
	 * @param type
	 *            냉동실이면 true, 냉장실이면 false
	 * @param location
	 *            위치, 냉장고 몇 번째칸
	 * @param newExpirationDate
	 *            유통기한
	 */
	public Food(String newName, int newQuantity, int newWeight, int newCalories, boolean type, String location,
			Calendar newExpirationDate, String newMemo) {
		name = newName;
		quantity = newQuantity;
		weight = newWeight;
		calories = newCalories;
		freezeType = type;
		floor = location;
		expirationDate = newExpirationDate;
		insertedDate = Calendar.getInstance();
		memo = newMemo;
	}

	public boolean isProhibited(FoodList fList) {
		return fList.getProhibitedList().indexOf(name) != -1;
	}

	public boolean isExpired() {
		return expirationDate.before(Calendar.getInstance());
	}

	public String getName() {
		return name;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getWeight() {
		return weight;
	}

	public int getCalories() {
		return calories;
	}

	public boolean getFreezeType() {
		return freezeType;
	}

	public String getFloor() {
		return floor;
	}

	public Calendar getExpirationDate() {
		return expirationDate;
	}

	public Calendar getinsertedDate() {
		return insertedDate;
	}

	public String getMemo() {
		return memo;
	}

	public void setQuantity(int newQuantity) {
		quantity = newQuantity;
	}

	public void setWeight(int newWeight) {
		weight = newWeight;
	}

	public void setCalories(int newCalories) {
		calories = newCalories;
	}

	public void setFreezeType(boolean newFreezeType) {
		freezeType = newFreezeType;
	}

	public void setFloor(String newFloor) {
		floor = newFloor;
	}

	public void setMemo(String newMemo) {
		memo = newMemo;
	}
	
	public int getLeftDays()
	{
		Calendar threeDays, twoDays, oneDay, today;
		threeDays = Calendar.getInstance();
		twoDays = Calendar.getInstance();
		oneDay = Calendar.getInstance();
		today = Calendar.getInstance();
		
		threeDays.add(Calendar.DATE, -3);
		twoDays.add(Calendar.DATE, -2);
		oneDay.add(Calendar.DATE, -1);
		if(expirationDate.compareTo(threeDays) == 0) return 3;
		if(expirationDate.compareTo(twoDays) == 0) return 2;
		if(expirationDate.compareTo(oneDay) == 0) return 1;
		if(expirationDate.compareTo(today) > 0) return 4;
		return expirationDate.compareTo(today);
	}

	/* p@ 몇시 몇분에 넣었는지 필요할까요?, 귀찮으니 그냥 안넣는걸로? 메모는 추가하였습니다. */
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		return name + " : Quantity -> " + quantity + ", weight -> " + weight + "g, calories -> " + calories
				+ "kcal each, Location -> " + (freezeType ? "Freezer" : "Cooler") + ", floor " + floor
				+ ", Inserted at " + sdf.format(insertedDate.getTime()) + ", Expiration Date -> "
				+ sdf.format(expirationDate.getTime()) + ", memo : " + memo;
	}
}
