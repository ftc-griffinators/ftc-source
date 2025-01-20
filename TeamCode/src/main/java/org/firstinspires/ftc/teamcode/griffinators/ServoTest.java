package org.firstinspires.ftc.teamcode.griffinators;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@TeleOp(name ="ServoTest",group = "Tests")
public class ServoTest extends LinearOpMode {
    private final double CLAW_EXTENDED=0;
    private final double CLAW_RETRACTED=0.26;
    ServoImplEx  clawExtend;

    @Override
    public void runOpMode() throws InterruptedException {

        clawExtend = hardwareMap.get(ServoImplEx.class,"clawExtend");

        clawExtend.setPwmRange(new PwmControl.PwmRange(500,2500));
        clawExtend.scaleRange(CLAW_EXTENDED,CLAW_RETRACTED);


    }

}