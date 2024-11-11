package Matchmaking.Model;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;

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
    private Integer roundId;

    @Column(name = "match_id")
    private Integer matchId;

    @Type(JsonType.class)  
    @Column(name = "players_data", columnDefinition = "json")
    private JsonNode playersData;  


    public Round() {
    }

    public Round(Long tournamentId, Integer roundId, Integer matchId, JsonNode playersData) {
        this.tournamentId = tournamentId;
        this.roundId = roundId;
        this.matchId = matchId;
        this.playersData = playersData;
    }

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

    public JsonNode getPlayersData() {
        return playersData;
    }

    public void setPlayersData(JsonNode playersData) {
        this.playersData = playersData;
    }
}