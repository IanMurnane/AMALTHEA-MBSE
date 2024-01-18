package app4mc.project.tool.java.Modules;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.DataSize;
import org.eclipse.app4mc.amalthea.model.DataSizeUnit;
import org.eclipse.app4mc.amalthea.model.Frequency;
import org.eclipse.app4mc.amalthea.model.FrequencyDomain;
import org.eclipse.app4mc.amalthea.model.FrequencyUnit;
import org.eclipse.app4mc.amalthea.model.HwConnection;
import org.eclipse.app4mc.amalthea.model.HwModule;
import org.eclipse.app4mc.amalthea.model.HwPort;
import org.eclipse.app4mc.amalthea.model.HwStructure;
import org.eclipse.app4mc.amalthea.model.Memory;
import org.eclipse.app4mc.amalthea.model.MemoryDefinition;
import org.eclipse.app4mc.amalthea.model.MemoryType;
import org.eclipse.app4mc.amalthea.model.PortType;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.ProcessingUnitDefinition;
import org.eclipse.app4mc.amalthea.model.PuType;
import org.eclipse.app4mc.amalthea.model.StructureType;

public class Hardware {
	public static void run(Amalthea model, AmaltheaFactory factory) {
		FrequencyDomain freq300 = addFrequencyDomain(model, factory, "300 MHz", 0.3);
		FrequencyDomain freq2_0 = addFrequencyDomain(model, factory, "2.0 GHz", 2.0);

		ProcessingUnitDefinition puInfineonTricore = addProcessingUnitDefinition(model, factory, "Infineon TriCore CPU", PuType.CPU);
		ProcessingUnitDefinition puNvidiaPascal = addProcessingUnitDefinition(model, factory, "Nvidia Pascal GPU", PuType.GPU);
		ProcessingUnitDefinition puNvidiaParker = addProcessingUnitDefinition(model, factory, "Nvidia Parker SoC", PuType.CPU);

		Memory rom8 = addROMDefinition(model, factory, "8MB FLASH ROM", 8, 32);
		Memory ram4 = addMemoryDefinition(model, factory, "4GB GDDR5 RAM", 4, 256);
		Memory ram16 = addMemoryDefinition(model, factory, "16GB GDDR5 RAM", 16, 32);
		Memory ram32 = addMemoryDefinition(model, factory, "32GB DDR3 RAM", 32, 64);

		// ECU - Tesla HW2.5 AP
		HwStructure teslaHW25 = addHwStructure(model, factory, "Tesla HW2.5", StructureType.ECU);
		List<ProcessingUnit> coresNvidiaParker = createCores(factory, puNvidiaParker, freq2_0, 256, 6);
		attachHardware(factory, teslaHW25, "Nvidia Parker SoC 1", StructureType.SO_C, ram32, null, coresNvidiaParker);
		attachHardware(factory, teslaHW25, "Nvidia Parker SoC 2", StructureType.SO_C, ram32, null, coresNvidiaParker);
		List<ProcessingUnit> coresNvidiaPascal = createCores(factory, puNvidiaPascal, null, 128, 1280);
		attachHardware(factory, teslaHW25, "Nvidia Pascal GPU (GTX 1060)", StructureType.CLUSTER, ram4, null, coresNvidiaPascal);
		List<ProcessingUnit> coresInfineonTricore = createCores(factory, puInfineonTricore, freq300, 32, 3);
		attachHardware(factory, teslaHW25, "Infineon TriCore CPU", StructureType.MICROCONTROLLER, ram16, rom8, coresInfineonTricore);
	}

	private static HwStructure addHwStructure(Amalthea model, AmaltheaFactory factory, String name, StructureType structureType) {
		HwStructure hwStructure = factory.createHwStructure();
		hwStructure.setName(name);
		hwStructure.setStructureType(structureType);
		model.getHwModel().getStructures().add(hwStructure);
		return hwStructure;
	}

