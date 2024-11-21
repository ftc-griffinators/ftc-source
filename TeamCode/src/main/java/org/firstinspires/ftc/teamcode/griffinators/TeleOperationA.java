package org.firstinspires.ftc.teamcode.griffinators;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
import static org.firstinspires.ftc.teamcode.griffinators.Parts.Utility.sliderSmoothMovement;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;

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





    DcMotorEx frontLeft, frontRight, backLeft, backRight,sliderLeft,sliderRight;
    Servo clawGrab2, clawGrab, clawRightRot, clawLeftRot, clawExtend;


    public void sliderExtensionTopBox(){
        sliderRight.setTargetPosition(4100);
        sliderLeft.setTargetPosition(4100);
        smoothing(sliderLeft.getCurrentPosition());

        // smoothing(sliderLeft.getCurrentPosition());

    }
    public void sliderRetractionTopBox(){
        sliderRight.setTargetPosition(50);
        sliderLeft.setTargetPosition(50);
        smoothing(sliderLeft.getCurrentPosition());

       // smoothing(sliderLeft.getCurrentPosition());
    }

    public void sliderExtensionMidBox(){
        sliderRight.setTargetPosition(2100);
        sliderLeft.setTargetPosition(2100);
        smoothing(sliderLeft.getCurrentPosition());

    }

    public void sliderRetractionMidBox(){
        sliderRight.setTargetPosition(50);
        sliderLeft.setTargetPosition(50);
        smoothing(sliderLeft.getCurrentPosition());
    }


    private void smoothing(int initial){

        while (sliderLeft.isBusy() && sliderRight.isBusy() ){
            sliderRight.setPower(sliderSmoothMovement(0,4000,Math.abs(initial-sliderRight.getCurrentPosition())));
            sliderLeft.setPower(sliderSmoothMovement(0,4000,Math.abs(initial-sliderLeft.getCurrentPosition())));

        }
    }

    @Override
    public void runOpMode() throws InterruptedException{


        telemetry.addData("Status", "Initialized");
        telemetry.update();
        //Expansion Hub 0
        frontLeft =  hardwareMap.get(DcMotorEx.class,"leftFront");
        //Control Hub 0
        frontRight =hardwareMap.get(DcMotorEx.class,"rightRear");
        //Expansion Hub 1
        backLeft = hardwareMap.get(DcMotorEx.class,"leftRear");
        // Control Hub Port 1
        backRight = hardwareMap.get(DcMotorEx.class,"rightFront");

        // Expansion Hub 2
        sliderLeft=hardwareMap.get(DcMotorEx.class,"leftSlider");
        //Control Hub port 2
        sliderRight=hardwareMap.get(DcMotorEx.class,"rightSlider");



        //"clawExtend" is port 2 on control hub
        clawExtend = hardwareMap.get(Servo.class,"clawExtend");

        //"clawRightRot" is port 0 on control hub
        clawRightRot = hardwareMap.get(Servo.class,"clawRightRot");

        // "clawLeftRot" is port 1 on control hub
        clawLeftRot = hardwareMap.get(Servo.class,"clawLeftRot");

        int sliderStateTopBox=0;
        int sliderStateMidBox=0;




        Localizer localizer = new ThreeDeadWheelLocalizer(hardwareMap, MecanumDrive.PARAMS.inPerTick);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        sliderLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        sliderLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sliderRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        sliderLeft.setTargetPosition(50);
        sliderRight.setTargetPosition(50);

        sliderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        sliderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        sliderRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        sliderLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Pose2d pose = new Pose2d(0, 0, 0);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();



        //Field centric mecanum drive
        while (opModeIsActive()){
            pose = pose.plus(localizer.update().value());


           /* if (gamepad1.a) {
                clawLeftRot.setPosition(clawLeftRot.getPosition()+ 0.1);
                clawRightRot.setPosition(clawRightRot.getPosition()+0.1);
            }
            if (gamepad1.b) {
                clawLeftRot.setPosition(clawLeftRot.getPosition()- 0.1);
                clawRightRot.setPosition(clawRightRot.getPosition()- 0.1);
            }
*/

            if (gamepad1.a){
                clawLeftRot.setPosition(0.5);
            }
            if (gamepad1.b){
                clawLeftRot.setPosition(1);
            }
//            if (gamepad1.right_bumper){
//                clawGrab.setPosition(clawGrab.getPosition()+0.25);
//                clawGrab2.setPosition(clawGrab2.getPosition()+0.25);
//            }
//            if (gamepad1.left_bumper){
//                clawGrab.setPosition(clawGrab.getPosition()-0.25);
//                clawGrab2.setPosition(clawGrab2.getPosition()-0.25);
//            }





            double x = -gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double turn= gamepad1.right_stick_x;

            telemetry.addData("x", x);
            telemetry.addData("y", y);
            telemetry.addData("turn", turn);

            double heading = pose.heading.toDouble();
            double rotX = x * Math.cos(-heading) - y * Math.sin(-heading);
            double rotY = x * Math.sin(-heading) + y * Math.cos(-heading);


            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(turn), 1);


            telemetry.addData("denominator", denominator);

            double frontLeftPower = (rotY + rotX + turn) / denominator;
            double frontRightPower = (rotY - rotX - turn )/ denominator;
            double backLeftPower = (rotY - rotX + turn )/ denominator;
            double backRightPower = (rotY + rotX - turn) / denominator;


            telemetry.addData("frontLeftPower", frontLeftPower);
            telemetry.addData("backLeftPower", backLeftPower);
            telemetry.addData("frontRightPower", frontRightPower);
            telemetry.addData("backRightPower", backRightPower);



            telemetry.update();
            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(frontRightPower);
            backRight.setPower(backRightPower);

//Slider move to the top basket
            if (gamepad1.x){
                switch (sliderStateTopBox){
                    case 0: sliderExtensionTopBox();
                        sliderStateTopBox=1;
                        sliderStateMidBox=0;
                        break;
                    case  1: sliderRetractionTopBox();
                        sliderStateTopBox=0;
                        break;
                }
            }
            //Slider move to the middle basket
            if (gamepad1.y){
                switch (sliderStateMidBox){
                    case 0:sliderExtensionMidBox();
                    sliderStateMidBox=1;
                    sliderStateTopBox=0;
                    break;
                    case 1: sliderRetractionMidBox();
                    sliderStateMidBox=0;
                    break;
                }
            }


        }
    }
}