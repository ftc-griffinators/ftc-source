package org.firstinspires.ftc.teamcode.griffinators;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.DeadWheelDirectionDebugger;
import com.acmerobotics.roadrunner.ftc.DriveViewFactory;
import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.PositionVelocityPair;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Localizer;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.ThreeDeadWheelLocalizer;

@TeleOp(name = "Testing",group = "Robot")

public class TestOp extends LinearOpMode {

    DcMotor frontLeft, frontRight, backLeft, backRight, sliderLeft, sliderRight;
    Servo clawExtension, clawGrab, clawRightRot, clawLeftRot;


    @Override
    public void runOpMode() throws InterruptedException {


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


        Encoder leftSliderEncoder=new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class,"leftSlider"))) ;
        Encoder rightSliderEncoder=new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class,"rightSlider")));
        leftSliderEncoder.setDirection(DcMotorSimple.Direction.REVERSE);







        //"ce" is port 5 on control hub
        clawExtension = hardwareMap.get(Servo.class,"ce");
        //"c" is port 4
        clawGrab = hardwareMap.get(Servo.class,"c");
        //"r" is port 2
        clawRightRot = hardwareMap.get(Servo.class,"r");
        // "l" is port 3
        clawLeftRot = hardwareMap.get(Servo.class,"l");


        ThreeDeadWheelLocalizer deadWheel = new ThreeDeadWheelLocalizer(hardwareMap, MecanumDrive.PARAMS.inPerTick);


        Localizer localizer= new ThreeDeadWheelLocalizer(hardwareMap,MecanumDrive.PARAMS.inPerTick);
        Pose2d pose=new Pose2d(0,0,0);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sliderRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sliderLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    sliderLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()){
            pose = pose.plus(localizer.update().value());


            if (gamepad1.b){

                sliderRight.setTargetPosition(4000);
                sliderLeft.setTargetPosition(4000);
                sliderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sliderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                while (sliderLeft.isBusy() && sliderRight.isBusy()){
                    sliderRight.setPower(0.5);
                    sliderLeft.setPower(0.5);
                }



            }




            PositionVelocityPair par0PoseAndVel =deadWheel.par0.getPositionAndVelocity();
            PositionVelocityPair par1PoseAndVel =deadWheel.par1.getPositionAndVelocity();
            PositionVelocityPair perpPoseAndVel =deadWheel.perp.getPositionAndVelocity();

            PositionVelocityPair rightSliderEncoderPoseAndVel=rightSliderEncoder.getPositionAndVelocity();
            PositionVelocityPair leftSliderEncoderPoseAndVel=leftSliderEncoder.getPositionAndVelocity();


            telemetry.addData("Position of encoder 1",par0PoseAndVel.position);
            telemetry.addLine();
            telemetry.addData("Position of encoder 2",par1PoseAndVel.position);
            telemetry.addLine();
            telemetry.addData("Position of perp encoder",perpPoseAndVel.position);
            telemetry.addLine();
            telemetry.addData("Total position",pose.position);
            telemetry.addLine();
            telemetry.addData("Right slider encoder pose",rightSliderEncoderPoseAndVel.position);
            telemetry.addData("Left slider encoder pose",leftSliderEncoderPoseAndVel.position);

            telemetry.update();

            /*
            clawRightRot.setDirection(Servo.Direction.REVERSE);
            clawRightRot.setPosition(0.5);
            clawLeftRot.setPosition(0.5);

             */
        }











    }

}
