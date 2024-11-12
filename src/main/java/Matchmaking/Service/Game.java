package Matchmaking.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Matchmaking.Entity.Player;

public class Game {
  private List<Player> playerList;

  public Game(List<Player> playerList){
    this.playerList = playerList;
  }

  public List<Player> gameEnd(){
    List<Player> mutablePlayerList = new ArrayList<>(playerList);
    return mutablePlayerList;
  }
}
