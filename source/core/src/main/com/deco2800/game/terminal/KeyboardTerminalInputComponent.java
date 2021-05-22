package com.deco2800.game.terminal;

import com.badlogic.gdx.Input;
import com.deco2800.game.input.InputComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Input handler for the debug terminal for keyboard and touch (mouse) input.
 *
 * <p>The debug terminal can be opened and closed by pressing 'F1' and a message can be entered via
 * the keyboard.
 */
public class KeyboardTerminalInputComponent extends InputComponent {
  private static final Logger logger =
      LoggerFactory.getLogger(KeyboardTerminalInputComponent.class);
  private static final int toggleOpenKey = Input.Keys.F1;
  private Terminal terminal;

  public KeyboardTerminalInputComponent() {
    this(new Terminal());
  }

  public KeyboardTerminalInputComponent(Terminal terminal) {
    super(10);
    this.terminal = terminal;
  }


  @Override
  public void create() {
    super.create();
    terminal = entity.getComponent(Terminal.class);
  }

  /**
   * If the toggle key is pressed, the terminal will open / close.
   *
   * <p>Otherwise, handles input if the terminal is open. This is because keyDown events are
   * triggered alongside keyTyped events. If the user is typing in the terminal, the input shouldn't
   * trigger any other input handlers.
   *
   * @param keycode one of the constants in {@link Input.Keys}
   * @return whether the input was processed
   */
  @Override
  public boolean keyDown(int keycode) {
    // handle open and close terminal
    if (keycode == toggleOpenKey) {
      terminal.toggleIsOpen();
      return true;
    }

    return terminal.isOpen();
  }

  /**
   * Handles input if the terminal is open. If 'enter' is typed, the entered message will be
   * processed, otherwise the message will be updated with the new character.
   *
   * @param character The character
   * @return whether the input was processed
   */
  @Override
  public boolean keyTyped(char character) {
    if (!terminal.isOpen()) {
      return false;
    }

    if (character == '\r' || character == '\n') {
      if (terminal.processMessage()) {
        terminal.toggleIsOpen();
      }
      terminal.setEnteredMessage("");
      return true;
    } else if (character == '\b') {
      terminal.handleBackspace();
      return true;
    } else if(Character.isLetterOrDigit(character) || character == ' ') {
      // append character to message
      terminal.appendToMessage(character);
      return true;
    }
    return false;
  }

  /**
   * Handles input if the terminal is open. This is because keyUp events are triggered alongside
   * keyTyped events. If the user is typing in the terminal, the input shouldn't trigger any other
   * input handlers.
   *
   * @param keycode one of the constants in {@link Input.Keys}
   * @return whether the input was processed
   */
  @Override
  public boolean keyUp(int keycode) {
    return terminal.isOpen();
  }
}