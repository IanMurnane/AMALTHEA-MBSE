package app4mc.project.tool.java.Modules;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.DataSize;
import org.eclipse.app4mc.amalthea.model.DataSizeUnit;
import org.eclipse.app4mc.amalthea.model.Label;

public class InputVariables {
	public static void run(Amalthea model, AmaltheaFactory factory) {
		// Label - <size in bits>
		Map<String, Integer> labelSizeMap = new HashMap<>();
		labelSizeMap.put("FrontCameraA", 32);
		labelSizeMap.put("FrontCameraB", 32);
		labelSizeMap.put("FrontCameraC", 32);
		labelSizeMap.put("RRearwardLookingSideCamera", 32);
		labelSizeMap.put("LRearwardLookingSideCamera", 32);
		labelSizeMap.put("RForwardLookingSideCamera", 32);
		labelSizeMap.put("LForwardLookingSideCamera", 32);
		labelSizeMap.put("RearviewCamera", 32);
		labelSizeMap.put("FrontUltrasonicA", 16);
		labelSizeMap.put("FrontUltrasonicB", 16);
		labelSizeMap.put("FrontUltrasonicC", 16);
		labelSizeMap.put("FrontUltrasonicD", 16);
		labelSizeMap.put("FrontUltrasonicE", 16);
		labelSizeMap.put("FrontUltrasonicF", 16);
		labelSizeMap.put("RearUltrasonicA", 16);
		labelSizeMap.put("RearUltrasonicB", 16);
		labelSizeMap.put("RearUltrasonicC", 16);
		labelSizeMap.put("RearUltrasonicD", 16);
		labelSizeMap.put("RearUltrasonicE", 16);
		labelSizeMap.put("RearUltrasonicF", 16);
		labelSizeMap.put("FrontRadar", 32);
		
		addLabels(model, factory, labelSizeMap);
	}
	
	private static void addLabels(Amalthea model, AmaltheaFactory factory, Map<String, Integer> labelSizeMap) {
		for (Map.Entry<String, Integer> entry : labelSizeMap.entrySet()) {
		    String labelName = entry.getKey();
		    int labelSize = entry.getValue();
		    
		    Label label = factory.createLabel();
		    label.setName(labelName);

		    DataSize dataSize = factory.createDataSize();
		    dataSize.setUnit(DataSizeUnit.BIT);
		    dataSize.setValue(BigInteger.valueOf(labelSize));

		    label.setSize(dataSize);
		    model.getSwModel().getLabels().add(label);
		}	
	}
}
