package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Utility.ActionStep;
import org.firstinspires.ftc.teamcode.parts.Claw;
import org.firstinspires.ftc.teamcode.parts.Drive;
import org.firstinspires.ftc.teamcode.parts.Slider;
import org.firstinspires.ftc.teamcode.systems.VisionSystem;


public class TeleOpRed extends LinearOpMode
{


    public void ne(ActionStep c)
    {

    }

    @Override
    public void runOpMode() throws InterruptedException
    {

        Slider slider = new Slider(hardwareMap);
        Claw claw = new Claw(hardwareMap);
        Drive drive = new Drive(hardwareMap);
        VisionSystem vision = new VisionSystem(hardwareMap, 2);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        while (opModeIsActive())
        {
            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;


            drive.mecanumDriving(x, y, turn, 0.8);

            if (gamepad1.start)
            {
                drive.pose = new Pose2d(0, 0, 0);
            }

        }
    }
}
