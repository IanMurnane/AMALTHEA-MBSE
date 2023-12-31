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
		Map<String, Integer> labelSizeMap = new HashMap<>();
		labelSizeMap.put("MyLabel1", 16);
		labelSizeMap.put("MyLabel2", 8);
		
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
