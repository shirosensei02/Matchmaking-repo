package cs204.project.Model.results;

public class Results {
    
}

// package cs204.project.model.tournament;

// // import java.util.ArrayList;
// import java.util.List;
// import java.time.LocalDate;

// // import javax.persistence.*;

// // import jakarta.annotation.Generated;
// import jakarta.persistence.*;

// @Entity
// @Table(name = "tournaments")
// public class Tournament {
//     @Id
//     @GeneratedValue(strategy = GenerationType.AUTO) // or GenerationType.IDENTITY
//     private Long id;

//     @Column(name = "name", unique = true, nullable = false) // name unique or not?
//     private String name;
//     @Column(name = "date", nullable = false)
//     private LocalDate date;
//     @Column(name = "rank_range", nullable = false)
//     private int[] rankRange;
//     @Column(name = "status", nullable = false)
//     private String status;
//     @Column(name = "region", nullable = false)
//     private String region;
//     @Column(name = "player_list", nullable = false)
//     private List<Long> playerList;

//     // Constructors
//     public Tournament() {
//     }
    
//     public Tournament(Long id, String name, LocalDate date, 
//                     int[] rankRange, String status, String region, 
//                     List<Long> playerList) {
//       this.id = id;
//       this.name = name;
//       this.date = date;
//       this.rankRange = rankRange;
//       this.status = status;
//       this.region = region;
//       this.playerList = playerList;
//     }

//     public Tournament(String name, LocalDate date, 
//                     int[] rankRange, String status, String region) {
//       this.name = name;
//       this.date = date;
//       this.rankRange = rankRange;
//       this.status = status;
//       this.region = region;
//     }

//     public Tournament(String name, LocalDate date, 
//         int[] rankRange, String status, String region, 
//         List<Long> playerList) {
//       this.name = name;
//       this.date = date;
//       this.rankRange = rankRange;
//       this.status = status;
//       this.region = region;
//       this.playerList = playerList;
//     }

//     // Getter setters
//     public String getName() {
//       return name;
//     }
//     public void setName(String name) {
//       this.name = name;
//     }
//     public LocalDate getDate() {
//       return date;
//     }
//     public void setDate(LocalDate date) {
//       this.date = date;
//     }
//     public int[] getRankRange() {
//       return rankRange;
//     }
//     public void setRankRange(int[] rankRange) {
//       this.rankRange = rankRange;
//     }
//     public String getStatus() {
//       return status;
//     }
//     public void setStatus(String status) {
//       this.status = status;
//     }
//     public String getRegion() {
//       return region;
//     }
//     public void setRegion(String region) {
//       this.region = region;
//     }
//     public List<Long> getPlayerList() {
//       return playerList;
//     }
//     public void setPlayerList(List<Long> playerList) {
//       this.playerList = playerList;
//     }

//     public Long getId() {
//       return id;
//     }

//     public void setId(Long id) {
//       this.id = id;
//     }

//     public boolean isPlayerListFull() {
//       return this.getPlayerList().size() >= 32;
//     }
// }
