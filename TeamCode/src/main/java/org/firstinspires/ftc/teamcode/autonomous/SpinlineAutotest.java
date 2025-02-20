package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_PITCH_BOT;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_ROT_FRONT;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_ROT_GROUND_EXTENDED;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_ROT_MID;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.parts.Claw;
import org.firstinspires.ftc.teamcode.parts.Slider;
import org.firstinspires.ftc.teamcode.systems.VisionSystem;


@Autonomous(name = "spline")
@Config
public class SpinlineAutotest extends LinearOpMode
{
    public static double rightSampleZone_X = 15.5;
    public static double rightSampleZone_y = 14.2;
    public static double rightSampleZone_H = 0.05;


    public static double midSampleZone_X = 10.850166841617362;
    public static double midSampleZone_y = 24.071284829215873;
    public static double midSampleZone_H = 0.05;


    public static double leftSampleZone_X = 16;
    public static double leftSampleZone_y = 14.5;
    public static double leftSampleZone_H = 0.05;


    public static double offset = 9;
    public static double powerMultiplier = 0.55;




    public static Pose2d boxScoringPose = new Pose2d(4.8, 25.8, -0.85);
    public static Pose2d rightSampleZone = new Pose2d(rightSampleZone_X, rightSampleZone_y, rightSampleZone_H);
    public static Pose2d midSampleZone = new Pose2d(midSampleZone_X, midSampleZone_y, midSampleZone_H);
    public static Pose2d leftSampleZone = new Pose2d(leftSampleZone_X, leftSampleZone_y, leftSampleZone_H);
    public static Pose2d turnToNeg90 = new Pose2d(0, 0, -1.5788);
    public static Pose2d bar = new Pose2d(54, 0, 0);
    public int blueLimeLightPipeline = 0;
    public int yellowLimeLightPipeline = 1;
    public int redLimeLightPipeline = 2;

    private void score(Claw claw, Slider slider) {
        claw.retract();
        claw.rotateArm(CLAW_ROT_MID);
        claw.middleAlignment();
        slider.sliderExtensionTopBox();
        sleep(1100);
        claw.boxScoring();
        sleep(400);
        claw.release();
        sleep(150);
        claw.grab();
        claw.rotateArm(CLAW_ROT_MID);
        claw.retract();
        slider.sliderRetraction();
        claw.rotateArm(CLAW_ROT_FRONT);
        claw.grabPrep();
        sleep(1800);
    }

    private void endScore(Claw claw, Slider slider) {
        claw.retract();
        claw.rotateArm(CLAW_ROT_MID);
        claw.middleAlignment();
        slider.sliderExtensionTopBox();
        sleep(1200);
        claw.boxScoring();
        sleep(800);
        claw.release();
        sleep(200);
        claw.grab();
        claw.rotateArm(CLAW_ROT_MID);
        claw.retract();
        slider.sliderRetraction();
        claw.clearSub();
        sleep(2000);
    }





    @Override
    public void runOpMode() throws InterruptedException
    {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));

        VisionSystem visionSystem=new VisionSystem(hardwareMap,1);

        Claw claw = new Claw(hardwareMap);
        Slider slider = new Slider(hardwareMap);

        // visionSystem.pausePipeline();


        Action startToBox =
                drive.actionBuilder(drive.pose).splineTo(new Vector2d(boxScoringPose.position.x,
                        boxScoringPose.position.y),boxScoringPose.heading).build();
        Action BoxToRightSampleZone =
                drive.actionBuilder(boxScoringPose).splineTo(new Vector2d(rightSampleZone.position.x, rightSampleZone.position.y),rightSampleZone.heading).build();
        Action RightSampleZoneToBox =
                drive.actionBuilder(rightSampleZone).strafeTo(new Vector2d(boxScoringPose.position.x, boxScoringPose.position.y)).turnTo(boxScoringPose.heading).build();
        Action BoxToMidSampleZone =
                drive.actionBuilder(boxScoringPose).splineTo(new Vector2d(midSampleZone.position.x, midSampleZone.position.y),midSampleZone.heading).build();
        Action MidSampleZoneToBox =
                drive.actionBuilder(midSampleZone).strafeTo(new Vector2d(boxScoringPose.position.x, boxScoringPose.position.y)).turnTo(boxScoringPose.heading).build();
        Action BoxToLeftSampleZone =
                drive.actionBuilder(boxScoringPose).strafeTo(new Vector2d(leftSampleZone.position.x, leftSampleZone.position.y)).turnTo(leftSampleZone.heading).build();
        Action LeftSampleZoneToBox =
                drive.actionBuilder(leftSampleZone).strafeTo(new Vector2d(boxScoringPose.position.x, boxScoringPose.position.y)).turnTo(boxScoringPose.heading).build();

        slider.initSlider();
        claw.clawAutoSampleInit();
        waitForStart();


        Actions.runBlocking(startToBox);
        claw.clawPitch.setPosition(CLAW_PITCH_BOT);
        score(claw, slider);

        Actions.runBlocking(BoxToRightSampleZone);
        checkAndMove(slider, claw, visionSystem, drive);
        pickup(claw, slider);
        Actions.runBlocking(RightSampleZoneToBox);
        score(claw, slider);

        Actions.runBlocking(BoxToMidSampleZone);
        checkAndMove(slider, claw, visionSystem, drive);
        pickup(claw, slider);
        Actions.runBlocking(MidSampleZoneToBox);
        endScore(claw, slider);






    }


    public void checkAndMove(Slider slider, Claw claw, VisionSystem vision, MecanumDrive drive) {
        slider.moveSlidersTo(600);
        claw.clawPitch.setPosition(CLAW_PITCH_BOT);
        claw.extend();
        claw.rotateArm(CLAW_ROT_FRONT);
        claw.release();
        sleep(500);


        double x;
        double y;

        ElapsedTime time=new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        time.startTime();
        while (!vision.isTargetCentered(0, offset) || time.time() > 5000) {

            x = vision.getTargetPosition().x;
            y = vision.getTargetPosition().y + offset;

            PoseVelocity2d velocityVector = new PoseVelocity2d(new Vector2d(y, -x), 0);

            drive.setDrivePowers(velocityVector, powerMultiplier);
        }
        drive.setDrivePowers(new PoseVelocity2d(new Vector2d(0, 0), 0), 1);
    }

    private void pickup(Claw claw, Slider slider) {
        slider.sliderRetraction();
        sleep(200);
        claw.extend();
        claw.clawPitch.setPosition(CLAW_PITCH_BOT);
        claw.release();
        sleep(300);

        claw.rotateArm(CLAW_ROT_GROUND_EXTENDED);
        sleep(300);
        claw.grab();
        sleep(500);
        claw.rotateArm(CLAW_ROT_FRONT);
    }

}
