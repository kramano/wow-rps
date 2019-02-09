package max.rindon.domain;

public class Round {
    public final Move playerMove;
    public final Move aiMove;
    public final Outcome outcome;

    public Round(Move playerMove, Move aiMove, Outcome outcome) {
        this.playerMove = playerMove;
        this.aiMove = aiMove;
        this.outcome = outcome;
    }
}
