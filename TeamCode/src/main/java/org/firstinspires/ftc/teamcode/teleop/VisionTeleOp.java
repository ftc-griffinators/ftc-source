package org.firstinspires.ftc.teamcode.teleop;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.math.Transform;
import org.firstinspires.ftc.teamcode.systems.VisionSystem;
import org.firstinspires.ftc.vision.opencv.ColorRange;

@TeleOp(name = "Vision Test")
public class VisionTeleOp extends LinearOpMode
{
    @SuppressLint("DefaultLocale")
    @Override
    public void runOpMode() throws InterruptedException
    {
        VisionSystem vision;
        try
        {
            vision = new VisionSystem(hardwareMap, "Webcam1", "claw");
            telemetry.addData("Status", "Vision System Initialized");
        }
        catch (Exception e)
        {
            telemetry.addData("Error", "Failed to initialize: " + e.getMessage());
            telemetry.update();
            return;
        }

        vision.setTargetColor(ColorRange.BLUE);

        telemetry.addData("Controls", "A: Toggle Claw");
        telemetry.addData("Controls", "B: Switch Color (Blue/Red)");
        telemetry.addData("Controls", "Dpad: Adjust ROI");
        telemetry.addData("Controls", "Left Stick: Manual Control");
        telemetry.update();

        waitForStart();

        boolean isBlue = true;
        boolean lastAState = false;
        boolean lastBState = false;
        boolean clawClosed = false;

        while (opModeIsActive())
        {
            boolean currentAState = gamepad1.a;
            if (currentAState && !lastAState)
            {
                if (clawClosed)
                {
                    vision.openClaw();
                    clawClosed = false;
                }
                else
                {
                    vision.closeClaw();
                    clawClosed = true;
                }
            }
            lastAState = currentAState;

            boolean currentBState = gamepad1.b;
            if (currentBState && !lastBState)
            {
                isBlue = !isBlue;
                vision.setTargetColor(isBlue ? ColorRange.BLUE : ColorRange.RED);
            }
            lastBState = currentBState;

            if (gamepad1.dpad_up)
            {
                vision.setROI(0.5, 0.3, 0.5, 0.5);  // Top half
            }
            else if (gamepad1.dpad_down)
            {
                vision.setROI(0.5, 0.7, 0.5, 0.5);  // Bottom half
            }
            else if (gamepad1.dpad_left)
            {
                vision.setROI(0.25, 0.5, 0.5, 0.5); // Left half
            }
            else if (gamepad1.dpad_right)
            {
                vision.setROI(0.75, 0.5, 0.5, 0.5); // Right half
            }
            else if (gamepad1.y)
            {
                vision.setROI(0.5, 0.5, 1.0, 1.0);  // Full frame
            }

            // Get current target pose and alignment data
            Transform targetPose = vision.getTargetPose();
            Transform alignmentDelta = vision.getAlignmentDelta();

            // Basic telemetry
            telemetry.addData("Color Mode", isBlue ? "Blue" : "Red");
            telemetry.addData("Claw State", clawClosed ? "Closed" : "Open");

            // Enhanced pose telemetry
            if (targetPose != Transform.INVALID)
            {
                telemetry.addData("Target Found", "Yes");
                telemetry.addData("Position Error", String.format("X: %.1f, Y: %.1f", alignmentDelta.position.x, alignmentDelta.position.y));
                telemetry.addData("Angle Error", String.format("%.1f°", alignmentDelta.orientation.yaw));
                telemetry.addData("Centered", vision.isTargetCentered() ? "Yes" : "No");
            }
            else
            {
                telemetry.addData("Target Found", "No");
            }

            // Update vision system telemetry and display
            vision.updateTelemetry(telemetry);
            telemetry.update();

            // Control loop timing
            sleep(16);  // ~60Hz update rate
        }

        // Cleanup
        vision.shutdown();
    }
}