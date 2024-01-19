package app4mc.project.tool.java.Modules;

import java.math.BigInteger;
import java.util.List;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.PeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.StimuliModel;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;

public class Stimuli {
	public static void run(Amalthea model, AmaltheaFactory factory) {
		StimuliModel stimuliModel = model.getStimuliModel();

		for (Integer period : List.of(5, 10, 20)) {
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
}
