package java_code.entity.player.controls;

import java_code.entity.Direction;
import java_code.entity.player.controls.events.GoEvent;
import java_code.entity.player.controls.events.StopEvent;
import java_code.main_loop.events.FrameEvent;
import java_code.window.input.events.KeyDownEvent;
import java_code.window.input.events.KeyUpEvent;

import java.util.HashMap;
import java.util.Map;

public class MovementControls extends AbstractControls {

    private final Map<Actions, Boolean> performingActions = new HashMap<>();

    public MovementControls() {
        super();
        for (Actions a : Actions.values()) {
            performingActions.put(a, false);
        }
    }

    private boolean dirChanged = false;

    @Override
    protected void onKeyPressed(KeyDownEvent e) {
        boolean prev;
        if (controlsConfig.isDown(e.keyId)) {
            prev = performingActions.get(Actions.DOWN);
            performingActions.put(Actions.DOWN, true);
            if (!prev)
                dirChanged = true;
        } else if (controlsConfig.isUp(e.keyId)) {
            prev = performingActions.get(Actions.UP);
            performingActions.put(Actions.UP, true);
            if (!prev)
                dirChanged = true;
        } else if (controlsConfig.isLeft(e.keyId)) {
            prev = performingActions.get(Actions.LEFT);
            performingActions.put(Actions.LEFT, true);
            if (!prev)
                dirChanged = true;
        } else if (controlsConfig.isRight(e.keyId)) {
            prev = performingActions.get(Actions.RIGHT);
            performingActions.put(Actions.RIGHT, true);
            if (!prev)
                dirChanged = true;
        }
    }

    @Override
    protected void onKeyReleased(KeyUpEvent e) {
        if (controlsConfig.isDown(e.keyId)) {
            performingActions.put(Actions.DOWN, false);
            dirChanged = true;
        } else if (controlsConfig.isUp(e.keyId)) {
            performingActions.put(Actions.UP, false);
            dirChanged = true;
        } else if (controlsConfig.isLeft(e.keyId)) {
            performingActions.put(Actions.LEFT, false);
            dirChanged = true;
        } else if (controlsConfig.isRight(e.keyId)) {
            performingActions.put(Actions.RIGHT, false);
            dirChanged = true;
        }
    }

    private Direction getDirection() {
        int hor = 0;
        int ver = 0;

        if (performingActions.get(Actions.UP)) ++ver;
        if (performingActions.get(Actions.DOWN)) --ver;
        if (performingActions.get(Actions.LEFT)) --hor;
        if (performingActions.get(Actions.RIGHT)) ++hor;

        if (hor == 0 && ver == 1) return Direction.TOP;
        if (hor == 0 && ver == -1) return Direction.BOTTOM;
        if (hor == 1 && ver == 0) return Direction.RIGHT;
        if (hor == -1 && ver == 0) return Direction.LEFT;
        if (hor == 1 && ver == 1) return Direction.TOP_RIGHT;
        if (hor == -1 && ver == 1) return Direction.TOP_LEFT;
        if (hor == 1 && ver == -1) return Direction.BOTTOM_RIGHT;
        if (hor == -1 && ver == -1) return Direction.BOTTOM_LEFT;
        return null;
    }

    @Override
    protected void onFrame(FrameEvent e) {
        if (dirChanged) {
            Direction newDir = getDirection();
            if (newDir == null) eventMediator.callEvent(new StopEvent());
            else eventMediator.callEvent(new GoEvent(newDir));
        }
        dirChanged = false;
    }
}
