package com.deco2800.game.components.gamearea;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
//import com.deco2800.game.components.maingame.MainGameActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.deco2800.game.GdxGame.ScreenType.MAIN_GAME;
import static com.deco2800.game.GdxGame.ScreenType.MAIN_MENU;

public class GameWinActions  extends Component {
    private static final Logger logger = LoggerFactory.getLogger(GameWinActions.class);
    private final GdxGame game;

    public GameWinActions(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("replay", this::onReplay);
        entity.getEvents().addListener("exit", this::onExit);
    }

    /**
     * Swaps to the Main Menu screen.
     */
    private void onExit() {
        logger.info("Exiting main game screen");
        //game.setScreen(GdxGame.ScreenType.MAIN_MENU);
        game.setScreen(MAIN_MENU);
    }


    /**
     * Swaps to the Main Game Screen when the character dies.
     */
    private void onReplay() {
        logger.info("Restart Game");
        //game.setScreen(GdxGame.ScreenType.MAIN_GAME);
        game.setScreen(MAIN_GAME);
    }
}