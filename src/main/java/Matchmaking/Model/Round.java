package Matchmaking.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "rounds")
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tournament_id")
    private Long tournamentId;

    @Column(name = "round_id")
    private Integer roundId;  // Tracks the round (0, 1, 2, etc.)

    @Column(name = "match_id")
    private Integer matchId;  // Tracks the match within the round (1, 2, 3, 4)

    @Column(name = "players_data", columnDefinition = "json")
    private String playersData;  // JSON structure containing 8 players for this match

    public Round() {
    }

    public Round(Long tournamentId, Integer roundId, Integer matchId, String playersData) {
        this.tournamentId = tournamentId;
        this.roundId = roundId;
        this.matchId = matchId;
        this.playersData = playersData;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public Integer getRoundId() {
        return roundId;
    }

    public void setRoundId(Integer roundId) {
        this.roundId = roundId;
    }

    public Integer getMatchId() {
        return matchId;
    }

    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    public String getPlayersData() {
        return playersData;
    }

    public void setPlayersData(String playersData) {
        this.playersData = playersData;
    }
}