import java.io.IOException;


public class Controller {
	
	private View myGUI;
	private Model scraper;
	private String[] namesData;
	private String[] scoresData;
	private String[] timeData;
	private String vMixIP;
	private int input;
	
	Controller() {
		myGUI = new View(this);
		myGUI.setBounds(100, 100, 500, 630);
		myGUI.setResizable(false);
		myGUI.setVisible(true);
	}

	public static void main(String[] args) {
		new Controller();
	}
	
	public void newModel(String _vMixIP, int _vMixFirstInput) {
		vMixIP = _vMixIP;
		input = _vMixFirstInput-1;
		scraper = new Model();
		try {
			scraper.dataToString();
		} catch (IOException e) {
			myGUI.status.setText("Fehler beim konvertieren der Daten!");
			e.printStackTrace();
		}
		namesData = scraper.getNames();
		scoresData = scraper.getScores();
		timeData = scraper.getTime();
		myGUI.status.setText("Daten abgerufen");
		myGUI.setData(namesData, scoresData, timeData);
		loop();
	}
	
	private void loop() {
		myGUI.status.setText("Daten werden gesendet");
		for (int i=0; i<timeData.length; i++) {
			int title[] = myGUI.getDestination(namesData[i+i]);
			if (title[0]!=0) {
			try {
				makeURL(scoresData[i+i], scoresData[i+i+1], timeData[i], title);
			} catch (IOException e) {
				System.out.println("Fehler beim senden");
				e.printStackTrace();
			}
			}
		}
		myGUI.status.setText("Daten erfolgreich gesendet");
	}
	
	private void makeURL(String scoreHome, String scoreAway, String time, int title[]) throws IOException {
		scraper.sendToVmix("http://" + vMixIP + "/api/?Function=setText&Input=" + (input
				+ title[0]) + "&SelectedName=Heim_" + title[1] + "&Value=" + scoreHome);
		scraper.sendToVmix("http://" + vMixIP + "/api/?Function=setText&Input=" + (input
				+ title[0]) + "&SelectedName=Gast_" + title[1] + "&Value=" + scoreAway);
		scraper.sendToVmix("http://" + vMixIP + "/api/?Function=setText&Input=" + (input
				+ title[0]) + "&SelectedName=Zeitpunkt_" + title[1] + "&Value=" + time);
	}

}