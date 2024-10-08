package Matchmaking;

import Matchmaking.Model.Round;
import Matchmaking.Model.RoundRepository;
import Matchmaking.Model.RoundService;
import Matchmaking.Controller.MatchmakingController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RoundControllerTest {

    @Mock
    private RoundService roundService;

    @InjectMocks
    private MatchmakingController roundController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateRounds() {
        // Sample data
        Long tournamentId = 1L;
        String playersData = "[{\"player_id\":1,\"rank_id\":3},{\"player_id\":2,\"rank_id\":4}]"; // JSON data

        // Mock service behavior
        List<Round> rounds = new ArrayList<>();
        rounds.add(new Round(tournamentId, 1, "[{\"players\":[1,2,3,4,5,6,7,8]}]"));
        when(roundService.createTournamentRounds(tournamentId, playersData)).thenReturn(rounds);

        // Call the controller method
        ResponseEntity<List<Round>> response = roundController.createRounds(tournamentId, playersData);

        // Verify the result
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        // Verify the service method was called
        verify(roundService).createTournamentRounds(tournamentId, playersData);
    }
}
