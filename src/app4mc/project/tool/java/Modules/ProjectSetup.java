package app4mc.project.tool.java.Modules;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.HWModel;
import org.eclipse.app4mc.amalthea.model.OSModel;
import org.eclipse.app4mc.amalthea.model.SWModel;

public class ProjectSetup {
	public static void run(Amalthea model, AmaltheaFactory factory) {
		// place generic project setup/structure etc here
		// all other elements should be placed in separate files
		
		SWModel sw = factory.createSWModel();
		model.setSwModel(sw);
		
		HWModel hw = factory.createHWModel();
		model.setHwModel(hw);

		OSModel os = factory.createOSModel();
		model.setOsModel(os);
	}
}
