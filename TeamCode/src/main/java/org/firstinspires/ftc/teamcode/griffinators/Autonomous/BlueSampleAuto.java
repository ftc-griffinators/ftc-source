package org.firstinspires.ftc.teamcode.griffinators.Autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.MecanumDrive;

@Config
@Autonomous(name = "BlueSampleAuto")
public class BlueSampleAuto extends LinearOpMode {
    public static class Params{
        Pose2d boxPosition= new Pose2d(0,0,0);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive mecanumDrive= new MecanumDrive(hardwareMap,new Pose2d(0,0,0));

        Pose2d first = new Pose2d(mecanumDrive.pose.position.plus(new Vector2d(24, 12)), mecanumDrive.pose.heading);
        Pose2d second = new Pose2d(mecanumDrive.pose.position.plus(new Vector2d(24, -12)), mecanumDrive.pose.heading);



        Action moveToSubmersible= mecanumDrive.actionBuilder(mecanumDrive.pose)
                .strafeTo(first.position).build();
        Action moveToSubmersible2= mecanumDrive.actionBuilder(first)
                .strafeTo(second.position).build();
        Action moveToSubmersible3  = mecanumDrive.actionBuilder(second)
                .splineTo(mecanumDrive.pose.position, 0).build();



        waitForStart();

        for (int i = 0; i < 5; i++) {
            Actions.runBlocking(moveToSubmersible);
            Actions.runBlocking(moveToSubmersible2);
            Actions.runBlocking(moveToSubmersible3);
        }
    }
}
