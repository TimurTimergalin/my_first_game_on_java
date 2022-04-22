package java_code.window.input;

import java_code.event.EventMediator;
import java_code.window.input.events.KeyDownEvent;
import java_code.window.input.events.KeyUpEvent;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
        EventMediator.get().callEvent(new KeyDownEvent(e.getKeyCode()));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        EventMediator.get().callEvent(new KeyUpEvent(e.getKeyCode()));
    }
}
