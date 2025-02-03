package org.firstinspires.ftc.teamcode.tests;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.Utility.Timing;
import org.firstinspires.ftc.teamcode.Utility.Transform;
import org.firstinspires.ftc.teamcode.parts.Claw;
import org.firstinspires.ftc.teamcode.systems.VisionSystem;

import java.util.List;

@TeleOp(name = "Vision Test", group = "Robot")
public class VisionTeleOp extends LinearOpMode
{

    public int wrong;
    public int excess;
    public int rot;
    public boolean reload;
    public boolean toggle=false;
    @SuppressLint("DefaultLocale")
    @Override
    public void runOpMode() {
       VisionSystem vision = new VisionSystem(hardwareMap,1);

         telemetry.addLine("Vision Test Ready");
        telemetry.update();

        Claw claw=new Claw(hardwareMap);


        Timing delayer=Timing.delayTimer;



        waitForStart();
        while (opModeIsActive()) {


            List<List<Double>> corners=vision.getCorners();


            if (vision.hasValidTarget() && corners==null){
                wrong++;
            }
            if (corners.size()!=4){
                excess++;
            }
            if (corners.isEmpty()){
                rot++;
            }
            Transform pose = vision.getTargetDiffPose(corners);

            if (gamepad1.dpad_up){
             claw.clawAlignment.setPosition(0.5);
            }
                if (gamepad1.dpad_down){
                   claw.clawAlignment.setPosition(0);
                }
            if (gamepad1.dpad_right){
             claw.clawAlignment.setPosition(0.25);
                }
                if (gamepad1.dpad_left){
                 claw.clawAlignment.setPosition(0.75);
            }
                if (gamepad1.y){
              claw.clawAlignment.setPosition(1);
                }


                if (gamepad1.left_stick_button){

                    claw.alignerReset();
                    claw.alignStatus=1;
                    delayer.delay(400);
                }
                if (claw.alignStatus==1 && delayer.isDelayDone()){
                    claw.orientationAligning(corners,vision,pose);
                }






            telemetry.addData("Valid", vision.hasValidTarget());
                telemetry.addData("Corner", corners);
                telemetry.addData("num of time valid return but corners is null:", wrong);
                telemetry.addData("number of times corners is not 4:", excess);
                telemetry.addData("number of times corners is 0:", rot);
                telemetry.addData("equality",excess==rot);

                telemetry.addData("TX", String.format("%.2f°", pose.position.x));
                telemetry.addData("TY", String.format("%.2f°", pose.position.y));
                telemetry.addData("theta", pose.orientation.yaw*(180/Math.PI));
                telemetry.addData("Is aligned:", vision.isTargetAligned(corners));
                telemetry.addData("Is centered:", vision.isTargetCentered(corners));
                telemetry.addData("Turn direction:", vision.turnLeftOrRight(corners));
                telemetry.addData("Is running", vision.limelight.isRunning());
                telemetry.addData("Is connected", vision.limelight.isConnected());
                telemetry.addData("Time since last update", vision.limelight.getTimeSinceLastUpdate());
                telemetry.addData("Connection info", vision.limelight.getConnectionInfo());
            telemetry.addData("Reloaded", reload);





            if (gamepad1.start){
                reload=vision.limelight.reloadPipeline();

            }

            if (gamepad1.a){
                vision.stopPipeline();
                reload=false;
            }

            if(gamepad1.b){
                vision.limelight.pipelineSwitch(0);
                vision.limelight.setPollRateHz(100);
                vision.limelight.start();
            }




            telemetry.update();

        }
vision.stopPipeline();










        }
    }