package app4mc.project.tool.java.Modules;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.ProcessingUnitDefinition;

public class Hardware {
	public static void run(Amalthea model, AmaltheaFactory factory) {
		ProcessingUnitDefinition psu = factory.createProcessingUnitDefinition();
		psu.setName("Infineon TriCore CPU");
		model.getHwModel().getDefinitions().add(psu);

		ProcessingUnitDefinition gpu = factory.createProcessingUnitDefinition();
		gpu.setName("Nvidia Pascal GPU");
		model.getHwModel().getDefinitions().add(gpu);

		ProcessingUnitDefinition soc = factory.createProcessingUnitDefinition();
		soc.setName("Nvidia Parker SoC");
		model.getHwModel().getDefinitions().add(soc);
	}
}
