package org.firstinspires.ftc.teamcode.Mechanism;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class MecanumDrive {

    //Drivetrain Motors
    private DcMotor rFront,rBack,lFront,lBack;
    private IMU imu;

    public void init(HardwareMap hwMap) {
        rFront = hwMap.get(DcMotor.class, "rFront");
        rBack = hwMap.get(DcMotor.class, "rBack");
        lFront = hwMap.get(DcMotor.class, "lFront");
        lBack = hwMap.get(DcMotor.class, "lBack");

        lFront.setDirection(DcMotorSimple.Direction.REVERSE);
        lBack.setDirection(DcMotorSimple.Direction.REVERSE);

        rFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        imu = hwMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot revOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
        );

        imu.initialize(new IMU.Parameters(revOrientation));
    }

    public void drive(double forward, double strafe, double rotate) {
        double rFrontPower = forward - strafe - rotate;
        double rBackPower = forward + strafe - rotate;
        double lFrontPower = forward + strafe + rotate;
        double lBackPower = forward - strafe + rotate;

        double maxPower = 1.0;

        maxPower = Math.max(maxPower, Math.abs(rFrontPower));
        maxPower = Math.max(maxPower, Math.abs(rBackPower));
        maxPower = Math.max(maxPower, Math.abs(lFrontPower));
        maxPower = Math.max(maxPower, Math.abs(lBackPower));

        rFront.setPower(rFrontPower/maxPower);
        rBack.setPower(rBackPower/maxPower);
        lFront.setPower(lFrontPower/maxPower);
        lBack.setPower(lBackPower/maxPower);
    }

    public void driveFieldRelative(double forward, double strafe, double rotate) {
        double theta =  Math.atan2(forward,strafe);
        double r = Math.hypot(strafe,forward);

        theta = AngleUnit.normalizeRadians(theta -
                imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS)
        );

        double newForward = r * Math.sin(theta);
        double newStrafe = r * Math.cos(theta);

        this.drive(newForward,newStrafe,rotate);
    }



}
