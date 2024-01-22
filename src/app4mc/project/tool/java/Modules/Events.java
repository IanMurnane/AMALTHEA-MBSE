package app4mc.project.tool.java.Modules;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.EventStimulus;
import org.eclipse.app4mc.amalthea.model.HwModule;
import org.eclipse.app4mc.amalthea.model.HwStructure;
import org.eclipse.app4mc.amalthea.model.OperatingSystem;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.RunnableCall;
import org.eclipse.app4mc.amalthea.model.RunnableEvent;
import org.eclipse.app4mc.amalthea.model.RunnableEventType;
import org.eclipse.app4mc.amalthea.model.SchedulerAllocation;
import org.eclipse.app4mc.amalthea.model.SchedulerDefinition;
import org.eclipse.app4mc.amalthea.model.SchedulingParameterDefinition;
import org.eclipse.app4mc.amalthea.model.StimuliModel;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.app4mc.amalthea.model.StructureType;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.TaskAllocation;
import org.eclipse.app4mc.amalthea.model.TaskScheduler;
import org.eclipse.app4mc.amalthea.model.util.HardwareUtil;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

public class Events {
	public static void run(Amalthea model, AmaltheaFactory factory) {
		
		addEvents(model,factory,2,"Camera_Input_Change");
		addEvents(model,factory,3,"Object_Detected");
		addEvents(model,factory,5,"Any_Sensor_Data_Update");
		
		addEvents(model,factory,6,"Obstacle_Detected_In_Path");
		addEvents(model,factory,6,"Imminent_Collision_Detected");
		addEvents(model,factory,9,"Lane_Departure_Detected");
		addEvents(model,factory,10,"Vehicle_In_Blind_Spot_Detected");
		
		addEvents(model,factory,11,"CD_Algorithm_Updated");
		addEvents(model,factory,12,"CD_Algorithm_Updated");
		addEvents(model,factory,13,"ACC_Algorithm_Updated");
		addEvents(model,factory,14,"LDW_Algorithm_Updated");
		addEvents(model,factory,15,"BSD_Algorithm_Updated");
		
		//add event stimuli to aperiodic tasks
		EList<Stimulus> stimuli = model.getStimuliModel().getStimuli();
		EList<Stimulus> eventStimuli = new BasicEList<Stimulus>();
		eventStimuli.add(stimuli.get(2));
		eventStimuli.add(stimuli.get(3));
		eventStimuli.add(stimuli.get(4));
		model.getSwModel().getTasks().get(1).getStimuli().addAll(eventStimuli);
		eventStimuli.clear();
		
		eventStimuli.add(stimuli.get(5));
		model.getSwModel().getTasks().get(2).getStimuli().addAll(eventStimuli);
		eventStimuli.clear();
		
		eventStimuli.add(stimuli.get(6));
		model.getSwModel().getTasks().get(3).getStimuli().addAll(eventStimuli);
		eventStimuli.clear();
		
		eventStimuli.add(stimuli.get(7));
		model.getSwModel().getTasks().get(5).getStimuli().addAll(eventStimuli);
		eventStimuli.clear();
		
		eventStimuli.add(stimuli.get(8));
		model.getSwModel().getTasks().get(6).getStimuli().addAll(eventStimuli);
		eventStimuli.clear();
		
		eventStimuli.add(stimuli.get(9));
		eventStimuli.add(stimuli.get(10));
		eventStimuli.add(stimuli.get(11));
		eventStimuli.add(stimuli.get(12));
		eventStimuli.add(stimuli.get(13));
		//model.getSwModel().getTasks().get(7).getStimuli().addAll(eventStimuli);
		
		
	}
	

	private static void addEvents(Amalthea model, AmaltheaFactory factory, int rIndex, String name) {
		RunnableEvent event = factory.createRunnableEvent();
		event.setEntity(model.getSwModel().getRunnables().get(rIndex));
		event.setName(name);
		event.setEventType(RunnableEventType.START);
		model.getEventModel().getEvents().add(event);

		Stimuli.createAperiodicStimulus(model,factory,name,event);
		
		
	}
}
