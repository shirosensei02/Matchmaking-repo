
import java.util.ArrayList;
import java.util.List;

public class Test {
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
