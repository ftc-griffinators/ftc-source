package org.firstinspires.ftc.teamcode.parts;

import android.graphics.Canvas;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.configuration.WebcamConfiguration;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.teamcode.math.Transform;
import org.firstinspires.ftc.teamcode.systems.VisionSystem;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessorImpl;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

public class Claw {
    public static double CLAW_EXTENDED=0;
    public static double CLAW_RETRACTED=0.26;
    public static double CLAW_ROT_MID=0.54;
    public static double CLAW_ROT_GROUND=0.205;
    public static double CLAW_ROT_FRONT=0.28;
    public static double CLAW_GRAB=0.83;
    public static double CLAW_RELEASE=0.71;
    public static double CLAW_ROT_BACK=0.76;
    public static double CLAW_PITCH_MAX;
    public static double CLAW_PITCH_MIN;
    public static double CLAW_ALIGNMENT_MAX;
    public static double CLAW_ALIGNMENT_MIN;

    VisionSystem vision;
    public Transform targetPose, alignmentDelta;

    ServoImplEx clawGrab, clawRightRot, clawLeftRot, clawExtend,clawAlignment, clawPitch;

    public Claw(HardwareMap hardwareMap, ColorRange color)  {
        //"clawExtend" is port 2 on control hub
        clawExtend = hardwareMap.get(ServoImplEx.class,"clawExtend");

        //"clawRightRot" is port 0 on control hub
        clawRightRot = hardwareMap.get(ServoImplEx.class,"clawRightRot");

        // "clawLeftRot" is port 1 on control hub
        clawLeftRot = hardwareMap.get(ServoImplEx.class,"clawLeftRot");
        //"clawGrab" is port 3
        clawGrab=hardwareMap.get(ServoImplEx.class,"clawGrab");

        this.vision = new VisionSystem(hardwareMap,"Webcam");
        this.targetPose = vision.getTargetPose();
        this.alignmentDelta = vision.getAlignmentDelta();

        clawExtend.setPwmRange(new PwmControl.PwmRange(500,2500));

        clawLeftRot.setPwmRange(new PwmControl.PwmRange(500,2500));

        clawRightRot.setPwmRange(new PwmControl.PwmRange(500,2500));

        clawGrab.setPwmRange(new PwmControl.PwmRange(500,2500));

        clawAlignment.setPwmRange(new PwmControl.PwmRange(500,2500));

        clawPitch.setPwmRange(new PwmControl.PwmRange(500,2500));

        clawLeftRot.setDirection(Servo.Direction.REVERSE);


    }


    public Pose2d alignmentCorrection= new Pose2d(alignmentDelta.position.x,alignmentDelta.position.y,0);



    public void alignmentRoutine(){
        if (targetPose!=Transform.INVALID){

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
