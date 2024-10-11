import java.util.ArrayList;
import java.util.List;

import main.java.cs204.project.model.Elo.Game;
import main.java.cs204.project.model.Elo.Player;

public class Elo {
  private List<Player> playerList;
  private final int[] score = { 5, 3, 2, 1, -1, -2, -3, -5 };
  private final int k = 32;

  public Elo(List<Player> playerList) {
    this.playerList = playerList;
  }

  public void updateRank(){
    Game game = new Game(playerList);
    playerList = game.endGame();

    for (int i = 0; i < playerList.size(); i++) {
      Player player = playerList.get(i);
      int expectedScore = calculateAverageExpectedScore(player);
      int updatedRank = player.getRank() + (k * (score[i] - expectedScore));
      player.setRank(updatedRank);
    }
  }

  // Method to calculate expected score for a player against another player
  private double calculateExpectedScore(int playerRank, int opposingPlayerRank) {
    return 1.0 / (1.0 + Math.pow(10, (opposingPlayerRank - playerRank) / 400.0));
  }

  // Method to calculate average expected score for a player against multiple
  // opponents
  private int calculateAverageExpectedScore(Player player) {
    double totalExpectedScore = 0.0;

    // Loop through each opponent's rating and calculate expected score
    for (Player opposingPlayer : playerList) {
      if (!opposingPlayer.equals(player)){
        totalExpectedScore += calculateExpectedScore(player.getRank(), opposingPlayer.getRank());
      }
    }

    // Return average expected score
    return (int) totalExpectedScore / (playerList.size() - 1);
  }

  public static void main(String[] args) {
    List<Player> players = new ArrayList<>();

    players.add(new Player(1, 1000));
    players.add(new Player(2, 2000));
    players.add(new Player(3, 1500));
    players.add(new Player(4, 1340));
    players.add(new Player(5, 900));
    players.add(new Player(6, 1690));
    players.add(new Player(7, 1200));
    players.add(new Player(8, 1700));

    Elo elo = new Elo(players);
    elo.updateRank();

    for (Player player : players) {
      System.out.println(player.toString());
    }
  }
}