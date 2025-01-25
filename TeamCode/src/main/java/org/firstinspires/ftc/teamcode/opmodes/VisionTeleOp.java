package org.firstinspires.ftc.teamcode.opmodes;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.math.Transform;
import org.firstinspires.ftc.teamcode.systems.VisionSystem;

import java.util.List;

@TeleOp(name = "Vision Test", group = "Robot")
public class VisionTeleOp extends LinearOpMode
{

    public int wrong;
    public int excess;
    @SuppressLint("DefaultLocale")
    @Override
    public void runOpMode()
    {
        VisionSystem vision = new VisionSystem(hardwareMap, "limeLight");
        telemetry.addLine("Vision Test Ready");
        telemetry.update();

        vision.setPipeline(0);

        waitForStart();
        while (opModeIsActive())
        {


            telemetry.addData("Has Target", vision.hasValidTarget());

            List<List<Double>> corners=vision.getCorners();

            if (vision.hasValidTarget() && corners==null){
                wrong++;
            }
            if (corners.size()>4){
                excess++;
            }

            Transform pose = vision.getTargetPose(corners);
            if (vision.hasValidTarget())
            {
                telemetry.addData("Valid",vision.hasValidTarget());
                telemetry.addData("Corner",corners);
                telemetry.addData("num of wrong",wrong);
                telemetry.addData("Num of corners",vision.numOfCorners);
                telemetry.addData("Corners overflow",excess);
                /*
                telemetry.addData("TX", String.format("%.2f°", pose.position.x));
                telemetry.addData("TY", String.format("%.2f°", pose.position.y));



                telemetry.addData("Orientation",pose.orientation.yaw);
                */

                        }

            telemetry.update();
        }
    }
}