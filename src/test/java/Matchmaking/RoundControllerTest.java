package Matchmaking;

import Matchmaking.Model.Round;
import Matchmaking.Controller.MatchmakingController;
import Matchmaking.Model.RoundService;

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

    // @Mock
    // private RoundService roundService;

    // @InjectMocks
    // private MatchmakingController matchmakingController;

    // @BeforeEach
    // public void setup() {
    //     MockitoAnnotations.openMocks(this);
    // }

    // @Test
    // public void testCreateNextRound() {
    //     // Sample data
    //     Long tournamentId = 1L;
    //     List<String> matchResultsData = new ArrayList<>();
    //     matchResultsData.add("[{\"player_id\":1,\"rank\":10},{\"player_id\":2,\"rank\":8}]");  // Sample match results
    //     matchResultsData.add("[{\"player_id\":3,\"rank\":15},{\"player_id\":4,\"rank\":12}]");
    //     matchResultsData.add("[{\"player_id\":5,\"rank\":6},{\"player_id\":6,\"rank\":7}]");
    //     matchResultsData.add("[{\"player_id\":7,\"rank\":9},{\"player_id\":8,\"rank\":5}]");
    //     int currentRound = 2;

    //     // Mock service behavior: return a list of 4 rounds
    //     List<Round> mockRounds = new ArrayList<>();
    //     mockRounds.add(new Round(tournamentId, currentRound, 1, "[{\"players\":[1,2,3,4,5,6,7,8]}]"));
    //     mockRounds.add(new Round(tournamentId, currentRound, 2, "[{\"players\":[9,10,11,12,13,14,15,16]}]"));
    //     mockRounds.add(new Round(tournamentId, currentRound, 3, "[{\"players\":[17,18,19,20,21,22,23,24]}]"));
    //     mockRounds.add(new Round(tournamentId, currentRound, 4, "[{\"players\":[25,26,27,28,29,30,31,32]}]"));

    //     // Mock the service call to return the list of rounds
    //     when(roundService.createNextRound(tournamentId, matchResultsData, currentRound)).thenReturn(mockRounds);

    //     // Call the controller method
    //     ResponseEntity<List<Round>> response = matchmakingController.createNextRound(tournamentId, matchResultsData, currentRound);

    //     // Verify the result
    //     assertEquals(200, response.getStatusCodeValue());  // Check for a 200 OK response
    //     assertNotNull(response.getBody());
    //     assertEquals(4, response.getBody().size());  // Expect 4 rounds

    //     // Verify that the rounds returned have correct data
    //     List<Round> returnedRounds = response.getBody();
    //     assertEquals(tournamentId, returnedRounds.get(0).getTournamentId());
    //     assertEquals(currentRound, returnedRounds.get(0).getRoundId());
    //     assertEquals(1, returnedRounds.get(0).getMatchId());
    //     assertEquals("[{\"players\":[1,2,3,4,5,6,7,8]}]", returnedRounds.get(0).getPlayersData());

    //     // Verify the service method was called with the correct parameters
    //     // verify(roundService).createNextRound(tournamentId, matchResultsData, currentRound);
    // }
}