public class AutomaticUploader extends Thread {

	Controller myController;
	View GUI;

	public AutomaticUploader(Controller _myController, View _GUI) {
		myController = _myController;
		GUI = _GUI;
	}

	public void run() {
		GUI.setStatus("automatischer Upload aktiviert");
		while(true) {
			try {
				Thread.sleep(10000L);
				myController.getData();
				myController.sendData();
			} catch (InterruptedException e) {
				GUI.setStatus("automatischer Upload deaktiviert");
				break;
			}
		}
	}
}