package org.firstinspires.ftc.teamcode.Utility;


import com.qualcomm.robotcore.util.ElapsedTime;

public class Timing
{
    public ElapsedTime delayTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    public double delay;
    boolean allowTimerReset = true;


    private void resetOnce()
    {
        if (allowTimerReset)
        {
            delayTimer.reset();
        }
    }


    public boolean delay(int delay)
    {
        resetOnce();
        allowTimerReset = false;
        if (delayTimer.time() > delay)
        {
            allowTimerReset = true;
            return true;
        }
        return false;
    }


}
