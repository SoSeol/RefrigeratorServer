package Refrigerator_Server;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import OCSF.Server.ConnectionToClient;
import Problem_Domain.*;

public class RefrigeratorServer extends OCSF.Server.AbstractServer {
	private RefrigeratorSystem sys;
	private static final int DEFAULT_PORT = 31337;
	private final static String CLIENT_INFO_CONNECTINFO = "connectInfo";
	private final static String CLIENT_INFO_LOGGEDON = "loggedOn";
	private final static String CLIENT_INFO_USERID = "userID";

	private enum ClientOrder {
		LOGIN, FOOD_MODIFY, FOOD_DELETE, FOOD_REGISTER, FOOD_SEARCH, FOOD_SHOW, USER_MODIFY, USER_DELETE, USER_REGISTER, USER_SHOW, USER_INFO, MSG_SHOW, MSG_DELETEOLD, MSG_MEMO;
	}

	public RefrigeratorServer(int port) {
		super(port);
		sys = new RefrigeratorSystem();
	}

	// get command message from client then go to handleclientorder method
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		System.out.println((String) msg);
		String[] recieved = ((String) msg).split("_");
		boolean writeFlag = false;
		switch (recieved[0]) {
		case "LOGIN":
			handleClientOrder(ClientOrder.LOGIN, recieved, client);
			break;
		case "FOOD":
			switch (recieved[1]) {
			case "MODIFY":
				handleClientOrder(ClientOrder.FOOD_MODIFY, recieved, client);
				writeFlag = true;
				break;
			case "DELETE":
				handleClientOrder(ClientOrder.FOOD_DELETE, recieved, client);
				writeFlag = true;
				break;
			case "REGISTER":
				handleClientOrder(ClientOrder.FOOD_REGISTER, recieved, client);
				writeFlag = true;
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
			if (writeFlag) {
				sys.writeFood();
				sys.writeMessage();
			}
			break;
		case "USER":
			switch (recieved[1]) {
			case "MODIFY":
				handleClientOrder(ClientOrder.USER_MODIFY, recieved, client);
				writeFlag = true;
				break;
			case "DELETE":
				handleClientOrder(ClientOrder.USER_DELETE, recieved, client);
				writeFlag = true;
				break;
			case "REGISTER":
				handleClientOrder(ClientOrder.USER_REGISTER, recieved, client);
				writeFlag = true;
				break;
			case "SHOW":
				handleClientOrder(ClientOrder.USER_SHOW, recieved, client);
				break;
			case "INFO":
				handleClientOrder(ClientOrder.USER_INFO, recieved, client);
				writeFlag = true;
				break;
			default:
				sendUnknownCommandError(client);
			}
			if (writeFlag) {
				sys.writeFood();
				sys.writeMessage();

			}
			break;
		case "MSG":
			if (recieved[1].compareTo("SHOW") == 0)
				handleClientOrder(ClientOrder.MSG_SHOW, recieved, client);
			else if (recieved[1].compareTo("DELETEOLD") == 0) {
				handleClientOrder(ClientOrder.MSG_DELETEOLD, recieved, client);
				sys.writeMessage();
				writeFlag = true;
			} else if (recieved[1].equals("MEMO")) {
				handleClientOrder(ClientOrder.MSG_MEMO, recieved, client);
				sys.writeMessage();
				writeFlag = true;
			} else
				sendUnknownCommandError(client);
			if (writeFlag) {
				sys.writeFood();
				sys.writeMessage();

			}
			break;
		default:
			sendUnknownCommandError(client);
		}
	}

