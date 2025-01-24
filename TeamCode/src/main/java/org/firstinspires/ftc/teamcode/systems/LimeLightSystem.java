package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;


import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.parts.Claw;

import java.util.List;

public class LimeLightSystem {

    Limelight3A limeLight;
    public int currentPipelineIndex;
    public LLResult result;
    public double INCHES_PER_PIXEL;

    public LimeLightSystem(HardwareMap hardwareMap,int pipelineIndex){
        limeLight= hardwareMap.get(Limelight3A.class,"limeLight");
        limeLight.setPollRateHz(60);
        this.currentPipelineIndex=pipelineIndex;
        limeLight.pipelineSwitch(pipelineIndex);
        limeLight.start();
    }


    public void switchPipeLine(int newPipelineIndex){
        this.currentPipelineIndex=newPipelineIndex;
        limeLight.pipelineSwitch(newPipelineIndex);
    }
    public boolean hasValidTarget()
    {
        LLResult res = limeLight.getLatestResult();
        return res != null && res.isValid();
    }

    public void updateResult(){
         result= limeLight.getLatestResult();
    }
    public List<LLResultTypes.ColorResult> getListColorResults(){
        if (hasValidTarget()){
            result=limeLight.getLatestResult();
            return result.getColorResults();
        }
        return  null;
    }
    public void angleAlignment(){
        LLResultTypes.ColorResult finalColor=getListColorResults().get(0);
        List<List<Double>> corners=finalColor.getTargetCorners();
        double deltaX=corners.get(0).get(0)-corners.get(3).get(0);
        double deltaY=corners.get(0).get(1)-corners.get(3).get(1);
        double angle=Math.atan2(deltaY,deltaX);
    }


    public void pauseLimeLight(){
        limeLight.pause();
           }


    }


