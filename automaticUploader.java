public class automaticUploader extends Thread {

	Controller myController;
	boolean stopper = false
			;

	public automaticUploader(Controller _myController) {
		myController = _myController;
	}

	public void enableUploader(boolean enable) {
		stopper = enable;
	}

	public void run() {
		while(true) {
			try {
				sleep(100L);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while(stopper) {
				try {
					sleep(30000L);
					myController.getData();
					myController.sendData();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
