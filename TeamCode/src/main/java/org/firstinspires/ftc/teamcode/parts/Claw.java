package org.firstinspires.ftc.teamcode.parts;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.Utility.Transform;
import org.firstinspires.ftc.teamcode.systems.VisionSystem;

import java.util.List;

@Config
public class Claw  {
    public static double CLAW_EXTENDED=0.45;
    public static double CLAW_RETRACTED=0.1;
    public static double CLAW_EXTENSION_ZERO=0;

    public static double CLAW_ROT_MID=0.61;
    public static double CLAW_ROT_FRONT=0.08;
    public static double CLAW_ROT_BACK=0.82;
    public static double CLAW_ROT_DIRECT_SCORE=0.51;
    public static double CLAW_ROT_GROUND_RETRACTED=0.01;
    public static double CLAW_ROT_GROUND_EXTENDED=0.03;
    public static double CLAW_ROT_AUTO_INIT=1;

    public static double CLAW_ROT_TOUCH=0.3;




    public static double CLAW_GRAB=0.25;
    public static double CLAW_RELEASE=0.35;
    public static double CLAW_PITCH_TOP=0.5;
    public static double CLAW_PITCH_MID=0.4;
    public static double CLAW_PITCH_BOT=0.13;
    public static double CLAW_PITCH_SCORE=0.53;
    public static double CLAW_PITCH_DIRECT_SCORE =0.3;
    public static double CLAW_ALIGNMENT_LEFTMOST=0.84;
    public static double CLAW_ALIGNMENT_RIGHTMOST=0.23;
    public static double CLAW_ALIGNMENT_MIDDLE=0.53;

    public static double CLAW_OPEN_TELEOP=0.35;



    public static double CLAW_270=0.65;
    public static double CLAW_45=0.32;


    public static double heightFromGround=0;


    public boolean startAligning=false;


    public ServoImplEx clawGrab, clawRightRot, clawLeftRot, clawExtend,clawAlignment, clawPitch;
    public TouchSensor sensor;
    AnalogInput rightArmEncoder, leftArmEncoder, extensionEncoder;

    public double rightArmPosition, leftArmPosition, extensionPosition;
    public Claw(HardwareMap hardwareMap) {


        //"clawExtend" is port 2 on control hub
        clawExtend = hardwareMap.get(ServoImplEx.class, "clawExtend");

        //"clawRightRot" is port 0 on control hub
        clawRightRot = hardwareMap.get(ServoImplEx.class, "clawRightRot");

        // "clawLeftRot" is port 1 on control hub
        clawLeftRot = hardwareMap.get(ServoImplEx.class, "clawLeftRot");
        //"clawGrab" is port 3
        clawGrab = hardwareMap.get(ServoImplEx.class, "clawGrab");

        clawAlignment = hardwareMap.get(ServoImplEx.class, "clawAlignment");

        clawPitch = hardwareMap.get(ServoImplEx.class, "clawPitch");


        rightArmEncoder = hardwareMap.get(AnalogInput.class, "rightArmEncoder");
        leftArmEncoder = hardwareMap.get(AnalogInput.class, "leftArmEncoder");
        extensionEncoder = hardwareMap.get(AnalogInput.class, "extensionEncoder");



        clawGrab.setPwmRange(new PwmControl.PwmRange(500, 2500));

        clawAlignment.setPwmRange(new PwmControl.PwmRange(500, 2500));

        clawPitch.setPwmRange(new PwmControl.PwmRange(500, 2500));

        clawAlignment.scaleRange(CLAW_ALIGNMENT_RIGHTMOST, CLAW_ALIGNMENT_LEFTMOST);

            }

    public Claw servoEncoderPosition(){
        rightArmPosition=rightArmEncoder.getVoltage()/3.3 *360;
        leftArmPosition=leftArmEncoder.getVoltage()/3.3 *360;
        extensionPosition= extensionEncoder.getVoltage()/3.3 * 360;
        return this;

    }

    public void orientationAligning( VisionSystem vision ) {

        List<List<Double>> corner=vision.getCorners();
        Transform pose=vision.getTargetDiffPose(corner);

        if (Double.isNaN(pose.orientation.yaw)) {

        } else if (vision.isTargetAligned(corner)) {
            startAligning =false;
        }
        else{
            clawAlignment.setPosition(alignmentAngleToServoPose(pose.orientation.yaw));
            startAligning =false;
        }

    }


    public double alignmentAngleToServoPose(double angle){
        return angle/Math.PI;
    }


    public void sampleGrabbing(List<List<Double>> corner, boolean isValid, VisionSystem vision) {

    }

    public void specimentGrabbing(){

    }
    public void placeInObservationZone(){

    }





    public void clawAutoSampleInit() throws InterruptedException {
        clawGrab.setPosition(CLAW_GRAB);
        clawExtend.setPosition(CLAW_EXTENSION_ZERO);
        Thread.sleep(200);
        clawAlignment.setPosition(CLAW_ALIGNMENT_MIDDLE);
        rotateArm(CLAW_ROT_AUTO_INIT);

    }

    public void clawAutoSampleInit2(){

    }
    public void teleOpInit(){
        clawPitch.setPosition(CLAW_PITCH_MID);
        clawAlignment.setPosition(CLAW_ALIGNMENT_MIDDLE);
        retract();
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

    public void boxScoringFront(){
        rotateArm(CLAW_ROT_DIRECT_SCORE);
        clawExtend.setPosition(CLAW_EXTENDED);
        clawPitch.setPosition(CLAW_PITCH_DIRECT_SCORE);
    }


    public void alignerReset(){
        clawAlignment.setPosition(CLAW_ALIGNMENT_MIDDLE);
        startAligning =true;
    }




    public void rotateArm(double position){
        clawRightRot.setPosition(position);
        clawLeftRot.setPosition(position);
    }
    public void specimentScoring(){
        clawPitch.setPosition(CLAW_PITCH_BOT);
    }


    public void extenderSweep(){


    }
    public void extenderLockOn(){

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
    public void middleAlignment(){
        clawAlignment.setPosition(0.5);
    }
    public void rightAlignment(){
        clawAlignment.setPosition(0);
    }
    public void leftAlignment(){
        clawAlignment.setPosition(1);
    }
    public void stopPoweringClaw(){

    }


    public void clearSub(){
        rotateArm(CLAW_ROT_FRONT);
        middleAlignment();
        clawPitch.setPosition(CLAW_PITCH_MID);
        startAligning=false;

    }








}


