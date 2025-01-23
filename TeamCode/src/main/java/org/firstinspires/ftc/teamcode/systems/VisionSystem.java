package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.math.Transform;

import java.util.Optional;

public class VisionSystem
{
    private static final double CENTER_THRESHOLD = 1.0; // delta degree from the crosshair
    private static final Transform TARGET_FRAME = new Transform(0, 0, 0);

    private final Limelight3A limelight;

    public VisionSystem(HardwareMap hw, String device)
    {
        this.limelight = hw.get(Limelight3A.class, device);
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

    public boolean hasValidTarget()
    {
        LLResult res = limelight.getLatestResult();
        return res != null && res.isValid();
    }

    public Transform getTargetPose()
    {
        LLResult res = limelight.getLatestResult();
        if (res == null || !res.isValid())
            return Transform.INVALID;
        return new Transform(res.getTx(), res.getTy(), 0.0);
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