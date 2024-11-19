package org.firstinspires.ftc.teamcode.griffinators;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.ServoControllerEx;
import com.qualcomm.robotcore.hardware.ServoImpl;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.configuration.ServoFlavor;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Localizer;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.ThreeDeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.griffinators.Parts.PIDController;
@TeleOp(name ="ServoTest",group = "Tests")
public class ServoTest extends LinearOpMode {
    Servo clawRightRot, clawLeftRot, clawExtend;

    @Override
    public void runOpMode() throws InterruptedException {


        telemetry.addData("Status", "Initialized");
        telemetry.update();
        clawRightRot= hardwareMap.get(Servo.class, "clawRightRot");
        clawLeftRot = hardwareMap.get(Servo.class, "clawLeftRot");
        clawExtend = hardwareMap.get(Servo.class, "clawExtend");

        clawRightRot.setDirection(Servo.Direction.REVERSE);
        waitForStart();
        while(opModeIsActive()){
            if (gamepad1.a) {
                clawLeftRot.setPosition(clawLeftRot.getPosition()+ 0.02);
                sleep(20);
                clawRightRot.setPosition(clawRightRot.getPosition()+0.02);
                sleep(20);

            }
            if (gamepad1.b) {
                clawLeftRot.setPosition(clawLeftRot.getPosition()- 0.10 );
                sleep(20);

                clawRightRot.setPosition(clawRightRot.getPosition()- 0.10);
                sleep(20);

            }
    }}}
