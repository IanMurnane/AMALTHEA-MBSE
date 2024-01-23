package app4mc.project.tool.java.Modules;

import java.util.List;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.RunnableEvent;
import org.eclipse.app4mc.amalthea.model.RunnableEventType;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.emf.common.util.EList;

public class Events {
	public static void run(Amalthea model, AmaltheaFactory factory) {
		addEvents(model, factory, 2, "Camera_Input_Change");
		addEvents(model, factory, 3, "Object_Detected");
		addEvents(model, factory, 5, "Any_Sensor_Data_Update");

		addEvents(model, factory, 6, "Obstacle_Detected_In_Path");
		addEvents(model, factory, 6, "Imminent_Collision_Detected");
		addEvents(model, factory, 9, "Lane_Departure_Detected");
		addEvents(model, factory, 10, "Vehicle_In_Blind_Spot_Detected");

		addEvents(model, factory, 11, "CD_Algorithm_Updated");
		addEvents(model, factory, 12, "CD_Algorithm_Updated2");
		addEvents(model, factory, 13, "ACC_Algorithm_Updated");
		addEvents(model, factory, 14, "LDW_Algorithm_Updated");
		addEvents(model, factory, 15, "BSD_Algorithm_Updated");

		//add event stimuli to aperiodic tasks
		EList<Stimulus> stimuli = model.getStimuliModel().getStimuli();
		model.getSwModel().getTasks().get(1).getStimuli().addAll(List.of(
				stimuli.get(2),
				stimuli.get(3),
				stimuli.get(4)
		));
		model.getSwModel().getTasks().get(2).getStimuli().add(stimuli.get(5));
		model.getSwModel().getTasks().get(3).getStimuli().add(stimuli.get(6));
		model.getSwModel().getTasks().get(5).getStimuli().add(stimuli.get(7));
		model.getSwModel().getTasks().get(6).getStimuli().add(stimuli.get(8));
		model.getSwModel().getTasks().get(7).getStimuli().addAll(List.of(
				stimuli.get(9),
				stimuli.get(10),
				stimuli.get(11),
				stimuli.get(12),
				stimuli.get(13)
		));
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
