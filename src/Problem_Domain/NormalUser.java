package Problem_Domain;

public class NormalUser extends User {

	private static final long serialVersionUID = 4018198761142196430L;

	public NormalUser(String newName, String newID, String newPW) {
		super(newName, newID, newPW);

	}

	public String toString() {
		return "Normal User, ID : " + getID() + ", Name : " + getName();
	}
}
