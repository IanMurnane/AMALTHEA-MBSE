package app4mc.project.tool.java;

import java.io.File;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.io.AmaltheaWriter;

import app4mc.project.tool.java.Modules.ProjectSetup;
import app4mc.project.tool.java.Modules.Hardware;
import app4mc.project.tool.java.Modules.OperatingSystems;
import app4mc.project.tool.java.Modules.Software;
import app4mc.project.tool.java.Modules.Stimuli;

public class Main {
	public static void main(String[] args) {
		final File outputFile = new File("model-output/projectOutput.amxmi");

		System.out.println("Creating new project");

		final AmaltheaFactory factory = AmaltheaFactory.eINSTANCE;
		Amalthea model = AmaltheaFactory.eINSTANCE.createAmalthea();

		// add modules here
		ProjectSetup.run(model, factory);
		Hardware.run(model,  factory);
		Software.run(model, factory);
		OperatingSystems.run(model, factory);
		Stimuli.run(model, factory);

		// output the model to a file
		if (AmaltheaWriter.writeToFile(model, outputFile)) {
			System.out.println("Wrote file: " + outputFile.getPath());
		} else {
			System.err.println("Error: Model not saved!");
		}

		System.out.println("Done.");
	}
}
