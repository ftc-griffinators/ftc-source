package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.parts.Utility.sliderSmoothMovement;

import com.acmerobotics.roadrunner.Pose2d;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;


import org.firstinspires.ftc.teamcode.Localizer;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.ThreeDeadWheelLocalizer;


@TeleOp(name ="TeleOperationA",group = "Robot")
public class TeleOperationA extends LinearOpMode {



    double test=0;


    double leftRot, rightRot, extend=0;
    DcMotorEx frontLeft, frontRight, backLeft, backRight,sliderLeft,sliderRight;
    ServoImplEx  clawGrab, clawRightRot, clawLeftRot, clawExtend;


    private final double CLAW_EXTENDED=0;
    private final double CLAW_RETRACTED=0.26;
    private final double CLAW_ROT_MID=0.54;
    private final double CLAW_ROT_GROUND=0.205;
    private final double CLAW_ROT_FRONT=0.28;
    private final double CLAW_GRAB=0.83;
    private final double CLAW_RELEASE=0.71;
    private final double CLAW_ROT_BACK=0.76;



    static int sliderStateTopBox=0;
    static int sliderStateMidBox=0;
    int clawExtendState=0;
    int boxScoringStateTop=0;
    int boxScoringStateBot =0;
    int clawGrabState=0;

    int init=0;


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

        // Expansion Hub 2
        sliderLeft=hardwareMap.get(DcMotorEx.class,"leftSlider");
        //Control Hub port 2
        sliderRight=hardwareMap.get(DcMotorEx.class,"rightSlider");



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


        sliderRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        sliderLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Pose2d pose = new Pose2d(0, 0, 0);



        telemetry.addData("Status", "Initialized");
        telemetry.update();
        clawLeftRot.setDirection(Servo.Direction.REVERSE);
        waitForStart();



        //Field centric mecanum drive
        while (opModeIsActive()){
            pose = pose.plus(localizer.update().value());
            if (init==0){
                sliderLeft.setTargetPosition(50);
                sliderRight.setTargetPosition(50);

                sliderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sliderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                clawGrab.setPosition(CLAW_RELEASE);
                rotateClaw(CLAW_ROT_GROUND);
                clawExtend.setPosition(CLAW_RETRACTED);


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




            //GRABBING TOGGLE
            if (gamepad1.right_bumper){
                switch (clawGrabState){
                    case 0: clawGrab.setPosition(CLAW_GRAB);
                        Thread.sleep(300);
                        clawGrabState=1;
                        break;
                    case 1: clawGrab.setPosition(CLAW_RELEASE);
                        Thread.sleep(300);
                        clawGrabState=0;
                        break;
                }
            }
            //EXTENSION
            if (gamepad1.left_bumper){
                switch (clawExtendState){
                    case 0: clawExtend.setPosition(CLAW_EXTENDED);
                        Thread.sleep(300);
                        clawExtendState=1;
                        break;
                    case 1: clawExtend.setPosition(CLAW_RETRACTED);
                        Thread.sleep(300);
                        clawExtendState=0;
                        break;
                }
            }


            //Grounded rotation
            if (gamepad1.a){
                clawRightRot.setPosition(CLAW_ROT_GROUND);
                clawLeftRot.setPosition(CLAW_ROT_GROUND);
            }
            //Forward Rotation
            if (gamepad1.x){
                clawRightRot.setPosition(CLAW_ROT_FRONT);
                clawLeftRot.setPosition(CLAW_ROT_FRONT);
            }

            //point up rotation
            if (gamepad1.dpad_left){
                clawRightRot.setPosition(CLAW_ROT_MID);
                clawLeftRot.setPosition(CLAW_ROT_MID);
            }


            //Slider move to the top basket

            /*
            if (gamepad1.dpad_up){
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
            */

            //Slider move to the middle basket
            if (gamepad1.dpad_up){
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

            //Reset Basket scoring
                if (gamepad1.start) {
                    boxScoringStateTop=0;
                    boxScoringStateBot =0;
                    clawExtend.setPosition(CLAW_RETRACTED);
                        sliderRetractionTopBox();
                    clawRightRot.setPosition(CLAW_ROT_GROUND);
                    clawLeftRot.setPosition(CLAW_ROT_GROUND);
                }



            //Grab success Basket scoring phase

            if (gamepad1.y){
                switch (boxScoringStateTop){
                    case 0:

                        clawRightRot.setPosition(CLAW_ROT_FRONT);
                        clawLeftRot.setPosition(CLAW_ROT_FRONT);
                        clawExtend.setPosition(CLAW_RETRACTED);
                        Thread.sleep(300);
                        boxScoringStateTop++;
                        break;
                    case 1:
                        sliderExtensionTopBox();
                        clawRightRot.setPosition(CLAW_ROT_BACK);
                        clawLeftRot.setPosition(CLAW_ROT_BACK);
                        Thread.sleep(500);
                        clawExtend.setPosition(CLAW_EXTENDED);
                        boxScoringStateTop++;
                        break;
                    case 2:
                        clawGrab.setPosition(CLAW_RELEASE);
                        Thread.sleep(300);
                        clawExtend.setPosition(CLAW_RETRACTED);
                        clawRightRot.setPosition(CLAW_ROT_GROUND);
                        clawLeftRot.setPosition(CLAW_ROT_GROUND);
                        sliderRetractionTopBox();
                        boxScoringStateTop=0;;
                        break;

                }
            }


            if (gamepad1.b){
                switch (boxScoringStateBot){
                    case 0:

                        clawRightRot.setPosition(CLAW_ROT_FRONT);
                        clawLeftRot.setPosition(CLAW_ROT_FRONT);
                        Thread.sleep(300);
                        clawExtend.setPosition(CLAW_RETRACTED);
                        boxScoringStateBot++;
                        break;
                    case 1:
                        sliderExtensionMidBox();
                        clawRightRot.setPosition(CLAW_ROT_BACK);
                        clawLeftRot.setPosition(CLAW_ROT_BACK);
                        Thread.sleep(500);
                        clawExtend.setPosition(CLAW_EXTENDED);
                        boxScoringStateBot++;
                        break;
                    case 2:
                        clawGrab.setPosition(CLAW_RELEASE);
                        Thread.sleep(300);
                        clawExtend.setPosition(CLAW_RETRACTED);
                        clawRightRot.setPosition(CLAW_ROT_GROUND);
                        clawLeftRot.setPosition(CLAW_ROT_GROUND);
                        sliderRetractionMidBox();
                        boxScoringStateBot =0;
                        break;
                }
            }

            }



        }
    }
