package org.firstinspires.ftc.teamcode.griffinators;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.ServoControllerEx;
import com.qualcomm.robotcore.hardware.ServoImpl;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.configuration.ServoFlavor;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Localizer;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.ThreeDeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.griffinators.Parts.PIDController;


@TeleOp(name ="TeleOperationA",group = "Robot")

public class TeleOperationA extends LinearOpMode {

    Hardware hardwareClass =new Hardware(this);



    private PIDController movementPID;







    DcMotorEx frontLeft, frontRight, backLeft, backRight,sliderLeft,sliderRight;
    Servo clawGrab2, clawGrab, clawRightRot, clawLeftRot, clawExtend;



    @Override
    public void runOpMode() throws InterruptedException{


        telemetry.addData("Status", "Initialized");
        telemetry.update();
        //Expansion Hub 0
        frontLeft =  hardwareMap.get(DcMotorEx.class,"leftFront");
        //Control Hub 0
        frontRight =hardwareMap.get(DcMotorEx.class,"rightFront");
        //Expansion Hub 1
        backLeft = hardwareMap.get(DcMotorEx.class,"leftRear");
        // Control Hub Port 1
        backRight = hardwareMap.get(DcMotorEx.class,"rightRear");
        // Expansion Hub 2
        sliderLeft=hardwareMap.get(DcMotorEx.class,"leftSlider");
        //Control Hub port 2
        sliderRight=hardwareMap.get(DcMotorEx.class,"rightSlider");
        clawRightRot= hardwareMap.get(Servo.class, "clawRightRot");
        clawLeftRot = hardwareMap.get(Servo.class, "clawLeftRot");
        clawExtend = hardwareMap.get(Servo.class, "clawExtend");
/*
        //"ce" is port 5 on control hub
        clawExtension = hardwareMap.get(Servo.class,"ce");
        //"c" is port 4
        clawGrab = hardwareMap.get(Servo.class,"c");
        //"r" is port 2
        clawRightRot = hardwareMap.get(Servo.class,"r");
        // "l" is port 3
        clawLeftRot = hardwareMap.get(Servo.class,"l");
*/

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Localizer localizer = new ThreeDeadWheelLocalizer(hardwareMap, MecanumDrive.PARAMS.inPerTick);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        sliderLeft.setDirection(DcMotorSimple.Direction.REVERSE);


        Pose2d pose = new Pose2d(0, 0, 0);

        //Limit kd to 0.2 or below
        movementPID = new PIDController(1,0.2,0.05,0.1);

        //Field centric mecanum drive with a PID controller
        while (opModeIsActive()){
            pose = pose.plus(localizer.update().value());
            if (gamepad1.a) {
                clawLeftRot.setPosition(clawLeftRot.getPosition()+ 0.1);
                clawRightRot.setPosition(clawRightRot.getPosition()+0.1);
            }
            if (gamepad1.b) {
                clawLeftRot.setPosition(clawLeftRot.getPosition()- 0.1);
                clawRightRot.setPosition(clawRightRot.getPosition()- 0.1);
            }
//            if (gamepad1.right_bumper){
//                clawGrab.setPosition(clawGrab.getPosition()+0.25);
//                clawGrab2.setPosition(clawGrab2.getPosition()+0.25);
//            }
//            if (gamepad1.left_bumper){
//                clawGrab.setPosition(clawGrab.getPosition()-0.25);
//                clawGrab2.setPosition(clawGrab2.getPosition()-0.25);
//            }


            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double turn= gamepad1.right_stick_x;



            if (gamepad1.dpad_down) { pose=new  Pose2d(0,0,0);}
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
    }
}