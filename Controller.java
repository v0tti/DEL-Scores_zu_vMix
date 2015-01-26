import java.io.IOException;


public class Controller {
	
	private View myGUI;
	private Model myModel;
	private String[][] namesData;
	private String[][] scoresData;
	private String[] timeData;
	private String vMixIP = "172.16.6.149:8088";
	private int firstInput = 2;
	
	public Controller() {
		myModel = new Model();
		myGUI = new View(this);
		myGUI.setBounds(100, 100, 500, 650);
		myGUI.setResizable(false);
		myGUI.setVisible(true);
	}

	public static void main(String[] args) {
		new Controller();
	}
	
	public void setIpAdress(String _vMixIP) {
		vMixIP = _vMixIP;
	}
	
	public void setFirstInput(int _vMixFirstInput) {
		firstInput = _vMixFirstInput;
	}
	
	public void getData() {
		try {
			setStatus("Daten werden abgerufen");
			myModel.loadData();
		} catch (IOException e) {
			setStatus("Fehler beim abrufen der Daten!");
		}
		namesData = myModel.getNames();
		scoresData = myModel.getScores();
		timeData = myModel.getTime();
		setStatus("Daten erfolgreich abgerufen");
		myGUI.setData(namesData, scoresData, timeData);
	}

	public void sendData() {
		setStatus("Daten werden gesendet");
		for (int i=0; i<timeData.length; i++) {
			int title[] = myGUI.getDestination(namesData[i][0]);
			if (title[0]!=0) {
				try {
					makeURL(scoresData[i][0], scoresData[i][1], timeData[i], title);
				} catch (IOException e) {
					setStatus("Fehler beim senden!");
				}
			}
		}
		setStatus("Daten gesendet");
	}

	public void setStatus(String _status) {
		myGUI.status.append(_status + "\n");
	}
	
	private void makeURL(String scoreHome, String scoreAway, String time, int title[]) throws IOException {
		myModel.sendToVmix("http://" + vMixIP + "/api/?Function=setText&Input=" + (firstInput
				+ title[0] - 1) + "&SelectedName=Heim_" + title[1] + "&Value=" + scoreHome);
		myModel.sendToVmix("http://" + vMixIP + "/api/?Function=setText&Input=" + (firstInput
				+ title[0] - 1) + "&SelectedName=Gast_" + title[1] + "&Value=" + scoreAway);
		myModel.sendToVmix("http://" + vMixIP + "/api/?Function=setText&Input=" + (firstInput
				+ title[0] - 1) + "&SelectedName=Zeitpunkt_" + title[1] + "&Value=" + time);
	}

}