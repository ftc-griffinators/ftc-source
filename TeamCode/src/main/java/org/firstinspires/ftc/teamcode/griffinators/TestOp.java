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

    DcMotorEx frontLeft, frontRight, backLeft, backRight,sliderLeft,sliderRight;

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

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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




            telemetry.addData("par0",localizer.par0.getPositionAndVelocity().position);
            telemetry.addData("par1",localizer.par1.getPositionAndVelocity().position);
            telemetry.addData("perp",localizer.perp.getPositionAndVelocity().position);


            telemetry.update();


        }











    }

}
