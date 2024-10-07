package main.java.cs204.project.model.Elo;

import java.util.Collections;
import java.util.List;

public class Game {
  private List<Player> playerList;

  public Game(List<Player> playerList){
    this.playerList = playerList;
  }

  public List<Player> endGame(){
    Collections.shuffle(playerList);
    return playerList;
  }
}
