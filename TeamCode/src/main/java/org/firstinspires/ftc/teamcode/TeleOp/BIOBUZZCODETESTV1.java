package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
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

    @Override
    public void init()
    {
        drive.init(hardwareMap);

        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        flywheelMaxPower = 0.5f;
    }

    @Override
    public void loop() {

        setDrive();

        setShooter();
    }

    public void setDrive() {

        //Get controller Inputs
        forward = gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;

        drive.drive(forward,-strafe,rotate);

    }

    public void setShooter() {

        if (gamepad1.right_bumper) {

            flywheel.setPower(flywheelMaxPower);
        }
        else {
            
            flywheel.setPower(0);
        }


    }


}
