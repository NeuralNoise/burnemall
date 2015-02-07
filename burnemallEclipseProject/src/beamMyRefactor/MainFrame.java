package beamMyRefactor;

import java.io.File;

import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;
import beamMyRefactor.model.Model;
import beamMyRefactor.view.ViewPanel;

public class MainFrame extends JFrame {

	private ViewPanel panel;
	
	public MainFrame () {
		super ("Beam");
		setLayout(new MigLayout());
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
	}
	
	public void init(Model model) {
		if (model.fileName==null)
			setTitle("Beam - NEW");
		else
			setTitle("Beam - " + new File(model.fileName).getName());
		if (panel!=null)
			remove(panel);
		panel = new ViewPanel(model);
		add(panel);
		pack();
	}

	public ViewPanel getViewPanel() {
		return panel;
	}
	
}
