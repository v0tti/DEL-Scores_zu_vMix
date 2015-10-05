import java.io.IOException;
import java.util.Date;


public class Controller {

	public static void main(String[] args) {
		new Controller();
	}

	private View myGUI;
	private Model myModel;
	private String[][] namesData;
	private String[][] scoresData;
	private String[] timeData;
	private String vMixIP = "172.16.6.149:8088";
	private int firstInput = 2;
	private AutomaticUploader myAutomaticUploader;

	public Controller() {
		myModel = new Model();
		myGUI = new View(this);
		myGUI.setBounds(100, 100, 500, 650);
		myGUI.setResizable(false);
		myGUI.setVisible(true);
		getData();
	}

	/**
	 * Sets the current vMix IP-Address
	 * @param _vMixIP String with th vMix IP-Adress and Port.
	 */
	public void setIpAddress(String _vMixIP) {
		vMixIP = _vMixIP;
	}

	/**
	 * Sets the current input number of the first .xaml title in vMix
	 * @param _vMixFirstInput Integer with the number of the first input
	 */
	public void setFirstInput(int _vMixFirstInput) {
		firstInput = _vMixFirstInput;
	}

	public void getData() {
		myGUI.setStatus(new Date().toString());
		try {
			myGUI.setStatus("Daten werden abgerufen");
			myModel.loadData();
		} catch (IOException e) {
			myGUI.setStatus("Fehler beim abrufen der Daten!");
		}
		namesData = myModel.getNames();
		scoresData = myModel.getScores();
		timeData = myModel.getTime();
		myGUI.setStatus("Daten abgerufen");
		myGUI.setData(namesData, scoresData, timeData);
	}

	public void sendData() {
		myGUI.setStatus("Daten werden gesendet");
		for (int i=0; i<timeData.length; i++) {
			int title[] = myGUI.getDestination(namesData[i][0]);
			if (title[0]!=0) {
				try {
					makeURL(scoresData[i][0], scoresData[i][1], timeData[i], title);
				} catch (IOException e) {
					myGUI.setStatus("Fehler beim senden! (Spiel " + title[0] + "." + title[1]+ ")");
				}
			}
		}
		myGUI.setStatus("Daten gesendet");
	}

	public void enableAutomaticUpload(boolean enable) {
		if (enable) {
			myAutomaticUploader = new AutomaticUploader(this, myGUI);
			myAutomaticUploader.start();
		} else {
			myAutomaticUploader.interrupt();
		}
	}

	private void makeURL(String scoreHome, String scoreAway, String time, int title[]) throws IOException {
		myModel.sendToVmix("http://" + vMixIP + "/api/?Function=setText&Input=" + (firstInput
				+ title[0] - 1) + "&SelectedName=Heim_" + title[1] + "&Value=" + scoreHome);
		myModel.sendToVmix("http://" + vMixIP + "/api/?Function=setText&Input=" + (firstInput
				+ title[0] - 1) + "&SelectedName=Gast_" + title[1] + "&Value=" + scoreAway);
		myModel.sendToVmix("http://" + vMixIP + "/api/?Function=setText&Input=" + (firstInput
				+ title[0] - 1) + "&SelectedName=Zeit_" + title[1] + "&Value=" + time);
	}

}