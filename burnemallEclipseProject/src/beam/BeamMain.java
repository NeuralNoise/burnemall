package beam;

import beam.controller.Controller;
import beam.controller.EditController;
import beam.model.Model;
import beam.model.ModelSerializer;
import beam.util.LocalProp;
import beam.util.Locator;
import beam.view.ViewPanel;

public class BeamMain {

	public static void main(String[] args) throws Exception {

		Model model=null;
		if(LocalProp.DEFAULT_LOAD!=null) 
			model = ModelSerializer.load(Locator.getFile(LocalProp.DEFAULT_LOAD));
		else
			model = new Model();
		
		final MainFrame frame = new MainFrame();
		frame.init(model);
		ViewPanel view = frame.getViewPanel();
				
		Controller c = new Controller(model,view);
		EditController ec = new EditController(c, model, frame, view);
		
		c.start();
	}
	
}
