package java_code.window;

import java_code.entity.player.controls.MovementControls;
import java_code.entity.player.controls.SpecialMovesControls;
import java_code.entity.player.controls.events.*;
import java_code.event.EventMediator;
import java_code.event.SubscribeEvent;
import java_code.window.input.KeyHandler;
import java_code.window.input.MouseHandler;

import javax.swing.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        super();
        setSize(200, 200);
        addKeyListener(new KeyHandler());
        addMouseListener(new MouseHandler());
        MovementControls mc = new MovementControls();
        SpecialMovesControls smc = new SpecialMovesControls();
        EventMediator.get().registerObject(this);
    }

    @SubscribeEvent
    private void onMove(GoEvent e) {
        System.out.println(e.dr);
    }

    @SubscribeEvent
    private void onStop(StopEvent e) {
        System.out.println("Stop");
    }

    @SubscribeEvent
    private void onDash(DashEvent e) {
        System.out.println("Dash");
    }

    @SubscribeEvent
    private void onSprint(StartSprintEvent e) {
        System.out.println("Start sprint");
    }

    @SubscribeEvent
    private void onStopSprint(StopSprintEvent e) {
        System.out.println("Stop sprint");
    }

}
