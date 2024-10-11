import java.util.List;

public class Elo {
  private List<Player> playerList;
  final private int[] score = {7, 4, 2, 1, -1,-2, -4, -7};

  public Elo(List<Player> playerList){
    this.playerList = playerList;
  }

  public List<Player> updateRank(){
    Game game = new Game(playerList);
    List<Player> positionRank = game.gameEnd();

    int average = positionRank.stream().mapToInt(Player::getRank).average();

    for (int i = 0; i < positionRank.size(); i++){
      int multiplier = 1;
      Player player = positionRank.get(i);
    }
  }

    // Method to calculate average expected score for a player against multiple opponents
    private double calculateAverageExpectedScore(int playerRating) {
      double totalExpectedScore = 0.0;
      int numberOfOpponents = playerList.size();

      // Loop through each opponent's rating and calculate expected score
      for (double opponentRating : opponentRatings) {
          totalExpectedScore += 1.0 / (1.0 + Math.pow(10, (opponentRating - playerRating) / 400.0));
      }

      // Return average expected score
      return totalExpectedScore / numberOfOpponents;
    }
}
