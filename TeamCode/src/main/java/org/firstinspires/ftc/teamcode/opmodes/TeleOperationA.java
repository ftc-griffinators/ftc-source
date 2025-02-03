package org.firstinspires.ftc.teamcode.opmodes;


import static org.firstinspires.ftc.teamcode.parts.Claw.*;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


import com.qualcomm.robotcore.hardware.DcMotorEx;


import org.firstinspires.ftc.teamcode.Localizer;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.ThreeDeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.ThreeDeadWheelLocalizer2;
import org.firstinspires.ftc.teamcode.parts.Claw;
import org.firstinspires.ftc.teamcode.parts.Drive;
import org.firstinspires.ftc.teamcode.parts.Slider;
import org.firstinspires.ftc.teamcode.systems.VisionSystem;


@TeleOp(name ="TeleOperationA",group = "Robot")
@Config
public class TeleOperationA extends LinearOpMode {
    DcMotorEx frontLeft, frontRight, backLeft, backRight; //sliderLeft,sliderRight;
   // ServoImplEx clawGrab, clawRightRot, clawLeftRot, clawExtend,clawAlignment, clawPitch;




    int boxScoringStateTop=0;
    int clawGrabState=0;
    int subClearing=0;
    int init=0;




    @Override
    public void runOpMode() throws InterruptedException{
        telemetry.addData("Status", "Initialized");
        telemetry.update();
       /*

        //Expansion Hub 0
        frontLeft =  hardwareMap.get(DcMotorEx.class,"leftFront");
        //TODO switch the right front and right rear port
        //Control Hub 0
        frontRight =hardwareMap.get(DcMotorEx.class,"rightFront");
        //Expansion Hub 1
        backLeft = hardwareMap.get(DcMotorEx.class,"leftRear");
        // Control Hub Port 1
        backRight = hardwareMap.get(DcMotorEx.class,"rightRear");


        Localizer localizer = new ThreeDeadWheelLocalizer2(hardwareMap, MecanumDrive.PARAMS.inPerTick);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);


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


        Pose2d pose = new Pose2d(0, 0, 0);

        */
        Drive drive=new Drive(hardwareMap);
        Claw claw=new Claw(hardwareMap);
        Slider slider=new Slider(hardwareMap);
        VisionSystem visionSystem=new VisionSystem(hardwareMap,0);



        telemetry.addData("Status", "Initialized");
        telemetry.update();
             waitForStart();

        //Field centric mecanum drive
        while (opModeIsActive()){
          //  pose = pose.plus(localizer.update().value());
            if (init==0){

                claw.teleOpInit();
                slider.initSlider();
                init=1;
            }

            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double turn= gamepad1.right_stick_x;


            drive.mecanumDriving(x,y,turn,0.8);
            /*

            double heading = pose.heading.toDouble();
            double rotX = x * Math.cos(heading) - y * Math.sin(heading);
            double rotY = x * Math.sin(heading) + y * Math.cos(heading);

            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(turn), 1);

            double frontLeftPower = (rotY + rotX + turn) / denominator;
            double frontRightPower = (rotY - rotX - turn )/ denominator;
            double backLeftPower = (rotY - rotX + turn )/ denominator;
            double backRightPower = (rotY + rotX - turn) / denominator;

            frontLeft.setPower(frontLeftPower*0.8);
            backLeft.setPower(backLeftPower*0.8);
            frontRight.setPower(frontRightPower*0.8);
            backRight.setPower(backRightPower*0.8);


             */


            //Grabbing
            if (gamepad1.right_bumper){
                switch (clawGrabState){
                    case 0:
                        claw.rotateArm(CLAW_ROT_GROUND);
                        Thread.sleep(500);
                        claw.grab();
                        Thread.sleep(500);
                        claw.rotateArm(CLAW_ROT_FRONT);
                        Thread.sleep(300);
                        clawGrabState=1;
                        break;
                    case 1: claw.release();
                        Thread.sleep(300);
                        clawGrabState=0;
                        break;
                }
            }


            //Toggle sub
            if (gamepad1.left_bumper){
                switch (subClearing){
                    case 0:
                        claw.grabPrep();
                        Thread.sleep(300);
                        subClearing=1;
                        break;
                    case 1:
                        claw.clearSub();
                        Thread.sleep(300);
                        subClearing=0;
                        break;
                }
            }

            if (gamepad1.start){
                drive.pose=new Pose2d(0,0,0);
            }
/*
            //Move away from sub
           if (gamepad1.y){
                    claw.grabPrep();
           }


           if (gamepad1.left_bumper){
               claw.clearSub();
           }




 */
           if (gamepad1.dpad_up){
               claw.clawAlignment.setPosition(CLAW_ALIGNMENT_MIDDLE);
           }
           if (gamepad1.dpad_left){
               claw.clawAlignment.setPosition(CLAW_270);
           }
           if (gamepad1.dpad_right){
               claw.clawAlignment.setPosition(CLAW_45);
           }
           if (gamepad1.dpad_down){
               claw.clawAlignment.setPosition(CLAW_ALIGNMENT_RIGHTMOST);
           }



            if (gamepad2.left_bumper){
                claw.clawExtend.setPosition(CLAW_EXTENDED);
            }
            if (gamepad2.right_bumper){
                claw.clawExtend.setPosition(CLAW_RETRACTED);
            }
/*
            if (gamepad2.a){
                claw.clawAlignment.setPosition(pl2SetAlignment);
            }


 */

            //Grab success Basket scoring phase
            if (gamepad2.y){
                switch (boxScoringStateTop){
                    case 0:
                        claw.clawAlignment.setPosition(CLAW_ALIGNMENT_RIGHTMOST);
                        slider.sliderExtensionTopBox();
                        Thread.sleep(1000);
                       claw.boxScoring();
                        boxScoringStateTop++;
                        break;
                    case 1:
                        claw.release();
                        Thread.sleep(300);
                        claw.rotateArm(CLAW_ROT_MID);
                        claw.retract();
                        slider.sliderRetraction();
                        claw.rotateArm(CLAW_ROT_FRONT);
                        claw.grabPrep();

                        boxScoringStateTop=0;;
                        break;
                }
            }


            }
        }
    }

