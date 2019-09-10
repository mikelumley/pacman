public class Game {

    private BoardUpdater updater = new BoardUpdater();
    private Tile[][] board;

    public Game() {
        this.board = BoardFactory.initialiseBoard();
    }

    public int play() {
        while(!this.updater.isGameOver()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Clear terminal
            System.out.print("\033[H\033[2J");

            this.updater.movePacman(this.board, Direction.UP);

            System.out.println("Score: " + this.updater.getScore());
            this.printBoard();
        }
        return this.updater.getScore();
    }

    private void printBoard() {
        for(Tile[] row : this.board) {
            for(Tile tile : row) {
                if (tile.getObjectOnTile() == GameObject.PACMAN)
                    System.out.print('P');
                else if (tile.getObjectOnTile() == GameObject.FOOD)
                    System.out.print('.');
                else
                    System.out.print(' ');
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }
}