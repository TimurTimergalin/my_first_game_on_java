package java_code.entity.player.controls;

import java_code.entity.player.controls.events.DashEvent;
import java_code.entity.player.controls.events.StartSprintEvent;
import java_code.entity.player.controls.events.StopSprintEvent;
import java_code.main_loop.events.FrameEvent;
import java_code.window.input.events.KeyDownEvent;
import java_code.window.input.events.KeyUpEvent;

public class SpecialMovesControls extends AbstractControls{
    private boolean callDash = false;
    private boolean dash = false;
    private boolean sprint = false;
    private boolean callSprint = false;

    @Override
    protected void onKeyPressed(KeyDownEvent e) {
        if (controlsConfig.isDash(e.keyId) && !dash) {
            callDash = true;
            dash = true;
        } else if (controlsConfig.isSprint(e.keyId)) {
            sprint = true;
            callSprint = true;
        }
    }

    @Override
    protected void onKeyReleased(KeyUpEvent e) {
        if (controlsConfig.isSprint(e.keyId)) {
            callSprint = true;
            sprint = false;
        } else if (controlsConfig.isDash(e.keyId)) {
            dash = false;
        }
    }

    @Override
    protected void onFrame(FrameEvent e) {
        if (callDash) {
            eventMediator.callEvent(new DashEvent());
        }
        if (callSprint) {
            if (sprint) {
                eventMediator.callEvent(new StartSprintEvent());
            } else {
                eventMediator.callEvent(new StopSprintEvent());}
        }

        callDash = false;
        callSprint = false;
    }
}
