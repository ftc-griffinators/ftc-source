package org.firstinspires.ftc.teamcode.griffinators;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@TeleOp(name ="ServoTest",group = "Tests")
@Config
public class ServoTest extends LinearOpMode {
    public static double CLAW_EXTENDED=0.26;
    public static double CLAW_RETRACTED=0;
    public static double CLAW_ROT_MID=0.15;
    public static double CLAW_ROT_FRONT=0;
    public static double CLAW_ROT_BACK=0.39;
    public static double CLAW_GRAB=0;
    public static double CLAW_RELEASE=0.3;
    public static double CLAW_PITCH_TOP=0.5;
    public static double CLAW_PITCH_MID=0.25;
    public static double CLAW_PITCH_BOT=0;
    public static double CLAW_ALIGNMENT_LEFTMOST=0.8;
    public static double CLAW_ALIGNMENT_RIGHTMOST=0.19;
    public static double CLAW_ALIGNMENT_MIDDLE=0.49;
    public static Servo.Direction d1= Servo.Direction.REVERSE;
    public static Servo.Direction d2= Servo.Direction.REVERSE;
    public static Servo.Direction d3= Servo.Direction.REVERSE;




    ServoImplEx clawGrab, clawRightRot, clawLeftRot, clawExtend,clawAlignment, clawPitch;;

    @Override
    public void runOpMode() throws InterruptedException {

        TelemetryPacket p=new TelemetryPacket();

        FtcDashboard dashboard=FtcDashboard.getInstance();


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



        waitForStart();
        while (opModeIsActive()){
            if (gamepad1.a){
             clawGrab.setPosition(CLAW_GRAB);

            }
            if (gamepad1.b){
                clawGrab.setPosition(CLAW_RELEASE);

            }
            if (gamepad1.x){
                clawExtend.setPosition(CLAW_EXTENDED);
            }
            if (gamepad1.y){
                clawExtend.setPosition(CLAW_RETRACTED);

            }
            if (gamepad1.left_bumper){
                clawAlignment.setPosition(CLAW_ALIGNMENT_LEFTMOST);

            }
            if (gamepad1.right_bumper){
                clawAlignment.setPosition(CLAW_ALIGNMENT_RIGHTMOST);
            }
            if (gamepad1.dpad_left){
              clawRightRot.setPosition(CLAW_ROT_FRONT);
            }
            if (gamepad1.dpad_right){
                clawRightRot.setPosition(CLAW_ROT_MID);
            }


            if (gamepad1.dpad_up){
                clawLeftRot.setPosition(CLAW_ROT_FRONT);
            }
            if (gamepad1.dpad_down){
                clawLeftRot.setPosition(CLAW_ROT_MID);
            }



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






        }



    }

}