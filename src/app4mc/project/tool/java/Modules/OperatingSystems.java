package app4mc.project.tool.java.Modules;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.OperatingSystem;
import org.eclipse.app4mc.amalthea.model.SchedulerDefinition;
import org.eclipse.app4mc.amalthea.model.SchedulingParameterDefinition;
import org.eclipse.app4mc.amalthea.model.TaskScheduler;

public class OperatingSystems {
	public static void run(Amalthea model, AmaltheaFactory factory) {
		int cores = 3;  // Infineon Tricore CPU has 3 cores

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

		// create os and attach core schedulers
		OperatingSystem os = factory.createOperatingSystem();
		os.setName("OperatingSystem");
		for (int i=0; i < cores; i++) {
			TaskScheduler taskScheduler = factory.createTaskScheduler();
			taskScheduler.setName("Task_Scheduler_Core_" + i);
			taskScheduler.setDefinition(schedulerDefinition);
			os.getTaskSchedulers().add(taskScheduler);
		}
		model.getOsModel().getOperatingSystems().add(os);
	}
}
