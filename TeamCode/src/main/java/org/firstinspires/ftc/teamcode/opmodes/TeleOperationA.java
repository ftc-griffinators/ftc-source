package org.firstinspires.ftc.teamcode.opmodes;


import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_270;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_45;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_ALIGNMENT_MIDDLE;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_ALIGNMENT_RIGHTMOST;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_EXTENDED;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_RETRACTED;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_ROT_FRONT;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_ROT_GROUND;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_ROT_MID;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Utility.ActionCondition;
import org.firstinspires.ftc.teamcode.Utility.CustomAction;
import org.firstinspires.ftc.teamcode.Utility.Sequencing;
import org.firstinspires.ftc.teamcode.Utility.Timing;
import org.firstinspires.ftc.teamcode.parts.Claw;
import org.firstinspires.ftc.teamcode.parts.Drive;
import org.firstinspires.ftc.teamcode.parts.Hanger;
import org.firstinspires.ftc.teamcode.parts.Slider;
import org.firstinspires.ftc.teamcode.systems.VisionSystem;


@TeleOp(name = "TeleOperationA", group = "Robot")
@Config
public class TeleOperationA extends LinearOpMode
{
    DcMotorEx frontLeft, frontRight, backLeft, backRight; //sliderLeft,sliderRight;
    // ServoImplEx clawGrab, clawRightRot, clawLeftRot, clawExtend,clawAlignment, clawPitch;


    int boxScoringStateTop = 0;
    int clawGrabState = 0;
    int subClearing = 0;
    int init = 0;


    @Override
    public void runOpMode() throws InterruptedException
    {
        telemetry.addData("Status", "Initialized");
        telemetry.update();


        Drive drive = new Drive(hardwareMap);
        Claw claw = new Claw(hardwareMap);
        Slider slider = new Slider(hardwareMap);
        VisionSystem vision = new VisionSystem(hardwareMap, 0);
        Hanger hanger = new Hanger(hardwareMap);
        Timing timer = new Timing();

        Slider encoder = slider.getSliderEncoder(hardwareMap);


        hanger.initHanger();


        CustomAction align = new CustomAction(claw::alignerReset,
                () -> claw.orientationAligning(vision));

        ActionCondition alignCondition = new ActionCondition(() -> timer.delay(200),
                () -> claw.startAligning);


        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        //Field centric mecanum drive
        while (opModeIsActive())
        {
            if (init == 0)
            {

                //claw.teleOpInit();
                slider.initSlider();
                init = 1;
            }

            telemetry.addData("Left encoder",
                    encoder.leftSliderEncoder.getPositionAndVelocity().position);
            telemetry.addData("Right encoder",
                    encoder.rightSliderEncoder.getPositionAndVelocity().position);

            telemetry.update();


            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;


            drive.mecanumDriving(x, y, turn, 0.8);


            if (gamepad1.left_stick_button)
            {
                Sequencing.allActionsStatus.put(align, true);
            }
            if (Sequencing.allActionsStatus.get(align))
            {
                Sequencing.runInParallel(align, alignCondition);
            }

            if (gamepad1.a)
            {
                hanger.hang();
            }

            if (gamepad1.b)
            {
                hanger.resetHanger();
                drive.stopDrive();
            }


            //Grabbing
            if (gamepad1.right_bumper)
            {
                switch (clawGrabState)
                {
                    case 0:
                        claw.rotateArm(CLAW_ROT_GROUND);
                        Thread.sleep(500);
                        claw.grab();
                        Thread.sleep(500);
                        claw.rotateArm(CLAW_ROT_FRONT);
                        Thread.sleep(300);
                        clawGrabState = 1;
                        break;
                    case 1:
                        claw.release();
                        Thread.sleep(300);
                        clawGrabState = 0;
                        break;

                }
            }


            //Toggle sub
            if (gamepad1.left_bumper)
            {
                switch (subClearing)
                {
                    case 0:
                        claw.grabPrep();
                        Thread.sleep(300);
                        subClearing = 1;
                        break;
                    case 1:
                        claw.clearSub();
                        Thread.sleep(300);
                        subClearing = 0;
                        break;
                }
            }

            if (gamepad1.start)
            {
                drive.pose = new Pose2d(0, 0, 0);
            }


            if (gamepad1.dpad_up)
            {
                claw.clawAlignment.setPosition(CLAW_ALIGNMENT_MIDDLE);
            }
            if (gamepad1.dpad_left)
            {
                claw.clawAlignment.setPosition(CLAW_270);
            }
            if (gamepad1.dpad_right)
            {
                claw.clawAlignment.setPosition(CLAW_45);
            }
            if (gamepad1.dpad_down)
            {
                claw.clawAlignment.setPosition(CLAW_ALIGNMENT_RIGHTMOST);
            }


            if (gamepad2.left_bumper)
            {
                claw.clawExtend.setPosition(CLAW_EXTENDED);
            }
            if (gamepad2.right_bumper)
            {
                claw.clawExtend.setPosition(CLAW_RETRACTED);
            }


            //Grab success Basket scoring phase
            if (gamepad2.y)
            {
                switch (boxScoringStateTop)
                {
                    case 0:
                        claw.clawAlignment.setPosition(CLAW_ALIGNMENT_RIGHTMOST);
                        slider.sliderExtensionTopBox();
                        Thread.sleep(1000);
                        claw.boxScoring();
                        boxScoringStateTop++;
                        break;
                    case 1:
                        claw.release();
                        Thread.sleep(300);
                        claw.rotateArm(CLAW_ROT_MID);
                        claw.retract();
                        slider.sliderRetraction();
                        claw.rotateArm(CLAW_ROT_FRONT);
                        claw.grabPrep();

                        boxScoringStateTop = 0;
                        ;
                        break;
                }
            }


        }
    }
}

