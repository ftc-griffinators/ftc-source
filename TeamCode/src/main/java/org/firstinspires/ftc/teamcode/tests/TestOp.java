package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
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
import org.firstinspires.ftc.teamcode.Utility.Timing;
import org.firstinspires.ftc.teamcode.parts.Claw;
import org.firstinspires.ftc.teamcode.parts.Hanger;
import org.firstinspires.ftc.teamcode.parts.Slider;

@TeleOp(name = "Testing",group = "Robot")
public class TestOp extends LinearOpMode {

    DcMotorEx frontLeft, frontRight, backLeft, backRight,sliderLeft,sliderRight;

    Encoder leftSliderEncoder,rightSliderEncoder;

    ;
    TelemetryPacket packet=new TelemetryPacket();
    FtcDashboard ftcDashboard=FtcDashboard.getInstance();

    //topBox=2500


    @Override
    public void runOpMode() throws InterruptedException {

        Claw claw=new Claw(hardwareMap);
        Slider slider=new Slider(hardwareMap);
        Hanger hanger=new Hanger(hardwareMap);
        Timing timer=new Timing();



        telemetry.addData("Status", "Initialized");
        telemetry.update();

        hanger.initHanger();

        waitForStart();


        while (opModeIsActive()) {
            if (gamepad1.a){
                hanger.hang();
            }
            if (gamepad1.b){
                hanger.resetHanger();
            }

        }

    }

}