	private static void attachHardware(AmaltheaFactory factory, HwStructure parentHwStructure, String name, StructureType structureType, Memory ram, Memory rom, List<ProcessingUnit> cores) {
		HwStructure hwStructure = factory.createHwStructure();
		hwStructure.setName(name);
		hwStructure.setStructureType(structureType);
		hwStructure.getModules().add(ram);
		if (rom != null) hwStructure.getModules().add(rom);
		hwStructure.getModules().addAll(cores);
		// connect all cores to ram
		HwModule assignedRam = hwStructure.getModules().get(0);
		boolean hasROM = rom != null;
		int connectionCount = 0;
		for (int i = hasROM ? 2 : 1; i < hwStructure.getModules().size(); i++) {
			HwModule core = hwStructure.getModules().get(i);
			HwConnection conn = factory.createHwConnection();
			conn.setName("conn_" + connectionCount++);
			conn.setPort1(core.getPorts().get(0));
			conn.setPort2(assignedRam.getPorts().get(0));
			hwStructure.getConnections().add(conn);
		}
		if (hasROM) {
			HwModule assignedRom = hwStructure.getModules().get(1);
			for (int i = 2; i < hwStructure.getModules().size(); i++) {
				HwModule core = hwStructure.getModules().get(i);
				HwConnection conn = factory.createHwConnection();
				conn.setName("conn_" + connectionCount++);
				conn.setPort1(core.getPorts().get(0));
				conn.setPort2(assignedRom.getPorts().get(0));
				hwStructure.getConnections().add(conn);
			}
		}
		parentHwStructure.getStructures().add(hwStructure);
	}

	private static ProcessingUnitDefinition addProcessingUnitDefinition(Amalthea model, AmaltheaFactory factory, String name, PuType puType) {
		ProcessingUnitDefinition processingUnitDefinition = factory.createProcessingUnitDefinition();
		processingUnitDefinition.setName(name);
		processingUnitDefinition.setPuType(puType);
		model.getHwModel().getDefinitions().add(processingUnitDefinition);
		return processingUnitDefinition;
	}

	private static List<ProcessingUnit> createCores(AmaltheaFactory factory, ProcessingUnitDefinition pud, FrequencyDomain frequencyDomain, int bitWidth, int quantity) {
		List<ProcessingUnit> cores = new ArrayList<>();
		for (int i=0; i < quantity; i++) {
			ProcessingUnit psu = factory.createProcessingUnit();
			psu.setDefinition(pud);
			psu.setName("Core_" + i);
			psu.setFrequencyDomain(frequencyDomain);
			HwPort hwPort = factory.createHwPort();
			hwPort.setName("port");
			hwPort.setPortType(PortType.INITIATOR);
			hwPort.setBitWidth(bitWidth);
			psu.getPorts().add(hwPort);
			cores.add(psu);
		}
		return cores;
	}

	private static Memory addMemoryDefinition(Amalthea model, AmaltheaFactory factory, String name, int size, int bitWidth) {
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
		mem.setName("RAM");
		HwPort hwPort = factory.createHwPort();
		hwPort.setName("port");
		hwPort.setPortType(PortType.RESPONDER);
		hwPort.setBitWidth(bitWidth);
		mem.getPorts().add(hwPort);
		return mem;
	}

	private static Memory addROMDefinition(Amalthea model, AmaltheaFactory factory, String name, int size, int bitWidth) {
		MemoryDefinition rom = factory.createMemoryDefinition();
		DataSize dataSize = factory.createDataSize();
		dataSize.setUnit(DataSizeUnit.MB);
		dataSize.setValue(BigInteger.valueOf(size));
		rom.setSize(dataSize);
		rom.setName(name);
		rom.setMemoryType(MemoryType.FLASH);
		model.getHwModel().getDefinitions().add(rom);

		// add a port and return as Memory
		Memory mem = factory.createMemory();
		mem.setDefinition(rom);
		mem.setName("ROM");
		HwPort hwPort = factory.createHwPort();
		hwPort.setName("port");
		hwPort.setPortType(PortType.RESPONDER);
		hwPort.setBitWidth(bitWidth);
		mem.getPorts().add(hwPort);
		return mem;
	}

	private static FrequencyDomain addFrequencyDomain(Amalthea model, AmaltheaFactory factory, String name, double frequency) {
		FrequencyDomain frequencyDomain = factory.createFrequencyDomain();
		Frequency freq = factory.createFrequency();
		freq.setValue(frequency);
		freq.setUnit(FrequencyUnit.GHZ);
		frequencyDomain.setDefaultValue(freq);
		frequencyDomain.setName(name);
		model.getHwModel().getDomains().add(frequencyDomain);
		return frequencyDomain;
	}
}
