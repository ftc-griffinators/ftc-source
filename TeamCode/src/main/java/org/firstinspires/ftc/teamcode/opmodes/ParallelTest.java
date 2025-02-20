package org.firstinspires.ftc.teamcode.opmodes;


import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_EXTENDED;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_OPEN_TELEOP;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_PITCH_MID;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_RETRACTED;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_ROT_FRONT;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_ROT_GROUND_EXTENDED;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_ROT_GROUND_RETRACTED;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_ROT_MID;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utility.ActionCondition;
import org.firstinspires.ftc.teamcode.Utility.CustomAction;
import org.firstinspires.ftc.teamcode.Utility.Sequencing;
import org.firstinspires.ftc.teamcode.Utility.Timing;
import org.firstinspires.ftc.teamcode.parts.Claw;
import org.firstinspires.ftc.teamcode.parts.Drive;
import org.firstinspires.ftc.teamcode.parts.Hanger;
import org.firstinspires.ftc.teamcode.parts.Slider;
import org.firstinspires.ftc.teamcode.systems.VisionSystem;

@TeleOp(name ="Parallel",group = "Robot")
@Config
public class ParallelTest extends LinearOpMode {





    int boxScoringStateTop=0;
    int clawGrabState=0;
    int subClearing=0;
    int init=0;
    int pipelineToggle=0;
    boolean extension;
    @Override
    public void runOpMode() throws InterruptedException{
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        Drive drive=new Drive(hardwareMap);
        Claw claw=new Claw(hardwareMap);
        Slider slider=new Slider(hardwareMap);
        Hanger hanger=new Hanger(hardwareMap);
        Timing timer=new Timing();
        VisionSystem vision=new VisionSystem(hardwareMap,1);

        hanger.initHanger();



         CustomAction align=new CustomAction(
                claw::alignerReset,
                ()->claw.orientationAligning(vision));

           ActionCondition alignCondition=new ActionCondition(
                ()->timer.delay(200),
                ()->claw.startAligning);



           CustomAction boxScoreStart =new CustomAction(
                   ()-> claw.rotateArm(CLAW_ROT_MID),
                claw::middleAlignment,
                slider::sliderExtensionTopBox,
                claw::boxScoring);

           ActionCondition boxScoreStartCondition =new ActionCondition(
                   ActionCondition::noCondition,
                ActionCondition::noCondition,
                ()->timer.delay(1200),
                ActionCondition::noCondition);



        telemetry.addData("Status", "Initialized");
        telemetry.update();
             waitForStart();

        //Field centric mecanum drive
        while (opModeIsActive()){
            if (init==0){

                claw.teleOpInit();
                slider.initSlider();
                init=1;
            }

            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double turn= gamepad1.right_stick_x;


            drive.mecanumDriving(x,y,turn,1);

            //Grab success Basket scoring phase



            if (gamepad2.y){
                switch (boxScoringStateTop){
                    case 0:
                        if (!extension) {
                            Sequencing.allowAction(boxScoreStart);
                            boxScoringStateTop++;
                        }
                        break;
                    case 1:
                        claw.release();
                        Thread.sleep(300);
                        claw.grab();
                        claw.rotateArm(CLAW_ROT_MID);
                        claw.retract();
                        slider.sliderRetraction();
                        claw.rotateArm(CLAW_ROT_FRONT);
                        claw.clearSub();

                        boxScoringStateTop=0;;
                        break;
                }
            }

            if (Sequencing.isActionAllowed(boxScoreStart)){
                Sequencing.runInParallel(boxScoreStart,boxScoreStartCondition);
            }




/*

            if (gamepad2.y){
                switch (boxScoringStateTop){
                    case 0:
                        if (!extension){
                            claw.rotateArm(CLAW_ROT_MID);
                            claw.middleAlignment();
                            slider.sliderExtensionTopBox();

                            drive.backRight.setPower(0);
                            drive.backLeft.setPower(0);
                            drive.frontLeft.setPower(0);
                            drive.frontRight.setPower(0);


                            Thread.sleep(1200);
                            claw.boxScoring();
                            boxScoringStateTop++;
                        }
                        break;
                    case 1:
                        claw.release();
                        Thread.sleep(300);
                        claw.grab();
                        claw.rotateArm(CLAW_ROT_MID);
                        claw.retract();
                        slider.sliderRetraction();
                        claw.rotateArm(CLAW_ROT_FRONT);
                        claw.clearSub();
                       ;

                        boxScoringStateTop=0;;
                        break;
                }
            }


 */
/*
            if (gamepad2.x){
                switch (boxScoringStateTop){
                    case 0:
                        if (!extension){
                            claw.rotateArm(CLAW_ROT_MID);
                            claw.middleAlignment();
                            slider.sliderExtensionTopBox();
                            Thread.sleep(1200);
                            claw.boxScoringFront();
                            boxScoringStateTop++;
                        }
                        break;
                    case 1:
                        claw.release();
                        Thread.sleep(300);
                        claw.clawPitch.setPosition(CLAW_PITCH_MID);
                        claw.grab();
                        claw.rotateArm(CLAW_ROT_MID);
                        claw.retract();
                        slider.sliderRetraction();
                        drive.backRight.setPower(-0.75);
                        drive.backLeft.setPower(-0.75);
                        drive.frontLeft.setPower(-0.75);
                        drive.frontRight.setPower(-0.75);
                        Thread.sleep(300);
                        drive.backRight.setPower(0);
                        drive.backLeft.setPower(0);
                        drive.frontLeft.setPower(0);
                        drive.frontRight.setPower(0);

                        claw.rotateArm(CLAW_ROT_FRONT);
                        claw.grabPrep();


                        boxScoringStateTop=0;;
                        break;
                }
            }

 */

            if (gamepad2.start){
                drive.stopDrive();
                hanger.hang();
            }

/*
            if (gamepad1.a){
                Sequencing.allowAction(align);
            }
            if (Sequencing.isActionAllowed(align)){
                Sequencing.runInParallel(align,alignCondition);
            }
 */






            //Grabbing
            if (gamepad1.right_bumper){
                switch (clawGrabState) {
                    case 0:
                        claw.rotateArm(extension ? CLAW_ROT_GROUND_EXTENDED : CLAW_ROT_GROUND_RETRACTED);
                        Thread.sleep(500);
                        claw.grab();
                        Thread.sleep(500);
                        claw.rotateArm(CLAW_ROT_FRONT);
                        Thread.sleep(300);
                        clawGrabState = 1;
                        break;
                    case 1:
                        claw.clawGrab.setPosition(CLAW_OPEN_TELEOP);
                          // claw.retract();
                        Thread.sleep(300);
                        clawGrabState = 0;
                        break;

                }
            }





            //Toggle sub
            if (gamepad1.left_bumper){
                switch (subClearing){
                    case 0:
                        claw.grabPrep();
                        Thread.sleep(300);
                       claw.clawGrab.setPosition(CLAW_OPEN_TELEOP);
                        subClearing=1;
                        break;
                    case 1:
                        claw.clearSub();
                        Thread.sleep(300);
                        subClearing=0;
                        break;
                }
            }

            if (gamepad1.start){
                drive.pose=new Pose2d(0,0,0);
            }






           if (gamepad1.dpad_up){
               claw.clawAlignment.setPosition(0.5);
               claw.startAligning=false;
           }
           if (gamepad1.dpad_left){
               claw.clawAlignment.setPosition(0.75);
               claw.startAligning=false;
           }
           if (gamepad1.dpad_right){
               claw.clawAlignment.setPosition(0.25);
               claw.startAligning=false;
           }
           if (gamepad1.dpad_down){
               claw.clawAlignment.setPosition(0);
               claw.startAligning=false;
           }



            if (gamepad2.left_bumper){
                claw.clawExtend.setPosition(CLAW_EXTENDED);
                extension=true;
            }
            if (gamepad2.right_bumper){
                claw.clearSub();
                sleep(100);
                claw.clawExtend.setPosition(CLAW_RETRACTED);
                extension=false;

            }








            }
        }
    }

