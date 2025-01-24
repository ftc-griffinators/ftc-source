package org.firstinspires.ftc.teamcode.griffinators;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@TeleOp(name ="ServoTest",group = "Tests")
@Config
public class ServoTest extends LinearOpMode {
    public static double CLAW_EXTENDED=0;
    public static double CLAW_RETRACTED=0.26;
    public static double CLAW_ROT_MID=0.54;
    public static double CLAW_ROT_GROUND=0.205;
    public static double CLAW_ROT_FRONT=0.28;
    public static double CLAW_ROT_BACK=0.76;
    public static double CLAW_GRAB=0.83;
    public static double CLAW_RELEASE=0.71;
    public static double CLAW_PITCH_TOP;
    public static double CLAW_PITCH_BOT;
    public static double CLAW_ALIGNMENT_LEFTMOST;
    public static double CLAW_ALIGNMENT_RIGHTMOST;
    public static double CLAW_ALIGNMENT_MIDDLE;


    public static Servo.Direction d1= Servo.Direction.REVERSE;
    public static Servo.Direction d2= Servo.Direction.REVERSE;
    public static Servo.Direction d3= Servo.Direction.REVERSE;




    ServoImplEx clawGrab, clawRightRot, clawLeftRot, clawExtend,clawAlignment, clawPitch;;

    @Override
    public void runOpMode() throws InterruptedException {


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
            if (gamepad1.dpad_up){
                clawPitch.setPosition(CLAW_PITCH_TOP);
            }
            if (gamepad1.dpad_down){
                clawPitch.setPosition(CLAW_PITCH_BOT);
            }
            if (gamepad1.dpad_left){
                clawAlignment.setPosition(CLAW_ALIGNMENT_MIDDLE);
            }

            if (gamepad1.dpad_right){

            }
            telemetry.addData("clawGrab.CLAW_GRAB: a",CLAW_GRAB);
            telemetry.addData("clawGrab.CLAW_RELEASE: b",CLAW_RELEASE);
            telemetry.addData("clawExtend.CLAW_EXTENDED: x",CLAW_EXTENDED);
            telemetry.addData("clawExtend.CLAW_RETRACTED: Y",CLAW_RETRACTED);
            telemetry.addData("clawAlignment.LEFT: left_bumper",CLAW_ALIGNMENT_LEFTMOST);
            telemetry.addData("clawAlignment.RIGHT: right_bumper",CLAW_ALIGNMENT_RIGHTMOST);
            telemetry.addData("clawPitch.TOP: dpad_up",CLAW_PITCH_TOP);
            telemetry.addData("clawPitch.BOTTOM: dpad_down",CLAW_PITCH_BOT);
            telemetry.addData("clawAlignment.MIDDLE: dpad_left",CLAW_ALIGNMENT_MIDDLE);




            telemetry.update();
        }



    }

}