
public class Controller {
	
	private View myGUI;
	private Model scraper;
	
	Controller() {
		myGUI = new View(this);
		myGUI.setBounds(100, 100, 500, 630);
		myGUI.setResizable(false);
		myGUI.setVisible(true);
	}

	public static void main(String[] args) {
		new Controller();
	}

}
