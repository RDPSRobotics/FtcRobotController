package org.firstinspires.ftc.teamcode;

import  com.qualcomm.robotcore.eventloop.opmode.OpMode;
import  com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp(name = "FlywheelTest")
public class FlywheelTunerTutorial extends OpMode {

    public DcMotorEx rFlywheel;
    public DcMotorEx lFlywheel;
    public double highVelocity = 1000;

    public double lowVelocity = 0;
    double curVelocity = lowVelocity;
    double rightF = 0;
    double rightP = 0;

    double leftF = 0;
    double leftP = 0;

    @Override
    public void init() {
        rFlywheel = hardwareMap.get(DcMotorEx.class, "rFlywheel");
        lFlywheel = hardwareMap.get(DcMotorEx.class, "lFlywheel");

        rFlywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lFlywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        lFlywheel.setDirection(DcMotorSimple.Direction.REVERSE);

        PIDFCoefficients rpidfCoefficients = new PIDFCoefficients(rightP,0,0,rightF);
        PIDFCoefficients lpidfCoefficients = new PIDFCoefficients(leftP,0,0,leftF);


        rFlywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, rpidfCoefficients);
        lFlywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, lpidfCoefficients);
        telemetry.addLine("Init Complete");
    }

    @Override
    public void loop() {

       if (gamepad1.xWasPressed()) {

           if (curVelocity == lowVelocity) {
               rFlywheel.setVelocity(highVelocity);
               lFlywheel.setVelocity(highVelocity);
           }
           else if (curVelocity == highVelocity) {
               rFlywheel.setVelocity((lowVelocity));
               lFlywheel.setVelocity(highVelocity);
           }
       }
    }
}
