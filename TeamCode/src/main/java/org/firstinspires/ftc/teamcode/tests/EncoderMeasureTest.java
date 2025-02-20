package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Localizer;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.ThreeDeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.ThreeDeadWheelLocalizer2;
import org.firstinspires.ftc.teamcode.parts.Claw;
import org.firstinspires.ftc.teamcode.parts.Drive;
import org.firstinspires.ftc.teamcode.parts.Hanger;
import org.firstinspires.ftc.teamcode.parts.Slider;

@TeleOp(name = "measurementTest",group = "Robot")
public class EncoderMeasureTest extends LinearOpMode {

    public Localizer localizer;
    Pose2d pose=new Pose2d(0,0,0);


    @Override
    public void runOpMode() throws InterruptedException {
        localizer = new ThreeDeadWheelLocalizer(hardwareMap, MecanumDrive.PARAMS.inPerTick);

        Slider slider=new Slider(hardwareMap);
        Hanger hanger=new Hanger(hardwareMap);
        Claw claw=new Claw(hardwareMap);

        Hanger hangerEncoder=hanger.getHangerEncoder(hardwareMap);
        Slider sliderEncoder=slider.getSliderEncoder(hardwareMap);

        TelemetryPacket packet= new TelemetryPacket();
        FtcDashboard dashboard=FtcDashboard.getInstance();


        telemetry.addData("Status", "Initialized");


        waitForStart();
        while (opModeIsActive()){
            pose = pose.plus(localizer.update().value());


            Claw positions=claw.servoEncoderPosition();


            telemetry.addData("xPose",pose.position.x);
            telemetry.addData("yPose",pose.position.y);
            telemetry.addData("heading",pose.heading.toDouble());


            telemetry.addData("leftArmPose",positions.leftArmPosition);
            telemetry.addData("rightArmPose",positions.rightArmPosition);
            telemetry.addData("extensionPose",positions.extensionPosition);

            packet.put("xPose",pose.position.x);
            packet.put("yPose",pose.position.y);
            packet.put("heading",pose.heading.toDouble());

           packet.put("leftSlider",sliderEncoder.leftSliderEncoder.getPositionAndVelocity().position);
            packet.put("rightSlider",sliderEncoder.rightSliderEncoder.getPositionAndVelocity().position);
            packet.put("rightHanger",hangerEncoder.rightHangerEncoder.getPositionAndVelocity().position);
            packet.put("leftHanger",hangerEncoder.leftHangerEncoder.getPositionAndVelocity().position);

            packet.put("leftArmPose",claw.leftArmPosition);
            packet.put("rightArmPose",claw.rightArmPosition);
            packet.put("extensionPose",claw.extensionPosition);

telemetry.addData("rightHanger",hangerEncoder.rightHangerEncoder.getPositionAndVelocity().position);
telemetry.addData("leftHanger",hangerEncoder.leftHangerEncoder.getPositionAndVelocity().position);
            telemetry.addData("leftSlider",sliderEncoder.leftSliderEncoder.getPositionAndVelocity().position);
            telemetry.addData("rightSlider",sliderEncoder.rightSliderEncoder.getPositionAndVelocity().position);

            dashboard.sendTelemetryPacket(packet);
            telemetry.update();
//
        }


    }
}
