package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Mechanism.MecanumDrive;

@TeleOp
public class BIOBUZZCODETESTV1 extends OpMode {

    //---------------Driving Variables------------
    MecanumDrive drive = new MecanumDrive();
    public double forward,strafe,rotate;

    //--------------Shooter Variables-------------

    private DcMotorEx flywheel;

    private double flywheelMaxPower;

    private final double triggerDeadzone = 0.2;

    //--------------Shooter Variables-------------

    private CRServo rCornerIntake;
    private CRServo lCornerIntake;

    private final double cornerIntakePower = 0.5;

    private DcMotor intake;

    private final double intakePower = 0.5;

    @Override
    public void init()
    {
        drive.init(hardwareMap);

        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        flywheelMaxPower = 0.5f;

        rCornerIntake = hardwareMap.get(CRServo.class, "rCornerIntake");
        lCornerIntake = hardwareMap.get(CRServo.class, "lCornerIntake");

        intake = hardwareMap.get(DcMotor.class, "intake");
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

            flywheel.setPower(flywheelMaxPower);
        }
        else {

            flywheel.setPower(0);
        }
    }

    private void setIntake() {

        if (gamepad1.right_bumper) {

            rCornerIntake.setPower(cornerIntakePower);
            lCornerIntake.setPower(cornerIntakePower);

            intake.setPower(intakePower);

        }

    }


}
