package org.firstinspires.ftc.teamcode.griffinators;

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

import org.firstinspires.ftc.teamcode.math.Transform;
import org.firstinspires.ftc.teamcode.parts.Claw;
import org.firstinspires.ftc.teamcode.parts.Slider;
import org.firstinspires.ftc.teamcode.systems.VisionSystem;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@TeleOp(name ="ServoTest",group = "Tests")
@Config
public class ServoTest extends LinearOpMode {
    public static double CLAW_EXTENDED=0.4;
    public static double CLAW_RETRACTED=0;
    public static double CLAW_ROT_MID=0.15;
    public static double CLAW_ROT_FRONT=0.06;
    public static double CLAW_ROT_BACK=0.4;
    public static double CLAW_ROT_GROUND=0;
    public static double CLAW_GRAB=0;
    public static double CLAW_RELEASE=0.09;
    public static double CLAW_PITCH_TOP=0.5;
    public static double CLAW_PITCH_MID=0.265;
    public static double CLAW_PITCH_BOT=0.02;
    public static double CLAW_PITCH_SCORE=0.4;
    public static double CLAW_ALIGNMENT_LEFTMOST=0.8;
    public static double CLAW_ALIGNMENT_RIGHTMOST=0.19;
    public static double CLAW_ALIGNMENT_MIDDLE=0.49;
    public static double CLAW_270=0.65;
    public static double CLAW_45=0.32;

    public int wrong;
    public int excess;



    ServoImplEx clawGrab, clawRightRot, clawLeftRot, clawExtend,clawAlignment, clawPitch;;
    DcMotorEx  sliderLeft,sliderRight;
    Encoder leftSliderEncoder,rightSliderEncoder;


    @Override
    public void runOpMode() throws InterruptedException {

        VisionSystem vision = new VisionSystem(hardwareMap, "limeLight");


        vision.setPipeline(0);

        TelemetryPacket p=new TelemetryPacket();

        FtcDashboard dashboard=FtcDashboard.getInstance();

        sliderLeft=hardwareMap.get(DcMotorEx.class,"leftSlider");
        sliderLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        //Control Hub port 2
        sliderRight=hardwareMap.get(DcMotorEx.class,"rightSlider");

        sliderLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sliderRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        sliderRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        sliderLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightSliderEncoder=new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class,"rightSlider")));
        leftSliderEncoder=new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class,"leftSlider")));
        leftSliderEncoder.setDirection(DcMotorSimple.Direction.REVERSE);



        clawExtend = hardwareMap.get(ServoImplEx.class,"clawExtend");

        //"clawRightRot" is port 0 on control hub
        clawRightRot = hardwareMap.get(ServoImplEx.class,"clawRightRot");

        // "clawLeftRot" is port 1 on control hub
        clawLeftRot = hardwareMap.get(ServoImplEx.class,"clawLeftRot");
        //"clawGrab" is port 3

        clawGrab=hardwareMap.get(ServoImplEx.class,"clawGrab");

        clawAlignment=hardwareMap.get(ServoImplEx.class,"clawAlignment");

        clawPitch=hardwareMap.get(ServoImplEx.class,"clawPitch");

        clawExtend.setPwmRange(new PwmControl.PwmRange(500,2500));

        clawLeftRot.setPwmRange(new PwmControl.PwmRange(500,2500));

        clawRightRot.setPwmRange(new PwmControl.PwmRange(500,2500));

        clawGrab.setPwmRange(new PwmControl.PwmRange(500,2500));

        clawAlignment.setPwmRange(new PwmControl.PwmRange(500,2500));

        clawPitch.setPwmRange(new PwmControl.PwmRange(500,2500));

        clawLeftRot.setDirection(Servo.Direction.REVERSE);

        clawExtend.setDirection(Servo.Direction.REVERSE);


        Slider slider=new Slider(hardwareMap);




        slider.sliderInit();


        waitForStart();
        while (opModeIsActive()){
            if (gamepad1.left_bumper){
             clawGrab.setPosition(CLAW_GRAB);
            }
            if (gamepad1.right_bumper){
                clawGrab.setPosition(CLAW_RELEASE);
            }
            if (gamepad1.left_stick_button){
                clawExtend.setPosition(CLAW_EXTENDED);
            }
            if (gamepad1.right_stick_button){
                clawExtend.setPosition(CLAW_RETRACTED);
            }
            if (gamepad1.dpad_left){
                clawAlignment.setPosition(CLAW_270);
            }
            if (gamepad1.dpad_right){
                clawAlignment.setPosition(CLAW_45);
            }
            if (gamepad1.dpad_up){
                clawAlignment.setPosition(CLAW_ALIGNMENT_MIDDLE);
            }
            if (gamepad1.dpad_down){
                clawAlignment.setPosition(CLAW_ALIGNMENT_RIGHTMOST);
            }

            if (gamepad1.a){
                clawLeftRot.setPosition(CLAW_ROT_GROUND);
                clawRightRot.setPosition(CLAW_ROT_GROUND);
            }
            if (gamepad1.x){
                clawRightRot.setPosition(CLAW_ROT_FRONT);
                clawLeftRot.setPosition(CLAW_ROT_FRONT);

            }
            if (gamepad1.y){
                clawRightRot.setPosition(CLAW_ROT_BACK);
                clawLeftRot.setPosition(CLAW_ROT_BACK);
            }
            if (gamepad2.dpad_down){
                clawPitch.setPosition(CLAW_PITCH_BOT);
            }
            if (gamepad2.dpad_left){
              clawPitch.setPosition(CLAW_PITCH_MID);
            }
            if (gamepad2.dpad_up){
                clawPitch.setPosition(CLAW_PITCH_SCORE);
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

            telemetry.update();

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



            telemetry.addData("lefSlider",leftSliderEncoder.getPositionAndVelocity().position);
            telemetry.addData("rightSlider",rightSliderEncoder.getPositionAndVelocity().position);
            telemetry.update();







        }



    }

}