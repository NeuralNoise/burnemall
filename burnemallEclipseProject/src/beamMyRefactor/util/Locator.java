package beamMyRefactor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Locator {

	private static File base;

	private static File getBase() {
		if (base==null) {
			/* ATTENTION : en cas d'exception, créer dans ce répertoire une classe comme ceci. Cette classe "LocalProp" est 
			 * en .gitignore, ce qui signifie qu'elle n'est pas partagée via GitHib et garde donc sa valeur locale.  
			 * public class LocalProp {
			 * public static final String RESOURCES_PATH="C:\\Users\\matthieu.dumas\\git\\Beam\\BeamEclipseProject\\resources"; 	
			 * public static final String DEFAULT_LOAD = "matthieu.xml";
			 * }
			 */
			base = new File(LocalProp.RESOURCES_PATH);
			if (!base.isDirectory())
				throw new IllegalStateException("Invalid resources directory");
		}
		return base;
	}

	public static InputStream getStream(String string) throws FileNotFoundException {
		File base = getBase();
		File file = new File(base, string);
		return new FileInputStream(file);
	}

	public static String getFile(String string) throws IOException {
		File base = getBase();
		File file = new File(base, string);
		return file.getCanonicalPath();
	}

}
