package org.firstinspires.ftc.teamcode.components.Autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.parts.Claw;
import org.firstinspires.ftc.teamcode.parts.Slider;


@Autonomous(name = "BlueSampleAuto")
@Config
public class BlueSampleAuto extends LinearOpMode {
    public static double xPose=18.81505947359435;
    public static double yPose=25.276865707856548;
    public static double headingDegree=0.5256;



    public static Pose2d boxScoringPose= new Pose2d(7.5324,24.2975,-40.3282*Math.PI/180);
    public static Pose2d rightSampleZone= new Pose2d(18.196714000801663,15.131687641769346,-0.087);
    public static Pose2d midSampleZone=new Pose2d(17.537188152180427,23.65763929740031,-0.087);
    public static Pose2d leftSampleZone=new Pose2d(18.81505947359435,25.276865707856548,0.4383081603154254);



    public double angleDegrees(double degrees){
        return degrees*(Math.PI/180);
    }
//x2=15.812798837347236
    //y2=1.4188852297650303
    //d2=45.57829841777924




    private void score(Claw claw, Slider slider){
        slider.sliderExtensionTopBox();
        sleep(1000);
        claw.rotateArm(Claw.CLAW_ROT_BACK);
        sleep(1100);
        claw.extend();
        sleep(200);
        claw.release();
        sleep(500);
        claw.rotateArm(Claw.CLAW_ROT_FRONT);
        claw.retract();
        slider.sliderRetractionBox();
        sleep(2000);
    }

    @Override
    public void runOpMode() throws InterruptedException {
         MecanumDrive drive= new MecanumDrive(hardwareMap,new Pose2d(0,0,0));


        Claw claw=new Claw(hardwareMap);
        Slider slider=new Slider(hardwareMap);



        claw.clawInit();
        Action startToBox = drive.actionBuilder(drive.pose).strafeTo(new Vector2d(boxScoringPose.position.x, boxScoringPose.position.y)).turnTo(boxScoringPose.heading).build();
        Action BoxToRightSampleZone= drive.actionBuilder(boxScoringPose).strafeTo(new Vector2d(rightSampleZone.position.x,rightSampleZone.position.y)).turnTo(rightSampleZone.heading).build();
        Action RightSampleZoneToBox = drive.actionBuilder(rightSampleZone).strafeTo(new Vector2d(boxScoringPose.position.x, boxScoringPose.position.y)).turnTo(boxScoringPose.heading).build();
        Action BoxToMidSampleZone= drive.actionBuilder(boxScoringPose).strafeTo(new Vector2d(midSampleZone.position.x, midSampleZone.position.y)).turnTo(midSampleZone.heading).build();
        Action MidSampleZoneToBox =drive.actionBuilder(midSampleZone).strafeTo(new Vector2d(boxScoringPose.position.x, boxScoringPose.position.y)).turnTo(boxScoringPose.heading).build();
        Action BoxToLeftSampleZone = drive.actionBuilder(boxScoringPose).strafeTo(new Vector2d(leftSampleZone.position.x, leftSampleZone.position.y)).turnTo(leftSampleZone.heading).build();
        Action LeftSampleZoneToBox =drive.actionBuilder(leftSampleZone).strafeTo(new Vector2d(boxScoringPose.position.x, boxScoringPose.position.y)).turnTo(boxScoringPose.heading).build();


        waitForStart();
        slider.sliderInit();



        Actions.runBlocking(startToBox);
        sleep(500);
        score(claw,slider);
        sleep(500);



       Actions.runBlocking(BoxToRightSampleZone);
        sleep(1000);
        Actions.runBlocking(RightSampleZoneToBox);
        sleep(1000);
        Actions.runBlocking(BoxToMidSampleZone);
        sleep(1000);
        Actions.runBlocking(MidSampleZoneToBox);
        sleep(1000);
        Actions.runBlocking(BoxToLeftSampleZone);
        sleep(1000);
        Actions.runBlocking(LeftSampleZoneToBox);

    }
}
