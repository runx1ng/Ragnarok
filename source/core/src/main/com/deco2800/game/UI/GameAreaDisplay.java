package com.deco2800.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class GameAreaDisplay extends UIComponent {
  private static final float zIndex = 4f;
  private String gameAreaName = "";
  private Label title;

  public GameAreaDisplay(String gameAreaName) {
    this.gameAreaName = gameAreaName;
  }

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    title = new Label(this.gameAreaName, skin);
    title.setFontScale(2f);
    stage.addActor(title);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    int screenHeight = Gdx.graphics.getHeight();
    float offsetX = 10f;
    float offsetY = 30f;

    title.setPosition(offsetX, screenHeight - offsetY);
  }

  @Override
  public float getZIndex() {
    return zIndex;
  }
}
