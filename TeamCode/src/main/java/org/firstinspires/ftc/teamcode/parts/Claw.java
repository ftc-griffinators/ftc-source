package org.firstinspires.ftc.teamcode.parts;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

public class Claw {
    public static double CLAW_EXTENDED=0;
    public static double CLAW_RETRACTED=0.26;
    public static double CLAW_ROT_MID=0.54;
    public static double CLAW_ROT_GROUND=0.205;
    public static double CLAW_ROT_FRONT=0.28;
    public static double CLAW_GRAB=0.83;
    public static double CLAW_RELEASE=0.71;
    public static double CLAW_ROT_BACK=0.76;



    ServoImplEx clawGrab, clawRightRot, clawLeftRot, clawExtend,clawAlignment, clawPitch;

    public Claw( HardwareMap hardwareMap)  {
        //"clawExtend" is port 2 on control hub
        clawExtend = hardwareMap.get(ServoImplEx.class,"clawExtend");

        //"clawRightRot" is port 0 on control hub
        clawRightRot = hardwareMap.get(ServoImplEx.class,"clawRightRot");

        // "clawLeftRot" is port 1 on control hub
        clawLeftRot = hardwareMap.get(ServoImplEx.class,"clawLeftRot");
        //"clawGrab" is port 3
        clawGrab=hardwareMap.get(ServoImplEx.class,"clawGrab");



        clawExtend.setPwmRange(new PwmControl.PwmRange(500,2500));

        clawLeftRot.setPwmRange(new PwmControl.PwmRange(500,2500));

        clawRightRot.setPwmRange(new PwmControl.PwmRange(500,2500));

        clawGrab.setPwmRange(new PwmControl.PwmRange(500,2500));


        clawLeftRot.setDirection(Servo.Direction.REVERSE);
    }
    public void clawInit(){
        clawRightRot.setPosition(CLAW_ROT_MID);
        clawLeftRot.setPosition(CLAW_ROT_MID);
        clawGrab.setPosition(CLAW_GRAB);
    }

    public void rotateArm(double position){
        clawRightRot.setPosition(position);
        clawLeftRot.setPosition(position);
    }
    public void extend(){
        clawExtend.setPosition(CLAW_EXTENDED);
    }
    public void retract(){
        clawExtend.setPosition(CLAW_RETRACTED);
    }
    public void  grab(){
        clawGrab.setPosition(CLAW_GRAB);
    }
    public void release(){
        clawGrab.setPosition(CLAW_RELEASE);
    }




}
