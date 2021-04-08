package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.UI.GameAreaDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityFactory;
import com.deco2800.game.math.RandomUtils;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);

  private static final String[] forestTextures = {
    "images/box_boy_leaf.png",
    "images/tree.png",
    "images/grass_1.png",
    "images/grass_2.png",
    "images/grass_3.png",
    "images/terrain_hex.png"
  };
  private static final String[] forestTextureAtlases = {
    "images/terrain_iso_grass.atlas"
  };
  private static final String[] forestSounds = {
    "sounds/Impact4.ogg"
  };
  private static final String[] forestMusic = {
    "sounds/BGM_03_mp3.mp3"
  };
  private static final int NUM_TREES = 5;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);

  private final TerrainFactory terrainFactory;

  public ForestGameArea(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
  }

  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    //Load assets
    loadAssets();

    // UI display
    Entity UI = new Entity();
    UI.addComponent(new GameAreaDisplay("Box Forest"));
    spawnEntity(UI);

    // Make terrain
    terrain = terrainFactory.createTerrain(TerrainType.FOREST_DEMO);
    spawnEntity(new Entity().addComponent(terrain));
    TiledMapTileLayer terrainLayer = (TiledMapTileLayer) terrain.getMap().getLayers().get(0);

    // Spawn Trees
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = new GridPoint2(terrainLayer.getWidth() - 2, terrainLayer.getHeight() - 2);
    for (int i = 0; i < NUM_TREES; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity tree = EntityFactory.createTree();
      spawnEntityAt(tree, randomPos, true, false);
    }

    // Spawn entities
    Entity player = EntityFactory.createPlayer();
    spawnEntityAt(player, PLAYER_SPAWN, true, true);

    // Play music
    Music music = ServiceLocator.getResourceService().getAsset("sounds/BGM_03_mp3.mp3", Music.class);
    music.setLooping(true);
    music.setVolume(0.3f);
    music.play();
  }

  private void loadAssets() {
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(forestTextures);
    resourceService.loadTextureAtlases(forestTextureAtlases);
    resourceService.loadSounds(forestSounds);
    resourceService.loadMusic(forestMusic);

    while (!resourceService.loadForMillis(10)) {
      logger.info("Loading... {}%", resourceService.getProgress());
    }
  }

  @Override
  public void dispose() {
    super.dispose();
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.getAsset("sounds/BGM_03_mp3.mp3", Music.class).stop();
    resourceService.clearAllAssets();
  }
}
