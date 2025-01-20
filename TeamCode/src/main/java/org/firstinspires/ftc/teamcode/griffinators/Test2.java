package org.firstinspires.ftc.teamcode.griffinators;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.ThreeDeadWheelLocalizer;

@TeleOp(name = "Test2",group = "Robot")
public class Test2 extends LinearOpMode {
    Pose2d pose = new Pose2d(0, 0, 0);
    TelemetryPacket packet = new TelemetryPacket();

    @Override
    public void runOpMode() throws InterruptedException {

        ThreeDeadWheelLocalizer localizer = new ThreeDeadWheelLocalizer(hardwareMap, MecanumDrive.PARAMS.inPerTick);

        waitForStart();
        while (opModeIsActive()) {

            pose = pose.plus(localizer.update().value());

            packet.put("xPose:", pose.position.x);
            packet.put("yPose:", pose.position.y);
            packet.put("angle", pose.heading.toDouble());

            FtcDashboard dashboard = FtcDashboard.getInstance();
            dashboard.sendTelemetryPacket(packet);


        }
    }
}