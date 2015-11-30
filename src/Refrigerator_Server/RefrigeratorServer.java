package Refrigerator_Server;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import OCSF.Server.ConnectionToClient;
import Problem_Domain.*;

public class RefrigeratorServer extends OCSF.Server.AbstractServer
{
	private RefrigeratorSystem sys;
	private static final int DEFAULT_PORT = 31337;
	private enum ClientOrder
	{
		LOGIN, GET_FOOD, GET_MSG, FOOD_MODIFY, FOOD_DELETE, FOOD_REGISTER, FOOD_SEARCH, FOOD_SHOW,
		USER_MODIFY, USER_DELETE, USER_REGISTER, USER_SHOW, MSG_SHOW, MSG_DELETEOLD;
	}
	
	public RefrigeratorServer(int port)
	{
		super(port);
		sys = new RefrigeratorSystem();
	}
	
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client)
	{
		String[] recieved = ((String)msg).split("_");
		switch(recieved[0])
		{
		case "GET":
			if(recieved[1].compareTo("FOOD") == 0)
				handleClientOrder(ClientOrder.GET_FOOD, recieved, client);
			else if(recieved[1].compareTo("USER") == 0)
				handleClientOrder(ClientOrder.GET_MSG, recieved, client);
			else
				sendUnknownCommandError(client);		
			break;
		case "LOGIN":
			handleClientOrder(ClientOrder.LOGIN, recieved, client);
			break;
		case "FOOD":
			switch(recieved[1])
			{
			case "MODIFY":
				handleClientOrder(ClientOrder.FOOD_MODIFY, recieved, client);
				sys.writeFood();
				sys.writeMessage();
				break;
			case "DELETE":
				handleClientOrder(ClientOrder.FOOD_DELETE, recieved, client);
				sys.writeFood();
				sys.writeMessage();
				break;
			case "REGISTER":
				handleClientOrder(ClientOrder.FOOD_REGISTER, recieved, client);
				sys.writeFood();
				sys.writeMessage();
				break;
			case "SEARCH":
				handleClientOrder(ClientOrder.FOOD_SEARCH, recieved, client);
				break;
			case "SHOW":
				handleClientOrder(ClientOrder.FOOD_SHOW, recieved, client);
				break;
			default:
				sendUnknownCommandError(client);		
			}
			break;
		case "USER":
			switch(recieved[1])
			{
			case "MODIFY":
				handleClientOrder(ClientOrder.USER_MODIFY, recieved, client);
				sys.writeUser();
				sys.writeMessage();
				break;
			case "DELETE":
				handleClientOrder(ClientOrder.USER_DELETE, recieved, client);
				sys.writeUser();
				sys.writeMessage();
				break;
			case "REGISTER":
				handleClientOrder(ClientOrder.USER_REGISTER, recieved, client);
				sys.writeUser();
				sys.writeMessage();
				break;
			case "SHOW":
				handleClientOrder(ClientOrder.USER_SHOW, recieved, client);
				break;
			default:
				sendUnknownCommandError(client);		
			}
		case "MSG":
			if(recieved[1].compareTo("SHOW") == 0)
				handleClientOrder(ClientOrder.MSG_SHOW, recieved, client);
			else if(recieved[1].compareTo("DELETEOLD") == 0)
			{
				handleClientOrder(ClientOrder.MSG_DELETEOLD, recieved, client);
				sys.writeMessage();
			}
			else
				sendUnknownCommandError(client);		
			break;
		default:
			sendUnknownCommandError(client);		
		}
	}
	
	/**
	 * �ʱ⿡ �α����� ���� �ʾ����Ƿ� Ŭ���̾�Ʈ�� loggedOn �׸��� false�� ����
	 */
	protected void clientConnected(ConnectionToClient client)
	{
		client.setInfo("loggedOn", false);
	}
	
	/**
	 * �α����� ���� ���� ���¿��� �α����� ������ �ٸ� ����� �����Ϸ��� �� ��
	 * <code>NOT_LOGGED_ON</code> �޼����� ������
	 * @param client
	 */
	private void sendNotLoggedOnError(ConnectionToClient client)
	{
		try
		{
			client.sendToClient("NOT_LOGGED_ON");
		} catch (IOException e) {}			
	}
	
	/**
	 * �̻��� ����� ������ ��� <code>UNKNOWN_CODE</code> �޼����� ������
	 * @param client
	 */
	private void sendUnknownCommandError(ConnectionToClient client)
	{
		try
		{
			client.sendToClient("UNKNOWN_COMMAND");
		} catch (IOException e) {}			
	}
	
	/**
	 * �����ڰ� �ƴ� ����ڰ� ����� �߰� / ���� / ������ �Ϸ��� �� �� 
	 * <code>NOT_ADMIN</code> �޼��� ����
	 * @param client
	 */
	private void sendPrevilegeError(ConnectionToClient client)
	{
		try
		{
			client.sendToClient("NOT_ADMIN");
		} catch (IOException e) {}			
	}
	
	/**
	 * ��� ��ɾ ���� ��� �޼��� ����
	 * @param type ��ɾ� ����
	 * @param result ��� 
	 * @param client �ش� �����
	 */
	private void sendResult(String type, boolean result, ConnectionToClient client)
	{
		String msg = type + (result ? "_TRUE" : "_FALSE");
		try
		{
			client.sendToClient(msg);
		} catch(IOException ioe) {}
	}
	
	/**
	 * ��� ó��
	 * @param order ��� ����
	 * @param recieved ��ūȭ�� ���ڿ� ���
	 * @param client �ش� �����
	 */
	private void handleClientOrder(ClientOrder order, String[] recieved, ConnectionToClient client)
	{
		int index;
		boolean result;
		User currentUser = null;
		if((String)client.getInfo("userID") != null)
			currentUser = sys.getUserList().checkID((String)client.getInfo("userID"));
		
		switch(order)
		{
		case FOOD_DELETE:
			if((boolean)client.getInfo("loggedOn") == false)
				sendNotLoggedOnError(client);		
			index = Integer.parseInt(recieved[2]);
			result = currentUser.deleteFood(index, sys.getFoodList(), sys.getMessageList());
			sendResult(order.toString(), result, client);
			break;
		case FOOD_MODIFY:
			if((boolean)client.getInfo("loggedOn") == false)
				sendNotLoggedOnError(client);		
			index = Integer.parseInt(recieved[2]);
			FoodEditType fEditType;
			switch(recieved[3])
			{
			case "FREEZER":
				fEditType = FoodEditType.FreezerCooler;
				break;
			case "QUANTITY":
				fEditType = FoodEditType.Quantity;
				break;
			case "WEIGHT":
				fEditType = FoodEditType.Weight;
				break;
			case "LOCATION":
				fEditType = FoodEditType.Location;
				break;
			case "CALORIES":
				fEditType = FoodEditType.Calories;
				break;
			default:
				sendUnknownCommandError(client);
				return;
			}
			result = currentUser.modifyFood(fEditType, index, recieved[4], sys.getFoodList(), sys.getMessageList());
			sendResult(order.toString(), result, client);
			break;
		case FOOD_REGISTER:
			if((boolean)client.getInfo("loggedOn") == false)
				sendNotLoggedOnError(client);
			boolean bFreezer;
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			if(recieved[6].compareTo("2") == 0) bFreezer = true;
			else bFreezer = false;
			
			// ��¥�� '��/��/�� ��:��' �������� �Ľ��� Calendar Ÿ������ ��ȯ
			// �����ϸ� 'CANNOT_PARSE_DATE' �޼��� ����
			try
			{
				cal.setTime(formatter.parse(recieved[8]));
			} catch (ParseException e)
			{
				try
				{
					client.sendToClient("CANNOT_PARSE_DATE");
					return;
				}
				catch (IOException ioe) {}
			}
			result = currentUser.registerFood(recieved[2], Integer.parseInt(recieved[3]), Integer.parseInt(recieved[4]), Integer.parseInt(recieved[5]), bFreezer, recieved[7], cal, sys.getFoodList(), sys.getMessageList());
			sendResult(order.toString(), result, client);		
			break;
		case FOOD_SEARCH:
			if((boolean)client.getInfo("loggedOn") == false)
				sendNotLoggedOnError(client);
			try
			{
				String msg = order + "_" + currentUser.searchItem(recieved[2], sys.getFoodList());
				client.sendToClient(msg);
			} catch (IOException ioe) {}
			break;
		case GET_FOOD:
		case FOOD_SHOW:
			if((boolean)client.getInfo("loggedOn") == false)
				sendNotLoggedOnError(client);
			try
			{
				String msg = "FOOD_" + currentUser.searchItem(sys.getFoodList());
				client.sendToClient(msg);
			} catch (IOException ioe) {}		
			break;
		case MSG_SHOW:
		case GET_MSG:
			try
			{
				String msg = "MSG_SHOW_" + sys.getMessageList().showList();
				client.sendToClient(msg);
			} catch (IOException ioe) {}	
			break;
		case LOGIN:
			//�̹� �α����Ͽ����� ALREADY_LOGGED_ON �޼����� ����
			if((boolean)client.getInfo("loggedOn") == true)
			{
				try
				{
					client.sendToClient("ALREADY_LOGGED_ON");
					return;
				}
				catch (IOException ioe) {}
			}
			currentUser = sys.getUserList().checkID(recieved[1]);
			if(currentUser == null)
			{
				sendResult("LOGIN", false, client);
				return;
			}
			result = currentUser.checkPassword(recieved[2]);
			
			if(result == false)
			{
				sendResult("LOGIN", false, client);
				return;
			}
			
			try
			{
				if(currentUser instanceof Administrator)
					client.sendToClient("LOGIN_TRUE_ADMINISTRATOR");
				else
					client.sendToClient("LOGIN_TRUE_NORMALUSER");
			} catch(IOException ioe) {}
			client.setInfo("userID", recieved[1]);
			client.setInfo("loggedOn", true);
			break;
		case MSG_DELETEOLD:
			if((boolean)client.getInfo("loggedOn") == false)
				sendNotLoggedOnError(client);
			if(currentUser instanceof NormalUser)
			{
				sendPrevilegeError(client);
				return;
			}
			((Administrator)currentUser).deleteOldMessages(sys.getMessageList());
			break;
		case USER_DELETE:
			if((boolean)client.getInfo("loggedOn") == false)
				sendNotLoggedOnError(client);
			if(currentUser instanceof NormalUser)
			{
				sendPrevilegeError(client);
				return;
			}
			
			index = Integer.parseInt(recieved[2]);
			result = ((Administrator)currentUser).deleteUser(index, sys.getUserList(), sys.getMessageList());
			sendResult(order.toString(), result, client);
			break;
		case USER_MODIFY:
			if((boolean)client.getInfo("loggedOn") == false)
				sendNotLoggedOnError(client);	
			if(currentUser instanceof NormalUser)
			{
				sendPrevilegeError(client);
				return;
			}
			UserEditType uEditType;
			switch(recieved[3])
			{
			case "ID":
				uEditType = UserEditType.ID;
				break;
			case "PW":
				uEditType = UserEditType.PW;
				break;
			case "NAME":
				uEditType = UserEditType.Name;
				break;
			default:
				sendUnknownCommandError(client);
				return;
			}
			result = ((Administrator)currentUser).modifyUser(uEditType, recieved[2], recieved[4], sys.getUserList(), sys.getMessageList());
			sendResult(order.toString(), result, client);
			break;
		case USER_REGISTER:
			if((boolean)client.getInfo("loggedOn") == false)
				sendNotLoggedOnError(client);	
			if(currentUser instanceof NormalUser)
			{
				sendPrevilegeError(client);
				return;
			}
			UserPrevilege prev;
			switch(recieved[5])
			{
			case "NORMAL":
				prev = UserPrevilege.Normal;
				break;
			case "ADMIN":
				prev = UserPrevilege.Administrator;
				break;
			default:
				sendUnknownCommandError(client);
				return;
			}
			result = ((Administrator)currentUser).registerUser(prev, recieved[4], recieved[2], recieved[3], sys.getUserList(), sys.getMessageList());
			break;
		case USER_SHOW:
			if((boolean)client.getInfo("loggedOn") == false)
				sendNotLoggedOnError(client);	
			if(currentUser instanceof NormalUser)
			{
				sendPrevilegeError(client);
				return;
			}
			try
			{
				String msg = "USER_" + sys.getUserList().showList();
				client.sendToClient(msg);
			} catch (IOException ioe) {}	
			break;			
		}
	}
	
	synchronized protected void clientException(ConnectionToClient client,
			Throwable exception) {
		System.out.println("client exception : " + exception.getMessage());
		System.err.println(exception);
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		int port = 0;
		try
		{
			port = Integer.parseInt(args[0]);
		}
		catch(Exception e)
		{
			port = DEFAULT_PORT;
		}
		
		RefrigeratorServer server = new RefrigeratorServer(port);
		System.out.println("SERVER READY");

		try {
			server.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.err.println("ERROR - Could not listen for clients!");
		}
	}
}
