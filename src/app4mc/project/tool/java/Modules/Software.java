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
import org.eclipse.app4mc.amalthea.model.LabelAccess;
import org.eclipse.app4mc.amalthea.model.LabelAccessEnum;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.RunnableCall;
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
		
		labelSizeMap.put("Gyroscope", 16);
		labelSizeMap.put("Accelerometer", 16);
		labelSizeMap.put("Magnetometer", 16);
		
		labelSizeMap.put("CollisionDetectiontOutput", 8);
		labelSizeMap.put("TargetVehiclDetectOutput", 8);
		labelSizeMap.put("DistanceCalcOutput", 16);
		labelSizeMap.put("LaneDetectionOutput", 8);
		labelSizeMap.put("ObjectDetectionOutput", 8);
		
		labelSizeMap.put("AudioWarning", 2);
		labelSizeMap.put("HapticFeedbackWarning", 2);
		labelSizeMap.put("DashboardDisplayWarning", 8);
		labelSizeMap.put("EmergencyBrake", 8);
		labelSizeMap.put("Throttle", 16);
		labelSizeMap.put("Steering", 16);
		labelSizeMap.put("SideMirrorLights", 2);
		
		

		
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
				"RearUltrasonicF", "RadarProcessed", "LiDARProcessed3DMap", "CameraProcessed", "UltrasonicProcessed", "IMUProcessed", "EnvironmentMap",
				"Gyroscope", "Accelerometer", "Magnetometer", "CollisionDetectiontOutput", "TargetVehiclDetectOutput", "DistanceCalcOutput", "LaneDetectionOutput", 
				"ObjectDetectionOutput", "AudioWarning", "HapticFeedbackWarning", "DashboardDisplayWarning", "EmergencyBrake", "Throttle", "Steering", 
				"SideMirrorLights");
		
		
		addRunnables(model,factory,labelsOrdered);
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

		}
		
		addRunnableCall(model,factory,0,0);
		addRunnableCall(model,factory,1,0);
		addRunnableCall(model,factory,2,0);
		addRunnableCall(model,factory,3,0);
		addRunnableCall(model,factory,4,0);
		addRunnableCall(model,factory,5,0);
		
		addRunnableCall(model,factory,6,1);
		addRunnableCall(model,factory,6,2);
		
		addRunnableCall(model,factory,7,3);
		addRunnableCall(model,factory,8,3);
		
		addRunnableCall(model,factory,9,4);
		addRunnableCall(model,factory,10,5);
		
		addRunnableCall(model,factory,11,6);
		addRunnableCall(model,factory,12,6);
		addRunnableCall(model,factory,13,6);
		addRunnableCall(model,factory,14,6);
		addRunnableCall(model,factory,15,6);
		
	
	}
	private static void addRunnables(Amalthea model, AmaltheaFactory factory, List<String> labelsOrdered) {
		
		List<String> runnables =new ArrayList<>();
		runnables.add("RadarDataProcessing");
		runnables.add("LiDARDataProcessing");
		runnables.add("CameraImgProcessing");
		runnables.add("UltrasonicDataProcessing");
		runnables.add("InertialMeasurementUnit");
		runnables.add("SensorFusionAlgorithm");
		
		runnables.add("CollisonDetection");
	
		runnables.add("TargetVehicleDetection");
		runnables.add("DistanceCalculation");
		
		runnables.add("LaneDetection");
		
		runnables.add("ObjectDetection");
		
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
		
		//model.getSwModel().getRunnables().get(0);
		addLabelsToRunnables(model.getSwModel().getRunnables().get(0),model,factory,0,4,26,26,labelsOrdered);
		addLabelsToRunnables(model.getSwModel().getRunnables().get(1),model,factory,5,5,27,27,labelsOrdered);
		addLabelsToRunnables(model.getSwModel().getRunnables().get(2),model,factory,6,13,28,28,labelsOrdered);
		addLabelsToRunnables(model.getSwModel().getRunnables().get(3),model,factory,14,25,29,29,labelsOrdered);
		addLabelsToRunnables(model.getSwModel().getRunnables().get(4),model,factory,32,34,30,30,labelsOrdered);
		addLabelsToRunnables(model.getSwModel().getRunnables().get(5),model,factory,26,30,31,31,labelsOrdered);
		
		addLabelsToRunnables(model.getSwModel().getRunnables().get(6),model,factory,31,31,35,35,labelsOrdered);
		addLabelsToRunnables(model.getSwModel().getRunnables().get(7),model,factory,31,31,36,36,labelsOrdered);
		addLabelsToRunnables(model.getSwModel().getRunnables().get(8),model,factory,31,31,36,36,labelsOrdered);
		addLabelsToRunnables(model.getSwModel().getRunnables().get(8),model,factory,36,36,37,37,labelsOrdered);
		addLabelsToRunnables(model.getSwModel().getRunnables().get(9),model,factory,31,31,38,38,labelsOrdered);
		addLabelsToRunnables(model.getSwModel().getRunnables().get(10),model,factory,31,31,39,39,labelsOrdered);
		
		addLabelsToRunnables(model.getSwModel().getRunnables().get(11),model,factory,35,35,40,42,labelsOrdered);
		addLabelsToRunnables(model.getSwModel().getRunnables().get(12),model,factory,35,35,40,43,labelsOrdered);
		addLabelsToRunnables(model.getSwModel().getRunnables().get(13),model,factory,36,37,40,40,labelsOrdered);
		addLabelsToRunnables(model.getSwModel().getRunnables().get(13),model,factory,-100,-100,44,44,labelsOrdered);
		addLabelsToRunnables(model.getSwModel().getRunnables().get(14),model,factory,38,38,40,42,labelsOrdered);
		addLabelsToRunnables(model.getSwModel().getRunnables().get(14),model,factory,-100,-100,45,45,labelsOrdered);
		addLabelsToRunnables(model.getSwModel().getRunnables().get(15),model,factory,39,39,40,40,labelsOrdered);
		addLabelsToRunnables(model.getSwModel().getRunnables().get(15),model,factory,-100,-100,46,46,labelsOrdered);
		
	
	}

	private static void addLabelsToRunnables(Runnable runnable, Amalthea model, AmaltheaFactory factory, int startIn, int endIn, int startOut, int endOut, List<String> labelsOrdered) {

		int counter; 
		if(startIn != -100) {
			for(int i=startIn; i<=endIn; i++) {
				LabelAccess l = factory.createLabelAccess();
				counter = 0;
				for (Label label : model.getSwModel().getLabels()) {
		            if (label.getName().equals(labelsOrdered.get(i))) {
		                break; 
		            }
		            counter ++;
				}
		            
				l.setData(model.getSwModel().getLabels().get(counter));
				l.setAccess(LabelAccessEnum.READ);
				runnable.getActivityGraph().getItems().add(l);
				
			}
		}
		
			for(int i=startOut; i<=endOut; i++) {
				LabelAccess l = factory.createLabelAccess();
				counter = 0;
				for (Label label : model.getSwModel().getLabels()) {
		            if (label.getName().equals(labelsOrdered.get(i))) {
		                break; 
		            }
		            counter ++;
				}
		            
				l.setData(model.getSwModel().getLabels().get(counter));
				l.setAccess(LabelAccessEnum.WRITE);
				runnable.getActivityGraph().getItems().add(l);
				
			}
	}
	private static void addRunnableCall(Amalthea model, AmaltheaFactory factory, int rIndex, int tIndex) {
		
		RunnableCall rc = factory.createRunnableCall();
		rc.setRunnable(model.getSwModel().getRunnables().get(rIndex));
		
		model.getSwModel().getTasks().get(tIndex).getActivityGraph().getItems().add(rc);
	}
}
