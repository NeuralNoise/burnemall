package beam;

import beam.controller.Controller;
import beam.model.Model;
import beam.view.View;

public class BeamMain {

	public static void main(String[] args) {
		final Model m = new Model();
		final View v = new View(m);
		final Controller c = new Controller(m,v);
		c.start();
	}
	
}
