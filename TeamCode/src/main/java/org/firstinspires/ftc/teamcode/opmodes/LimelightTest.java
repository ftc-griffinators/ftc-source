package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.parts.Claw;
import org.firstinspires.ftc.teamcode.systems.LimeLightSystem;

import java.util.Arrays;
import java.util.List;

@TeleOp(name = "Lime Light test")
public class LimelightTest extends LinearOpMode {

    public int blueLimeLightPipeline=0;
    public int yellowLimeLightPipeline=1;
    public int redLimeLightPipeline=2;

    @Override
    public void runOpMode() throws InterruptedException {
        Limelight3A limeLight= hardwareMap.get(Limelight3A.class,"limeLight");

        telemetry.setMsTransmissionInterval(11);
        limeLight.pipelineSwitch(1);
        limeLight.start();

        waitForStart();

        while (opModeIsActive()){
            LLStatus status=limeLight.getStatus();
            telemetry.addData("Name", "%s",
                    status.getName());
            telemetry.addData("LL", "Temp: %.1fC, CPU: %.1f%%, FPS: %d",
                    status.getTemp(), status.getCpu(),(int)status.getFps());
            telemetry.addData("Pipeline", "Index: %d, Type: %s",
                    status.getPipelineIndex(), status.getPipelineType());



            LLResult result= limeLight.getLatestResult();
            boolean valid=result!=null && result.isValid();


            if (valid){
                Pose3D llResult=result.getBotpose();

                List<LLResultTypes.ColorResult> list=result.getColorResults();

                LLResultTypes.ColorResult finalResult = list.get(0);

                Pose3D TPoseCSpace=finalResult.getTargetPoseCameraSpace();
                Pose3D TPoseRSpace=finalResult.getTargetPoseRobotSpace();
                Pose3D  CPoseTSpace= finalResult.getCameraPoseTargetSpace();
                Pose3D  RPoseFSpace= finalResult.getRobotPoseFieldSpace();
                Pose3D  RPoseTSpace= finalResult.getRobotPoseTargetSpace();



                    telemetry.addData("rbder",result.getTx());
                    telemetry.addData("rbder",result.getTy());
                    telemetry.addData("corner",finalResult.getTargetCorners());
                    telemetry.addData("Valid:",valid);
                    telemetry.addData("normalX",llResult.getPosition().x);
                    telemetry.addData("normalDegree", llResult.getOrientation().getYaw(AngleUnit.DEGREES));
                telemetry.addData("TPoseCSpace:",TPoseCSpace.getPosition());
                telemetry.addData("Valid:",valid);
                telemetry.addData("Valid:",valid);
                telemetry.addData("Valid:",valid);
                telemetry.addData("Valid:",valid);

                    telemetry.update();



            }


        }

        limeLight.stop();




    }
}
