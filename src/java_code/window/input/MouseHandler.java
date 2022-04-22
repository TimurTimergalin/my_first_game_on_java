package java_code.window.input;

import java_code.event.EventMediator;
import java_code.window.input.events.ButtonDownEvent;
import java_code.window.input.events.ButtonUpEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
        EventMediator.get().callEvent(new ButtonDownEvent(e.getButton() + 1000));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        EventMediator.get().callEvent(new ButtonUpEvent(e.getButton() + 1000));
    }
}
