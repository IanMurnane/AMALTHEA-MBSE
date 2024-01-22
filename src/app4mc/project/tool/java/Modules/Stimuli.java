package app4mc.project.tool.java.Modules;

import java.math.BigInteger;
import java.util.List;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.EventStimulus;
import org.eclipse.app4mc.amalthea.model.ModeValueList;
import org.eclipse.app4mc.amalthea.model.PeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.RunnableEvent;
import org.eclipse.app4mc.amalthea.model.RunnableEventType;
import org.eclipse.app4mc.amalthea.model.StimuliModel;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.app4mc.amalthea.model.StimulusEvent;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;

public class Stimuli {
	public static void run(Amalthea model, AmaltheaFactory factory) {
		StimuliModel stimuliModel = model.getStimuliModel();

		for (Integer period : List.of(10, 50)) {
			PeriodicStimulus periodicStimulus = createPeriodicStimulus(factory, period);
			stimuliModel.getStimuli().add(periodicStimulus);
		}
	}

	private static PeriodicStimulus createPeriodicStimulus(AmaltheaFactory factory, int period) {
		PeriodicStimulus periodicStimulus = factory.createPeriodicStimulus();
		periodicStimulus.setName("Timer_" + period + "ms");
		periodicStimulus.setRecurrence(createTime(factory, period));
		periodicStimulus.setOffset(createTime(factory, 0));
		return periodicStimulus;
	}

	private static Time createTime(AmaltheaFactory factory, int period) {
		Time t = factory.createTime();
		t.setUnit(TimeUnit.MS);
		t.setValue((BigInteger.valueOf(period)));
		return t;
	}
	static void createAperiodicStimulus(Amalthea model, AmaltheaFactory factory, String name, RunnableEvent event) {
		EventStimulus aperiodicStimuli = factory.createEventStimulus();
		aperiodicStimuli.setName(name);
		aperiodicStimuli.getTriggeringEvents().add(event);
		model.getStimuliModel().getStimuli().add(aperiodicStimuli);
		
	
	}

	
}
