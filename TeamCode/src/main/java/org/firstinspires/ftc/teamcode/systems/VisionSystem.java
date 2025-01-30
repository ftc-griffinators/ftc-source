package org.firstinspires.ftc.teamcode.systems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.math.Transform;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Config
public class VisionSystem

{
    public static int visonError=0;

    public static  double CENTER_THRESHOLD = 5.0; // delta degree from the crosshair
    public static  double ANGLE_THRESHOLD= 0.2; //5 degree within the correct alignment
    private static final Transform TARGET_FRAME = new Transform(0, 0, 0);
    private final Limelight3A limelight;
    public static final double DEGREE_TO_INCHES=0.5;

    public int numOfCorners=0;
    public VisionSystem(HardwareMap hardwareMap, int pipeline)
    {
        this.limelight = hardwareMap.get(Limelight3A.class, "limeLight");
        limelight.pipelineSwitch(pipeline);
        this.init();

    }


    public VisionSystem(HardwareMap hardwareMap)
    {
        this.limelight = hardwareMap.get(Limelight3A.class, "limeLight");
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

    public List<List<Double>> getCorners(){
        LLResult result = limelight.getLatestResult();
        if (result!=null ) {
            List<LLResultTypes.ColorResult> list = result.getColorResults();
            if( hasValidTarget() | !list.isEmpty() ){
                if (list.get(0).getTargetCorners().size()==4){
                    return new ArrayList<>(list.get(0).getTargetCorners()) ;
                }
            }
        }
        return new ArrayList<>();
    }


    private List<Double> midpoint(int corner1, int corner2, List<List<Double>> corners){
        List<Double> point=new ArrayList<>(2);
        point.add((corners.get(corner1).get(0)+corners.get(corner2).get(0))/2    );
        point.add( (corners.get(corner1).get(1)+corners.get(corner2).get(1))/2   );
        return point;
    }



    public List<List<Double>> twoMidpoints(List<List<Double>> corners){
        List<List<Double>> end=new ArrayList<>();

         numOfCorners=corners.size();
         if (corners.size()!=4){
             visonError++;
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
//Corners from getTargetCorners() are labeled 0,1,2,3. Corner 0 and 2 are always diagonal to each other,
    // Corner 1 and 3 are always diagonal to each other. Label name is the index of the getTargetCorners()

    public double getCamRelativeTargetOrientation(List<List<Double>> corners)
    {
       if (corners == null || corners.size() != 4) {
           return Double.NaN;
       }

        List<List<Double>> points = twoMidpoints(corners);
        if (points == null || points.size() != 2)
            return Double.NaN;

        try
        {
            double dx = points.get(0).get(0) - points.get(1).get(0);
            double dy = points.get(0).get(1) - points.get(1).get(1);

            double angle = Math.atan2(dy, dx);

            if (angle < (-Math.PI/2))
                return Math.PI + angle;
            if (angle > (Math.PI/2))
                return (-Math.PI) + angle;
            return angle;
        }
        catch (Exception e)
        {
            return Double.NaN;
        }
    }



    public String turnLeftOrRight(List<List<Double>> corners){
        double angle=getCamRelativeTargetOrientation(corners);
        if (angle>0){
            return "left";
        }
        if (angle<0){
            return "right";
        }

        return "error";
    }

    public Transform getTargetDiffPose(List<List<Double>> corners) {
        if (corners.size()!=4)
        {
            return Transform.INVALID;
        }
        LLResult res = limelight.getLatestResult();
        double orientation = getCamRelativeTargetOrientation(corners);
        if (!hasValidTarget()|| Double.isNaN(orientation)){return Transform.INVALID;}
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

    public boolean isTargetCentered(List<List<Double>> corners) {
        Transform crosshairAndAngle = getTargetDiffPose(corners);
        if (crosshairAndAngle == Transform.INVALID) {return false;}
        return (Math.abs(crosshairAndAngle.position.x) < CENTER_THRESHOLD) && (Math.abs(crosshairAndAngle.position.y) < CENTER_THRESHOLD) ;
    }

    public boolean isTargetAligned(List<List<Double>> corners){
        Transform crosshairAndAngle = getTargetDiffPose(corners);
        if (crosshairAndAngle == Transform.INVALID) {return false;}

        return (Math.abs(crosshairAndAngle.orientation.yaw ))<ANGLE_THRESHOLD ;
    }

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