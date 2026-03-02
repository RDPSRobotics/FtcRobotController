package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

enum ShootState{START, SHOOT, INTAKE, END}
@TeleOp
public class Shooting extends OpMode {
    //--------------Shooter Variables-------------
    public DcMotorEx rFlywheel;
    public DcMotorEx lFlywheel;
    public Servo kicker;
    PIDFCoefficients rpidfCoefficients = new PIDFCoefficients(88,0,0,13.534);
    PIDFCoefficients lpidfCoefficients = new PIDFCoefficients(97.6,0,0,15.3);
    double highVelocity = 856;
    double lowVelocity = 0;

    ShootState state = ShootState.START;
    //--------------Intake Variables--------------
    public DcMotorEx intake;

    public void init() {

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

        intake.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void loop() {

        switch (state) {
            case START:
                if (gamepad1.right_trigger > 0.3) {
                    rFlywheel.setVelocity(highVelocity);
                    lFlywheel.setVelocity(highVelocity);

                    //Auto Align
                }

                if (rFlywheel.getVelocity() + lFlywheel.getVelocity() >= (highVelocity * 2) - 20) {
                    resetRuntime();
                    state = ShootState.SHOOT;
                }
                break;
            case SHOOT:
                kicker.setPosition(0);

                if (getRuntime() > 0.25) {
                    kicker.setPosition(1);
                }

                if (getRuntime() > 0.5) {

                    resetRuntime();

                    state = ShootState.INTAKE;
                }
                break;
            case INTAKE:
                intake.setPower(0.5);

                if (getRuntime()  > 2) {
                    resetRuntime();

                    state = ShootState.END;
                }
                break;
            case END:
                rFlywheel.setVelocity(lowVelocity);
                lFlywheel.setVelocity(lowVelocity);

                intake.setPower(0);

                //Turn off auto alignment

                if (getRuntime() > 1) {

                    state = ShootState.START;
                    telemetry.addLine("Start");
                }
                break;
        }
    }
}
