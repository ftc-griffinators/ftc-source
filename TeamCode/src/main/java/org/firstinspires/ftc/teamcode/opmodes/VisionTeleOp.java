package org.firstinspires.ftc.teamcode.opmodes;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.math.Transform;
import org.firstinspires.ftc.teamcode.systems.VisionSystem;

@TeleOp(name = "Vision Test", group = "test")
public class VisionTeleOp extends LinearOpMode
{
    @SuppressLint("DefaultLocale")
    @Override
    public void runOpMode()
    {
        VisionSystem vision = new VisionSystem(hardwareMap, "limelight");
        telemetry.addLine("Vision Test Ready");
        telemetry.update();

        waitForStart();
        while (opModeIsActive())
        {
            if (gamepad1.a)
            {
                vision.setPipeline(0);
            }
            if (gamepad1.b)
            {
                vision.setPipeline(1);
            }

            telemetry.addData("Has Target", vision.hasValidTarget());

            Transform pose = vision.getTargetPose();
            if (pose != Transform.INVALID)
            {
                telemetry.addData("TX", String.format("%.2f°", pose.position.x));
                telemetry.addData("TY", String.format("%.2f°", pose.position.y));
                telemetry.addData("Area", String.format("%.2f%%", vision.getTargetArea()));
                telemetry.addData("Centered", vision.isTargetCentered());
                telemetry.addData("Staleness", vision.getStaleness() + "ms");
            }

            telemetry.update();
        }
    }
}