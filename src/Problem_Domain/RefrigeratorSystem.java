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
	private static UserList ulist;
	private static FoodList flist;
	private static MessageList mlist;

	public RefrigeratorSystem()
	{
		ulist = readUser();
		flist = readFood();
		mlist = readMessage();
		flist.checkExpired(mlist);
		flist.checkProhibited(mlist);
		writeMessage(mlist);
	}
	
	/* write : serialization
	 * read : deserialzation
	 */
	private static UserList readUser() {
		
		UserList userlist = null;
		FileInputStream fis;
		try {
			fis = new FileInputStream("UserList.ser");
			BufferedInputStream bis = new BufferedInputStream(fis);
			ObjectInputStream in = new ObjectInputStream(bis);
			userlist = (UserList)in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			userlist = new UserList();
			//e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		}
		return userlist;
	}
	
	synchronized public static void writeUser()
	{
		writeUser(ulist);
	}
	
	synchronized private static void writeUser(UserList userlist) {
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
	
	private static FoodList readFood() {
		
		FoodList foodlist = null;
		FileInputStream fis;
		try {
			fis = new FileInputStream("FoodList.ser");
			BufferedInputStream bis = new BufferedInputStream(fis);
			ObjectInputStream in = new ObjectInputStream(bis);
			
			foodlist = (FoodList)in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			foodlist = new FoodList();
			//e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		}
		return foodlist;
	}
	
	synchronized public static void writeFood()
	{
		writeFood(flist);
	}
	
	
	synchronized private static void writeFood(FoodList foodlist) {
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
	
	private static MessageList readMessage() {
		
		MessageList messagelist = null;
		FileInputStream fis;
		try {
			fis = new FileInputStream("MessageList.ser");
			BufferedInputStream bis = new BufferedInputStream(fis);
			ObjectInputStream in = new ObjectInputStream(bis);
			
			messagelist = (MessageList)in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			messagelist = new MessageList();
			//e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		}
		return messagelist;
	}
	
	synchronized public static void writeMessage()
	{
		writeMessage(mlist);
	}
	
	synchronized private static void writeMessage(MessageList messagelist) {
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
		
	public static UserList getUserList() { return ulist; }
	public static FoodList getFoodList() { return flist; }
	public static MessageList getMessageList() { return mlist; }	
	
}
