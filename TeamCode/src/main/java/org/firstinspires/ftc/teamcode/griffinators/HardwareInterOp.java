package org.firstinspires.ftc.teamcode.griffinators;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.external.samples.RobotHardware;

@TeleOp(name = "HardwareInter",group = "Robot")
public class HardwareInterOp extends LinearOpMode {
    Hardware hardware= new Hardware(this);

    int sliderState=0;

    @Override
    public void runOpMode() throws InterruptedException {
       hardware.initHardware();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
       waitForStart();


        double x = gamepad1.left_stick_x;
        double y = -gamepad1.left_stick_y;
        double turn= gamepad1.right_stick_x;

        hardware.TeleOpMecanumDrive(x,y,turn);


        if (gamepad1.b){
            switch (sliderState){
                case 0: hardware.sliderExtension();
                        sliderState=1;
                        break;
                case  1: hardware.sliderRetraction();
                        sliderState=0;
                        break;
            }
        }



    }
}
