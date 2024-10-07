package main.java.cs204.project.model.Elo;

//for testing

public class Player {
  private long id;
  private int rank;

  public Player(long id, int rank) {
    this.id = id;
    this.rank = rank;
  }

  public int getRank() {
    return rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (id ^ (id >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Player other = (Player) obj;
    if (id != other.id)
      return false;
    return true;
  }
}
