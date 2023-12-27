package app4mc.project.tool.java.Modules;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.SWModel;

public class Template {
	public static void run(Amalthea model) {
		SWModel sw = AmaltheaFactory.eINSTANCE.createSWModel();
		model.setSwModel(sw);
	}
}
