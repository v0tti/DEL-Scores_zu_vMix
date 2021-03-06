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
	private TextArea game[] = new TextArea[7];
	private Choice destination[] = new Choice[7];
	private Button save = new Button("Speichern");
	private Button send = new Button("Abrufen/Senden");
	private TextArea status = new TextArea(3, 20);
	private Checkbox automaticUpload = new Checkbox("Automatischer Upload", false);

	public View(Controller _controller) {
		super("DEL-Scores zu vMix 1.4.");
		this.controller = _controller;

		Panel panelNorth = new Panel();
		panelNorth.setLayout(new GridLayout(3, 2));
		panelNorth.add(new Label("vMix IP-Adresse und Port: ")); //vMix IP-Adress and Port
		vMixIP_TF = new TextField(30);
		panelNorth.add(vMixIP_TF);
		vMixIP_TF.setText("127.0.0.1:8088"); //set default IP to localhost
		panelNorth.add(new Label("Nummer des ersten Inputs: ")); //Number of the first Input
		vMixFirstInput_TF = new TextField(10);
		panelNorth.add(vMixFirstInput_TF);
		vMixFirstInput_TF.setText("2"); //set default to two (Standart in the WildWings-Preset)
		panelNorth.add(automaticUpload);
		automaticUpload.addItemListener(this); //listen if button changes
		panelNorth.add(save);
		save.addActionListener(this);
		this.add("North", panelNorth);

		Panel panelSouth = new Panel();
		panelSouth.setLayout(new GridLayout(8, 2));
		for (int i = 0; i < 7; i++) {
			game[i] = new TextArea(3, 20);
			game[i].setEditable(false);
			destination[i] = new Choice();
			destination[i].add("-"); //do not asign this match to a title
			destination[i].add("1.1."); //asign to title 1, row 1
			destination[i].add("1.2."); //asign to title 1, row 2
			destination[i].add("2.1."); //asign to title 2, row 1
			destination[i].add("2.2."); //asign to title 2, row 2
			destination[i].add("3.1."); //asign to title 3, row 1
			destination[i].add("3.2."); //asign to title 3, row 2
			panelSouth.add(game[i]);
			panelSouth.add(destination[i]);
		}
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
		for (int i = 0; i < _time.length; i++) {
			game[i].setText(_names[i][0].replaceAll("%20", " ") + "-"
					+ _names[i][1].replaceAll("%20", " ") + "\n"
					+ _scores[i][0] + ":" + _scores[i][1] + "\n"
					+ _time[i].replaceAll("%20", " "));
		}
	}

	/**
	 * Takes the name of a home team and gives back an Array with the destination of the match.
	 * @param _TeamHome the Name of the home team
	 * @return Two-Dimensional Integer-Array where Index 0 is the Input (1, 2 or 3) and Index 1 is the row (1 or 2).
	 */
	public int[] getDestination (String _TeamHome) {
		int[] returnDestination = new int[2];
		for (int i=0; i<7; i++) {
			if (game[i].getText().contains(_TeamHome.replaceAll("%20", " ")) ) {
				switch (destination[i].getSelectedIndex()) {
				case 1:
					returnDestination[0] = 1;
					returnDestination[1] = 1;
					System.out.println("1.1.");
					break;
				case 2:
					returnDestination[0] = 1;
					returnDestination[1] = 2;
					System.out.println("1.2.");
					break;
				case 3:
					returnDestination[0] = 2;
					returnDestination[1] = 1;
					System.out.println("2.1.");
					break;
				case 4:
					returnDestination[0] = 2;
					returnDestination[1] = 2;
					System.out.println("2.2.");
					break;
				case 5:
					returnDestination[0] = 3;
					returnDestination[1] = 1;
					System.out.println("3.1.");
					break;
				case 6:
					returnDestination[0] = 3;
					returnDestination[1] = 2;
					System.out.println("3.2.");
					break;
				default:
					returnDestination[0] = 0;
					returnDestination[1] = 0;
					System.out.println("-");
					break;
				}
				return returnDestination;	
			}
		}
		return null;
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
