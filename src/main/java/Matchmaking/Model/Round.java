package Matchmaking.Model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "rounds")
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tournament_id")
    private Long tournamentId;

    @Column(name = "round_number")
    private Integer roundNumber; // To track the round (1, 2, or 3)

    @Column(name = "matches", columnDefinition = "json")
    private String matches; // JSON containing 4 matches of 8 players each

    public Round() {
    }

    public Round(Long tournamentId, Integer roundNumber, String matches) {
        this.tournamentId = tournamentId;
        this.roundNumber = roundNumber;
        this.matches = matches;
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

    public Integer getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }

    public String getMatches() {
        return matches;
    }

    public void setMatches(String matches) {
        this.matches = matches;
    }
}