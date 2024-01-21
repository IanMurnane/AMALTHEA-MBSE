package app4mc.project.tool.java.Modules;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.ConnectionHandler;
import org.eclipse.app4mc.amalthea.model.ConnectionHandlerDefinition;
import org.eclipse.app4mc.amalthea.model.DataSize;
import org.eclipse.app4mc.amalthea.model.DataSizeUnit;
import org.eclipse.app4mc.amalthea.model.Frequency;
import org.eclipse.app4mc.amalthea.model.FrequencyDomain;
import org.eclipse.app4mc.amalthea.model.FrequencyUnit;
import org.eclipse.app4mc.amalthea.model.HwConnection;
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
		// == DEFINITIONS ==
		ConnectionHandlerDefinition connectionHandlerDefinition = factory.createConnectionHandlerDefinition();
		MemoryDefinition romDefinition = createMemoryDefinition(factory, "32GB EMMC NAND ROM", MemoryType.FLASH, 32, DataSizeUnit.GB, 64);
		MemoryDefinition ramDefinition = createMemoryDefinition(factory, "8GB DDR3 SDRAM", MemoryType.DRAM, 8, DataSizeUnit.GB, 64);
		ProcessingUnitDefinition puCore = createProcessingUnitDefinition(factory, "CPU Core", PuType.CPU);
		ProcessingUnitDefinition puInfineonTricore = createProcessingUnitDefinition(factory, "Infineon TriCore CPU", PuType.CPU);  // sak-tc297tx-128-f300s
		ProcessingUnitDefinition puNvidiaPascal = createProcessingUnitDefinition(factory, "Nvidia Pascal GPU (GTX 1060)", PuType.GPU);  // gp106-510-kc-a1
		ProcessingUnitDefinition puNvidiaParker = createProcessingUnitDefinition(factory, "Nvidia Parker A2 SoC", PuType.CPU);  // ta795sa-a2
		// parker contains these processors
		ProcessingUnitDefinition subPuNvidiaA57 = createProcessingUnitDefinition(factory, "ARM Cortex-A57 CPU", PuType.CPU);
		ProcessingUnitDefinition subPuNvidiaDenver2 = createProcessingUnitDefinition(factory, "Nvidia Denver 2 CPU", PuType.CPU);
		ProcessingUnitDefinition subPuNvidiaPascalGEFORCE = createProcessingUnitDefinition(factory, "Nvidia Pascal GEFORCE 256 CUDA GPU", PuType.GPU);
		model.getHwModel().getDefinitions().addAll(List.of(
				connectionHandlerDefinition,
				romDefinition,
				ramDefinition,
				puCore,
				puInfineonTricore,
				puNvidiaPascal,
				puNvidiaParker,
				subPuNvidiaA57,
				subPuNvidiaDenver2,
				subPuNvidiaPascalGEFORCE
		));

		// == DOMAINS ==
		FrequencyDomain freq300 = createFrequencyDomain(factory, "300 MHz", 0.3);
		FrequencyDomain freq2_0 = createFrequencyDomain(factory, "2.0 GHz", 2.0);
		model.getHwModel().getDomains().addAll(List.of(freq300, freq2_0));

		// == HARDWARE - Tesla HW2.5 ==
		HwStructure teslaHW25 = createHwStructure(factory, "Tesla HW2.5", StructureType.ECU);
		model.getHwModel().getStructures().add(teslaHW25);
		// modules
		Memory rom = createMemory(factory, romDefinition, "ROM");
		Memory ram = createMemory(factory, ramDefinition, "RAM");		
		// connection handlers
		List<ConnectionHandler> connectionHandlers = new ArrayList<>();
		for (int i = 0; i < 8; i++) {  // 8 when cuda cores are enabled
			ConnectionHandler c = createConnectionHandler(factory, connectionHandlerDefinition, "ConnectionHandler" + i);
			connectionHandlers.add(c);
			teslaHW25.getModules().add(c);
			// connections
			addConnection(factory, teslaHW25, c.getPorts().get(0), rom.getPorts().get(0));
			addConnection(factory, teslaHW25, c.getPorts().get(0), ram.getPorts().get(0));
		}
		int connectionHandlerIndex = 0;
		teslaHW25.getModules().addAll(List.of(rom, ram));
		
		// == SUB HARDWARE - Infineon TriCore ==
		HwStructure infineonTriCore = createHwStructure(factory, "Infineon TriCore CPU", StructureType.MICROCONTROLLER);
		addCores(factory, connectionHandlers.get(connectionHandlerIndex++), infineonTriCore, puCore, freq2_0, "Infineon", 32, 3);

		// == SUB HARDWARE - Nvidia Pascal ==
		HwStructure nvidiaPascal = createHwStructure(factory, "Nvidia Pascal GPU (GTX 1060)", StructureType.CLUSTER);
		addCores(factory, connectionHandlers.get(connectionHandlerIndex++), nvidiaPascal, puCore, freq2_0, "Pascal", 128, 10);  // 1280

		// == SUB HARDWARE - Nvidia Parker 1 ==
		HwStructure nvidiaParker1 = createHwStructure(factory, "Nvidia Parker A2 SoC 1", StructureType.SO_C);
		HwStructure subPuNvidiaA57_1 = createHwStructure(factory, "ARM Cortex-A57 CPU 1", StructureType.MICROCONTROLLER);
		addCores(factory, connectionHandlers.get(connectionHandlerIndex++), subPuNvidiaA57_1, puCore, freq2_0, "A57_1", 256, 4);
		HwStructure subPuNvidiaDenver2_1 = createHwStructure(factory, "Nvidia Denver 2 CPU 1", StructureType.MICROCONTROLLER);
		addCores(factory, connectionHandlers.get(connectionHandlerIndex++), subPuNvidiaDenver2_1, puCore, freq2_0, "Denver2_1", 256, 2);
		HwStructure subPuNvidiaPascalGEFORCE_1 = createHwStructure(factory, "Nvidia Pascal GEFORCE 256 CUDA GPU 1", StructureType.CLUSTER);
		addCores(factory, connectionHandlers.get(connectionHandlerIndex++), subPuNvidiaPascalGEFORCE_1, puCore, freq2_0, "GEFORCE_1", 128, 10);  // 256
		nvidiaParker1.getStructures().addAll(List.of(subPuNvidiaA57_1, subPuNvidiaDenver2_1, subPuNvidiaPascalGEFORCE_1));
		
		// == SUB HARDWARE - Nvidia Parker 2 ==
		HwStructure nvidiaParker2 = createHwStructure(factory, "Nvidia Parker A2 SoC 2", StructureType.SO_C);
		HwStructure subPuNvidiaA57_2 = createHwStructure(factory, "ARM Cortex-A57 CPU 2", StructureType.MICROCONTROLLER);
		addCores(factory, connectionHandlers.get(connectionHandlerIndex++), subPuNvidiaA57_2, puCore, freq2_0, "A57_2", 256, 4);
		HwStructure subPuNvidiaDenver2_2 = createHwStructure(factory, "Nvidia Denver 2 CPU 2", StructureType.MICROCONTROLLER);
		addCores(factory, connectionHandlers.get(connectionHandlerIndex++), subPuNvidiaDenver2_2, puCore, freq2_0, "Denver2_2", 256, 2);
		HwStructure subPuNvidiaPascalGEFORCE_2 = createHwStructure(factory, "Nvidia Pascal GEFORCE 256 CUDA GPU 2", StructureType.CLUSTER);
		addCores(factory, connectionHandlers.get(connectionHandlerIndex++), subPuNvidiaPascalGEFORCE_2, puCore, freq2_0, "GEFORCE_2", 128, 10);  // 256
		nvidiaParker2.getStructures().addAll(List.of(subPuNvidiaA57_2, subPuNvidiaDenver2_2, subPuNvidiaPascalGEFORCE_2));

		teslaHW25.getStructures().addAll(List.of(nvidiaPascal, infineonTriCore, nvidiaParker1, nvidiaParker2));
	}
	
	private static void addCores(AmaltheaFactory factory, ConnectionHandler connectionHandler, HwStructure hwStructure, ProcessingUnitDefinition pu, FrequencyDomain freq, String name, int bitWidth, int qty) {
		List<ProcessingUnit> cores = createCores(factory, pu, freq, name + "_Core", bitWidth, qty);
		for (ProcessingUnit core : cores) {
			addConnection(factory, hwStructure, core.getPorts().get(0), connectionHandler.getPorts().get(0));
		}
		hwStructure.getModules().addAll(cores);
	}
	
	private static List<ProcessingUnit> createCores(AmaltheaFactory factory, ProcessingUnitDefinition puDefinition, FrequencyDomain freqDomain, String name, int bitWidth, int qty) {
		List<ProcessingUnit> cores = new ArrayList<>();
		for (int i=0; i < qty; i++) {
			ProcessingUnit psu = factory.createProcessingUnit();
			psu.setDefinition(puDefinition);
			psu.setName(name + "_" + i);
			psu.setFrequencyDomain(freqDomain);
			HwPort hwPort = createPort(factory, PortType.INITIATOR, bitWidth);  // core-initiator ram-responder
			psu.getPorts().add(hwPort);
			cores.add(psu);
		}
		return cores;
	}
	
	private static void addConnection(AmaltheaFactory factory, HwStructure hwStructure, HwPort hwPortA, HwPort hwPortB) {
		int id = hwStructure.getConnections().size();
		HwConnection hwConnection = factory.createHwConnection();
		hwConnection.setName("conn_" + id);
		hwConnection.setPort1(hwPortA);
		hwConnection.setPort2(hwPortB);
		hwStructure.getConnections().add(hwConnection);
	}
	
	private static Memory createMemory(AmaltheaFactory factory, MemoryDefinition memoryDefinition, String name) {
		Memory memory = factory.createMemory();
		memory.setDefinition(memoryDefinition);
		memory.setName(name);
		HwPort hwPort = createPort(factory, PortType.RESPONDER, 64);
		memory.getPorts().add(hwPort);
		return memory;
	}
	
	private static ConnectionHandler createConnectionHandler(AmaltheaFactory factory, ConnectionHandlerDefinition connectionHandlerDefinition, String name) {
		ConnectionHandler connectionHandler = factory.createConnectionHandler();
		connectionHandler.setDefinition(connectionHandlerDefinition);
		connectionHandler.setName(name);
		HwPort connectionHandlerPort = createPort(factory, PortType.RESPONDER, 64);
		connectionHandler.getPorts().add(connectionHandlerPort);
		return connectionHandler;
	}
	
	private static HwPort createPort(AmaltheaFactory factory, PortType portType, int bitWidth) {
		HwPort hwPort = factory.createHwPort();
		hwPort.setName("port");
		hwPort.setPortType(portType);
		hwPort.setBitWidth(bitWidth);
		return hwPort;
	}

	private static HwStructure createHwStructure(AmaltheaFactory factory, String name, StructureType structureType) {
		HwStructure hwStructure = factory.createHwStructure();
		hwStructure.setName(name);
		hwStructure.setStructureType(structureType);
		return hwStructure;
	}

	private static ProcessingUnitDefinition createProcessingUnitDefinition(AmaltheaFactory factory, String name, PuType puType) {
		ProcessingUnitDefinition processingUnitDefinition = factory.createProcessingUnitDefinition();
		processingUnitDefinition.setName(name);
		processingUnitDefinition.setPuType(puType);
		return processingUnitDefinition;
	}

	private static MemoryDefinition createMemoryDefinition(AmaltheaFactory factory, String name, MemoryType memoryType, int size, DataSizeUnit dataSizeUnit, int bitWidth) {
		MemoryDefinition memoryDefinition = factory.createMemoryDefinition();
		DataSize dataSize = factory.createDataSize();
		dataSize.setUnit(dataSizeUnit);
		dataSize.setValue(BigInteger.valueOf(size));
		memoryDefinition.setSize(dataSize);
		memoryDefinition.setName(name);
		memoryDefinition.setMemoryType(memoryType);
		return memoryDefinition;
	}

	private static FrequencyDomain createFrequencyDomain(AmaltheaFactory factory, String name, double frequency) {
		FrequencyDomain frequencyDomain = factory.createFrequencyDomain();
		Frequency freq = factory.createFrequency();
		freq.setValue(frequency);
		freq.setUnit(FrequencyUnit.GHZ);
		frequencyDomain.setDefaultValue(freq);
		frequencyDomain.setName(name);
		return frequencyDomain;
	}
}
