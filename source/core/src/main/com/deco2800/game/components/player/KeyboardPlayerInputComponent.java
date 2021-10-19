package com.deco2800.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.RainbowBridge;
import com.deco2800.game.components.bridge.Bridge;
import com.deco2800.game.components.bridge.Lane;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.screens.RagnorakRacer;
import com.deco2800.game.utils.math.Vector2Utils;

import java.util.List;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
  private final Vector2 walkDirection = Vector2.Zero.cpy();
  public static int i = 0;
  public static float j = 0.5f;

   KeyboardPlayerInputComponent() {
    super(5);
  }

  /**
   * Triggers player events on specific keycodes.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyDown(int)
   */
  @Override
   boolean keyDown(int keycode) {
    Bridge bridge = RagnorakRacer.rainbowBridge;
    List<Lane> lanes = bridge.getLanes();
    float newY = 0;
    if (keycode == Keys.W){
      if(entity.getPosition().y <= lanes.get(3).getMid() - 9){
        System.out.println(lanes.get(3).getMid() - 7.5);
        entity.setPosition(entity.getPosition().x, lanes.get(i).getMid() - (1.7f * (i + 1) + j));
        newY = lanes.get(i).getMid() - (1.6f * (i + 1));
        i += 1;
        if (j <= 0.14f){
          j += 0.03f;
        }
        System.out.println("current lane number:" + i);
        System.out.println(newY);
      }
    } else if (keycode == Keys.S){
      if (entity.getPosition().y >= lanes.get(0).getMid() - 4){
          System.out.println(lanes.get(0).getMid());
          if (i > 0){
            entity.setPosition(entity.getPosition().x, lanes.get(i - 1).getMid() - (1.7f * (i + 1)));
            i -= 1;
            if (j >= 0.3f){
              j -= 0.03f;
            }
          }

          System.out.println("current lane number:" + i);
      }
    }else if (keycode == Keys.E){

      entity.setPosition(entity.getPosition().x, lanes.get(0).getMid() - (1.6f * (1 + 1)));
      i = 0;
    }else if (keycode == Keys.Q){
      entity.setPosition(entity.getPosition().x, lanes.get(3).getMid() - (1.6f * (4 + 1)));
      i = 3;
    }else if (keycode == Keys.SPACE){
      entity.getEvents().trigger("attack");
    }
    return true;
  }

  /**
   * Triggers player events on specific keycodes.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyUp(int)
   */
  @Override
   boolean keyUp(int keycode) {
    switch (keycode) {
      case Keys.W:
        walkDirection.sub(Vector2Utils.UP);
        return true;
      case Keys.S:
        walkDirection.sub(Vector2Utils.DOWN);
        return true;
      case Keys.SPACE:
        entity.getEvents().trigger("unAttack");
        return true;
      default:
        return false;
    }
  }

  private void triggerWalkEvent() {
    entity.getEvents().trigger("run");
    if (walkDirection.epsilonEquals(Vector2.Zero)) {
      entity.getEvents().trigger("run");
    } else {
      entity.getEvents().trigger("walk", walkDirection);
    }
  }
}
