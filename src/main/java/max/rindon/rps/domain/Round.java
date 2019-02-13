package max.rindon.rps.domain;


import java.util.Objects;

public class Round {
    public final Move playerMove;
    public final Move aiMove;
    public final Outcome outcome;

    public Round(Move playerMove, Move aiMove, Outcome outcome) {
        this.playerMove = playerMove;
        this.aiMove = aiMove;
        this.outcome = outcome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Round round = (Round) o;
        return playerMove == round.playerMove &&
                aiMove == round.aiMove &&
                outcome == round.outcome;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerMove, aiMove, outcome);
    }

    @Override
    public String toString() {
        return "Round{" +
                "playerMove=" + playerMove +
                ", aiMove=" + aiMove +
                ", outcome=" + outcome +
                '}';
    }
}
