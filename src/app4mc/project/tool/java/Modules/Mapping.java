package app4mc.project.tool.java.Modules;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.HwModule;
import org.eclipse.app4mc.amalthea.model.HwStructure;
import org.eclipse.app4mc.amalthea.model.OperatingSystem;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.RunnableCall;
import org.eclipse.app4mc.amalthea.model.SchedulerAllocation;
import org.eclipse.app4mc.amalthea.model.SchedulerDefinition;
import org.eclipse.app4mc.amalthea.model.SchedulingParameterDefinition;
import org.eclipse.app4mc.amalthea.model.StructureType;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.TaskAllocation;
import org.eclipse.app4mc.amalthea.model.TaskScheduler;
import org.eclipse.app4mc.amalthea.model.util.HardwareUtil;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

public class Mapping {
	@SuppressWarnings("null")
	public static void run(Amalthea model, AmaltheaFactory factory) {
		
		//Nvidia Pascal GPU (GTX 1060)
		HwStructure parentCPU = model.getHwModel().getStructures().get(0).getStructures().get((0));
		
		EList<HwModule> PascalGPUHwStructures = parentCPU.getModules();
		//System.out.println(parentCPU.getModules());
		HwModule executingCPU;
		EList<HwModule> responsibiliyCPU = new BasicEList<HwModule>();
		
		EList<TaskScheduler> taskScheduler = model.getOsModel().getOperatingSystems().get(0).getTaskSchedulers();
		//System.out.println(taskScheduler);
		for (HwModule hwS : PascalGPUHwStructures) {
			if (hwS.getName().contains("Core")) {
				responsibiliyCPU.add(hwS);
			}
		}
		executingCPU = PascalGPUHwStructures.get(0);
		TaskScheduler t_core = getScheduler(taskScheduler, "Pascal_GPU_Scheduler");
		addSchedulerAllocation(model,factory,executingCPU,responsibiliyCPU,t_core);
		
		//Infineon TriCore CPU
		HwStructure parentCPU1 = model.getHwModel().getStructures().get(0).getStructures().get((1));
		EList<HwModule> TriCoreHwStructures = parentCPU1.getModules();
		HwModule executingCPU1;
		EList<HwModule> responsibiliyCPU1 = new BasicEList<HwModule>();
		
		//System.out.println(taskScheduler);
		for (HwModule hwS : TriCoreHwStructures) {
			if (hwS.getName().contains("Core")) {
				//System.out.println(hwS);
				executingCPU1 = hwS;
				responsibiliyCPU1.add(hwS);
				int lastIndex = hwS.getName().indexOf("_C");
				String name = "TriCore_Scheduler" + hwS.getName().substring(lastIndex);
				t_core = getScheduler(taskScheduler, name);
				addSchedulerAllocation(model,factory,executingCPU1,responsibiliyCPU1,t_core);
				responsibiliyCPU1.clear();
			}
		}
		
	    
	    //Nvidia Parker SoC 1
		//ARM_Cortex_A57_SoC1
	    HwStructure parentCPU2 = model.getHwModel().getStructures().get(0).getStructures().get((2));
	   // System.out.println(parentCPU2);
	    EList<HwModule> SoC1CoreHwStructures = parentCPU2.getStructures().get(0).getModules();
		HwModule executingCPU2 = null;
		EList<HwModule> responsibiliyCPU2 = new BasicEList<HwModule>();
		
		for (HwModule hwS : SoC1CoreHwStructures) {
			if (hwS.getName().contains("Core")) {
				//System.out.println(hwS);
				executingCPU2 = hwS;
				responsibiliyCPU2.add(hwS);
				int lastIndex = hwS.getName().indexOf("_C");
				String name = "ARM_Cortex_A57_SoC1_Scheduler" + hwS.getName().substring(lastIndex);
				t_core = getScheduler(taskScheduler, name);
				addSchedulerAllocation(model,factory,executingCPU2,responsibiliyCPU2,t_core);
				responsibiliyCPU2.clear();
			}
		}
		
		//Denver_2_SoC1
		SoC1CoreHwStructures = parentCPU2.getStructures().get(1).getModules();
		executingCPU2 = null;
		responsibiliyCPU2 = new BasicEList<HwModule>();
		
		for (HwModule hwS : SoC1CoreHwStructures) {
			if (hwS.getName().contains("Core")) {
				//System.out.println(hwS);
				executingCPU2 = hwS;
				responsibiliyCPU2.add(hwS);
				int lastIndex = hwS.getName().indexOf("_C");
				String name = "Denver2_SoC1_Scheduler" + hwS.getName().substring(lastIndex);
				t_core = getScheduler(taskScheduler, name);
				addSchedulerAllocation(model,factory,executingCPU2,responsibiliyCPU2,t_core);
				responsibiliyCPU2.clear();
			}
		}
	  	    
	  	//Nvidia Parker SoC 2
		//ARM_Cortex_A57_SoC2
	    HwStructure parentCPU3 = model.getHwModel().getStructures().get(0).getStructures().get((3));
	    //System.out.println(parentCPU3);
	    EList<HwModule> SoC2CoreHwStructures = parentCPU3.getStructures().get(0).getModules();
		HwModule executingCPU3 = null;
		EList<HwModule> responsibiliyCPU3 = new BasicEList<HwModule>();
		
		for (HwModule hwS : SoC2CoreHwStructures) {
			if (hwS.getName().contains("Core")) {
				//System.out.println(hwS);
				executingCPU3 = hwS;
				responsibiliyCPU3.add(hwS);
				int lastIndex = hwS.getName().indexOf("_C");
				String name = "ARM_Cortex_A57_SoC2_Scheduler" + hwS.getName().substring(lastIndex);
				t_core = getScheduler(taskScheduler, name);
				addSchedulerAllocation(model,factory,executingCPU3,responsibiliyCPU3,t_core);
				responsibiliyCPU3.clear();
			}
		}
		
		//Denver_2_SoC2
		SoC2CoreHwStructures = parentCPU3.getStructures().get(1).getModules();
		executingCPU3 = null;
		responsibiliyCPU3 = new BasicEList<HwModule>();
		
		for (HwModule hwS : SoC2CoreHwStructures) {
			if (hwS.getName().contains("Core")) {
				//System.out.println(hwS);
				executingCPU3 = hwS;
				responsibiliyCPU3.add(hwS);
				int lastIndex = hwS.getName().indexOf("_C");
				String name = "Denver2_SoC2_Scheduler" + hwS.getName().substring(lastIndex);
				t_core = getScheduler(taskScheduler, name);
				addSchedulerAllocation(model,factory,executingCPU3,responsibiliyCPU3,t_core);
				responsibiliyCPU3.clear();
			}
		}
		
		addTaskAllocation(model,factory,taskScheduler,0,0);
		addTaskAllocation(model,factory,taskScheduler,0,17);
		
		addTaskAllocation(model,factory,taskScheduler,1,3);
		addTaskAllocation(model,factory,taskScheduler,1,7);
		
		addTaskAllocation(model,factory,taskScheduler,2,4);
		addTaskAllocation(model,factory,taskScheduler,2,8);
		
		addTaskAllocation(model,factory,taskScheduler,3,9);
		addTaskAllocation(model,factory,taskScheduler,3,13);
		
		addTaskAllocation(model,factory,taskScheduler,4,10);
		addTaskAllocation(model,factory,taskScheduler,4,14);
		
		addTaskAllocation(model,factory,taskScheduler,5,5);
		addTaskAllocation(model,factory,taskScheduler,5,6);
		
		addTaskAllocation(model,factory,taskScheduler,6,1);
		addTaskAllocation(model,factory,taskScheduler,6,2);
		addTaskAllocation(model,factory,taskScheduler,6,11);
		addTaskAllocation(model,factory,taskScheduler,6,12);
		
	}
	
