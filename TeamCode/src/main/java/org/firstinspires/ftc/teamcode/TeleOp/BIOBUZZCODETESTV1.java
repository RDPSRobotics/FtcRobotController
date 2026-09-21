package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Mechanism.MecanumDrive;

@TeleOp
public class BIOBUZZCODETESTV1 extends OpMode {

    //---------------Driving Variables------------
    MecanumDrive drive = new MecanumDrive();
    public double forward,strafe,rotate;

    @Override
    public void init()
    {
        drive.init(hardwareMap);
    }

    @Override
    public void loop() {

        //Get controller Inputs
        forward = gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;

    }


}
