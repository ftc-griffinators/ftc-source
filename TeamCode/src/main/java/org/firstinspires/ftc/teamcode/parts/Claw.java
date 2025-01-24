package org.firstinspires.ftc.teamcode.parts;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.math.Transform;
import org.firstinspires.ftc.teamcode.systems.VisionSystem;
import org.firstinspires.ftc.vision.opencv.ColorRange;

public class Claw {
    public static double CLAW_EXTENDED=0;
    public static double CLAW_RETRACTED=0.26;
    public static double CLAW_ROT_MID=0.54;
    public static double CLAW_ROT_GROUND=0.205;
    public static double CLAW_ROT_FRONT=0.28;
    public static double CLAW_GRAB=0.83;
    public static double CLAW_RELEASE=0.71;
    public static double CLAW_ROT_BACK=0.76;
    public static double CLAW_ALIGNMENT_LEFTMOST;
    public static double CLAW_ALIGNMENT_RIGHTMOST;
    public static double CLAW_PITCH_TOP;
    public static double CLAW_PITCH_BOT;

    public double currentAlignerOrientation=0;




    VisionSystem vision;
    public Transform targetPose=vision.getTargetPose();
      public Transform alignmentDelta=vision.getAlignmentDelta();

    ServoImplEx clawGrab, clawRightRot, clawLeftRot, clawExtend,clawAlignment, clawPitch;


    public Claw(HardwareMap hardwareMap, VisionSystem vision)  {
        //"clawExtend" is port 2 on control hub
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

        this.vision=vision;


    }


    public void aligning(){
        if (vision.hasValidTarget()){
            currentAlignerOrientation+=alignmentDelta.orientation.yaw;
            clawAlignment.setPosition(currentAlignerOrientation);

        }
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


