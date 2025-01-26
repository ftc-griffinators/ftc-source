package org.firstinspires.ftc.teamcode.opmodes;

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
import org.firstinspires.ftc.teamcode.parts.Slider;
import org.firstinspires.ftc.teamcode.systems.VisionSystem;


@TeleOp(name ="TeleOperationA",group = "Robot")
@Config
public class TeleOperationA extends LinearOpMode {
    DcMotorEx frontLeft, frontRight, backLeft, backRight; //sliderLeft,sliderRight;
   // ServoImplEx clawGrab, clawRightRot, clawLeftRot, clawExtend,clawAlignment, clawPitch;
   public static double CLAW_EXTENDED=0.38;
    public static double CLAW_RETRACTED=0;
    public static double CLAW_ROT_MID=0.15;
    public static double CLAW_ROT_FRONT=0.07;
    public static double CLAW_ROT_BACK=0.4;
    public static double CLAW_ROT_GROUND=0.03;
    public static double CLAW_GRAB=0;
    public static double CLAW_RELEASE=0.09;
    public static double CLAW_PITCH_TOP=0.5;
    public static double CLAW_PITCH_MID=0.265;
    public static double CLAW_PITCH_BOT=0.02;
    public static double CLAW_PITCH_SCORE=0.4;
    public static double CLAW_ALIGNMENT_LEFTMOST=0.8;
    public static double CLAW_ALIGNMENT_RIGHTMOST=0.19;
    public static double CLAW_ALIGNMENT_MIDDLE=0.49;
    public static double CLAW_270=0.65;
    public static double CLAW_45=0.32;


    static int sliderStateTopBox=0;
    static int sliderStateMidBox=0;
    int clawExtendState=0;
    int boxScoringStateTop=0;
    int boxScoringStateBot =0;
    int clawGrabState=0;
    int subClearing=0;
    int init=0;

    double pl2SetAlignment=0;


    /*
    public void rotateClaw(double position){
        clawRightRot.setPosition(position);
        clawLeftRot.setPosition(position);
    }
    public void sliderExtensionTopBox(){
        sliderRight.setTargetPosition(4200);
        sliderLeft.setTargetPosition(4200);
        smoothing(sliderLeft.getCurrentPosition());
    }
    public void sliderRetractionTopBox(){
        sliderRight.setTargetPosition(50);
        sliderLeft.setTargetPosition(50);
        smoothing(sliderLeft.getCurrentPosition());
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
            sliderRight.setPower(sliderSmoothMovement(0,4000,Math.abs(initial-sliderRight.getCurrentPosition())));
            sliderLeft.setPower(sliderSmoothMovement(0,4000,Math.abs(initial-sliderLeft.getCurrentPosition())));
    }
    */


    @Override
    public void runOpMode() throws InterruptedException{
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        //Expansion Hub 0
        frontLeft =  hardwareMap.get(DcMotorEx.class,"leftFront");
        //TODO switch the right front and right rear port
        //Control Hub 0
        frontRight =hardwareMap.get(DcMotorEx.class,"rightFront");
        //Expansion Hub 1
        backLeft = hardwareMap.get(DcMotorEx.class,"leftRear");
        // Control Hub Port 1
        backRight = hardwareMap.get(DcMotorEx.class,"rightRear");

        Claw claw=new Claw(hardwareMap);
        Slider slider=new Slider(hardwareMap);
        VisionSystem visionSystem=new VisionSystem(hardwareMap,"limeLight",0);



      /*
        // Expansion Hub 2
        sliderLeft=hardwareMap.get(DcMotorEx.class,"leftSlider");
        //Control Hub port 2
        sliderRight=hardwareMap.get(DcMotorEx.class,"rightSlider");

        sliderLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        sliderLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sliderRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        sliderRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        sliderLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
*/




/*
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


 */



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

        telemetry.addData("Status", "Initialized");
        telemetry.update();
             waitForStart();

        //Field centric mecanum drive
        while (opModeIsActive()){
            pose = pose.plus(localizer.update().value());
            if (init==0){

           /*     sliderLeft.setTargetPosition(50);
                sliderRight.setTargetPosition(50);

                sliderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sliderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                clawGrab.setPosition(CLAW_RELEASE);
                rotateClaw(CLAW_ROT_FRONT);
                clawExtend.setPosition(CLAW_RETRACTED);
*/

                claw.teleOpInit();
                slider.sliderInit();
                init=1;
            }

            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double turn= gamepad1.right_stick_x;




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



            /*
            //GRABBING TOGGLE
            if (gamepad1.right_bumper){
                switch (clawGrabState){
                    case 0: claw.grab();
                        Thread.sleep(300);
                        clawGrabState=1;
                        break;
                    case 1: claw.release();
                        Thread.sleep(300);
                        clawGrabState=0;
                        break;
                }
            }
            //EXTENSION
            if (gamepad1.left_bumper){
                switch (clawExtendState){
                    case 0: claw.extend();
                        Thread.sleep(300);
                        clawExtendState=1;
                        break;
                    case 1: claw.retract();
                        Thread.sleep(300);
                        clawExtendState=0;
                        break;
                }
            }



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
                pose=new Pose2d(0,0,0);
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

