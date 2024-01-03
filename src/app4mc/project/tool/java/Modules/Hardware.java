package app4mc.project.tool.java.Modules;

import java.math.BigInteger;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.DataSize;
import org.eclipse.app4mc.amalthea.model.DataSizeUnit;
import org.eclipse.app4mc.amalthea.model.Frequency;
import org.eclipse.app4mc.amalthea.model.FrequencyDomain;
import org.eclipse.app4mc.amalthea.model.FrequencyUnit;
import org.eclipse.app4mc.amalthea.model.HwPort;
import org.eclipse.app4mc.amalthea.model.HwStructure;
import org.eclipse.app4mc.amalthea.model.Memory;
import org.eclipse.app4mc.amalthea.model.MemoryDefinition;
import org.eclipse.app4mc.amalthea.model.MemoryType;
import org.eclipse.app4mc.amalthea.model.PortType;
import org.eclipse.app4mc.amalthea.model.ProcessingUnitDefinition;
import org.eclipse.app4mc.amalthea.model.StructureType;

public class Hardware {
	public static void run(Amalthea model, AmaltheaFactory factory) {
		createProcessingUnitDefinition(model, factory, "Infineon TriCore CPU");
		createProcessingUnitDefinition(model, factory, "Nvidia Pascal GPU");
		createProcessingUnitDefinition(model, factory, "Nvidia Parker SoC");

		Memory ram12 = createMemoryDefinition(model, factory, "12GB GDDR5 RAM", 12, 256);
		Memory ram16 = createMemoryDefinition(model, factory, "16GB GDDR5 RAM", 16, 32);
		Memory ram32 = createMemoryDefinition(model, factory, "32GB DDR3 RAM", 32, 64);

		createFrequencyDomain(model, factory, "1.5 GHz", 1.5);
		createFrequencyDomain(model, factory, "2.0 GHz", 2);
		
		// add hardware unit
		HwStructure hwStructure = factory.createHwStructure();
		hwStructure.setName("Tesla HW2.5");
		hwStructure.setStructureType(StructureType.ECU);
		
		// - add nvidia parker soc
		HwStructure hwStructurePU1 = factory.createHwStructure();
		hwStructurePU1.setName("Nvidia Parker SoC");
		hwStructurePU1.setStructureType(StructureType.SO_C);
		hwStructurePU1.getModules().add(ram32);
		hwStructure.getStructures().add(hwStructurePU1);

		// - add nvidia pascal gpu 1
		HwStructure hwStructurePU2 = factory.createHwStructure();
		hwStructurePU2.setName("Nvidia Pascal GPU 1");
		hwStructurePU2.setStructureType(StructureType.CLUSTER);
		hwStructurePU2.getModules().add(ram12);
		hwStructure.getStructures().add(hwStructurePU2);

		// - add infineon tricore cpu
		HwStructure hwStructurePU3 = factory.createHwStructure();
		hwStructurePU3.setName("Infineon TriCore CPU");
		hwStructurePU3.setStructureType(StructureType.MICROCONTROLLER);
		hwStructurePU3.getModules().add(ram16);
		hwStructure.getStructures().add(hwStructurePU3);
		
		model.getHwModel().getStructures().add(hwStructure);
	}

	private static void createProcessingUnitDefinition(Amalthea model, AmaltheaFactory factory, String name) {
		ProcessingUnitDefinition processingUnitDefinition = factory.createProcessingUnitDefinition();
		processingUnitDefinition.setName(name);
		model.getHwModel().getDefinitions().add(processingUnitDefinition);
	}

	private static Memory createMemoryDefinition(Amalthea model, AmaltheaFactory factory, String name, int size, int bitWidth) {
		MemoryDefinition ram = factory.createMemoryDefinition();
		DataSize dataSize = factory.createDataSize();
		dataSize.setUnit(DataSizeUnit.GB);
		dataSize.setValue(BigInteger.valueOf(size));
		ram.setSize(dataSize);
		ram.setName(name);
		ram.setMemoryType(MemoryType.DRAM);
		model.getHwModel().getDefinitions().add(ram);
		
		// add a port and return as Memory
		Memory mem = factory.createMemory();
		mem.setDefinition(ram);
		HwPort hwPort = factory.createHwPort();
		hwPort.setName("port");
		hwPort.setPortType(PortType.RESPONDER);
		hwPort.setBitWidth(bitWidth);
		mem.getPorts().add(hwPort);
		return mem;
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
