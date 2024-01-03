package app4mc.project.tool.java.Modules;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.app4mc.amalthea.model.ActivityGraph;
import org.eclipse.app4mc.amalthea.model.ActivityGraphItem;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.DataSize;
import org.eclipse.app4mc.amalthea.model.DataSizeUnit;
import org.eclipse.app4mc.amalthea.model.Group;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.RunnableSequencingConstraint;

public class Software {
	public static void run(Amalthea model, AmaltheaFactory factory) {
		// Label - <size in bits>
		Map<String, Integer> labelSizeMap = new HashMap<>();
		labelSizeMap.put("FrontRadar", 32);
		labelSizeMap.put("RearRadar", 32);
		labelSizeMap.put("LeftRadar", 32);
		labelSizeMap.put("RightRadar", 32);
		labelSizeMap.put("LongRangeFrontRadar", 32);
		labelSizeMap.put("FrontLiDAR", 32);
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
		
		labelSizeMap.put("RadarProcessed", 32);
		labelSizeMap.put("LiDARProcessed3DMap", 32);
		labelSizeMap.put("CameraProcessed", 32);
		labelSizeMap.put("UltrasonicProcessed", 32);
		labelSizeMap.put("IMUProcessed", 32);
		labelSizeMap.put("EnvironmentMap", 32);

		
		addLabels(model, factory, labelSizeMap);
		List<String> tasks =new ArrayList<>();
		tasks.add("SensorFusion");
		tasks.add("ForwardCollisionWarning");
		tasks.add("AutonomousEmergencyBraking");
		tasks.add("AdaptiveCruiseControl");
		tasks.add("LaneDepartureWarning");
		tasks.add("BlindSpotDetection");
		tasks.add("AdvancedDriver-AssistanceSystemDecision-Making");
		
		List<String> labelsOrdered = new ArrayList<>();
		Collections.addAll(labelsOrdered, "FrontRadar", "RearRadar", "LeftRadar", "RightRadar", "LongRangeFrontRadar", "FrontLiDAR", 
				"FrontCameraA", "FrontCameraB", "FrontCameraC", "RRearwardLookingSideCamera", "LRearwardLookingSideCamera", "RForwardLookingSideCamera", 
				"LForwardLookingSideCamera", "RearviewCamera", "FrontUltrasonicA", "FrontUltrasonicB", "FrontUltrasonicC", "FrontUltrasonicD",
				"FrontUltrasonicE", "FrontUltrasonicF", "RearUltrasonicA", "RearUltrasonicB", "RearUltrasonicC", "RearUltrasonicD", "RearUltrasonicE",
				"RearUltrasonicF", "RadarProcessed", "LiDARProcessed3DMap", "CameraProcessed", "UltrasonicProcessed", "IMUProcessed", "EnvironmentMap");
		
		
		addRunnables(model,factory);
		addTasks(model, factory, tasks);
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
	
	private static void addTasks(Amalthea model, AmaltheaFactory factory, List<String> tasks) {
		for (String t :  tasks) {
			Task task = factory.createTask();
			ActivityGraph A = factory.createActivityGraph();
			task.setName(t);
			task.setActivityGraph(A);

			model.getSwModel().getTasks().add(task);
			Group callSeq = factory.createGroup();
			callSeq.setName("CallSequence");
			model.getSwModel().getTasks().get(tasks.indexOf(t)).getActivityGraph().getItems().add(callSeq);
			//List<Runnable>  runnables = model.getSwModel().getRunnables();
			//model.getSwModel().getRunnables().add(runnables.get(0));
		}
	}
	private static void addRunnables(Amalthea model, AmaltheaFactory factory) {
		
		List<String> runnables =new ArrayList<>();
		runnables.add("RadarDataProcessing");
		runnables.add("LiDARDataProcessing");
		runnables.add("CameraImgProcessing");
		runnables.add("UltrasonicDataProcessing");
		runnables.add("InertialMeasurementUnit");
		runnables.add("SensorFusion");
		
		runnables.add("CollisonDetection");
		runnables.add("CDDecisonMaking");
		
		runnables.add("AEBDecisionMaking");
		
		runnables.add("TargetVehicleDetection");
		runnables.add("DistanceCalculation");
		
		runnables.add("LaneDetection");
		runnables.add("LDDecisionMaking");
		
		runnables.add("ObjectDetection");
		runnables.add("BSDecisionMaking");
		
		runnables.add("FCWarningTrigger");
		runnables.add("EmergencyBrakingControl");
		runnables.add("ACCControl");
		runnables.add("LDWarningTrigger");
		runnables.add("BSDWarningTrigger");
		
		for (String r : runnables ) {
			Runnable runnable = factory.createRunnable();
			runnable.setName(r);
			ActivityGraph A = factory.createActivityGraph();
			runnable.setActivityGraph(A);
			model.getSwModel().getRunnables().add(runnable);
		}
	
		System.out.println(model.getSwModel().getLabels().get(0).toString());
		
		
		
	}
}
