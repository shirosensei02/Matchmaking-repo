package Matchmaking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Matchmaking.Model.Elo.Elo;
import Matchmaking.Model.Elo.Player;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EloTest {
    private Elo elo;
    private List<Player> players;

    @BeforeEach
    void setUp() {
        players = new ArrayList<>();
        // Add test players with different initial ranks
        players.add(new Player((long) 1, 1000));
        players.add(new Player((long) 2, 1200));
        players.add(new Player((long) 3, 800));
        players.add(new Player((long) 4, 1500));
        players.add(new Player((long) 5, 1100));
        players.add(new Player((long) 6, 900));
        players.add(new Player((long) 7, 1300));
        players.add(new Player((long) 8, 1400));
        
        elo = new Elo(players);
    }

    @Test
    void testScoreImpact() {
        // Create two players with same rank to test score impact
        List<Player> equalPlayers = new ArrayList<>();
        int initialRank = 1000;
        for (int i = 0; i < 8; i++) {
            equalPlayers.add(new Player((long) i, initialRank));
        }
        
        Elo equalElo = new Elo(equalPlayers);
        List<Player> updatedPlayers = equalElo.updateRank();

        // First place should gain more points than second place
        int firstPlaceGain = updatedPlayers.get(0).getRank() - initialRank;
        int secondPlaceGain = updatedPlayers.get(1).getRank() - initialRank;
        assertTrue(firstPlaceGain > secondPlaceGain,
                "First place should gain more points than second place");
    }
}