package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.math.Transform;

import java.util.List;
import java.util.Optional;

public class VisionSystem
{
    private static final double CENTER_THRESHOLD = 1.0; // delta degree from the crosshair
    private static final Transform TARGET_FRAME = new Transform(0, 0, 0);
    private final Limelight3A limelight;

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
    public int cornerParing(){
        return 1;
}
public double testOrientation(){
    LLResult res = limelight.getLatestResult();
    LLResultTypes.ColorResult colorResult=res.getColorResults().get(0);
    List<List<Double>> corners=colorResult.getTargetCorners();
    double deltaX1=corners.get(0).get(0)-corners.get(2).get(0);
    double deltaY1=corners.get(0).get(1)-corners.get(2).get(1);
    double deltaX2=corners.get(1).get(0)-corners.get(3).get(0);
    double deltaY2=corners.get(1).get(1)-corners.get(3).get(1);
    return (Math.atan2(deltaY1,deltaX1)+Math.atan2(deltaY2,deltaX2))/2;
}

    public double getTargetOrientation(){
        LLResult res = limelight.getLatestResult();
        LLResultTypes.ColorResult colorResult=res.getColorResults().get(0);
        List<List<Double>> corners=colorResult.getTargetCorners();
        double deltaX=corners.get(0).get(0)-corners.get(2).get(0);
        double deltaY=corners.get(0).get(1)-corners.get(2).get(1);
        return Math.atan2(deltaY,deltaX);

    }

    public Transform getTargetPose()
    {
        LLResult res = limelight.getLatestResult();
        LLResultTypes.ColorResult colorResult=res.getColorResults().get(0);
        if (res == null || !res.isValid())
            return Transform.INVALID;
        return new Transform(res.getTx(), res.getTy(), getTargetOrientation());
    }

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