package org.firstinspires.ftc.teamcode.tests;


import static org.firstinspires.ftc.teamcode.parts.Claw.*;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.parts.Claw;
import org.firstinspires.ftc.teamcode.parts.Slider;
import org.firstinspires.ftc.teamcode.systems.VisionSystem;

@TeleOp(name ="ServoTest",group = "Tests")
@Config
public class ServoTest extends LinearOpMode {


    public int wrong;
    public int excess;





    @Override
    public void runOpMode() throws InterruptedException {

        VisionSystem vision = new VisionSystem(hardwareMap);


        vision.setPipeline(0);

        TelemetryPacket p=new TelemetryPacket();

        FtcDashboard dashboard=FtcDashboard.getInstance();



        Slider slider=new Slider(hardwareMap);

        Claw claw =new Claw(hardwareMap);

        Slider encoder= slider.getSliderEncoder(hardwareMap);


        slider.initSlider();


        waitForStart();
        while (opModeIsActive()){


            if (gamepad1.left_bumper){
             claw.clawGrab.setPosition(CLAW_GRAB);
            }
            if (gamepad1.right_bumper){
                claw.clawGrab.setPosition(CLAW_RELEASE);
            }
            if (gamepad1.left_stick_button){
                claw.clawExtend.setPosition(CLAW_EXTENDED);
            }
            if (gamepad1.right_stick_button){
                claw.clawExtend.setPosition(CLAW_RETRACTED);
            }
            if (gamepad1.dpad_left){
                claw.clawAlignment.setPosition(CLAW_270);
            }
            if (gamepad1.dpad_right){
                claw.clawAlignment.setPosition(CLAW_45);
            }
            if (gamepad1.dpad_up){
                claw.clawAlignment.setPosition(CLAW_ALIGNMENT_MIDDLE);
            }
            if (gamepad1.dpad_down){
                claw.clawAlignment.setPosition(CLAW_ALIGNMENT_RIGHTMOST);
            }

            if (gamepad1.a){
                claw.clawLeftRot.setPosition(CLAW_ROT_GROUND);
                claw.clawRightRot.setPosition(CLAW_ROT_GROUND);
            }
            if (gamepad1.x){
                claw.clawRightRot.setPosition(CLAW_ROT_FRONT);
                claw.clawLeftRot.setPosition(CLAW_ROT_FRONT);

            }
            if (gamepad1.y){
                claw.clawRightRot.setPosition(CLAW_ROT_BACK);
                claw.clawLeftRot.setPosition(CLAW_ROT_BACK);
            }
            if (gamepad2.dpad_down){
                claw.clawPitch.setPosition(CLAW_PITCH_BOT);
            }
            if (gamepad2.dpad_left){
                claw.clawPitch.setPosition(CLAW_PITCH_MID);
            }
            if (gamepad2.dpad_up){
                claw.clawPitch.setPosition(CLAW_PITCH_SCORE);
            }

            if (gamepad2.y){
               slider.sliderExtensionTopBox();
            }
            if (gamepad2.a){
                slider.sliderRetraction();
            }
/*
            Transform pose = vision.getTargetDiffPose(corners);
            if (vision.hasValidTarget())
            {
                telemetry.addData("Valid",vision.hasValidTarget());
                telemetry.addData("Corner",corners);
                telemetry.addData("num of wrong",wrong);
                telemetry.addData("Num of corners",vision.numOfCorners);
                telemetry.addData("Corners overflow",excess);


                telemetry.addData("angleDelta",pose.orientation.yaw);
                telemetry.addData("TX", String.format("%.2f°", pose.position.x));
                telemetry.addData("TY", String.format("%.2f°", pose.position.y));
                telemetry.addData("Is aligned",vision.isTargetAligned(corners));

                telemetry.addData("Is centered",vision.isTargetCentered(corners));


                telemetry.addData("Orientation",pose.orientation.yaw*(180/Math.PI));

            }




 */



            telemetry.addData("clawGrab.CLAW_GRAB: a",CLAW_GRAB);
            telemetry.addData("clawGrab.CLAW_RELEASE: b",CLAW_RELEASE);
            telemetry.addData("clawExtend.CLAW_EXTENDED: x",CLAW_EXTENDED);
            telemetry.addData("clawExtend.CLAW_RETRACTED: Y",CLAW_RETRACTED);
            telemetry.addData("clawAlignment.LEFT: left_bumper",CLAW_ALIGNMENT_LEFTMOST);
            telemetry.addData("clawAlignment.RIGHT: right_bumper",CLAW_ALIGNMENT_RIGHTMOST);
            telemetry.addData("clawLeftRot.FRONT: dpad_up",CLAW_ROT_FRONT);
            telemetry.addData("clawLeftRot.MID: dpad_down",CLAW_ROT_MID);
            telemetry.addData("clawRightRot.FRONT: dpad_left",CLAW_ROT_FRONT);
            telemetry.addData("clawRightRot.MIDDLE: dpad_right",CLAW_ROT_MID);


            p.put("clawGrab.CLAW_GRAB: a",CLAW_GRAB);
            p.put("clawGrab.CLAW_RELEASE: b",CLAW_RELEASE);
            p.put("clawExtend.CLAW_EXTENDED: x",CLAW_EXTENDED);
            p.put("clawExtend.CLAW_RETRACTED: Y",CLAW_RETRACTED);
            p.put("clawAlignment.LEFT: left_bumper",CLAW_ALIGNMENT_LEFTMOST);
            p.put("clawAlignment.RIGHT: right_bumper",CLAW_ALIGNMENT_RIGHTMOST);
            p.put("clawLeftRot.FRONT: dpad_up",CLAW_ROT_FRONT);
            p.put("clawLeftRot.MID: dpad_down",CLAW_ROT_MID);
            p.put("clawRightRot.FRONT: dpad_left",CLAW_ROT_FRONT);
            p.put("clawRightRot.MIDDLE: dpad_right",CLAW_ROT_MID);



            dashboard.sendTelemetryPacket(p);



            telemetry.addData("lefSlider",encoder.leftSliderEncoder.getPositionAndVelocity().position);
            telemetry.addData("rightSlider",encoder.rightSliderEncoder.getPositionAndVelocity().position);
            telemetry.update();







        }



    }

}