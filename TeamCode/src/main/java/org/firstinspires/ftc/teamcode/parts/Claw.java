package org.firstinspires.ftc.teamcode.parts;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.Utility.Transform;
import org.firstinspires.ftc.teamcode.systems.VisionSystem;

import java.util.List;

@Config
public class Claw {
    public static double CLAW_EXTENDED=0.38;
    public static double CLAW_RETRACTED=0;
    public static double CLAW_ROT_MID=0.15;
    public static double CLAW_ROT_FRONT=0.07;
    public static double CLAW_ROT_BACK=0.44;
    public static double CLAW_ROT_GROUND=0.03;
    public static double CLAW_ROT_TOUCH=0.28;
    public static double CLAW_GRAB=0;
    public static double CLAW_RELEASE=0.1;
    public static double CLAW_PITCH_TOP=0.5;
    public static double CLAW_PITCH_MID=0.265;
    public static double CLAW_PITCH_BOT=0.02;
    public static double CLAW_PITCH_SCORE=0.48;
    public static double CLAW_ALIGNMENT_LEFTMOST=0.84;
    public static double CLAW_ALIGNMENT_RIGHTMOST=0.23;
    public static double CLAW_ALIGNMENT_MIDDLE=0.53;

    public static double CLAW_270=0.65;
    public static double CLAW_45=0.32;


    public double currentServoPose=0.5;

    public int alignStatus=0;

    public static int o=0;
    public  static int in=0;
    public static String wrong =" ";

    public ServoImplEx clawGrab, clawRightRot, clawLeftRot, clawExtend,clawAlignment, clawPitch;

    public Claw(HardwareMap hardwareMap)  {
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

        clawAlignment.scaleRange(CLAW_ALIGNMENT_RIGHTMOST,CLAW_ALIGNMENT_LEFTMOST);



        clawLeftRot.setDirection(Servo.Direction.REVERSE);

        clawExtend.setDirection(Servo.Direction.REVERSE);



    }

/*
    public double scale(double min, double max){

    }
*/


    public void orientationAligning(List<List<Double>> corner, VisionSystem vision, Transform pose) {

        String direction =vision.turnLeftOrRight(corner);
        if (Double.isNaN(pose.orientation.yaw)) {

        } else if (vision.isTargetAligned(corner)) {
            alignStatus=0;
        }
        else{
            clawAlignment.setPosition(alignmentAngleToServoPose(pose.orientation.yaw));
            alignStatus=0;
        }

    }

    public double alignmentAngleToServoPose(double angle){
        return angle/Math.PI;
    }

    public  void  anotherAligning(List<List<Double>> corner, VisionSystem vision, Transform pose){
        double angle=(pose.orientation.yaw);

        if (angle>-0.361111111*Math.PI && angle<-0.1388888*Math.PI){
            clawAlignment.setPosition(0.25);
        }
        if (angle>0.1388888*Math.PI && angle<0.36111111*Math.PI){
            clawAlignment.setPosition(0.75);
        }

    }


    public void sampleGrabbing(List<List<Double>> corner, boolean isValid, VisionSystem vision) {
        alignerReset();

        //Put in code
    }

    public void specimentGrabbing(){

    }
    public void placeInObservationZone(){

    }





    public void clawAutoSampleInit() throws InterruptedException {
        clawGrab.setPosition(CLAW_GRAB);

        Thread.sleep(200);
        clawAlignment.setPosition(CLAW_ALIGNMENT_RIGHTMOST);
        Thread.sleep(300);
        clawPitch.setPosition(CLAW_PITCH_MID);
        rotateArm(CLAW_ROT_BACK);
        Thread.sleep(300);
    }
    public void teleOpInit(){
        clawPitch.setPosition(CLAW_PITCH_MID);
        clawAlignment.setPosition(CLAW_ALIGNMENT_MIDDLE);
        clawGrab.setPosition(CLAW_RELEASE);

     rotateArm(CLAW_ROT_FRONT);
    }
    public  void  teleOpWithBarParkingInit(){
        rotateArm(CLAW_ROT_BACK);
        clawAlignment.setPosition(CLAW_ALIGNMENT_RIGHTMOST);
        clawPitch.setPosition(CLAW_PITCH_MID);
    }

    public void boxScoring(){
       rotateArm(CLAW_ROT_BACK);
        clawExtend.setPosition(CLAW_EXTENDED);
        clawPitch.setPosition(CLAW_PITCH_SCORE);
    }


    public void alignerReset(){
        clawAlignment.setPosition(CLAW_ALIGNMENT_MIDDLE);
    }


    public void rotateArm(double position){
        clawRightRot.setPosition(position);
        clawLeftRot.setPosition(position);
    }
    public void specimentScoring(){
        clawPitch.setPosition(CLAW_PITCH_BOT);
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
    public void grabPrep() {
        clawPitch.setPosition(CLAW_PITCH_BOT);
        clawAlignment.setPosition(CLAW_ALIGNMENT_MIDDLE);
        rotateArm(CLAW_ROT_FRONT);
        clawGrab.setPosition(CLAW_RELEASE);
    }
    public void specimentPrep(){
        clawExtend.setPosition(CLAW_RETRACTED);
        rotateArm(CLAW_ROT_MID);
        clawPitch.setPosition(CLAW_PITCH_MID);
    }


    public void clearSub(){
        rotateArm(CLAW_ROT_FRONT);
        clawAlignment.setPosition(CLAW_ALIGNMENT_RIGHTMOST);
        clawPitch.setPosition(CLAW_PITCH_MID);
    }








}


