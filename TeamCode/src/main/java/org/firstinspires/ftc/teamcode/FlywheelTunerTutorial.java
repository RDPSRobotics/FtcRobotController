package org.firstinspires.ftc.teamcode;

import  com.qualcomm.robotcore.eventloop.opmode.OpMode;
import  com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "FlywheelTest")
public class FlywheelTunerTutorial extends OpMode {

    public DcMotorEx rFlywheel;
    public DcMotorEx lFlywheel;
    public Servo kicker;
    public double highVelocity = 856;
    public double lowVelocity = 0;
    double curVelocity = lowVelocity;
    double rightF = 13.534;
    double rightP = 88;

    double leftF = 15.3;
    double leftP = 97.6;

    @Override
    public void init() {
        rFlywheel = hardwareMap.get(DcMotorEx.class, "rFlywheel");
        lFlywheel = hardwareMap.get(DcMotorEx.class, "lFlywheel");

        kicker = hardwareMap.get(Servo.class, "kicker");

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

               kicker.setPosition(0.05);
           }
       }

       if (gamepad1.aWasPressed()) {
           if (curVelocity == highVelocity) {
               rFlywheel.setVelocity(lowVelocity);
               lFlywheel.setVelocity(lowVelocity);
               kicker.setPosition(0.0);
           }
       }

    }
}
