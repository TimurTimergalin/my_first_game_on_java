package java_code.entity.player.controls;

import java_code.event.EventMediator;
import java_code.event.SubscribeEvent;
import java_code.main_loop.events.FrameEvent;
import java_code.window.input.events.KeyDownEvent;
import java_code.window.input.events.KeyUpEvent;

abstract class AbstractControls {
    protected final ControlsLoader controlsConfig = ControlsLoader.get();
    protected final EventMediator eventMediator = EventMediator.get();

    @SubscribeEvent
    private void _onKeyPressed(KeyDownEvent e) {
        onKeyPressed(e);
    }

    @SubscribeEvent
    private void _onKeyReleased(KeyUpEvent e) {
        onKeyReleased(e);
    }

    @SubscribeEvent
    private void _onFrame(FrameEvent e) {
        onFrame(e);
    }

    protected void onFrame(FrameEvent e) {}
    protected void onKeyPressed(KeyDownEvent e) {}
    protected void onKeyReleased(KeyUpEvent e) {}

    public AbstractControls() {
        eventMediator.registerObject(this);
    }
}
