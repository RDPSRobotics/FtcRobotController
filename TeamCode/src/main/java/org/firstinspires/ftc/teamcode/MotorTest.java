package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotserver.internal.webserver.RobotControllerWebHandlers;

@TeleOp
public class MotorTest extends OpMode {

    DcMotor lFlywheel;
DcMotor rFlywheel;

    public void init(){
   lFlywheel = hardwareMap.get(DcMotor.class,"lFlywheel") ;
        lFlywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rFlywheel = hardwareMap.get(DcMotor.class,"rFlywheel") ;
        rFlywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lFlywheel.setDirection(DcMotorSimple.Direction.REVERSE);
    }


    public void loop(){

        if (gamepad1.a ==true){
            lFlywheel.setPower(0.5);
            rFlywheel.setPower(0.5);
        }
        else {
            lFlywheel.setPower(0);
            rFlywheel.setPower(0);
        }
    }
}
