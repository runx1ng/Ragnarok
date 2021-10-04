package com.deco2800.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.deco2800.game.rendering.AnimationRenderComponent5;
import com.deco2800.game.rendering.AnimationRenderComponent6;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
  private static final Vector2 MAX_SPEED = new Vector2(3f, 3f); // Metres per second
  private static final Logger logger = LoggerFactory.getLogger(PlayerActions.class);
  private PhysicsComponent physicsComponent;
  private Vector2 walkDirection = Vector2.Zero.cpy();
  private boolean moving = false;
  private  CombatStatsComponent combatStatsComponent;
  final String a = (String) "attack";
  AnimationRenderComponent animator;
  AnimationRenderComponent5 animator2;

  @Override
  public void create() {
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    animator2 = this.entity.getComponent(AnimationRenderComponent5.class);
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    entity.getEvents().addListener("walk", this::walk);
    entity.getEvents().addListener("walkStop", this::stopWalking);
    entity.getEvents().addListener(a, this::attack);
    entity.getEvents().addListener("unAttack", this::unAttack);
    entity.getEvents().addListener("run", this::attack);
    entity.getEvents().addListener("run", this::run);
    entity.getEvents().addListener("coin", this::attack);
  }

  @Override
  public void update() {
    if(animator2.getCurrentAnimation() == null) {
       animator2.startAnimation("run");
    }
    if (moving) {
      updateSpeed();
    }
  }

  private void updateSpeed() {
    Body body = physicsComponent.getBody();
    Vector2 velocity = body.getLinearVelocity();
    Vector2 desiredVelocity = walkDirection.cpy().scl(MAX_SPEED);
    // impulse = (desiredVel - currentVel) * mass
    Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  /**
   * Moves the player towards a given direction.
   *
   * @param direction direction to move in
   */
  void walk(Vector2 direction) {
    this.walkDirection = direction;
    moving = true;

  }

  /**
   * Stops the player from walking.
   */
  void stopWalking() {
    this.walkDirection = Vector2.Zero.cpy();
    updateSpeed();
    moving = false;
  }

  /**
   * Makes the player attack.
   */
  void attack() {
    logger.info(a);
    Array<Entity> entities = ServiceLocator.getEntityService().getEntities();
    Entity nearest = findNearestTargets(entities);
    logger.info("attack nearest--{}", nearest);
    if (nearest != null) {
//      else if (nearest.getType().equals(Entity.Type.BREAD) || nearest.getType().equals(Entity.Type.AID)) {
//        logger.info ("nearest.getType()--{}", nearest.getType());
//        nearest.dispose();
//        Sound attSound = ServiceLocator.getResourceService().getAsset("sounds/buff.ogg", Sound.class);
//        attSound.play();
//        animator.startAnimation("buff");
//      }
      if (nearest.getType().equals(Entity.Type.GHOST) || nearest.getType().equals(Entity.Type.GHOSTKING)) {
        logger.info ("nearest.getType()--{}", nearest.getType());
        nearest.dispose();
        Sound attSound = ServiceLocator.getResourceService().getAsset("sounds/buff2.ogg", Sound.class);
        attSound.play();
        animator.startAnimation("buff2");

        entity.getEvents().trigger("updateGold");
      }
    }
    Sound attackSound = ServiceLocator.getResourceService().getAsset("sounds/attack.ogg", Sound.class);
    attackSound.play();
    animator.startAnimation(a);
    animator2.stopAnimation();
    animator.startAnimation("attack");


  }

  void unAttack(){
    animator.stopAnimation();
    animator2.startAnimation("run");
  }
  void run(){animator2.startAnimation("run"); }

  private Entity findNearestTargets(Array<Entity> entities) {
    Entity result = null;
    float minDstEnemy = 1.8f;
    float minDstObstacle = 1.8f;
    for (Entity en: entities) {
      if (en.getType() == Entity.Type.GHOST || en.getType() == Entity.Type.GHOSTKING) {
        float dst = entity.getPosition().dst(en.getPosition());
        if (minDstEnemy > dst) {
          result = en;
        }
      } else if (en.getType() == Entity.Type.OBSTACLE ) {
        float dst = entity.getPosition().dst(en.getPosition());
        if (minDstObstacle > dst) {
          result = en;
        }
      }
    }
    return result;
  }

//  void touch() {
//    Array<Entity> entities = ServiceLocator.getEntityService().getEntities();
//    Entity nearest = findNearestTargets(entities);
//    if (nearest != null) {
//      if (nearest.getType().equals(Entity.Type.BREAD) || nearest.getType().equals(Entity.Type.AID)) {
//        entities.removeValue(nearest, true);
//        Sound attackSound1 = ServiceLocator.getResourceService().getAsset("sounds/buff.ogg", Sound.class);
//        attackSound1.play();
//        animator.startAnimation("attack");
//      }
//    }
//  }
}
