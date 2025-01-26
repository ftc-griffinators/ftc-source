package org.firstinspires.ftc.teamcode.griffinators;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.ThreeDeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.math.Transform;
import org.firstinspires.ftc.teamcode.parts.Claw;
import org.firstinspires.ftc.teamcode.systems.VisionSystem;

import java.util.List;

@TeleOp(name = "Test2",group = "Robot")
public class Test2 extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {


        Claw claw=new Claw(hardwareMap);
        VisionSystem vision=new VisionSystem(hardwareMap,"limeLight",0);



        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("op mode","run");
            List<List<Double>> list=vision.getCorners();
            telemetry.addData("corners null", list==null? "Error " : list.size());
            boolean valid=vision.hasValidTarget();

            Transform pose=vision.getTargetDiffPose(list);
            telemetry.addData("Num of right",Claw.o);
            telemetry.addData("Pose",pose.orientation.yaw);
            telemetry.addData("Valid",valid);
            telemetry.addData("num of wrong",Claw.in);
            telemetry.addData("NAN",vision.getCamRelativeTargetOrientation(list));
            telemetry.addData("vison",VisionSystem.visonError);

             telemetry.update();

            claw.sampleGrabbing(list,valid,vision);
        }
    }
}