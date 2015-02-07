package beam.model;

import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class ModelSerializer {

	public static Model load(String fname) throws Exception {
		Serializer serializer = new Persister();
		File source = new File(fname);
		Model model = serializer.read(Model.class, source);
		model.fileName = fname;
		return model;
	}

	public static Model load(File file) throws Exception {
		Serializer serializer = new Persister();
		Model model = serializer.read(Model.class, file);
		model.fileName = file.getCanonicalPath();
		System.out.println(model.fileName);
		return model;
	}

	public static void main(String[] args) throws Exception {
		Serializer serializer = new Persister();
		Model example = new Model();
		File result = new File("c:\\temp\\xmlsimple\\example.xml");
		serializer.write(example, result);
		
		serializer = new Persister();
		File source = new File("c:\\temp\\xmlsimple\\example.xml");
		Model exr = serializer.read(Model.class, source);
		System.out.println(exr);
	}

	public static void write(Model model) throws Exception {
		Serializer serializer = new Persister();
		serializer.write(model, new File(model.fileName));
	}

}
