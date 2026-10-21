package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Mechanism.Webcam;

@TeleOp
public class AutoAlignment extends OpMode {

    Webcam webcam = new Webcam();

    @Override
    public void init() {

        webcam.init(hardwareMap, telemetry);

    }

    @Override
    public void loop() {

    }

}
