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
import com.qualcomm.robotcore.hardware.Servo;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Localizer;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.ThreeDeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.griffinators.Parts.PIDController;


@TeleOp(name ="TeleOperationA",group = "Robot")

public class TeleOperationA extends LinearOpMode {



    private PIDController movementPID;

    //Speed in InPerTicks
    private double maxAxialSpeed=0.0;
    private double maxLateralSpeed=0.0;
    private double maxTurnSpeed=0.0;




    private Pose2d pose = new Pose2d(0, 0, 0);

    DcMotorEx frontLeft, frontRight, backLeft, backRight;
    Servo clawExtension, clawGrab, clawRightRot, clawLeftRot;

       double i=0.4;





    @Override
    public void runOpMode() throws InterruptedException{




        telemetry.addData("Status", "Initialized");
        telemetry.update();


        frontLeft =  hardwareMap.get(DcMotorEx.class,"leftFront");
        frontRight =hardwareMap.get(DcMotorEx.class,"rightFront");
        backLeft = hardwareMap.get(DcMotorEx.class,"leftBack");
        backRight = hardwareMap.get(DcMotorEx.class,"rightBack");


        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //"ce" is port 5 on control hub
        clawExtension = hardwareMap.get(Servo.class,"ce");
        //"c" is port 4
        clawGrab = hardwareMap.get(Servo.class,"c");
        //"cr" ia port 2
        clawRightRot = hardwareMap.get(Servo.class,"cr");
        // "cl" is port 3
        clawLeftRot = hardwareMap.get(Servo.class,"cl");

        backLeft.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Localizer localizer = new ThreeDeadWheelLocalizer(hardwareMap, MecanumDrive.PARAMS.inPerTick);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        while (opModeIsActive()){

        }









        //Limit kd to 0.2 or below
        movementPID = new PIDController(1,0.2,0.05,0.1);

        //Field centric mecanum drive with a PID controller
        while (opModeIsActive()){
            pose = pose.plus(localizer.update().value());




            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double turn= gamepad1.right_stick_x;
            double heading;
            double limitedTurn= Math.max(-0.25,Math.min(turn,0.25));

            if (gamepad1.dpad_down) { pose=new  Pose2d(0,0,0);}
            heading = pose.heading.toDouble();
            double rotX = x * Math.cos(-heading) - y * Math.sin(-heading);
            double rotY = x * Math.sin(-heading) + y * Math.cos(-heading);

            double PidFLpower =movementPID.testCorrection(rotY + rotX + limitedTurn,frontLeft.getPower());
            double PidFRpower =movementPID.testCorrection(rotY - rotX + limitedTurn,frontRight.getPower());
            double PidBLpower =movementPID.testCorrection(rotY - rotX - limitedTurn,backLeft.getPower());
            double PidBRpower =movementPID.testCorrection(rotY + rotX - limitedTurn, backRight.getPower());

            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(limitedTurn), 1);

            double frontLeftPower = PidFLpower / denominator;
            double backLeftPower = PidFRpower / denominator;
            double frontRightPower = PidBLpower / denominator;
            double backRightPower = PidBRpower / denominator;

            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(frontRightPower);
            backRight.setPower(backRightPower);

            //Discrete testing
            //Will go forwards at 50% power when (a) is pressed
            if (gamepad1.a){
                frontLeft.setPower(0.4);
                backLeft.setPower(0.4);
                frontRight.setPower(0.4);
                backRight.setPower(0.4);
            }
            TelemetryPacket packet=new TelemetryPacket();



            packet.put("Front left power",frontLeft.getPower());
            packet.put("Front right power",frontRight.getPower());
            packet.put("Back left power",backLeft.getPower());
            packet.put("Back right power",backRight.getPower());

            packet.addLine("Front left power");
            packet.fieldOverlay().fillCircle(0,0,1);

            FtcDashboard dashboard= FtcDashboard.getInstance();
            dashboard.sendTelemetryPacket(packet);


            telemetry.addData("FL",frontLeft.getPower());
            telemetry.addData("FR",frontRight.getPower());
            telemetry.addData("BL",backLeft.getPower());
            telemetry.addData("BR",backRight.getPower());
            telemetry.update();







    //Claw
            clawExtension.setDirection(Servo.Direction.REVERSE);
            clawGrab.setDirection(Servo.Direction.FORWARD);
//Start the initial claw extension servo at 0.4

            //Around 0.71 is the fully retracted state
            //Around 0.2 is the fully extended state
           





        }
    }
}