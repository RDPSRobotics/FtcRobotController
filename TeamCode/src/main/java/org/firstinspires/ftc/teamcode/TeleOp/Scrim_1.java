package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Mechanism.MecanumDrive;

@TeleOp
public class Scrim_1 extends OpMode {


    //---------------Driving Variables------------
    MecanumDrive drive = new MecanumDrive();
    public double forward,strafe,rotate;

    //--------------Shooter Variables-------------

    private DcMotorEx flywheel;

    public final double flywheelMaxPower = 1300;

    public final double triggerDeadzone = 0.2;

    PIDFCoefficients fpidfCoefficients = new PIDFCoefficients(3,0,0,22.5);

    //--------------Intake Variables-------------

    private DcMotorEx intake;

    public final double intakePower = 0.5;

    @Override
    public void init()
    {
        drive.init(hardwareMap);

        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");

        flywheel.setDirection(DcMotorSimple.Direction.REVERSE);

        flywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, fpidfCoefficients);

        intake = hardwareMap.get(DcMotorEx.class, "intake");

        intake.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {

        setDrive();

        setShooter();

        setIntake();
    }

    private void setDrive() {

        //Get controller Inputs
        forward = gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;

        drive.drive(forward,-strafe,rotate);

    }

    private void setShooter() {

        if (gamepad1.right_trigger > triggerDeadzone) {

            flywheel.setVelocity(flywheelMaxPower);
        }
        else {

            flywheel.setPower(0);
        }
    }

    private void setIntake() {

        if (gamepad1.right_bumper) {

            intake.setPower(intakePower);

        }
        else if (gamepad1.left_bumper) {
            intake.setPower(-intakePower);
        }
        else {
            intake.setPower(0);
        }

    }


}
