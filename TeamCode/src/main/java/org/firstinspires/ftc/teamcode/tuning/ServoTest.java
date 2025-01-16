package org.firstinspires.ftc.teamcode.tuning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ServoTest", group = "Tests")
public class ServoTest extends LinearOpMode
{
    Servo clawRightRot, clawLeftRot, clawExtend;

    @Override
    public void runOpMode() throws InterruptedException
    {


        telemetry.addData("Status", "Initialized");
        telemetry.update();
        clawRightRot = hardwareMap.get(Servo.class, "clawRightRot");
        clawLeftRot = hardwareMap.get(Servo.class, "clawLeftRot");
        clawExtend = hardwareMap.get(Servo.class, "clawExtend");

        clawRightRot.setDirection(Servo.Direction.REVERSE);
        waitForStart();
        while (opModeIsActive())
        {
            if (gamepad1.a)
            {
                clawLeftRot.setPosition(clawLeftRot.getPosition() + 0.1);
                sleep(200);
                clawRightRot.setPosition(clawRightRot.getPosition() + 0.1);
                sleep(200);

            }
            if (gamepad1.b)
            {
                clawLeftRot.setPosition(clawLeftRot.getPosition() - 0.10);
                sleep(200);

                clawRightRot.setPosition(clawRightRot.getPosition() - 0.10);
                sleep(200);

            }

            if (gamepad1.dpad_left)
            {
                clawLeftRot.setPosition(0);
            }

        }
    }
}
