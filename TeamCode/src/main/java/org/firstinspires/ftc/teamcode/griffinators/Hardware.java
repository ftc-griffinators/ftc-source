package org.firstinspires.ftc.teamcode.griffinators;


import static org.firstinspires.ftc.teamcode.griffinators.Parts.Utility.sliderSmoothMovement;

import org.firstinspires.ftc.teamcode.griffinators.Parts.Utility.*;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Localizer;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.ThreeDeadWheelLocalizer;

import java.util.Base64;

public class Hardware {
    private LinearOpMode myOpMode = null;
    public Hardware (LinearOpMode opmode) {myOpMode = opmode;}

    DcMotorEx frontLeft,frontRight,backLeft,backRight,sliderLeft,sliderRight;
    Servo clawExtension, clawGrab, clawRightRot, clawLeftRot;
    Encoder leftSliderEncoder,rightSliderEncoder;



    Pose2d pose = new Pose2d(0, 0, 0);
    Localizer localizer = new ThreeDeadWheelLocalizer(myOpMode.hardwareMap, MecanumDrive.PARAMS.inPerTick);
    public void initHardware(){
        //Expansion Hub 0
        frontLeft =  myOpMode.hardwareMap.get(DcMotorEx.class,"leftFront");
        //Control Hub 0
        frontRight =myOpMode.hardwareMap.get(DcMotorEx.class,"rightFront");
        //Expansion Hub 1
        backLeft = myOpMode.hardwareMap.get(DcMotorEx.class,"leftRear");
        // Control Hub Port 1
        backRight = myOpMode.hardwareMap.get(DcMotorEx.class,"rightRear");
        // Expansion Hub 2
        sliderLeft=myOpMode.hardwareMap.get(DcMotorEx.class,"leftSlider");
        //Control Hub port 2
        sliderRight=myOpMode.hardwareMap.get(DcMotorEx.class,"rightSlider");

         leftSliderEncoder=new OverflowEncoder(new RawEncoder(myOpMode.hardwareMap.get(DcMotorEx.class,"leftSlider"))) ;
         rightSliderEncoder=new OverflowEncoder(new RawEncoder(myOpMode.hardwareMap.get(DcMotorEx.class,"rightSlider")));
        //"ce" is port 5 on control hub
        clawExtension = myOpMode.hardwareMap.get(Servo.class,"ce");
        //"c" is port 4
        clawGrab = myOpMode.hardwareMap.get(Servo.class,"c");
        //"r" is port 2
        clawRightRot = myOpMode.hardwareMap.get(Servo.class,"r");
        // "l" is port 3
        clawLeftRot = myOpMode.hardwareMap.get(Servo.class,"l");

        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        sliderLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        leftSliderEncoder.setDirection(DcMotorSimple.Direction.REVERSE);


        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }


    public void TeleOpMecanumDrive(double x,double y,double turn){
        pose = pose.plus(localizer.update().value());

        double heading = pose.heading.toDouble();
        double rotX = x * Math.cos(-heading) - y * Math.sin(-heading);
        double rotY = x * Math.sin(-heading) + y * Math.cos(-heading);

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(turn), 1);

        double frontLeftPower = rotY + rotX + turn / denominator;
        double backLeftPower = rotY - rotX + turn / denominator;
        double frontRightPower = rotY - rotX - turn / denominator;
        double backRightPower = rotY + rotX - turn / denominator;

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
    }
    public void sliderExtension(){
        sliderRight.setTargetPosition(4000);
        sliderLeft.setTargetPosition(4000);
        sliderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        sliderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (sliderLeft.isBusy() && sliderRight.isBusy()){
            sliderRight.setPower(sliderSmoothMovement(0,4000,sliderRight.getCurrentPosition()));
            sliderLeft.setPower(sliderSmoothMovement(0,4000,sliderLeft.getCurrentPosition()));
        }
    }
    public void sliderRetraction(){
        sliderRight.setTargetPosition(0);
        sliderLeft.setTargetPosition(0);

        sliderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        sliderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (sliderLeft.isBusy() && sliderRight.isBusy()){
            sliderRight.setPower(sliderSmoothMovement(0,4000,4000-sliderRight.getCurrentPosition()));
            sliderLeft.setPower(sliderSmoothMovement(0,4000,4000-sliderLeft.getCurrentPosition()));
        }
    }

}
