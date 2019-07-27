package fr.bdd.deathrace.network.protocol;

import javafx.scene.input.KeyCode;

import java.io.Serializable;

public class KeyEvent implements Serializable {
    static final long serialVersionUID = 42L;

    private boolean pressed;
    private KeyCode keyCode;

    public KeyEvent(boolean pressed, KeyCode keyCode) {
        this.pressed = pressed;
        this.keyCode = keyCode;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public KeyCode getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(KeyCode keyCode) {
        this.keyCode = keyCode;
    }
}
