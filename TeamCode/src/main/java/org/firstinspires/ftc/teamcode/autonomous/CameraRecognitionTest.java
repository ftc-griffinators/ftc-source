package org.firstinspires.ftc.teamcode.autonomous;

import static com.qualcomm.robotcore.util.ElapsedTime.Resolution.MILLISECONDS;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_PITCH_BOT;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_ROT_FRONT;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_ROT_GROUND_EXTENDED;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.parts.Claw;
import org.firstinspires.ftc.teamcode.parts.Slider;
import org.firstinspires.ftc.teamcode.systems.VisionSystem;

@Autonomous(name = "camera alignment")
@Config
public class CameraRecognitionTest extends LinearOpMode {

    public static double offset = 11;
    public static double powerMultiplier = 0.33;

    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));

        VisionSystem visionSystem = new VisionSystem(hardwareMap,1);

        Claw claw = new Claw(hardwareMap);
        Slider slider = new Slider(hardwareMap);

        waitForStart();

        slider.initSlider();


        checkAndMove(slider, claw, visionSystem, drive);
        pickup(claw,slider);


    }


    public void checkAndMove(Slider slider, Claw claw, VisionSystem vision,MecanumDrive drive){
        slider.moveSlidersTo(600);
        claw.clawPitch.setPosition(CLAW_PITCH_BOT);
        claw.extend();
        claw.rotateArm(CLAW_ROT_FRONT);
        claw.release();
        sleep(1000);


        double x;
        double y;


        while (!vision.isTargetCentered(0, offset)){

            x=vision.getTargetPosition().x;
            y=vision.getTargetPosition().y + offset;

            PoseVelocity2d velocityVector = new PoseVelocity2d(new Vector2d(y, -x), 0 );

            drive.setDrivePowers(velocityVector, powerMultiplier);
        }
        drive.setDrivePowers(new PoseVelocity2d(new Vector2d(0,0), 0), 1);

        claw.grab();

        sleep(3000);
    }

    private void pickup(Claw claw,Slider slider) {
        slider.sliderRetraction();
        sleep(300);
        claw.extend();
        claw.clawPitch.setPosition(CLAW_PITCH_BOT);
        claw.release();
        sleep(400);

        claw.rotateArm(CLAW_ROT_GROUND_EXTENDED);
        sleep(500);
        claw.grab();
        sleep(500);
        claw.rotateArm(CLAW_ROT_FRONT);
        sleep(200);
    }
}
