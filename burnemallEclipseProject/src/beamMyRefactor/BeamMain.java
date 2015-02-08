package beamMyRefactor;

import java.util.logging.Level;
import java.util.logging.Logger;

import tools.LogUtil;
import beamMyRefactor.controller.Controller;
import beamMyRefactor.controller.EditController;
import beamMyRefactor.model.Model;
import beamMyRefactor.model.ModelSerializer;
import beamMyRefactor.util.LocalProp;
import beamMyRefactor.util.Locator;
import beamMyRefactor.view.ViewPanel;

public class BeamMain {

	public static void main(String[] args) throws Exception {

		Logger.getLogger("").setLevel(Level.INFO);
		LogUtil.init();
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
