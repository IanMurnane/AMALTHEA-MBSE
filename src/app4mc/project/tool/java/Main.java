package app4mc.project.tool.java;

import java.io.File;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.io.AmaltheaWriter;

import app4mc.project.tool.java.Modules.Template;

public class Main {
	public static void main(String[] args) {
		final File outputFile = new File("model-output/project.amxmi");

		System.out.println("Creating new project");
				
		Amalthea model = AmaltheaFactory.eINSTANCE.createAmalthea();
		
		// add your modules here
		Template.run(model);

		// output the model to a file
		if (AmaltheaWriter.writeToFile(model, outputFile)) {
			System.out.println("Wrote file: " + outputFile.getAbsolutePath());
		} else {
			System.err.println("Error: Model not saved!");
		}

		System.out.println("Finished");
	}
}
