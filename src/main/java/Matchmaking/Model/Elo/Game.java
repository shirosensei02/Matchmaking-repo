package Matchmaking.Model.Elo;

import java.util.Collections;
import java.util.List;

public class Game {
    private List<Player> playerList;

    public Game(List<Player> playerList) {
        this.playerList = playerList;
    }

    public List<Player> gameEnd() {
        Collections.shuffle(playerList);
        return playerList;
    }
}