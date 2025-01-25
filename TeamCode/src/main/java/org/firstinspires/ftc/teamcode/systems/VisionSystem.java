package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.math.Transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class VisionSystem
{
    private static final double CENTER_THRESHOLD = 1.0; // delta degree from the crosshair
    private static final double LENGTH_EQUAL_THRESHOLD=0.1;
    private static final Transform TARGET_FRAME = new Transform(0, 0, 0);
    private final Limelight3A limelight;

    public int numOfCorners=0;
    public VisionSystem(HardwareMap hardwareMap, String deviceName, int pipeline)
    {
        this.limelight = hardwareMap.get(Limelight3A.class, deviceName);
        limelight.pipelineSwitch(pipeline);
        this.init();

    }


    public VisionSystem(HardwareMap hardwareMap, String device)
    {
        this.limelight = hardwareMap.get(Limelight3A.class, device);
        limelight.pipelineSwitch(0);
        this.init();

    }

    private void init()
    {
        limelight.setPollRateHz(60);
        limelight.start();
    }

    public void setPipeline(int n)
    {
        limelight.pipelineSwitch(n);
    }
    public void pausePipeline(){
        limelight.pause();
    }
    public void stopPipeline(){
        limelight.stop();
    }
    public boolean hasValidTarget()
    {
        LLResult res = limelight.getLatestResult();
        return res != null && res.isValid();
    }

    private List<Double> midpoint(int corner1, int corner2, List<List<Double>> corners){
        List<Double> point=new ArrayList<>(2);
        point.add(    (corners.get(corner1).get(0)+corners.get(corner2).get(0))/2    );
        point.add( (corners.get(corner1).get(1)+corners.get(corner2).get(1))/2   );
        return point;
    }



    public List<List<Double>> twoMidpoints(List<List<Double>> corners){
        List<List<Double>> end=new ArrayList<>();

         numOfCorners=corners.size();
         if (corners.size()<4){
             return null;
         }
         double d0_1=Math.sqrt(Math.pow(corners.get(0).get(0)-corners.get(1).get(0),2) + Math.pow(corners.get(0).get(1)-corners.get(1).get(1),2));
         double d0_3 =Math.sqrt(Math.pow(corners.get(0).get(0)-corners.get(3).get(0),2) + Math.pow(corners.get(0).get(1)-corners.get(3).get(1),2));
         if (d0_1< d0_3){
             List<Double> midPoint1= midpoint(0,1,corners);
             List<Double> midPoint2= midpoint(2,3,corners);
             end.add(midPoint1);
             end.add(midPoint2);
            return end;
         }
         else {
             List<Double> midPoint1= midpoint(0,3,corners);
             List<Double> midPoint2= midpoint(1,2,corners);
             end.add(midPoint1);
             end.add(midPoint2);
             return end;

         }
}

public List<List<Double>> getCorners(){
        if (hasValidTarget()){
            return limelight.getLatestResult().getColorResults().get(0).getTargetCorners();
        }
        return null;
       }


    public double getTargetOrientation(List<List<Double>> corners){

       List<List<Double>> points=twoMidpoints(corners);
       if (points==null) {
           return Double.NaN;
       }
        double deltaX1=points.get(0).get(0)-points.get(1).get(0);
        double deltaY1=points.get(0).get(1)-points.get(1).get(1);

        return Math.atan2(deltaY1,deltaX1);
    }

    public Transform getTargetPose(List<List<Double>> corners)
    {
        LLResult res = limelight.getLatestResult();
        double orientation = getTargetOrientation(corners);
        if (res == null || !res.isValid() || Double.isNaN(orientation)){return Transform.INVALID;}
        return new Transform(res.getTx(), res.getTy(), orientation);
    }
/*
    public Transform getAlignmentDelta()
    {
        Transform currentPose = getTargetPose();
        if (currentPose == Transform.INVALID)
            return Transform.INVALID;
        return currentPose.delta(TARGET_FRAME);
    }
    public boolean isTargetCentered()
    {
        Transform delta = getAlignmentDelta();
        if (delta == Transform.INVALID)
            return false;
        return Math.abs(delta.position.x) < CENTER_THRESHOLD;
    }
*/
    public double getTargetArea()
    {
        return Optional.ofNullable(limelight.getLatestResult())
                .filter(LLResult::isValid)
                .map(LLResult::getTa)
                .orElse(0.0);
    }

    public long getStaleness()
    {
        LLResult result = limelight.getLatestResult();
        return result != null ? result.getStaleness() : -1;
    }
}