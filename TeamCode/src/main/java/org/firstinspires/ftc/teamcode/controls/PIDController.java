package org.firstinspires.ftc.teamcode.controls;

public class PIDController
{
    private final double kp;
    private final double ki;
    private final double kd;
    private final double deadzone;
    private final double maxIntegral;

    private double lastError = 0;
    private double integral = 0;
    private double lastTime;

    public PIDController(double kp, double ki, double kd, double deadzone, double maxIntegral)
    {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.deadzone = deadzone;
        this.maxIntegral = maxIntegral;
        this.lastTime = System.nanoTime() / 1e9;  // Convert to seconds
    }

    public double calculate(double target, double current)
    {
        double currentTime = System.nanoTime() / 1e9;
        double dt = currentTime - lastTime;
        lastTime = currentTime;

        if (dt <= 0)
        { return 0; }

        double error = target - current;

        if (Math.abs(error) < deadzone)
        {
            integral = 0;
            return 0;
        }

        double proportional = kp * error;
        integral += error * dt;
        integral = Math.min(Math.max(integral, -maxIntegral), maxIntegral);
        double integralTerm = ki * integral;

        double derivative = (error - lastError) / dt;
        double derivativeTerm = kd * derivative;

        lastError = error;

        return proportional + integralTerm + derivativeTerm;
    }

    public void reset()
    {
        integral = 0;
        lastError = 0;
    }
}