package org.firstinspires.ftc.teamcode.griffinators;

import static org.firstinspires.ftc.teamcode.griffinators.Parts.Utility.sliderSmoothMovement;

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
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Localizer;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.ThreeDeadWheelLocalizer;

@TeleOp(name = "Testing",group = "Robot")

public class TestOp extends LinearOpMode {

    DcMotor frontLeft, frontRight, backLeft, backRight, sliderLeft, sliderRight;
    ServoImplEx clawExtend, clawGrab, clawRightRot, clawLeftRot;

    Encoder leftSliderEncoder,rightSliderEncoder;

    double leftRot, rightRot, extend=0;




    @Override
    public void runOpMode() throws InterruptedException {


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


     /*   rightSliderEncoder=new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class,"rightSlider")));
        leftSliderEncoder=new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class,"leftSlider")));
*/

        // Expansion Hub 2
        sliderLeft=hardwareMap.get(DcMotorEx.class,"leftSlider");



        //Control Hub port 2
        sliderRight=hardwareMap.get(DcMotorEx.class,"rightSlider");

        sliderLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sliderRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

      //  rightSliderEncoder.setDirection(DcMotorSimple.Direction.REVERSE);;


//"clawExtend" is port 2 on control hub
        clawRightRot = hardwareMap.get(ServoImplEx.class,"clawRightRot");
        //"clawGrab" is port 3 on control hub
        //clawGrab = hardwareMap.get(Servo.class,"clawGrab");
        //"clawRightRot" is port 0 on control hub
        clawExtend = hardwareMap.get(ServoImplEx.class,"clawExtend");

        // "clawLeftRot" is port 1 on control hub
        clawLeftRot = hardwareMap.get(ServoImplEx.class,"clawLeftRot");



        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()){

            if (gamepad2.dpad_up){
                leftRot+=0.0001;
            }
            if (gamepad2.dpad_down){
                leftRot-=0.0001;
            }
            if (gamepad2.y){
                rightRot+=0.0001;
            }
            if (gamepad2.a){
                rightRot-=0.0001;
            }

            if (gamepad2.b){
                extend+=0.0001;
            }
            if (gamepad2.x){
                extend-=0.0001;
            }
            if (gamepad1.y){
                clawExtend.setPosition(extend);
                telemetry.addData("extended","ex");
            }
            if (gamepad1.x){
                clawLeftRot.setPosition(leftRot);
            }
            if (gamepad1.b){
                clawRightRot.setPosition(rightRot);
            }
            telemetry.addData("press b to move | rightRot",rightRot);
            telemetry.addData("press x to move | leftRot",leftRot);
            telemetry.addData("press y to move | extend",extend);

            telemetry.update();

            //claw extension 0= full extension | 0.26=full retraction
            //




/*
            PositionVelocityPair rightSliderPoseAndVel=rightSliderEncoder.getPositionAndVelocity();
            PositionVelocityPair leftSliderPoseAndVel=leftSliderEncoder.getPositionAndVelocity();


            telemetry.addData("rightSliderEncoder",rightSliderPoseAndVel.position);
            telemetry.addData("leftSliderEncoder",leftSliderPoseAndVel.position);

            telemetry.update();

*/

        }











    }

}
