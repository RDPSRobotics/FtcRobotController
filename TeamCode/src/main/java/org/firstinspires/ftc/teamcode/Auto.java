package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Mechanism.Webcam;

@Autonomous
public class Auto extends OpMode {
    //---------------Driving Variables------------
    DcMotor rFront,rBack,lFront,lBack;

    //----------------Webcam/Auto Alignment Variables---------------
    Webcam webcam = new Webcam();
    double kP = 0.019;
    double error = 0;
    double lastError = 0;
    double goalX = 0;
    double angleTolerance = 0.4;
    double kD = 0;
    double curTime = 0;
    double lastTime = 0;

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
        rFront = hardwareMap.get(DcMotor.class, "rFront");
        rBack = hardwareMap.get(DcMotor.class, "rBack");
        lFront = hardwareMap.get(DcMotor.class, "lFront");
        lBack = hardwareMap.get(DcMotor.class, "lBack");

        lFront.setDirection(DcMotorSimple.Direction.REVERSE);
        lBack.setDirection(DcMotorSimple.Direction.REVERSE);

        rFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

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

        intake.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void loop() {
        //Close range
        //Move backward then shoot plus auto alignment
        //then move out of the white line to get points for leave
    }

}
