package Matchmaking;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import Matchmaking.Entity.Round;
import Matchmaking.Repo.RoundRepository;

import java.util.List;

import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") 
public class MatchmakingRepositoryIntegrationTest {

    @Autowired
    private RoundRepository roundRepository;

    @Test
    void testSaveAndRetrieveRound() {
        Round round = new Round();
        round.setTournamentId(1L);
        round.setRoundId(1);
        round.setMatchId(1);
        roundRepository.save(round);

        List<Round> retrievedRounds = roundRepository.findAll();

        assertEquals(1, retrievedRounds.size(), "There should be one round in the database.");
        assertEquals(round.getTournamentId(), retrievedRounds.get(0).getTournamentId());
    }
}
