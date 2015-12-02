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
		LOGIN, FOOD_MODIFY, FOOD_DELETE, FOOD_REGISTER, FOOD_SEARCH, 
		FOOD_SHOW, USER_MODIFY, USER_DELETE, USER_REGISTER, USER_SHOW, USER_INFO, 
		MSG_SHOW, MSG_DELETEOLD, MSG_MEMO;
	}

	public RefrigeratorServer(int port) {
		super(port);
		sys = new RefrigeratorSystem();
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
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

	/**
	 * 명령 처리
	 * 
	 * @param order
	 *            명령 종류
	 * @param recieved
	 *            토큰화된 문자열 목록
	 * @param client
	 *            해당 사용자
	 */
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
			result = currentUser.deleteFood(index, sys.getFoodList(),sys.getMessageList());
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
			result = currentUser.modifyFood(fEditType, index, recieved[4],sys.getFoodList(), sys.getMessageList());
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

			// 날짜를 '년/월/일 시:분' 형식으로 파싱해 Calendar 타입으로 변환
			// 실패하면 'CANNOT_PARSE_DATE' 메세지 전송
			try {
				cal.setTime(formatter.parse(recieved[8]));
			} catch (ParseException e) {
				try {
					client.sendToClient("CANNOT_PARSE_DATE");
					return;
				} catch (IOException ioe) {
				}
			}
			result = currentUser.registerFood(recieved[2],
					Integer.parseInt(recieved[3]),
					Integer.parseInt(recieved[4]),
					Integer.parseInt(recieved[5]), bFreezer, recieved[7], cal,
					recieved[9], sys.getFoodList(), sys.getMessageList());
			sendResult(order.toString(), result, client);
			break;
		case FOOD_SEARCH:
			if ((boolean) client.getInfo(CLIENT_INFO_LOGGEDON) == false)
				sendNotLoggedOnError(client);
			try {
				String msg = order + "_"
						+ sys.getFoodList().searchList(recieved[2]);
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
				client.sendToClient("MSG_SHOW_" + sys.getMessageList().showList());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			break;
		case MSG_MEMO:
			index = Integer.parseInt(recieved[2]) - 1;
			sys.getFoodList().elementAt(index).setMemo(recieved[3]);
			sendResult(order.toString(), true, client);
			break;
		case LOGIN:
			// 이미 로그인하였으면 ALREADY_LOGGED_ON 메세지를 전송
			for(Thread t : this.getClientConnections())
			{
				ConnectionToClient c = (ConnectionToClient) t;
				if(((boolean) c.getInfo(CLIENT_INFO_LOGGEDON)) == true &&
						((String) c.getInfo(CLIENT_INFO_USERID)).compareTo(recieved[1]) == 0)
				{
					try
					{
						client.sendToClient("ALREADY_LOGGED_ON");
						return;
					}
					catch (IOException ioe)
					{
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
				if (currentUser instanceof Administrator)
					client.sendToClient("LOGIN_TRUE_ADMINISTRATOR");
				else
					client.sendToClient("LOGIN_TRUE_NORMALUSER");
			} catch (IOException ioe) {
			}
			client.setInfo(CLIENT_INFO_USERID, recieved[1]);
			client.setInfo(CLIENT_INFO_LOGGEDON, true);
			System.out.println("Client " + client + " logged on as ID " + recieved[1] + '.');
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
			/*
			 * p@ id 뺍니다. case "ID": uEditType = UserEditType.ID; break;
			 */
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
			result = ((Administrator) currentUser).modifyUser(uEditType,
					recieved[2], recieved[4], sys.getUserList(),
					sys.getMessageList());
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
				String msg = "USER_" + sys.getUserList().showList();
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
				currentUser.setPW(recieved[4]);
				break;
			default:
				sendUnknownCommandError(client);
				return;
			}
			result = sys.getUserList().updateList(UpdateUserAction.EDIT,
					currentUser, currentUser.getID(), sys.getMessageList());
			sendResult(order.toString() + '_' + currentUser.getID() + '_'
					+ currentUser.getName(), result, client);
			sys.writeUser();
			break;
		}

	}

	/**
	 * 초기에 로그인을 하지 않았으므로 클라이언트의 loggedOn 항목을 false로 지정
	 */
	protected void clientConnected(ConnectionToClient client) {
		System.out.println("Client " + client + " connecteed.");
		client.setInfo(CLIENT_INFO_LOGGEDON, false);
		client.setInfo(CLIENT_INFO_CONNECTINFO, client.toString());
	}

	/**
	 * 로그인을 하지 않은 상태에서 로그인을 제외한 다른 명령을 수행하려고 할 떄 <code>NOT_LOGGED_ON</code> 메세지를
	 * 전송함
	 * 
	 * @param client
	 */
	private void sendNotLoggedOnError(ConnectionToClient client) {
		try {
			client.sendToClient("NOT_LOGGED_ON");
		} catch (IOException e) {
		}
	}

	/**
	 * 이상한 명령을 보냈을 경우 <code>UNKNOWN_CODE</code> 메세지를 전송함
	 * 
	 * @param client
	 */
	private void sendUnknownCommandError(ConnectionToClient client) {
		try {
			client.sendToClient("UNKNOWN_COMMAND");
		} catch (IOException e) {
		}
	}

	/**
	 * 관리자가 아닌 사용자가 사용자 추가 / 수정 / 삭제를 하려고 할 때 <code>NOT_ADMIN</code> 메세지 전송
	 * 
	 * @param client
	 */
	private void sendPrevilegeError(ConnectionToClient client) {
		try {
			client.sendToClient("NOT_ADMIN");
		} catch (IOException e) {
		}
	}

	/**
	 * 몇몇 명령어에 대한 결과 메세지 보냄
	 * 
	 * @param type
	 *            명령어 종류
	 * @param result
	 *            결과
	 * @param client
	 *            해당 사용자
	 */
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
		//System.out.println("Client exception : " + exception.getMessage());
		try {
			client.close();
			System.out.println("Client " + client.getInfo(CLIENT_INFO_CONNECTINFO) + " disconnected.");
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
			server.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.err.println("ERROR - Could not listen for clients!");
		}
	}
}
