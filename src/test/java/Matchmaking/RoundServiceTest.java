package Matchmaking;

import Matchmaking.Model.Round;
import Matchmaking.Model.RoundRepository;
import Matchmaking.Model.RoundService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RoundServiceTest {

    @Mock
    private RoundRepository roundRepository;

    @InjectMocks
    private RoundService roundService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTournamentRounds() {
        // Sample tournament and players data
        Long tournamentId = 1L;
        String playersData = "[{\"player_id\":1,\"rank_id\":3},{\"player_id\":2,\"rank_id\":4},...]";

        // Mock the repository's save behavior
        when(roundRepository.save(any(Round.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the service method
        List<Round> rounds = roundService.createTournamentRounds(tournamentId, playersData);

        // Verify that 3 rounds are created
        assertEquals(3, rounds.size());

        // Verify that save was called 3 times (once for each round)
        verify(roundRepository, times(3)).save(any(Round.class));
    }

    @Test
    public void testMatchmakingAlgorithm() {
        List<String> players = new ArrayList<>();
        for (int i = 1; i <= 32; i++) {
            players.add("Player " + i);
        }

        List<List<String>> groups = roundService.matchmakingAlgorithm(players);

        // Check that the matchmaking algorithm splits players into 4 groups of 8 players each
        assertEquals(4, groups.size());
        assertEquals(8, groups.get(0).size());
    }
}