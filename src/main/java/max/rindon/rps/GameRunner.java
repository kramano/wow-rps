package max.rindon.rps;

import max.rindon.rps.ai.Strategy;
import max.rindon.rps.domain.Game;
import max.rindon.rps.ui.Command;
import max.rindon.rps.ui.IO;
import max.rindon.rps.ui.UI;

class GameRunner {
    private final IO io;
    private final Game game;

    public GameRunner(IO io, Strategy aiStrategy) {
        this.io = io;
        this.game = new Game(aiStrategy);
    }

    public void run() {
        io.write(UI.WELCOME_MESSAGE);
        io.write(UI.HELP_MESSAGE);
        while (true) {
            String playerInput = io.prompt(UI.MOVE_PROMPT_MESSAGE);
            // check for command
            Command command = UI.parseCommand(playerInput).orElse(Command.PLAY); // if we didn't recognize any command - proceed to playing

            if (command == Command.QUIT) {
                io.write(UI.renderStatistics(game.getStatistics()));
                return;
            } else if (command == Command.HELP) {
                io.write(UI.HELP_MESSAGE);
                continue;
            } else if (command == Command.STATS) {
                io.write(UI.renderStatistics(game.getStatistics()));
                continue;
            }

            String message = UI
                    .parseMove(playerInput)
                    .map(game::playRound)
                    .map(UI::renderRound)
                    .orElse(UI.renderParseMoveError(playerInput));

            io.write(message);
        }
    }
}
