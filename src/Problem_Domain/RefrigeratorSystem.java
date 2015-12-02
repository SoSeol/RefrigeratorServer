package Problem_Domain;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RefrigeratorSystem {
	/*
	 * p@ static »®´Ï´Ù. private static UserList ulist; private static FoodList
	 * flist; private static MessageList mlist;
	 */

	private UserList ulist;
	private FoodList flist;
	private MessageList mlist;

	public RefrigeratorSystem() {
		ulist = readUser();
		flist = readFood();
		mlist = readMessage();
		flist.checkExpired(mlist);
		flist.checkProhibited(mlist);
		writeMessage(mlist);
	}

	/*
	 * write : serialization read : deserialzation
	 */
	// p@ private static UserList readUser() {
	private UserList readUser() {
		UserList userlist = null;
		FileInputStream fis;
		try {
			fis = new FileInputStream("UserList.ser");
			BufferedInputStream bis = new BufferedInputStream(fis);
			ObjectInputStream in = new ObjectInputStream(bis);
			userlist = (UserList) in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			userlist = new UserList();
			// e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return userlist;
	}

	// p@ synchronized public static void writeUser() {
	synchronized public void writeUser() {
		writeUser(ulist);
	}

	// p@ synchronized private static void writeUser(UserList userlist) {
	synchronized private void writeUser(UserList userlist) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("UserList.ser");
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			ObjectOutputStream out = new ObjectOutputStream(bos);

			out.writeObject(userlist);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// p@ private static FoodList readFood() {
	private FoodList readFood() {

		FoodList foodlist = null;
		FileInputStream fis;
		try {
			fis = new FileInputStream("FoodList.ser");
			BufferedInputStream bis = new BufferedInputStream(fis);
			ObjectInputStream in = new ObjectInputStream(bis);

			foodlist = (FoodList) in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			foodlist = new FoodList();
			// e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return foodlist;
	}

	// p@ synchronized public static void writeFood() {
	synchronized public void writeFood() {
		writeFood(flist);
	}

	// p@ synchronized private static void writeFood(FoodList foodlist) {
	synchronized private void writeFood(FoodList foodlist) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("FoodList.ser");
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			ObjectOutputStream out = new ObjectOutputStream(bos);

			out.writeObject(foodlist);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// p@ private static MessageList readMessage() {
	private MessageList readMessage() {

		MessageList messagelist = null;
		FileInputStream fis;
		try {
			fis = new FileInputStream("MessageList.ser");
			BufferedInputStream bis = new BufferedInputStream(fis);
			ObjectInputStream in = new ObjectInputStream(bis);

			messagelist = (MessageList) in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			messagelist = new MessageList();
			// e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return messagelist;
	}

	// p@ synchronized public static void writeMessage() {
	synchronized public void writeMessage() {
		writeMessage(mlist);
	}

	// p@ synchronized private static void writeMessage(MessageList messagelist)
	// {
	synchronized private void writeMessage(MessageList messagelist) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("MessageList.ser");
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			ObjectOutputStream out = new ObjectOutputStream(bos);

			out.writeObject(messagelist);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// p@ public static UserList getUserList() {
	public UserList getUserList() {
		return ulist;
	}

	// p@ public static FoodList getFoodList() {
	public FoodList getFoodList() {
		return flist;
	}

	// p@ public static MessageList getMessageList() {
	public MessageList getMessageList() {
		return mlist;
	}

}
