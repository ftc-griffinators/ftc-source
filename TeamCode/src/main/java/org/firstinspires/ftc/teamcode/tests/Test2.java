package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.parts.Hanger;
import org.firstinspires.ftc.teamcode.parts.Slider;

@TeleOp(name = "Test2",group = "Robot")
public class Test2 extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Slider slider=new Slider(hardwareMap);
        Hanger hanger=new Hanger(hardwareMap);
        hanger.initHanger();
        slider.initSlider();

        telemetry.addData("Status","init");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()){
            if (gamepad1.a){
                hanger.hang();
            }
            if (gamepad1.b){
                hanger.resetHanger();
            }

        }



    }
}