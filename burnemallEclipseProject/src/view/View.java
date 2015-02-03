package beam.view;

import javax.swing.JFrame;
import javax.swing.JPanel;

import beam.model.Model;

public class View extends JFrame {

	static final int WIDTH = 200;
	static final int HEIGHT = 200;
	
	private Model model;
	private ViewPanel panel;
	
	public View (Model model) {
		super ("Beam");
		this.model = model;
		panel = new ViewPanel(model);
		add(panel);
		setSize(WIDTH, HEIGHT);
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
}