	private static void addSchedulerAllocation(Amalthea model, AmaltheaFactory factory, HwModule executingCPU1, EList<HwModule> responsibiliyCPU1, TaskScheduler t_core) {
	
		SchedulerAllocation schedulerAllocation = factory.createSchedulerAllocation();
		schedulerAllocation.setExecutingPU((ProcessingUnit) executingCPU1);
		schedulerAllocation.setScheduler(t_core);
		for (HwModule r : responsibiliyCPU1) {
			schedulerAllocation.getResponsibility().add((ProcessingUnit) r);
		}
		
		model.getMappingModel().getSchedulerAllocation().add(schedulerAllocation);
	}
	private static TaskScheduler getScheduler(EList<TaskScheduler> taskScheduler, String name ) {
		TaskScheduler t_core =null ;
		for(TaskScheduler t : taskScheduler) {
			if(t.getName().equals(name)) {
				t_core = t;
				return t_core;
			}
		}
		return t_core;
	}
	private static void addTaskAllocation(Amalthea model, AmaltheaFactory factory, EList<TaskScheduler> taskScheduler, int t_index, int s_index) {
		
		TaskAllocation taskAllocation = factory.createTaskAllocation();
		Task task = model.getSwModel().getTasks().get(t_index);
		TaskScheduler scheduler = taskScheduler.get(s_index);
		
		taskAllocation.setTask(task);
		taskAllocation.setScheduler(scheduler);
		model.getMappingModel().getTaskAllocation().add(taskAllocation);
	}
}
