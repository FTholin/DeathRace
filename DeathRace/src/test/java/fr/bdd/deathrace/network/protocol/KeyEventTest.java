package fr.bdd.deathrace.network.protocol;

import javafx.scene.input.KeyCode;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class KeyEventTest {
    private KeyEvent keyEventPressedUp;
    private KeyEvent keyEventRealeasedDown;

    @Before
    public void setup() {
        keyEventPressedUp = new KeyEvent(true, KeyCode.UP);
        keyEventRealeasedDown = new KeyEvent(false, KeyCode.DOWN);
    }

    @Test
    public void isPressed() {
        assertTrue(keyEventPressedUp.isPressed());
        assertFalse(keyEventRealeasedDown.isPressed());
    }


    @Test
    public void setPressed() {
        KeyEvent keyEventPressed = new KeyEvent(true, KeyCode.UP);
        assertTrue(keyEventPressed.isPressed());

        keyEventPressed.setPressed(false);
        assertFalse(keyEventPressed.isPressed());
    }

    @Test
    public void getKeyCode() {
        assertEquals(KeyCode.UP, keyEventPressedUp.getKeyCode());
        assertEquals(KeyCode.DOWN, keyEventRealeasedDown.getKeyCode());
    }

    @Test
    public void setKeyCode() {
        KeyEvent keyEventPressed = new KeyEvent(true, KeyCode.UP);
        assertEquals(KeyCode.UP, keyEventPressed.getKeyCode());

        keyEventPressed.setKeyCode(KeyCode.RIGHT);
        assertEquals(KeyCode.RIGHT, keyEventPressed.getKeyCode());
    }
}