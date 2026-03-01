package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Mechanism.MecanumDrive;
import org.firstinspires.ftc.teamcode.Mechanism.Webcam;

@TeleOp
public class RDPS extends OpMode {

    //---------------Driving Variables------------
    MecanumDrive drive = new MecanumDrive();
    public double forward,strafe,rotate;

    //----------------Webcam/Auto Alignment Variables---------------
    Webcam webcam = new Webcam();

    //--------------Shooter Variables-------------
    public DcMotorEx rFlywheel;
    public DcMotorEx lFlywheel;
    public Servo kicker;
    PIDFCoefficients rpidfCoefficients = new PIDFCoefficients(88,0,0,13.534);
    PIDFCoefficients lpidfCoefficients = new PIDFCoefficients(97.6,0,0,15.3);
    double highVelocity = 856;
    double lowVelocity = 0;
    double curVelocity;

    //--------------Intake Variables--------------
    public DcMotorEx intake;

    public void init() {

        //Drive Init
        drive.init(hardwareMap);

        //Webcam Init
        webcam.init(hardwareMap,telemetry);

        //Shooting Init
        rFlywheel = hardwareMap.get(DcMotorEx.class, "rFlywheel");
        lFlywheel = hardwareMap.get(DcMotorEx.class, "lFlywheel");

        kicker = hardwareMap.get(Servo.class, "kicker");

        rFlywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lFlywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        lFlywheel.setDirection(DcMotorSimple.Direction.REVERSE);

        rFlywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, rpidfCoefficients);
        lFlywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, lpidfCoefficients);
        telemetry.addLine("Init Complete");

        //Intake Init
        intake = hardwareMap.get(DcMotorEx.class, "intake");
    }

    public void loop() {
        //Controller Inputs
        forward = -gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x * 1.1;
        rotate = gamepad1.right_stick_x;

        drive.drive(forward,strafe,rotate);

        if (gamepad1.xWasPressed()) {

            if (curVelocity == lowVelocity) {
                rFlywheel.setVelocity(highVelocity);
                lFlywheel.setVelocity(highVelocity);

                kicker.setPosition(1);

                curVelocity = highVelocity;
            }
            else if (curVelocity == highVelocity) {
                rFlywheel.setVelocity(lowVelocity);
                lFlywheel.setVelocity(lowVelocity);

                kicker.setPosition(0);

                curVelocity = lowVelocity;
            }
        }

        if (gamepad1.left_trigger > 0.3) {
            intake.setPower(1);
        }
        else {
            intake.setPower(0);
        }
    }
}
