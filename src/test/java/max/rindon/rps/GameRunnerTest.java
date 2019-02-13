package max.rindon.rps;

import max.rindon.rps.ai.Strategies;
import max.rindon.rps.ui.IO;
import max.rindon.rps.ui.UI;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameRunnerTest {

    private static final String ROUND_ONE = "    _______\n" +
            "---'   ____)\n" +
            "      (_____)\n" +
            "      (_____)\n" +
            "      (____)\n" +
            "---.__(___)\n" +
            "\n" +
            ">=====VS=====<\n" +
            "    _______\n" +
            "---'   ____)\n" +
            "      (_____)\n" +
            "      (_____)\n" +
            "      (____)\n" +
            "---.__(___)\n" +
            "\n" +
            "And it's a draw.\n" +
            "-------------------------------------------------------";

    private static final String ROUND_TWO = "    _______\n" +
            "---'   ____)____\n" +
            "          ______)\n" +
            "          _______)\n" +
            "         _______)\n" +
            "---.__________)\n" +
            "\n" +
            ">=====VS=====<\n" +
            "    _______\n" +
            "---'   ____)\n" +
            "      (_____)\n" +
            "      (_____)\n" +
            "      (____)\n" +
            "---.__(___)\n" +
            "\n" +
            "Greetings, you won!\n" +
            "-------------------------------------------------------";

    private static final String ROUND_THREE = "    _______\n" +
            "---'   ____)____\n" +
            "          ______)\n" +
            "       __________)\n" +
            "      (____)\n" +
            "---.__(___)\n" +
            ">=====VS=====<\n" +
            "    _______\n" +
            "---'   ____)\n" +
            "      (_____)\n" +
            "      (_____)\n" +
            "      (____)\n" +
            "---.__(___)\n" +
            "\n" +
            "Sorry, you lost. Maybe next time!\n" +
            "-------------------------------------------------------";

    private static final String STATS = "You: 1\n" +
            "AI: 1\n" +
            "Draw: 1";


    @Test
    void integrationTest() {
        TestIO io = new TestIO(Arrays.asList("r", "P", "s", ":s", ":h", ":q"));
        new GameRunner(io, Strategies.ALWAYS_ROCK).run();
        List<String> output = io.output;
        List<String> expectedOutput = Arrays.asList(
                UI.WELCOME_MESSAGE,
                UI.HELP_MESSAGE,
                UI.MOVE_PROMPT_MESSAGE,
                ROUND_ONE,
                UI.MOVE_PROMPT_MESSAGE,
                ROUND_TWO,
                UI.MOVE_PROMPT_MESSAGE,
                ROUND_THREE,
                UI.MOVE_PROMPT_MESSAGE,
                STATS,
                UI.MOVE_PROMPT_MESSAGE,
                UI.HELP_MESSAGE,
                UI.MOVE_PROMPT_MESSAGE,
                STATS
        );
        assertEquals(expectedOutput, output);
    }

    private static class TestIO implements IO {
        private final List<String> input;
        private int inputPosition;
        private final List<String> output;

        TestIO(List<String> input) {
            this.input = input;
            this.inputPosition = 0;
            this.output = new ArrayList<>();
        }

        @Override
        public String read() {
            String res = input.get(inputPosition);
            inputPosition++;
            return res;
        }

        @Override
        public void write(String message) {
            output.add(message);
        }
    }
}