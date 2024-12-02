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


    private final double CLAW_EXTENDED=0;
    private final double CLAW_RETRACTED=0.26;
    private final double CLAW_ROT_MID=0.54;
    private final double CLAW_ROT_GROUND=0.19;
    private final double CLAW_ROT_FRONT=0.25;
    private final double CLAW_GRAB=0.83;
    private final double CLAW_RELEASE=0.71;
    private final double CLAW_ROT_BACK=0.80;



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

        rightSliderEncoder=new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class,"rightSlider")));
        leftSliderEncoder=new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class,"leftSlider")));

        // Expansion Hub 2
        sliderLeft=hardwareMap.get(DcMotorEx.class,"leftSlider");



        //Control Hub port 2
        sliderRight=hardwareMap.get(DcMotorEx.class,"rightSlider");

        sliderLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sliderRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

       rightSliderEncoder.setDirection(DcMotorSimple.Direction.REVERSE);;


//"clawExtend" is port 2 on control hub
        clawRightRot = hardwareMap.get(ServoImplEx.class,"clawRightRot");
        //"clawGrab" is port 3 on control hub
        clawGrab = hardwareMap.get(ServoImplEx.class,"clawGrab");
        //"clawRightRot" is port 0 on control hub
        clawExtend = hardwareMap.get(ServoImplEx.class,"clawExtend");

        // "clawLeftRot" is port 1 on control hub
        clawLeftRot = hardwareMap.get(ServoImplEx.class,"clawLeftRot");

        clawExtend.setPwmRange(new PwmControl.PwmRange(500,2500));

        clawLeftRot.setPwmRange(new PwmControl.PwmRange(500,2500));

        clawRightRot.setPwmRange(new PwmControl.PwmRange(500,2500));

        clawGrab.setPwmRange(new PwmControl.PwmRange(500,2500));

        clawLeftRot.setDirection(Servo.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()){
            if (gamepad1.y){
                frontLeft.setPower(0.5);
            }
            if (gamepad1.b){
                frontRight.setPower(0.5);
            }
            if (gamepad1.x){
                backLeft.setPower(0.5);

            }
            if (gamepad1.a){
                backRight.setPower(0.5);
            }





        }











    }

}
