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
        // Sample data
        Long tournamentId = 1L;
        String playersData = "[{\"player_id\":1,\"rank_id\":3},{\"player_id\":2,\"rank_id\":4},...]"; // Simulated
                                                                                                      // player JSON
                                                                                                      // data

        // Simulate behavior of repository's save method
        when(roundRepository.save(any(Round.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the service method
        List<Round> rounds = roundService.createTournamentRounds(tournamentId, playersData);

        // Verify the rounds were created for 3 rounds
        assertEquals(3, rounds.size());

        // Verify the repository's save method was called
        verify(roundRepository, times(3)).save(any(Round.class));
    }

    @Test
    public void testCreateRounds() {
        // Sample data
        Long tournamentId = 1L;
        String playersData = "[{\"player_id\":1,\"rank_id\":3},{\"player_id\":2,\"rank_id\":4}]"; // JSON with 32
                                                                                                  // players

        // Simulate repository behavior for save
        when(roundRepository.save(any(Round.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Test the service logic
        List<Round> rounds = roundService.createTournamentRounds(tournamentId, playersData);

        assertEquals(3, rounds.size()); // Expect 3 rounds

        // Verify the repository save method was called 3 times
        verify(roundRepository, times(3)).save(any(Round.class));
    }
}
