public class AutomaticUploader extends Thread {

	Controller myController;

	public AutomaticUploader(Controller _myController) {
		myController = _myController;
	}

	public void run() {
		while(true) {
			try {
				sleep(30000L);
				myController.getData();
				myController.sendData();
			} catch (InterruptedException e) {
				System.out.println("Uploader interupted");
				break;
			}
		}
	}
}
