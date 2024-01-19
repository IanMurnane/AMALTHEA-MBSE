package app4mc.project.tool.java.Modules;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.OperatingSystem;
import org.eclipse.app4mc.amalthea.model.RunnableCall;
import org.eclipse.app4mc.amalthea.model.SchedulerDefinition;
import org.eclipse.app4mc.amalthea.model.SchedulingParameterDefinition;
import org.eclipse.app4mc.amalthea.model.TaskScheduler;

public class OperatingSystems {
	public static void run(Amalthea model, AmaltheaFactory factory) {
	
		// define scheduler parameters
		SchedulingParameterDefinition param1 = factory.createSchedulingParameterDefinition();
		param1.setName("priority");
		SchedulingParameterDefinition param2 = factory.createSchedulingParameterDefinition();
		param2.setName("taskGroup");
		model.getOsModel().getSchedulingParameterDefinitions().add(param1);
		model.getOsModel().getSchedulingParameterDefinitions().add(param2);

		// define scheduler
		SchedulerDefinition schedulerDefinition = factory.createSchedulerDefinition();
		schedulerDefinition.setName("OSEK");  // Linux with PREEMPT-RT Patch
		schedulerDefinition.getProcessParameters().add(param1);
		schedulerDefinition.getProcessParameters().add(param2);
		model.getOsModel().getSchedulerDefinitions().add(schedulerDefinition);
		
		SchedulerDefinition schedulerDefinitionWrap = factory.createSchedulerDefinition();
		schedulerDefinitionWrap.setName("Wrap_Scheduler");  
		model.getOsModel().getSchedulerDefinitions().add(schedulerDefinitionWrap);

		// create os and attach core schedulers
		OperatingSystem os = factory.createOperatingSystem();
		os.setName("OperatingSystem");
		model.getOsModel().getOperatingSystems().add(os);
		
		//OSEK Schedulers for TriCore CPUs & Parker SoC 1 & SoC2 CPUs
		attachSchedulers(model,factory,schedulerDefinition,"TriCore",3);
		attachSchedulers(model,factory,schedulerDefinition,"ARM_Cortex_A57_SoC1",4);
		attachSchedulers(model,factory,schedulerDefinition,"Denver2_SoC1",2);
		attachSchedulers(model,factory,schedulerDefinition,"ARM_Cortex_A57_SoC2",4);
		attachSchedulers(model,factory,schedulerDefinition,"Denver2_SoC2",2);
		
		//Wrap Schedulers for Pascal GPU & Pascal GEFORCE GPU (SoC1 & SoC2)
		attachSchedulers(model,factory,schedulerDefinitionWrap,"GEFORCE_GPU_SoC1",1);
		attachSchedulers(model,factory,schedulerDefinitionWrap,"GEFORCE_GPU_SoC2",1);
		attachSchedulers(model,factory,schedulerDefinitionWrap,"Pascal_GPU",1);
		
	}
	
	private static void attachSchedulers(Amalthea model, AmaltheaFactory factory, SchedulerDefinition schedulerDefinition, String coreName, int cores) {
		OperatingSystem os = model.getOsModel().getOperatingSystems().get(0);
		for (int i=0; i < cores; i++) {
			TaskScheduler taskScheduler = factory.createTaskScheduler();
			if(coreName.contains("GPU"))
			{
				taskScheduler.setName(coreName + "_Scheduler");
			}
			else {
				taskScheduler.setName(coreName + "_Scheduler_Core_" + i);
			}
		
			taskScheduler.setDefinition(schedulerDefinition);
			os.getTaskSchedulers().add(taskScheduler);
		}
		model.getOsModel().getOperatingSystems().add(os);
		
	}
}