	// process client order
	private void handleClientOrder(ClientOrder order, String[] recieved,
			ConnectionToClient client) {
		int index;
		boolean result;
		User currentUser = null;
		if ((String) client.getInfo(CLIENT_INFO_USERID) != null)
			currentUser = sys.getUserList().checkID(
					(String) client.getInfo(CLIENT_INFO_USERID));

		switch (order) {
		case FOOD_DELETE:
			if ((boolean) client.getInfo(CLIENT_INFO_LOGGEDON) == false)
				sendNotLoggedOnError(client);
			index = Integer.parseInt(recieved[2]) - 1;
			result = currentUser.deleteFood(index, sys.getFoodList(),
					sys.getMessageList());
			sendResult(order.toString(), result, client);
			break;
		case FOOD_MODIFY:
			if ((boolean) client.getInfo(CLIENT_INFO_LOGGEDON) == false)
				sendNotLoggedOnError(client);
			index = Integer.parseInt(recieved[2]) - 1;
			FoodEditType fEditType;
			switch (recieved[3]) {
			case "freezeType":
				fEditType = FoodEditType.FreezerCooler;
				break;
			case "quantity":
				fEditType = FoodEditType.Quantity;
				break;
			case "weight":
				fEditType = FoodEditType.Weight;
				break;
			case "floor":
				fEditType = FoodEditType.Location;
				break;
			case "calories":
				fEditType = FoodEditType.Calories;
				break;
			case "memo":
				fEditType = FoodEditType.Memo;
				break;
			default:
				sendUnknownCommandError(client);
				return;
			}
			result = currentUser.modifyFood(fEditType, index, recieved[4],
					sys.getFoodList(), sys.getMessageList());
			sendResult(order.toString(), result, client);
			break;
		case FOOD_REGISTER:
			if ((boolean) client.getInfo(CLIENT_INFO_LOGGEDON) == false)
				sendNotLoggedOnError(client);
			boolean bFreezer;
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			if (recieved[6].compareTo("2") == 0)
				bFreezer = true;
			else
				bFreezer = false;

			try {
				cal.setTime(formatter.parse(recieved[8]));
			} catch (ParseException e) {
				try {
					client.sendToClient("CANNOT_PARSE_DATE");
					return;
				} catch (IOException ioe) {
				}
			}

			if (recieved.length > 9)
				result = currentUser.registerFood(recieved[2],
						Integer.parseInt(recieved[3]),
						Integer.parseInt(recieved[4]),
						Integer.parseInt(recieved[5]), bFreezer, recieved[7],
						cal, recieved[9], sys.getFoodList(),
						sys.getMessageList());
			else
				result = currentUser
						.registerFood(recieved[2],
								Integer.parseInt(recieved[3]),
								Integer.parseInt(recieved[4]),
								Integer.parseInt(recieved[5]), bFreezer,
								recieved[7], cal, "No memo", sys.getFoodList(),
								sys.getMessageList());
			sendResult(order.toString(), result, client);
			break;
		case FOOD_SEARCH:
			if ((boolean) client.getInfo(CLIENT_INFO_LOGGEDON) == false)
				sendNotLoggedOnError(client);
			try {
				String tempStr = sys.getFoodList().searchList(recieved[2]);
				if (tempStr == null) {
					client.sendToClient(order + "_Food not found.");
					return;
				}
				String msg = order + "_" + tempStr;
				client.sendToClient(msg);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			break;
		case FOOD_SHOW:
			if ((boolean) client.getInfo(CLIENT_INFO_LOGGEDON) == false)
				sendNotLoggedOnError(client);
			try {
				String msg = "FOOD_" + sys.getFoodList().showList();
				client.sendToClient(msg);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			break;
		case MSG_SHOW:
			try {
				client.sendToClient("MSG_SHOW_"
						+ sys.getMessageList().showList());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			break;
		case MSG_MEMO:
			Message newMemo;
			newMemo = new Message("Memo " + recieved[2] + " stored by "
					+ currentUser.getName(), currentUser.getID());
			sys.getMessageList().add(newMemo);
			sendResult(order.toString(), true, client);
			break;
		case LOGIN:
			for (Thread t : this.getClientConnections()) {
				ConnectionToClient c = (ConnectionToClient) t;
				if (((boolean) c.getInfo(CLIENT_INFO_LOGGEDON)) == true
						&& ((String) c.getInfo(CLIENT_INFO_USERID))
								.compareTo(recieved[1]) == 0) {
					try {
						client.sendToClient("ALREADY_LOGGED_ON");
						return;
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				}
			}

			currentUser = sys.getUserList().checkID(recieved[1]);
			if (currentUser == null) {
				sendResult("LOGIN", false, client);
				return;
			}
			result = currentUser.checkPassword(recieved[2]);

			if (result == false) {
				sendResult("LOGIN", false, client);
				return;
			}

			try {
				StringBuffer buf = new StringBuffer();
				buf.append("LOGIN_TRUE_");
				if (currentUser instanceof Administrator)
					buf.append("ADMINISTRATOR");
				else
					buf.append("NORMALUSER");
				buf.append("_");
				buf.append(currentUser.getName());
				client.sendToClient(buf.toString());
			} catch (IOException ioe) {
			}
			client.setInfo(CLIENT_INFO_USERID, recieved[1]);
			client.setInfo(CLIENT_INFO_LOGGEDON, true);
			System.out.println("Client " + client + " logged on as ID "
					+ recieved[1] + '.');
			break;
		case MSG_DELETEOLD:
			if ((boolean) client.getInfo(CLIENT_INFO_LOGGEDON) == false)
				sendNotLoggedOnError(client);
			if (currentUser instanceof NormalUser) {
				sendPrevilegeError(client);
				return;
			}
			((Administrator) currentUser).deleteOldMessages(sys
					.getMessageList());

			sendResult("MSG_DELETE", true, client);
			break;
		case USER_DELETE:
			if ((boolean) client.getInfo(CLIENT_INFO_LOGGEDON) == false)
				sendNotLoggedOnError(client);
			if (currentUser instanceof NormalUser) {
				sendPrevilegeError(client);
				return;
			}

			index = Integer.parseInt(recieved[2]) - 1;
			result = ((Administrator) currentUser).deleteUser(index,
					sys.getUserList(), sys.getMessageList());
			sendResult(order.toString(), result, client);
			sys.writeUser();
			break;
		case USER_MODIFY:
			if ((boolean) client.getInfo(CLIENT_INFO_LOGGEDON) == false)
				sendNotLoggedOnError(client);
			if (currentUser instanceof NormalUser) {
				sendPrevilegeError(client);
				return;
			}
			UserEditType uEditType;
			switch (recieved[3]) {
			case "pw":
				uEditType = UserEditType.PW;
				break;
			case "name":
				uEditType = UserEditType.Name;
				break;
			default:
				sendUnknownCommandError(client);
				return;
			}
			index = Integer.parseInt(recieved[2]) - 1;
			result = ((Administrator) currentUser).modifyUser(uEditType, index,
					recieved[4], sys.getUserList(), sys.getMessageList());
			sendResult(order.toString(), result, client);
			break;
		case USER_REGISTER:
			if ((boolean) client.getInfo(CLIENT_INFO_LOGGEDON) == false)
				sendNotLoggedOnError(client);
			if (currentUser instanceof NormalUser) {
				sendPrevilegeError(client);
				return;
			}
			UserPrevilege prev;
			switch (recieved[5]) {
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
			result = ((Administrator) currentUser).registerUser(prev,
					recieved[4], recieved[2], recieved[3], sys.getUserList(),
					sys.getMessageList());
			sendResult(order.toString(), result, client);
			sys.writeUser();
			break;
		case USER_SHOW:
			if ((boolean) client.getInfo(CLIENT_INFO_LOGGEDON) == false)
				sendNotLoggedOnError(client);
			if (currentUser instanceof NormalUser) {
				sendPrevilegeError(client);
				return;
			}
			try {
				String msg = "USER_LIST_" + sys.getUserList().showList();
				client.sendToClient(msg);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			break;
		case USER_INFO:
			if ((boolean) client.getInfo(CLIENT_INFO_LOGGEDON) == false)
				sendNotLoggedOnError(client);

			currentUser = sys.getUserList().checkID(recieved[2]);
			switch (recieved[3]) {
			case "pw":
				currentUser.setPW(recieved[4]);
				break;
			case "name":
				currentUser.setName(recieved[4]);
				break;
			default:
				sendUnknownCommandError(client);
				return;
			}
			result = sys.getUserList().updateList(UpdateUserAction.EDIT,
					currentUser, currentUser.getID(), sys.getMessageList(),
					false);
			sendResult(order.toString() + '_' + currentUser.getID() + '_'
					+ currentUser.getName(), result, client);
			sys.writeUser();
			break;
		}

	}

	protected void clientConnected(ConnectionToClient client) {
		System.out.println("Client " + client + " connecteed.");
		client.setInfo(CLIENT_INFO_LOGGEDON, false);
		client.setInfo(CLIENT_INFO_CONNECTINFO, client.toString());
	}

	private void sendNotLoggedOnError(ConnectionToClient client) {
		try {
			client.sendToClient("NOT_LOGGED_ON");
		} catch (IOException e) {
		}
	}

	private void sendUnknownCommandError(ConnectionToClient client) {
		try {
			client.sendToClient("UNKNOWN_COMMAND");
		} catch (IOException e) {
		}
	}

	private void sendPrevilegeError(ConnectionToClient client) {
		try {
			client.sendToClient("NOT_ADMIN");
		} catch (IOException e) {
		}
	}

	// sent to client
	private void sendResult(String type, boolean result,
			ConnectionToClient client) {
		String msg = type + (result ? "_TRUE" : "_FALSE");
		try {
			client.sendToClient(msg);
		} catch (IOException ioe) {
		}
	}

	synchronized protected void clientException(ConnectionToClient client,
			Throwable exception) {
		try {
			client.close();
			System.out.println("Client "
					+ client.getInfo(CLIENT_INFO_CONNECTINFO)
					+ " disconnected.");
		} catch (IOException e) {
		}
	}

	public static void main(String[] args) {
		int port = 0;
		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception e) {
			port = DEFAULT_PORT;
		}

		RefrigeratorServer server = new RefrigeratorServer(port);

		try {
			System.out.println("Server is almost ready...");
			server.listen();
		} catch (Exception ex) {
			System.err.println("ERROR - Could not listen for clients!");
		}
	}
}
