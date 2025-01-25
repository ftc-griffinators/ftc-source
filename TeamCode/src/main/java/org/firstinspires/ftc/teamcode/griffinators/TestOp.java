package org.firstinspires.ftc.teamcode.griffinators;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.ThreeDeadWheelLocalizer;

@TeleOp(name = "Testing",group = "Robot")
public class TestOp extends LinearOpMode {

    DcMotorEx frontLeft, frontRight, backLeft, backRight,sliderLeft,sliderRight;

    Encoder leftSliderEncoder,rightSliderEncoder;

    double leftRot, rightRot, extend=0;

    //topBox=2500

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

        frontLeft =  hardwareMap.get(DcMotorEx.class,"leftFront");
        //TODO switch the right front and right rear port
        //Control Hub 0
        frontRight =hardwareMap.get(DcMotorEx.class,"rightFront");
        //Expansion Hub 1
        backLeft = hardwareMap.get(DcMotorEx.class,"leftRear");
        // Control Hub Port 1
        backRight = hardwareMap.get(DcMotorEx.class,"rightRear");

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

        rightSliderEncoder=new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class,"rightSlider")));
        leftSliderEncoder=new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class,"leftSlider")));



       // Expansion Hub 2
        sliderLeft=hardwareMap.get(DcMotorEx.class,"leftSlider");
        sliderLeft.setDirection(DcMotorSimple.Direction.REVERSE);


        //Control Hub port 2
        sliderRight=hardwareMap.get(DcMotorEx.class,"rightSlider");

        sliderLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sliderRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

       rightSliderEncoder.setDirection(DcMotorSimple.Direction.REVERSE);;

        Pose2d pose = new Pose2d(0, 0, 0);


        ThreeDeadWheelLocalizer localizer=new ThreeDeadWheelLocalizer(hardwareMap,MecanumDrive.PARAMS.inPerTick);


        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()){
            pose = pose.plus(localizer.update().value());




            telemetry.addData("xPose",pose.position.x);
            telemetry.addData("yPose",pose.position.y);
            telemetry.addData("heading",pose.heading);

            telemetry.addData("lefSlider",leftSliderEncoder.getPositionAndVelocity().position);
            telemetry.addData("rightSlider",rightSliderEncoder.getPositionAndVelocity().position);
            telemetry.update();

        }











    }

}
