package app4mc.project.tool.java.Modules;

import java.math.BigInteger;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.DataSize;
import org.eclipse.app4mc.amalthea.model.DataSizeUnit;
import org.eclipse.app4mc.amalthea.model.Frequency;
import org.eclipse.app4mc.amalthea.model.FrequencyDomain;
import org.eclipse.app4mc.amalthea.model.FrequencyUnit;
import org.eclipse.app4mc.amalthea.model.MemoryDefinition;
import org.eclipse.app4mc.amalthea.model.ProcessingUnitDefinition;

public class Hardware {
	public static void run(Amalthea model, AmaltheaFactory factory) {
		createProcessingUnitDefinition(model, factory, "Infineon TriCore CPU");
		createProcessingUnitDefinition(model, factory, "Nvidia Pascal GPU");
		createProcessingUnitDefinition(model, factory, "Nvidia Parker SoC");

		createMemoryDefinition(model, factory, "RAM12", 12);
		createMemoryDefinition(model, factory, "RAM16", 16);
		createMemoryDefinition(model, factory, "RAM32", 32);

		createFrequencyDomain(model, factory, "GHz1_5", 1.5);
		createFrequencyDomain(model, factory, "GHz2_0", 2);
	}

	private static void createProcessingUnitDefinition(Amalthea model, AmaltheaFactory factory, String name) {
		ProcessingUnitDefinition processingUnitDefinition = factory.createProcessingUnitDefinition();
		processingUnitDefinition.setName(name);
		model.getHwModel().getDefinitions().add(processingUnitDefinition);
	}

	private static void createMemoryDefinition(Amalthea model, AmaltheaFactory factory, String name, int size) {
		MemoryDefinition ram = factory.createMemoryDefinition();
		DataSize dataSize = factory.createDataSize();
		dataSize.setUnit(DataSizeUnit.GB);
		dataSize.setValue(BigInteger.valueOf(size));
		ram.setSize(dataSize);
		ram.setName(name);
		model.getHwModel().getDefinitions().add(ram);
	}

	private static void createFrequencyDomain(Amalthea model, AmaltheaFactory factory, String name, double frequency) {
		FrequencyDomain frequencyDomain = factory.createFrequencyDomain();
		Frequency freq = factory.createFrequency();
		freq.setValue(frequency);
		freq.setUnit(FrequencyUnit.GHZ);
		frequencyDomain.setDefaultValue(freq);
		frequencyDomain.setName(name);
		model.getHwModel().getDomains().add(frequencyDomain);
	}
}
