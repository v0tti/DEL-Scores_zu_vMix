import java.awt.Button;
import java.awt.Choice;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class View extends Frame implements ActionListener {
	
	private Controller controller;
	private TextField vMixIP_TF, vMixFirstInput_TF;
	private TextArea game[] = new TextArea[7];
	private Choice destination[] = new Choice[7];
	private Button save = new Button("Speichern");
	public Label status = new Label("Konfiguration");
	
	View(Controller _controller) {
		super("DEL-Scores zu vMix");
		this.controller = _controller;
		
		Panel panelNorth = new Panel();
		panelNorth.setLayout(new GridLayout(2, 2));
		panelNorth.add(new Label("vMix IP-Adresse und Port: "));
		vMixIP_TF = new TextField(30);
		panelNorth.add(vMixIP_TF);
		panelNorth.add(new Label("Nummer des ersten Inputs: "));
		vMixFirstInput_TF = new TextField(10);
		panelNorth.add(vMixFirstInput_TF);
		this.add("North", panelNorth);

		Panel panelSouth = new Panel();
		panelSouth.setLayout(new GridLayout(8, 2));
		for (int i=0; i<7; i++) {
			game[i] = new TextArea(3, 20);
			game[i].setEditable(false);
			destination[i] = new Choice();
			destination[i].add("-");
			destination[i].add("1.1");
			destination[i].add("1.2");
			destination[i].add("2.1");
			destination[i].add("2.2");
			destination[i].add("3.1");
			destination[i].add("3.2");
			panelSouth.add(game[i]);
			panelSouth.add(destination[i]);
		}
		panelSouth.add(save);
		panelSouth.add(status);
		this.add("South", panelSouth);
		
		this.addWindowListener(new MyWindowAdapter());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}


class MyWindowAdapter extends WindowAdapter {

	@Override
	public void windowClosing(WindowEvent event) {
		System.exit(0);
	}
}
