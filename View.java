import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class View extends Frame implements ActionListener, ItemListener {

	private Controller controller;
	private TextField vMixIP_TF, vMixFirstInput_TF;
	private Button save = new Button("Speichern");
	private Button send = new Button("Abrufen/Senden");
	private Label destination[] = new Label[6];
	private Choice matchselect[] = new Choice[6];
	private TextArea status = new TextArea(3, 20);
	private Checkbox automaticUpload = new Checkbox("Automatischer Upload", false);

	public View(Controller _controller) {
		super("DEL-Scores zu vMix 2.0.");
		this.controller = _controller;

		Panel panelNorth = new Panel();
		panelNorth.setLayout(new GridLayout(3, 2));
		panelNorth.add(new Label("vMix IP-Adresse:Port ", Label.RIGHT)); //vMix IP-Adress and Port
		vMixIP_TF = new TextField(30);
		panelNorth.add(vMixIP_TF);
		vMixIP_TF.setText("127.0.0.1:8088"); //set default IP to localhost
		panelNorth.add(new Label("Erste Input-Nr ", Label.RIGHT)); //Number of the first Input
		vMixFirstInput_TF = new TextField(10);
		panelNorth.add(vMixFirstInput_TF);
		vMixFirstInput_TF.setText("2"); //set default to two (Standart in the WildWings-Preset)
		panelNorth.add(automaticUpload);
		automaticUpload.addItemListener(this); //listen if button changes
		panelNorth.add(save);
		save.addActionListener(this);
		this.add("North", panelNorth);

		Panel panelCenter = new Panel();
		panelCenter.setLayout(new GridLayout(6, 2));
		destination[0] = new Label("Maske 1, Zeile 1", Label.RIGHT);
		destination[1] = new Label("Maske 1, Zeile 2", Label.RIGHT);
		destination[2] = new Label("Maske 2, Zeile 1", Label.RIGHT);
		destination[3] = new Label("Maske 2, Zeile 2", Label.RIGHT);
		destination[4] = new Label("Maske 3, Zeile 1", Label.RIGHT);
		destination[5] = new Label("Maske 3, Zeile 2", Label.RIGHT);
		for (int i = 0; i<6; i++) {
			panelCenter.add(destination[i]);
			matchselect[i] = new Choice();
			panelCenter.add(matchselect[i]);
		}
		this.add("Center", panelCenter);

		Panel panelSouth = new Panel();
		panelSouth.setLayout(new GridLayout(2, 1));
		panelSouth.add(send);
		send.addActionListener(this);
		panelSouth.add(status);
		status.setEditable(false);
		this.add("South", panelSouth);

		this.addWindowListener(new MyWindowAdapter());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (save == e.getSource()) {
			controller.setIpAddress(vMixIP_TF.getText());
			controller.setFirstInput(Integer.parseInt(vMixFirstInput_TF.getText()));
		} else if (send == e.getSource()) {
			controller.getData();
			controller.sendData();
		}
	}

	public void itemStateChanged(ItemEvent e) {
		controller.enableAutomaticUpload(automaticUpload.getState());
	}

	/**
	 * Sets the given data of matches to the GUI.
	 * @param _names two dimensional String array where the names of the teams are stored
	 * @param _scores tow dimensional String array where the score is stored
	 * @param _time one dimensional String array where the current time of the match is stored
	 */
	public void setData(String[][] _names, String[][] _scores, String[] _time) {
		for (int j = 0; j < 6; j++) {
			String selectedItem = matchselect[j].getSelectedItem();
			matchselect[j].removeAll();

			matchselect[j].add("-       ");
			for (int i = 0; i < _time.length; i++) {
				matchselect[j].add(_names[i][0] + " - " + _names[i][1] + " (" + _scores[i][0] + ":" + _scores[i][1] + ")");

				if (selectedItem!=null && matchselect[j].getItem(i).startsWith(selectedItem.substring(0, 8))) {
					matchselect[j].select(i);
				}
			}

		}
	}

	public int[][] getSelected() {
		int[][] returnDestination = new int[6][3];
		for (int i=0; i<6; i++) {
			if (!matchselect[i].getSelectedItem().startsWith("-")) {
				returnDestination[i][0] = matchselect[i].getSelectedIndex()-1;

				switch (i) {
				case 0:
					returnDestination[i][1] = 1;
					returnDestination[i][2] = 1;
					System.out.println("1.1.");
					break;
				case 1:
					returnDestination[i][1] = 1;
					returnDestination[i][2] = 2;
					System.out.println("1.2.");
					break;
				case 2:
					returnDestination[i][1] = 2;
					returnDestination[i][2] = 1;
					System.out.println("2.1.");
					break;
				case 3:
					returnDestination[i][1] = 2;
					returnDestination[i][2] = 2;
					System.out.println("2.2.");
					break;
				case 4:
					returnDestination[i][1] = 3;
					returnDestination[i][2] = 1;
					System.out.println("3.1.");
					break;
				case 5:
					returnDestination[i][1] = 3;
					returnDestination[i][2] = 2;
					System.out.println("3.2.");
					break;
				default:
					returnDestination[i][1] = 0;
					returnDestination[i][2] = 0;
					System.out.println("-");
					break;
				}
			} else {
				returnDestination[i][1] = -1;
				returnDestination[i][1] = -1;
				returnDestination[i][2] = -1;
			}
		}

		return returnDestination;	
	}

	/**
	 * Appends the status log with the given String.
	 * @param _status Sting of the status-message
	 */
	public void setStatus(String _status) {
		status.append("\n" + _status);
	}
}

class MyWindowAdapter extends WindowAdapter {

	@Override
	public void windowClosing(WindowEvent event) {
		System.exit(0);
	}
}
