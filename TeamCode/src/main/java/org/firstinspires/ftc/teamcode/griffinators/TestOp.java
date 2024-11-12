package org.firstinspires.ftc.teamcode.griffinators;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.DeadWheelDirectionDebugger;
import com.acmerobotics.roadrunner.ftc.DriveViewFactory;
import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.PositionVelocityPair;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Localizer;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.ThreeDeadWheelLocalizer;

@TeleOp(name = "Testing",group = "Robot")

public class TestOp extends LinearOpMode {

    DcMotor frontLeft, frontRight, backLeft, backRight, sliderLeft, sliderRight;


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

        ThreeDeadWheelLocalizer deadWheel = new ThreeDeadWheelLocalizer(hardwareMap, MecanumDrive.PARAMS.inPerTick);


               Localizer localizer= new ThreeDeadWheelLocalizer(hardwareMap,MecanumDrive.PARAMS.inPerTick);
        Pose2d pose=new Pose2d(0,0,0);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    sliderLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()){
            pose = pose.plus(localizer.update().value());

            if (gamepad1.dpad_up){
                sliderRight.setPower(0.4);
                sliderLeft.setPower(0.4);
            }
            if (gamepad1.dpad_down){
                sliderRight.setPower(-0.4);
                sliderLeft.setPower(-0.4);

            }
            if (!(gamepad1.dpad_up) || !(gamepad1.dpad_down)){
                sliderRight.setPower(0);
                sliderLeft.setPower(0);
            }

            PositionVelocityPair par0PoseAndVel =deadWheel.par0.getPositionAndVelocity();
            PositionVelocityPair par1PoseAndVel =deadWheel.par1.getPositionAndVelocity();
            PositionVelocityPair perpPoseAndVel =deadWheel.perp.getPositionAndVelocity();


            telemetry.addData("Position of encoder 1",par0PoseAndVel.position);
            telemetry.addLine();
            telemetry.addData("Position of encoder 2",par1PoseAndVel.position);
            telemetry.addLine();
            telemetry.addData("Position of perp encoder",perpPoseAndVel.position);
            telemetry.addLine();
            telemetry.addData("Total position",pose.position);
            telemetry.update();

        }










    }

}
