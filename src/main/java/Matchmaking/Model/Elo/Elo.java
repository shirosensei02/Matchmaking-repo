package Matchmaking.Model.Elo;

import java.util.ArrayList;
import java.util.List;


public class Elo {
  private List<Player> playerList;
  private final int[] score = { 5, 3, 2, 1, -1, -2, -3, -5 };
  private final int k = 32;

  public Elo(List<Player> playerList) {
    this.playerList = playerList;
  }

  public List<Player> updateRank(){
    Game game = new Game(playerList);
    playerList = game.gameEnd();

    for (int i = 0; i < playerList.size(); i++) {
      Player player = playerList.get(i);
      int expectedScore = calculateAverageExpectedScore(player);
      int updatedRank = player.getRank() + (k * (score[i] - expectedScore));
      if (updatedRank < 0){
        updatedRank = 0;
      }
      player.setRank(updatedRank);
    }
    return playerList;
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

}