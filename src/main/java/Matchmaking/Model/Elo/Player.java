package Matchmaking.Model.Elo;

public class Player {
    private Long id;
    private Integer rank;

    // Constructor
    public Player(Long i, Integer rank) {
        this.id = i;
        this.rank = rank;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
