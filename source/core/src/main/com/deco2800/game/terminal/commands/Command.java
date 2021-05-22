package com.deco2800.game.terminal.commands;

import java.util.ArrayList;

/**
 * A generic command class.
 */
public interface Command {
  /**
   * Action command.
   * @param args command args
   * @return command was successful
   */
  boolean action(ArrayList<String> args);
}