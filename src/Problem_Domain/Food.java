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

	/**
	 * @param newName
	 * 
	 * @param newQuantity
	 * 
	 * @param newWeight
	 * 
	 * @param newCalories
	 * 
	 * @param type
	 *            freezer true, cooler false
	 * @param location
	 *            such as row, col, floor
	 * @param newExpirationDate
	 * 
	 */
	public Food(String newName, int newQuantity, int newWeight,
			int newCalories, boolean type, String location,
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
		return getLeftDays() < 0;
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

	public int getLeftDays() {
		Calendar threeDays, twoDays, oneDay;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		threeDays = Calendar.getInstance();
		twoDays = Calendar.getInstance();
		oneDay = Calendar.getInstance();

		threeDays.add(Calendar.DATE, 3);
		twoDays.add(Calendar.DATE, 2);
		oneDay.add(Calendar.DATE, 1);

		if (sdf.format(threeDays.getTime()).compareTo(
				sdf.format(expirationDate.getTime())) == 0)
			return 3;
		if (sdf.format(twoDays.getTime()).compareTo(
				sdf.format(expirationDate.getTime())) == 0)
			return 2;
		if (sdf.format(oneDay.getTime()).compareTo(
				sdf.format(expirationDate.getTime())) == 0)
			return 1;
		if (sdf.format(Calendar.getInstance().getTime()).compareTo(
				sdf.format(expirationDate.getTime())) == 0)
			return 0;
		if (sdf.format(Calendar.getInstance().getTime()).compareTo(
				sdf.format(expirationDate.getTime())) > 0)
			return -1;
		return 4;
	}

	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		return name + " : Quantity -> " + quantity + ", weight -> " + weight
				+ "g, calories -> " + calories + "kcal each, Location -> "
				+ (freezeType ? "Freezer" : "Cooler") + ", floor " + floor
				+ ", Inserted at " + sdf.format(insertedDate.getTime())
				+ ", Expiration Date -> "
				+ sdf.format(expirationDate.getTime()) + ", memo : " + memo;
	}
}
