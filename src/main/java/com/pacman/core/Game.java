package com.pacman.core;

import com.pacman.input.IInputService;
import com.pacman.input.InputAdaptor;
import com.pacman.output.IOutputService;
import com.pacman.output.OutputAdaptor;
import com.pacman.utils.BoardFactory;

public class Game {

    private static final int TIME_BETWEEN_UPDATES = 500;

    private GameState currentGameState;
    private IInputService inputService;
    private InputAdaptor inputAdaptor = new InputAdaptor();
    private IOutputService outputService;
    private OutputAdaptor outputAdaptor = new OutputAdaptor();
    private IGameController gameController;
    private IMonsterController monsterController;

    public Game(IInputService inputService, IOutputService outputService,
                IGameController gameController, IMonsterController monsterController) {
        this.currentGameState = new GameState(BoardFactory.initialiseBoard());
        this.inputService = inputService;
        this.outputService = outputService;
        this.gameController = gameController;
        this.monsterController = monsterController;
    }

    public int play() {
        int score = 0;
        Board currentBoard = this.currentGameState.getBoard();

        while(!this.gameController.isGameOver(currentBoard)) {
            try {
                Thread.sleep(TIME_BETWEEN_UPDATES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int characterCode = this.inputService.readUserInput();
            GameAction gameAction = this.inputAdaptor.inputToAction(characterCode);

            if (gameAction == GameAction.EXIT) {
                this.inputService.closeInputService();
                System.out.print("Exiting\r\n");
                System.exit(0);
            }
            else if (gameAction != GameAction.NO_INPUT)
                this.currentGameState.setCurrentAction(gameAction);

            currentBoard = this.gameController.movePacman(this.currentGameState);
            currentBoard = this.gameController.moveMonsters(currentBoard, this.monsterController);

            String boardAsString = this.outputAdaptor.gameStateToString(this.currentGameState);
            score = this.gameController.calculateScore(currentBoard);
            this.outputService.displayBoard(boardAsString, score);

            this.currentGameState.setPacmanMouthOpen(!this.currentGameState.isPacmanMouthOpen());
        }
        return score;
    }
}
