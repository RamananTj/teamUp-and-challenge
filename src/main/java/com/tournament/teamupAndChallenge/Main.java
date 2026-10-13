package com.tournament.teamupAndChallenge;

import java.util.List;
import java.util.Scanner;

import java.time.LocalDate;
import java.time.LocalTime;

import com.tournament.teamupAndChallenge.entity.User;
import com.tournament.teamupAndChallenge.entity.Player;
import com.tournament.teamupAndChallenge.entity.Sport;
import com.tournament.teamupAndChallenge.entity.PlayerSport;

import com.tournament.teamupAndChallenge.entity.Team;
import com.tournament.teamupAndChallenge.entity.TeamMember;
import com.tournament.teamupAndChallenge.entity.Venue;
import com.tournament.teamupAndChallenge.entity.Match;
import com.tournament.teamupAndChallenge.entity.MatchParticipant;
import com.tournament.teamupAndChallenge.entity.Referee;
import com.tournament.teamupAndChallenge.entity.RefereeAssignment;
import com.tournament.teamupAndChallenge.entity.MatchResult;
import com.tournament.teamupAndChallenge.entity.Tournament;
import com.tournament.teamupAndChallenge.entity.TournamentParticipant;
import com.tournament.teamupAndChallenge.entity.TournamentMatch;

import com.tournament.teamupAndChallenge.repository.*;
import com.tournament.teamupAndChallenge.service.*;
import com.tournament.teamupAndChallenge.util.HibernateUtil;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    
    private static TeamService teamService;
    private static TeamMemberService teamMemberService;
    private static VenueService venueService;
    private static MatchService matchService;
    private static MatchParticipantService participantService;
    
    private static RefereeService refereeService;
    private static RefereeAssignmentService refereeAssignmentService;
    private static MatchResultService matchResultService;
    private static TournamentService tournamentService;
    private static TournamentParticipantService tournamentParticipantService;
    private static TournamentMatchService tournamentMatchService;
    

    public static void main(String[] args) {

        // Repositories
        UserRepository userRepository = new UserRepositoryImpl();
        PlayerRepository playerRepository = new PlayerRepositoryImpl();
        SportRepository sportRepository = new SportRepositoryImpl();
        PlayerSportRepository playerSportRepository = new PlayerSportRepositoryImpl();

        // Services
        UserService userService = new UserService(userRepository);
        PlayerService playerService = new PlayerService(playerRepository);
        SportService sportService = new SportService(sportRepository);
               
        PlayerSportService playerSportService = new PlayerSportService(
                        								playerRepository,
                    									sportRepository,
                    									playerSportRepository
                									);
        
        // New repositories
        TeamRepository teamRepository = new TeamRepositoryImpl();
        TeamMemberRepository teamMemberRepository = new TeamMemberRepositoryImpl();
        VenueRepository venueRepository = new VenueRepositoryImpl();
        MatchRepository matchRepository = new MatchRepositoryImpl();
        MatchParticipantRepository participantRepository = new MatchParticipantRepositoryImpl();
        
        RefereeRepository refereeRepository = new RefereeRepositoryImpl();
        RefereeAssignmentRepository refereeAssignmentRepository = new RefereeAssignmentRepositoryImpl();
        MatchResultRepository matchResultRepository = new MatchResultRepositoryImpl();
        TournamentRepository tournamentRepository = new TournamentRepositoryImpl();
        TournamentParticipantRepository tournamentParticipantRepository = new TournamentParticipantRepositoryImpl();
        TournamentMatchRepository tournamentMatchRepository = new TournamentMatchRepositoryImpl();

        // new Services
        teamService = new TeamService(teamRepository, playerRepository, sportRepository);
        teamMemberService = new TeamMemberService(teamMemberRepository, teamRepository, playerRepository);
        venueService = new VenueService(venueRepository);
        matchService = new MatchService(matchRepository, sportRepository,venueRepository, userRepository);
        participantService = new MatchParticipantService(participantRepository, matchRepository,playerRepository, teamRepository);
        

		refereeService = new RefereeService(refereeRepository, playerRepository);
		refereeAssignmentService = new RefereeAssignmentService(refereeAssignmentRepository, refereeRepository, matchRepository);
		matchResultService = new MatchResultService(matchResultRepository, matchRepository);
		tournamentService = new TournamentService(tournamentRepository, sportRepository, venueRepository, userRepository);
		tournamentParticipantService = new TournamentParticipantService(tournamentParticipantRepository, tournamentRepository,playerRepository, teamRepository, playerSportRepository);
		tournamentMatchService = new TournamentMatchService(tournamentMatchRepository, tournamentRepository, matchRepository);

        boolean running = true;

        try {
            while (running) {

                System.out.println("\n===== TEAM UP: SPORTS PLATFORM =====");
                System.out.println("1. User Management");
                System.out.println("2. Player Management");
                System.out.println("3. Sport Management");
                System.out.println("4. Player-Sport Management");
                System.out.println("5. Team Management");
                System.out.println("6. Team Member Management");
                System.out.println("7. Venue Management");
                System.out.println("8. Match Management");
                System.out.println("9. Match Participant Management");
                System.out.println("10. Referee Management");
                System.out.println("11. Referee Assignment Management");
                System.out.println("12. Match Result Management");
                System.out.println("13. Tournament Management");
                System.out.println("14. Tournament Participant Management");
                System.out.println("15. Tournament Match Management");
                System.out.println("0. Exit");
                System.out.print("Enter choice: ");

                int choice = readInt();

                switch (choice) {

                    case 1 -> userMenu(userService);                       

                    case 2 -> playerMenu(playerService);

                    case 3 -> sportMenu(sportService);
                       
                    case 4 ->playerSportMenu(playerSportService, playerService, sportService);
                        
                    case 5 -> teamMenu();
                    case 6 -> teamMemberMenu();
                    case 7 -> venueMenu();
                    case 8 -> matchMenu();
                    case 9 -> participantMenu();
                    case 10 -> refereeMenu();
                    case 11 -> refereeAssignmentMenu();
                    case 12 -> matchResultMenu();
                    case 13 -> tournamentMenu();
                    case 14 -> tournamentParticipantMenu();
                    case 15 -> tournamentMatchMenu();
                    case 0 -> {
                    	running = false;
                        System.out.println("Exiting Team Up...");
                    }                  
                    default -> System.out.println("Invalid choice.");
                }
            }

        } finally {
            sc.close();
            HibernateUtil.getSessionFactory().close();
        }
    }

    // ================= INPUT HELPERS =================

    private static int readInt() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static Long readLong(String message) {
        System.out.print(message);

        try {
            return Long.parseLong(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
            return null;
        }
    }

    // ================= USER MENU =================

    private static void userMenu(UserService service) {

        boolean back = false;

        while (!back) {

            System.out.println("\n===== USER MANAGEMENT =====");
            System.out.println("1. Create User");
            System.out.println("2. View All Users");
            System.out.println("3. Find User By ID");
            System.out.println("4. Delete User");
            System.out.println("5. Update User");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            int choice = readInt();

            try {
                switch (choice) {

                    case 1: {
                        User user = new User();

                        System.out.print("Enter name: ");
                        user.setName(sc.nextLine());

                        System.out.print("Enter email: ");
                        user.setEmail(sc.nextLine());

                        System.out.print("Enter phone: ");
                        user.setPhone(sc.nextLine());

                        service.createUser(user);

                        System.out.println("User created successfully.");
                        break;
                    }

                    case 2: {
                        List<User> users = service.getAllUsers();

                        for (User user : users) {
                            System.out.println(
                                    "ID: " + user.getId()
                                    + " | Name: " + user.getName()
                                    + " | Email: " + user.getEmail()
                                    + " | Phone: " + user.getPhone()
                            );
                        }

                        if (users.isEmpty()) {
                            System.out.println("No users found.");
                        }
                        break;
                    }

                    case 3: {
                        Long id = readLong("Enter User ID: ");
                        if (id == null) break;

                        User user = service.getUser(id);

                        if (user == null) {
                            System.out.println("User not found.");
                        } else {
                            System.out.println(
                                    user.getId() + " | "
                                    + user.getName() + " | "
                                    + user.getEmail() + " | "
                                    + user.getPhone()
                            );
                        }
                        break;
                    }

                    case 4: {
                        Long id = readLong("Enter User ID to delete: ");
                        if (id == null) break;

                        service.deleteUser(id);
                        System.out.println("Delete operation completed.");
                        break;
                    }
                    
                    case 5: {
                        Long id = readLong("Enter User ID to update: ");
                        if (id == null) break;

                        User user = service.getUser(id);

                        if (user == null) {
                            System.out.println("User not found.");
                            break;
                        }

                        System.out.println("Current name: " + user.getName());
                        System.out.print("Enter new name: ");
                        user.setName(sc.nextLine());

                        System.out.println("Current email: " + user.getEmail());
                        System.out.print("Enter new email: ");
                        user.setEmail(sc.nextLine());

                        System.out.println("Current phone: " + user.getPhone());
                        System.out.print("Enter new phone: ");
                        user.setPhone(sc.nextLine());

                        service.updateUser(user);
                        System.out.println("User updated successfully.");
                        break;
                    }
                    
                    case 0:
                        back = true;
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (RuntimeException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    // ================= PLAYER MENU =================

    private static void playerMenu(PlayerService service) {

        boolean back = false;

        while (!back) {

            System.out.println("\n===== PLAYER MANAGEMENT =====");
            System.out.println("1. Create Player");
            System.out.println("2. View All Players");
            System.out.println("3. Find Player By ID");
            System.out.println("4. Find Players By City");
            System.out.println("5. Delete Player");
            System.out.println("6. Update Player");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            int choice = readInt();

            try {
                switch (choice) {

                    case 1: {
                        Player player = new Player();

                        System.out.print("Enter player name: ");
                        player.setName(sc.nextLine());

                        System.out.print("Enter city: ");
                        player.setCity(sc.nextLine());

                        System.out.print("Enter phone: ");
                        player.setPhone(sc.nextLine());

                        service.createPlayer(player);

                        System.out.println("Player created successfully.");
                        break;
                    }

                    case 2: {
                        List<Player> players = service.getAllPlayers();

                        for (Player player : players) {
                            System.out.println(
                                    "ID: " + player.getId()
                                    + " | Name: " + player.getName()
                                    + " | City: " + player.getCity()
                                    + " | Phone: " + player.getPhone()
                            );
                        }

                        if (players.isEmpty()) {
                            System.out.println("No players found.");
                        }
                        break;
                    }

                    case 3: {
                        Long id = readLong("Enter Player ID: ");
                        if (id == null) break;

                        Player player = service.getPlayer(id);

                        if (player == null) {
                            System.out.println("Player not found.");
                        } else {
                            System.out.println(
                                    player.getId() + " | "
                                    + player.getName() + " | "
                                    + player.getCity() + " | "
                                    + player.getPhone()
                            );
                        }
                        break;
                    }

                    case 4: {
                        System.out.print("Enter city: ");
                        String city = sc.nextLine();

                        List<Player> players =
                                service.getPlayersByCity(city);

                        for (Player player : players) {
                            System.out.println(
                                    player.getId() + " | "
                                    + player.getName() + " | "
                                    + player.getCity()
                            );
                        }

                        if (players.isEmpty()) {
                            System.out.println("No players found.");
                        }
                        break;
                    }

                    case 5: {
                        Long id = readLong("Enter Player ID to delete: ");
                        if (id == null) break;

                        service.deletePlayer(id);
                        System.out.println("Delete operation completed.");
                        break;
                    }
                    
                    case 6: {
                        Long id = readLong("Enter Player ID to update: ");
                        if (id == null) break;

                        Player player = service.getPlayer(id);

                        if (player == null) {
                            System.out.println("Player not found.");
                            break;
                        }

                        System.out.println("Current name: " + player.getName());
                        System.out.print("Enter new name: ");
                        player.setName(sc.nextLine());

                        System.out.println("Current city: " + player.getCity());
                        System.out.print("Enter new city: ");
                        player.setCity(sc.nextLine());

                        System.out.println("Current phone: " + player.getPhone());
                        System.out.print("Enter new phone: ");
                        player.setPhone(sc.nextLine());

                        service.updatePlayer(player);
                        System.out.println("Player updated successfully.");
                        break;
                    }
                    
                    case 0:
                        back = true;
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (RuntimeException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    // ================= SPORT MENU =================

    private static void sportMenu(SportService service) {

        boolean back = false;

        while (!back) {

            System.out.println("\n===== SPORT MANAGEMENT =====");
            System.out.println("1. Create Sport");
            System.out.println("2. View All Sports");
            System.out.println("3. Find Sport By ID");
            System.out.println("4. Find Sport By Name");
            System.out.println("5. Delete Sport");
            System.out.println("6. Update Sport");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            int choice = readInt();

            try {
                switch (choice) {

                    case 1: {
                        Sport sport = new Sport();

                        System.out.print("Enter sport name: ");
                        sport.setName(sc.nextLine());

                        System.out.print(
                                "Enter type (INDIVIDUAL/TEAM): ");
                        sport.setType(sc.nextLine().toUpperCase());

                        System.out.print("Enter minimum players: ");
                        int minPlayers = readInt();

                        System.out.print("Enter maximum players: ");
                        int maxPlayers = readInt();

                        if (minPlayers <= 0 || maxPlayers < minPlayers) {
                            System.out.println(
                                    "Invalid player limits.");
                            break;
                        }

                        sport.setMinimumPlayers(minPlayers);
                        sport.setMaximumPlayers(maxPlayers);

                        service.createSport(sport);

                        System.out.println("Sport created successfully.");
                        break;
                    }

                    case 2: {
                        List<Sport> sports = service.getAllSports();

                        for (Sport sport : sports) {
                            System.out.println(
                                    "ID: " + sport.getId()
                                    + " | Name: " + sport.getName()
                                    + " | Type: " + sport.getType()
                                    + " | Min Players: "
                                    + sport.getMinimumPlayers()
                                    + " | Max Players: "
                                    + sport.getMaximumPlayers()
                            );
                        }

                        if (sports.isEmpty()) {
                            System.out.println("No sports found.");
                        }
                        break;
                    }

                    case 3: {
                        Long id = readLong("Enter Sport ID: ");
                        if (id == null) break;

                        Sport sport = service.getSport(id);

                        if (sport == null) {
                            System.out.println("Sport not found.");
                        } else {
                            System.out.println(
                                    sport.getId() + " | "
                                    + sport.getName() + " | "
                                    + sport.getType()
                            );
                        }
                        break;
                    }

                    case 4: {
                        System.out.print("Enter sport name: ");
                        String name = sc.nextLine();

                        Sport sport = service.getSportByName(name);

                        if (sport == null) {
                            System.out.println("Sport not found.");
                        } else {
                            System.out.println(
                                    sport.getId() + " | "
                                    + sport.getName() + " | "
                                    + sport.getType()
                            );
                        }
                        break;
                    }

                    case 5: {
                        Long id = readLong("Enter Sport ID to delete: ");
                        if (id == null) break;

                        service.deleteSport(id);
                        System.out.println("Delete operation completed.");
                        break;
                    }
                    
                    case 6: {
                        Long id = readLong("Enter Sport ID to update: ");
                        if (id == null) break;

                        Sport sport = service.getSport(id);

                        if (sport == null) {
                            System.out.println("Sport not found.");
                            break;
                        }

                        System.out.println("Current name: " + sport.getName());
                        System.out.print("Enter new name: ");
                        sport.setName(sc.nextLine());

                        System.out.println("Current type: " + sport.getType());
                        System.out.print("Enter new type (INDIVIDUAL/TEAM): ");
                        sport.setType(sc.nextLine().trim().toUpperCase());

                        System.out.print("Enter new minimum players: ");
                        int min = readInt();

                        System.out.print("Enter new maximum players: ");
                        int max = readInt();

                        if (min <= 0 || max < min) {
                            System.out.println("Invalid player limits.");
                            break;
                        }

                        sport.setMinimumPlayers(min);
                        sport.setMaximumPlayers(max);

                        service.updateSport(sport);
                        System.out.println("Sport updated successfully.");
                        break;
                    }
                    
                    case 0:
                        back = true;
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (RuntimeException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    // ================= PLAYER-SPORT MENU =================

    private static void playerSportMenu(
            PlayerSportService service,
            PlayerService playerService,
            SportService sportService) {

        boolean back = false;

        while (!back) {

            System.out.println("\n===== PLAYER-SPORT MANAGEMENT =====");
            System.out.println("1. Register Player For A Sport");
            System.out.println("2. View Sports Of A Player");
            System.out.println("3. View Players Of A Sport");
            System.out.println("4. View All Registrations");
            System.out.println("5. Delete Registration");
            System.out.println("6. Update Registration");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            int choice = readInt();

            try {
                switch (choice) {

                    case 1: {
                        Long playerId = readLong("Enter Player ID: ");
                        if (playerId == null) break;

                        Long sportId = readLong("Enter Sport ID: ");
                        if (sportId == null) break;

                        System.out.print("Enter skill level: ");
                        String skillLevel = sc.nextLine();

                        service.registerSport(playerId, sportId, skillLevel);

                        System.out.println("Player registered for sport successfully.");
                        break;
                    }

                    case 2: {
                        Long playerId = readLong("Enter Player ID: ");
                        if (playerId == null) break;

                        if (playerService.getPlayer(playerId) == null) {
                            System.out.println("Player not found.");
                            break;
                        }

                        List<PlayerSport> registrations = service.getSportsByPlayer(playerId);

                        for (PlayerSport ps : registrations) {
                            System.out.println(
                                    "Sport: " + ps.getSport().getName()
                                    + " | Skill: " + ps.getSkillLevel()
                                    + " | Matches: " + ps.getMatchesPlayed()
                                    + " | Wins: " + ps.getMatchesWon()
                                    + " | Rating: " + ps.getRating()
                            );
                        }

                        if (registrations.isEmpty()) {
                            System.out.println("This player has no registered sports.");
                        }
                        break;
                    }

                    case 3: {
                        Long sportId = readLong("Enter Sport ID: ");
                        if (sportId == null) break;

                        if (sportService.getSport(sportId) == null) {
                            System.out.println("Sport not found.");
                            break;
                        }

                        List<PlayerSport> registrations = service.getPlayersBySport(sportId);

                        for (PlayerSport ps : registrations) {
                            System.out.println(
                                    "Player: " + ps.getPlayer().getName()
                                    + " | City: " + ps.getPlayer().getCity()
                                    + " | Skill: " + ps.getSkillLevel()
                            );
                        }

                        if (registrations.isEmpty()) {
                            System.out.println(
                                    "No players registered for this sport.");
                        }
                        break;
                    }

                    case 4: {
                        List<PlayerSport> registrations = service.getAllRegistrations();

                        for (PlayerSport ps : registrations) {
                            System.out.println(
                                    "Registration ID: " + ps.getId()
                                    + " | Player: "
                                    + ps.getPlayer().getName()
                                    + " | Sport: "
                                    + ps.getSport().getName()
                                    + " | Skill: " + ps.getSkillLevel()
                            );
                        }

                        if (registrations.isEmpty()) {
                            System.out.println("No registrations found.");
                        }
                        break;
                    }

                    case 5: {
                        Long id = readLong("Enter Registration ID to delete: ");
                        if (id == null) break;

                        service.deleteRegistration(id);

                        System.out.println(
                                "Registration deleted successfully.");
                        break;
                    }
                    
                    case 6: {
                        Long id = readLong("Enter Registration ID to update: ");
                        if (id == null) break;

                        PlayerSport registration = service.getRegistration(id);

                        if (registration == null) {
                            System.out.println("Registration not found.");
                            break;
                        }

                        System.out.println("Current skill level: " + registration.getSkillLevel());
                        System.out.print("Enter new skill level: ");
                        registration.setSkillLevel(sc.nextLine());

                        service.updateRegistration(registration);

                        System.out.println("Registration updated successfully.");
                        break;
                    }

                    case 0:
                        back = true;
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (RuntimeException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }
    
 // ================= TEAM MENU =================

    private static void teamMenu() {

        boolean back = false;

        while (!back) {
            System.out.println("\n===== TEAM MANAGEMENT =====");
            System.out.println("1. Create Team");
            System.out.println("2. View All Teams");
            System.out.println("3. Find Team By ID");
            System.out.println("4. Update Team");
            System.out.println("5. Delete Team");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            int choice = readInt();

            try {
                switch (choice) {

                    case 1: {
                        System.out.print("Enter team name: ");
                        String name = sc.nextLine();

                        Long sportId = readLong("Enter Sport ID: ");
                        if (sportId == null) break;

                        Long captainId = readLong("Enter Captain Player ID: ");
                        if (captainId == null) break;

                        System.out.print("Enter city: ");
                        String city = sc.nextLine();

                        teamService.createTeam(name, sportId, captainId, city);

                        System.out.println("Team created successfully.");
                        break;
                    }

                    case 2: {
                        List<Team> teams = teamService.getAllTeams();

                        for (Team team : teams) {
                            System.out.println(
                                    "ID: " + team.getId()
                                    + " | Name: " + team.getName()
                                    + " | Sport: " + team.getSport().getName()
                                    + " | Captain: " + team.getCaptain().getName()
                                    + " | City: " + team.getCity());
                        }

                        if (teams.isEmpty()) {
                            System.out.println("No teams found.");
                        }
                        break;
                    }

                    case 3: {
                        Long id = readLong("Enter Team ID: ");
                        if (id == null) break;

                        Team team = teamService.getTeam(id);

                        if (team == null) {
                            System.out.println("Team not found.");
                        } else {
                            System.out.println(
                                    "ID: " + team.getId()
                                    + " | Name: " + team.getName()
                                    + " | Sport: " + team.getSport().getName()
                                    + " | Captain: " + team.getCaptain().getName()
                                    + " | City: " + team.getCity());
                        }
                        break;
                    }
                    
                    case 4: {
                        Long id = readLong("Enter Team ID to update: ");
                        if (id == null) break;

                        Team team = teamService.getTeam(id);

                        if (team == null) {
                            System.out.println("Team not found.");
                            break;
                        }

                        System.out.println("Current name: " + team.getName());
                        System.out.print("Enter new name: ");
                        team.setName(sc.nextLine());

                        System.out.println("Current city: " + team.getCity());
                        System.out.print("Enter new city: ");
                        team.setCity(sc.nextLine());

                        teamService.updateTeam(team);
                        System.out.println("Team updated successfully.");
                        break;
                    }

                    case 5: {
                        Long id = readLong("Enter Team ID to delete: ");
                        if (id == null) break;

                        teamService.deleteTeam(id);
                        System.out.println("Delete operation completed.");
                        break;
                    }

                    case 0:
                        back = true;
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (RuntimeException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }
    
 // ================= TEAM MEMBER MENU =================

    private static void teamMemberMenu() {

        boolean back = false;

        while (!back) {
            System.out.println("\n===== TEAM MEMBER MANAGEMENT =====");
            System.out.println("1. Add Player to Team");
            System.out.println("2. View All Team Members");
            System.out.println("3. Find Member By ID");
            System.out.println("4. Update Member");
            System.out.println("5. Remove Member");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            int choice = readInt();

            try {
                switch (choice) {

                    case 1: {
                        Long teamId = readLong("Enter Team ID: ");
                        if (teamId == null) break;

                        Long playerId = readLong("Enter Player ID: ");
                        if (playerId == null) break;

                        //validate team member (player) is already present for the team - From TeamMember tm where tm.player_id = playerId and tm.team_id = : teamId
                        
                        System.out.print("Enter role (PLAYER/CAPTAIN/VICE_CAPTAIN): ");
                        String role = sc.nextLine().trim().toUpperCase();

                        teamMemberService.addMember(teamId, playerId, role);

                        System.out.println("Player added to team successfully.");
                        break;
                    }

                    case 2: {
                        List<TeamMember> members = teamMemberService.getAllMembers();

                        for (TeamMember member : members) {
                            System.out.println(
                                    "ID: " + member.getId()
                                    + " | Team: " + member.getTeam().getName()
                                    + " | Player: " + member.getPlayer().getName()
                                    + " | Role: " + member.getRole()
                                    + " | Joined: " + member.getJoinedAt());
                        }

                        if (members.isEmpty()) {
                            System.out.println("No team members found.");
                        }
                        break;
                    }

                    case 3: {
                        Long id = readLong("Enter Team Member ID: ");
                        if (id == null) break;

                        TeamMember member = teamMemberService.getMember(id);

                        if (member == null) {
                            System.out.println("Team member not found.");
                        } else {
                            System.out.println(
                                    "ID: " + member.getId()
                                    + " | Team: " + member.getTeam().getName()
                                    + " | Player: " + member.getPlayer().getName()
                                    + " | Role: " + member.getRole()
                                    + " | Joined: " + member.getJoinedAt());
                        }
                        break;
                    }
                    
                    case 4: {
                        Long id = readLong("Enter Team Member ID to update: ");
                        if (id == null) break;

                        TeamMember member = teamMemberService.getMember(id);

                        if (member == null) {
                            System.out.println("Team member not found.");
                            break;
                        }

                        System.out.println("Current role: " + member.getRole());
                        System.out.print("Enter new role (PLAYER/CAPTAIN/VICE_CAPTAIN): ");
                        String role = sc.nextLine().trim().toUpperCase();

                        if (!List.of("PLAYER", "CAPTAIN", "VICE_CAPTAIN").contains(role)) {
                            System.out.println("Invalid role.");
                            break;
                        }

                        member.setRole(role);
                        teamMemberService.updateMember(member);

                        System.out.println("Team member updated successfully.");
                        break;
                    }
                    
                    case 5: {
                        Long id = readLong("Enter Member ID to remove: ");
                        if (id == null) break;

                        teamMemberService.deleteMember(id);
                        System.out.println("Member removal completed.");
                        break;
                    }

                    case 0:
                        back = true;
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (RuntimeException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }
    
 // ================= VENUE MENU =================

    private static void venueMenu() {

        boolean back = false;

        while (!back) {
            System.out.println("\n===== VENUE MANAGEMENT =====");
            System.out.println("1. Create Venue");
            System.out.println("2. View All Venues");
            System.out.println("3. Find Venue By ID");
            System.out.println("4. Update Venue");
            System.out.println("5. Delete Venue");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            int choice = readInt();

            try {
                switch (choice) {

                    case 1: {
                        System.out.print("Enter venue name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter address: ");
                        String address = sc.nextLine();

                        System.out.print("Enter city: ");
                        String city = sc.nextLine();
                        
                        System.out.print("Enter capacity: ");
                        int capacity = readInt();

                        if (capacity <= 0) {
                            System.out.println("Invalid capacity.");
                            break;
                        }

                        venueService.createVenue(name, address, city, capacity);

                        System.out.println("Venue created successfully.");
                        break;
                    }

                    case 2: {
                        List<Venue> venues = venueService.getAllVenues();

                        for (Venue venue : venues) {
                            System.out.println(
                                    "ID: " + venue.getId()
                                    + " | Name: " + venue.getName()
                                    + " | City: " + venue.getCity()
                                    + " | Capacity: " + venue.getCapacity()
                                    + " | Available: " + venue.getAvailable());
                        }

                        if (venues.isEmpty()) {
                            System.out.println("No venues found.");
                        }
                        break;
                    }

                    case 3: {
                        Long id = readLong("Enter Venue ID: ");
                        if (id == null) break;

                        Venue venue = venueService.getVenue(id);

                        if (venue == null) {
                            System.out.println("Venue not found.");
                        } else {
                            System.out.println(
                                    "ID: " + venue.getId()
                                    + " | Name: " + venue.getName()
                                    + " | Address: " + venue.getAddress()
                                    + " | City: " + venue.getCity()
                                    + " | Capacity: " + venue.getCapacity()
                                    + " | Available: " + venue.getAvailable());
                        }
                        break;
                    }
                    
                    case 4: {
                        Long id = readLong("Enter Venue ID to update: ");
                        if (id == null) break;

                        Venue venue = venueService.getVenue(id);

                        if (venue == null) {
                            System.out.println("Venue not found.");
                            break;
                        }

                        System.out.print("Enter new venue name: ");
                        venue.setName(sc.nextLine());

                        System.out.print("Enter new address: ");
                        venue.setAddress(sc.nextLine());

                        System.out.print("Enter new city: ");
                        venue.setCity(sc.nextLine());

                        System.out.print("Enter new capacity: ");
                        int capacity = readInt();

                        if (capacity <= 0) {
                            System.out.println("Invalid capacity.");
                            break;
                        }

                        venue.setCapacity(capacity);

                        System.out.print("Is venue available? (true/false): ");
                        String available = sc.nextLine().trim();

                        if (!available.equalsIgnoreCase("true")
                                && !available.equalsIgnoreCase("false")) {
                            System.out.println("Enter true or false Only.");
                            break;
                        }

                        venue.setAvailable(Boolean.parseBoolean(available));

                        venueService.updateVenue(venue);
                        System.out.println("Venue updated successfully.");
                        break;
                    }   
                    
                    case 5: {
                        Long id = readLong("Enter Venue ID to delete: ");
                        if (id == null) break;

                        venueService.deleteVenue(id);
                        System.out.println("Delete operation completed.");
                        break;
                    }

                    case 0:
                        back = true;
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (RuntimeException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }
    
 // ================= MATCH MENU =================

    private static void matchMenu() {

        boolean back = false;

        while (!back) {
            System.out.println("\n===== MATCH MANAGEMENT =====");
            System.out.println("1. Schedule Match");
            System.out.println("2. View All Matches");
            System.out.println("3. Find Match By ID");
            System.out.println("4. Update Match");
            System.out.println("5. Delete Match");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            int choice = readInt();

            try {
                switch (choice) {

                    case 1: {
                        System.out.print("Enter match title: ");
                        String title = sc.nextLine();

                        Long sportId = readLong("Enter Sport ID: ");
                        if (sportId == null) break;

                        Long venueId = readLong(
                                "Enter Venue ID (0 if no venue): ");
                        if (venueId == null) break;

                        if (venueId == 0) {
                            venueId = null;
                        }

                        Long organizerId =
                                readLong("Enter Organizer User ID: ");
                        if (organizerId == null) break;

                        System.out.print("Enter date (YYYY-MM-DD): ");
                        LocalDate date = LocalDate.parse(sc.nextLine());

                        System.out.print("Enter start time (HH:mm)[24 hrs Format]: ");
                        LocalTime startTime = LocalTime.parse(sc.nextLine());

                        System.out.print("Enter end time (HH:mm)[24 hrs Format]: ");
                        LocalTime endTime = LocalTime.parse(sc.nextLine());

                        System.out.print(
                                "Enter type (CASUAL/COMPETITIVE/TOURNAMENT): ");
                        String type = sc.nextLine().trim().toUpperCase();

                        matchService.scheduleMatch(
                                title, sportId, venueId, organizerId,
                                date, startTime, endTime, type);

                        System.out.println("Match scheduled successfully.");
                        break;
                    }

                    case 2: {
                        List<Match> matches = matchService.getAllMatches();

                        for (Match match : matches) {
                            System.out.println(
                                    "ID: " + match.getId()
                                    + " | Title: " + match.getTitle()
                                    + " | Sport: " + match.getSport().getName()
                                    + " | Date: " + match.getMatchDate()
                                    + " | Start: " + match.getStartTime()
                                    + " | Status: " + match.getStatus());
                        }

                        if (matches.isEmpty()) {
                            System.out.println("No matches found.");
                        }
                        break;
                    }

                    case 3: {
                        Long id = readLong("Enter Match ID: ");
                        if (id == null) break;

                        Match match = matchService.getMatch(id);

                        if (match == null) {
                            System.out.println("Match not found.");
                        } else {
                            System.out.println(
                                    "ID: " + match.getId()
                                    + " | Title: " + match.getTitle()
                                    + " | Sport: " + match.getSport().getName()
                                    + " | Venue: "
                                    + (match.getVenue() == null
                                        ? "Not assigned"
                                        : match.getVenue().getName())
                                    + " | Date: " + match.getMatchDate()
                                    + " | Start: " + match.getStartTime()
                                    + " | End: " + match.getEndTime()
                                    + " | Type: " + match.getMatchType()
                                    + " | Status: " + match.getStatus());
                        }
                        break;
                    }
                    
                    case 4: {
                        Long id = readLong("Enter Match ID to update: ");
                        if (id == null) break;

                        Match match = matchService.getMatch(id);

                        if (match == null) {
                            System.out.println("Match not found.");
                            break;
                        }

                        System.out.println("Current title: " + match.getTitle());
                        System.out.print("Enter new title: ");
                        match.setTitle(sc.nextLine());

                        System.out.println("Current date: " + match.getMatchDate());
                        System.out.print("Enter new date (YYYY-MM-DD): ");
                        match.setMatchDate(LocalDate.parse(sc.nextLine()));

                        System.out.println("Current start time: " + match.getStartTime());
                        System.out.print("Enter new start time (HH:mm): ");
                        LocalTime start = LocalTime.parse(sc.nextLine());

                        System.out.println("Current end time: " + match.getEndTime());
                        System.out.print("Enter new end time (HH:mm): ");
                        LocalTime end = LocalTime.parse(sc.nextLine());

                        if (!end.isAfter(start)) {  //end.isAfter(start) - Returns true if end is chronologically later than start. Returns false if end is the same time as start or earlier than start
                            System.out.println("End time must be later than start time.");
                            break;
                        }

                        match.setStartTime(start);
                        match.setEndTime(end);

                        System.out.println("Current type: " + match.getMatchType());
                        System.out.print("Enter new type (CASUAL/COMPETITIVE/TOURNAMENT): ");
                        String type = sc.nextLine().trim().toUpperCase();

                        if (!List.of("CASUAL", "COMPETITIVE", "TOURNAMENT").contains(type)) {
                            System.out.println("Invalid match type.");
                            break;
                        }

                        match.setMatchType(type);

                        System.out.println("Current status: " + match.getStatus());
                        System.out.print("Enter new status (OPEN/FULL/SCHEDULED/COMPLETED/CANCELLED): ");
                        String status = sc.nextLine().trim().toUpperCase();

                        if (!List.of("OPEN", "FULL", "SCHEDULED", "COMPLETED", "CANCELLED").contains(status)) {
                            System.out.println("Invalid match status.");
                            break;
                        }

                        match.setStatus(status);

                        matchService.updateMatch(match);
                        System.out.println("Match updated successfully.");
                        break;
                    }

                    case 5: {
                        Long id = readLong("Enter Match ID to delete: ");
                        if (id == null) break;

                        matchService.deleteMatch(id);
                        System.out.println("Delete operation completed.");
                        break;
                    }

                    case 0:
                        back = true;
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (RuntimeException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }
    
 // ================= MATCH PARTICIPANT MENU =================

    private static void participantMenu() {

        boolean back = false;

        while (!back) {
            System.out.println("\n===== MATCH PARTICIPANT MANAGEMENT =====");
            System.out.println("1. Register Individual Player");
            System.out.println("2. Register Team");
            System.out.println("3. View All Participants");
            System.out.println("4. Find Participant By ID");
            System.out.println("5. Update Participant");
            System.out.println("6. Remove Participant");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            int choice = readInt();

            try {
                switch (choice) {

                    case 1: {
                        Long matchId = readLong("Enter Match ID: ");
                        if (matchId == null) break;

                        Long playerId = readLong("Enter Player ID: ");
                        if (playerId == null) break;

                        System.out.print("Enter side (HOME/AWAY): ");
                        String side = sc.nextLine().trim().toUpperCase();

                        participantService.addPlayer(matchId, playerId, side);

                        System.out.println("Individual player registered successfully.");
                        break;
                    }

                    case 2: {
                        Long matchId = readLong("Enter Match ID: ");
                        if (matchId == null) break;

                        Long teamId = readLong("Enter Team ID: ");
                        if (teamId == null) break;

                        System.out.print("Enter side (HOME/AWAY): ");
                        String side = sc.nextLine().trim().toUpperCase();

                        participantService.addTeam(matchId, teamId, side);

                        System.out.println("Team registered successfully.");
                        break;
                    }

                    case 3: {
                        List<MatchParticipant> participants = participantService.getAllParticipants();

                        for (MatchParticipant participant : participants) {
                            String name = participant.getPlayer() != null
                                    ? participant.getPlayer().getName()
                                    : participant.getTeam() != null
                                        ? participant.getTeam().getName()
                                        : "Unknown";

                            System.out.println(
                                    "ID: " + participant.getId()
                                    + " | Match: "
                                    + participant.getMatch().getTitle()
                                    + " | Participant: " + name
                                    + " | Side: " + participant.getSide()
                                    + " | Status: " + participant.getStatus());
                        }

                        if (participants.isEmpty()) {
                            System.out.println("No participants found.");
                        }
                        break;
                    }

                    case 4: {
                        Long id = readLong("Enter Participant ID: ");
                        if (id == null) break;

                        MatchParticipant participant =
                                participantService.getParticipant(id);

                        if (participant == null) {
                            System.out.println("Participant not found.");
                        } else {
                            String name = participant.getPlayer() != null
                                    ? participant.getPlayer().getName()
                                    : participant.getTeam() != null
                                        ? participant.getTeam().getName()
                                        : "Unknown";

                            System.out.println(
                                    "ID: " + participant.getId()
                                    + " | Match: "
                                    + participant.getMatch().getTitle()
                                    + " | Participant: " + name
                                    + " | Side: " + participant.getSide()
                                    + " | Status: " + participant.getStatus());
                        }
                        break;
                    }

                    case 5: {
                        Long id = readLong("Enter Participant ID to update: ");
                        if (id == null) break;

                        MatchParticipant participant = participantService.getParticipant(id);

                        if (participant == null) {
                            System.out.println("Participant not found.");
                            break;
                        }

                        System.out.println("Current side: " + participant.getSide());
                        System.out.print("Enter new side (HOME/AWAY): ");
                        String side = sc.nextLine().trim().toUpperCase();

                        if (!List.of("HOME", "AWAY").contains(side)) {
                            System.out.println("Invalid side.");
                            break;
                        }

                        System.out.println("Current status: " + participant.getStatus());
                        System.out.print("Enter new status (PENDING/ACCEPTED/REJECTED): ");
                        String status = sc.nextLine().trim().toUpperCase();

                        if (!List.of("PENDING", "ACCEPTED", "REJECTED").contains(status)) {
                            System.out.println("Invalid status.");
                            break;
                        }

                        participant.setSide(side);
                        participant.setStatus(status);

                        participantService.updateParticipant(participant);
                        System.out.println("Participant updated successfully.");
                        break;
                    }
                    
                    case 6: {
                        Long id = readLong("Enter Participant ID to remove: ");
                        if (id == null) break;

                        participantService.deleteParticipant(id);
                        System.out.println("Participant removal completed.");
                        break;
                    }

                    case 0:
                        back = true;
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (RuntimeException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }
    
    


    // ================= REFEREE MENU =================

    private static void refereeMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n===== REFEREE MANAGEMENT =====");
            System.out.println("1. Register Referee");
            System.out.println("2. View All Referees");
            System.out.println("3. Find Referee By ID");
            System.out.println("4. Update Referee");
            System.out.println("5. Delete Referee");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            int choice = readInt();

            try {
                switch (choice) {
                    case 1 -> {
                        Long playerId = readLong("Enter Player ID: ");
                        if (playerId == null) break;

                        System.out.print("Enter certification level: ");
                        String certification = sc.nextLine().trim();

                        System.out.print("Enter experience years: ");
                        int years = readInt();
                        if (years < 0) {
                            System.out.println("Experience cannot be negative.");
                            break;
                        }

                        refereeService.createReferee(playerId, certification, years);
                        System.out.println("Referee registered successfully.");
                    }
                    case 2 -> {
                        List<Referee> referees = refereeService.getAllReferees();
                        for (Referee r : referees) {
                            System.out.println(
                                "ID: " + r.getId()
                                + " | Player: " + (r.getPlayer() == null ? "N/A" : r.getPlayer().getName())
                                + " | Certification: " + r.getCertificationLevel()
                                + " | Experience: " + r.getExperienceYears()
                                + " | Status: " + r.getStatus());
                        }
                        if (referees.isEmpty()) System.out.println("No referees found.");
                    }
                    case 3 -> {
                        Long id = readLong("Enter Referee ID: ");
                        if (id == null) break;
                        Referee r = refereeService.getReferee(id);
                        if (r == null) {
                            System.out.println("Referee not found.");
                        } else {
                            System.out.println(
                                "ID: " + r.getId()
                                + " | Player: " + (r.getPlayer() == null ? "N/A" : r.getPlayer().getName())
                                + " | Certification: " + r.getCertificationLevel()
                                + " | Experience: " + r.getExperienceYears()
                                + " | Status: " + r.getStatus());
                        }
                    }
                    case 4 -> {
                        Long id = readLong("Enter Referee ID to update: ");
                        if (id == null) break;
                        Referee r = refereeService.getReferee(id);
                        if (r == null) {
                            System.out.println("Referee not found.");
                            break;
                        }

                        System.out.println("Current certification: " + r.getCertificationLevel());
                        System.out.print("Enter new certification level: ");
                        r.setCertificationLevel(sc.nextLine().trim());

                        System.out.println("Current experience years: " + r.getExperienceYears());
                        System.out.print("Enter new experience years: ");
                        int years = readInt();
                        if (years < 0) {
                            System.out.println("Experience cannot be negative.");
                            break;
                        }
                        r.setExperienceYears(years);

                        System.out.println("Current status: " + r.getStatus());
                        System.out.print("Enter new status (ACTIVE/INACTIVE/SUSPENDED): ");
                        String status = sc.nextLine().trim().toUpperCase();
                        if (!List.of("ACTIVE", "INACTIVE", "SUSPENDED").contains(status)) {
                            System.out.println("Invalid referee status.");
                            break;
                        }
                        r.setStatus(status);
                        refereeService.updateReferee(r);
                        System.out.println("Referee updated successfully.");
                    }
                    case 5 -> {
                        Long id = readLong("Enter Referee ID to delete: ");
                        if (id == null) break;
                        refereeService.deleteReferee(id);
                        System.out.println("Referee deleted successfully.");
                    }
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (RuntimeException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }


    // ================= REFEREE ASSIGNMENT MENU =================

    private static void refereeAssignmentMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n===== REFEREE ASSIGNMENT MANAGEMENT =====");
            System.out.println("1. Assign Referee To Match");
            System.out.println("2. View All Assignments");
            System.out.println("3. Find Assignment By ID");
            System.out.println("4. Update Assignment");
            System.out.println("5. Delete Assignment");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            int choice = readInt();

            try {
                switch (choice) {
                    case 1 -> {
                        Long matchId = readLong("Enter Match ID: ");
                        if (matchId == null) break;
                        Long refereeId = readLong("Enter Referee ID: ");
                        if (refereeId == null) break;

                        System.out.print("Enter role (MAIN_REFEREE/ASSISTANT_REFEREE/LINE_JUDGE): ");
                        String role = sc.nextLine().trim().toUpperCase();

                        refereeAssignmentService.assignReferee(matchId, refereeId, role);
                        System.out.println("Referee assigned successfully.");
                    }
                    case 2 -> {
                        List<RefereeAssignment> assignments =
                                refereeAssignmentService.getAllAssignments();
                        for (RefereeAssignment a : assignments) {
                            System.out.println(
                                "ID: " + a.getId()
                                + " | Match: " + (a.getMatch() == null ? "N/A" : a.getMatch().getTitle())
                                + " | Referee: " + (a.getReferee() == null || a.getReferee().getPlayer() == null
                                    ? "N/A" : a.getReferee().getPlayer().getName())
                                + " | Role: " + a.getRole()
                                + " | Assigned At: " + a.getAssignedAt());
                        }
                        if (assignments.isEmpty()) System.out.println("No assignments found.");
                    }
                    case 3 -> {
                        Long id = readLong("Enter Assignment ID: ");
                        if (id == null) break;
                        RefereeAssignment a = refereeAssignmentService.getAssignment(id);
                        if (a == null) {
                            System.out.println("Assignment not found.");
                        } else {
                            System.out.println(
                                "ID: " + a.getId()
                                + " | Match: " + (a.getMatch() == null ? "N/A" : a.getMatch().getTitle())
                                + " | Referee: " + (a.getReferee() == null || a.getReferee().getPlayer() == null
                                    ? "N/A" : a.getReferee().getPlayer().getName())
                                + " | Role: " + a.getRole()
                                + " | Assigned At: " + a.getAssignedAt());
                        }
                    }
                    case 4 -> {
                        Long id = readLong("Enter Assignment ID to update: ");
                        if (id == null) break;
                        RefereeAssignment a = refereeAssignmentService.getAssignment(id);
                        if (a == null) {
                            System.out.println("Assignment not found.");
                            break;
                        }

                        Long matchId = readLong("Enter new Match ID: ");
                        if (matchId == null) break;
                        Long refereeId = readLong("Enter new Referee ID: ");
                        if (refereeId == null) break;

                        // Resolve the related entities through the services.
                        Match match = matchService.getMatch(matchId);
                        Referee referee = refereeService.getReferee(refereeId);
                        if (match == null || referee == null) {
                            System.out.println("Match or referee not found.");
                            break;
                        }

                        System.out.print("Enter new role (MAIN_REFEREE/ASSISTANT_REFEREE/LINE_JUDGE): ");
                        String role = sc.nextLine().trim().toUpperCase();
                        if (!List.of("MAIN_REFEREE", "ASSISTANT_REFEREE", "LINE_JUDGE").contains(role)) {
                            System.out.println("Invalid role.");
                            break;
                        }

                        a.setMatch(match);
                        a.setReferee(referee);
                        a.setRole(role);
                        refereeAssignmentService.updateAssignment(a);
                        System.out.println("Assignment updated successfully.");
                    }
                    case 5 -> {
                        Long id = readLong("Enter Assignment ID to delete: ");
                        if (id == null) break;
                        refereeAssignmentService.deleteAssignment(id);
                        System.out.println("Assignment deleted successfully.");
                    }
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (RuntimeException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }


    // ================= MATCH RESULT MENU =================

    private static void matchResultMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n===== MATCH RESULT MANAGEMENT =====");
            System.out.println("1. Record Match Result");
            System.out.println("2. View All Results");
            System.out.println("3. Find Result By ID");
            System.out.println("4. Update Result");
            System.out.println("5. Delete Result");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            int choice = readInt();

            try {
                switch (choice) {
                    case 1 -> {
                        Long matchId = readLong("Enter Match ID: ");
                        if (matchId == null) break;
                        System.out.print("Enter home score: ");
                        int home = readInt();
                        System.out.print("Enter away score: ");
                        int away = readInt();
                        if (home < 0 || away < 0) {
                            System.out.println("Scores must be non-negative.");
                            break;
                        }
                        System.out.print("Enter remarks (optional): ");
                        String remarks = sc.nextLine();
                        matchResultService.recordResult(matchId, home, away, remarks);
                        System.out.println("Match result recorded successfully.");
                    }
                    case 2 -> {
                        List<MatchResult> results = matchResultService.getAllResults();
                        for (MatchResult r : results) {
                            System.out.println(
                                "ID: " + r.getId()
                                + " | Match: " + (r.getMatch() == null ? "N/A" : r.getMatch().getTitle())
                                + " | Home: " + r.getHomeScore()
                                + " | Away: " + r.getAwayScore()
                                + " | Outcome: " + r.getOutcome()
                                + " | Recorded At: " + r.getRecordedAt());
                        }
                        if (results.isEmpty()) System.out.println("No match results found.");
                    }
                    case 3 -> {
                        Long id = readLong("Enter Result ID: ");
                        if (id == null) break;
                        MatchResult r = matchResultService.getResult(id);
                        if (r == null) {
                            System.out.println("Match result not found.");
                        } else {
                            System.out.println(
                                "ID: " + r.getId()
                                + " | Match: " + (r.getMatch() == null ? "N/A" : r.getMatch().getTitle())
                                + " | Home: " + r.getHomeScore()
                                + " | Away: " + r.getAwayScore()
                                + " | Outcome: " + r.getOutcome()
                                + " | Remarks: " + r.getRemarks());
                        }
                    }
                    case 4 -> {
                        Long id = readLong("Enter Result ID to update: ");
                        if (id == null) break;
                        MatchResult r = matchResultService.getResult(id);
                        if (r == null) {
                            System.out.println("Match result not found.");
                            break;
                        }

                        System.out.println("Current home score: " + r.getHomeScore());
                        System.out.print("Enter new home score: ");
                        int home = readInt();
                        System.out.println("Current away score: " + r.getAwayScore());
                        System.out.print("Enter new away score: ");
                        int away = readInt();
                        if (home < 0 || away < 0) {
                            System.out.println("Scores must be non-negative.");
                            break;
                        }
                        System.out.print("Enter new remarks: ");
                        r.setRemarks(sc.nextLine());
                        r.setHomeScore(home);
                        r.setAwayScore(away);
                        matchResultService.updateResult(r);
                        System.out.println("Match result updated successfully.");
                    }
                    case 5 -> {
                        Long id = readLong("Enter Result ID to delete: ");
                        if (id == null) break;
                        matchResultService.deleteResult(id);
                        System.out.println("Match result deleted successfully.");
                    }
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (RuntimeException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }


    // ================= TOURNAMENT MENU =================

    private static void tournamentMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n===== TOURNAMENT MANAGEMENT =====");
            System.out.println("1. Create Tournament");
            System.out.println("2. View All Tournaments");
            System.out.println("3. Find Tournament By ID");
            System.out.println("4. Update Tournament");
            System.out.println("5. Delete Tournament");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            int choice = readInt();

            try {
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter tournament name: ");
                        String name = sc.nextLine().trim();
                        Long sportId = readLong("Enter Sport ID: ");
                        if (sportId == null) break;
                        Long organizerId = readLong("Enter Organizer User ID: ");
                        if (organizerId == null) break;
                        Long venueId = readLong("Enter Venue ID (0 if not decided): ");
                        if (venueId == null) break;
                        if (venueId == 0L) venueId = null;

                        System.out.print("Enter registration start date (YYYY-MM-DD): ");
                        LocalDate registrationStart = LocalDate.parse(sc.nextLine().trim());
                        System.out.print("Enter registration end date (YYYY-MM-DD): ");
                        LocalDate registrationEnd = LocalDate.parse(sc.nextLine().trim());
                        System.out.print("Enter tournament start date (YYYY-MM-DD): ");
                        LocalDate startDate = LocalDate.parse(sc.nextLine().trim());
                        System.out.print("Enter tournament end date (YYYY-MM-DD): ");
                        LocalDate endDate = LocalDate.parse(sc.nextLine().trim());

                        System.out.print("Enter maximum participants: ");
                        int maxParticipants = readInt();
                        System.out.print("Enter format (KNOCKOUT/LEAGUE/ROUND_ROBIN): ");
                        String format = sc.nextLine().trim().toUpperCase();
                        System.out.print("Enter description: ");
                        String description = sc.nextLine();

                        tournamentService.createTournament(
                            name, sportId, organizerId, venueId,
                            registrationStart, registrationEnd,
                            startDate, endDate, maxParticipants, format, description);
                        System.out.println("Tournament created successfully.");
                    }
                    case 2 -> {
                        List<Tournament> tournaments = tournamentService.getAllTournaments();
                        for (Tournament t : tournaments) {
                            System.out.println(
                                "ID: " + t.getId()
                                + " | Name: " + t.getName()
                                + " | Sport: " + (t.getSport() == null ? "N/A" : t.getSport().getName())
                                + " | Organizer: " + (t.getOrganizer() == null ? "N/A" : t.getOrganizer().getName())
                                + " | Venue: " + (t.getVenue() == null ? "Not assigned" : t.getVenue().getName())
                                + " | Dates: " + t.getStartDate() + " to " + t.getEndDate()
                                + " | Format: " + t.getFormat()
                                + " | Status: " + t.getStatus());
                        }
                        if (tournaments.isEmpty()) System.out.println("No tournaments found.");
                    }
                    case 3 -> {
                        Long id = readLong("Enter Tournament ID: ");
                        if (id == null) break;
                        Tournament t = tournamentService.getTournament(id);
                        if (t == null) {
                            System.out.println("Tournament not found.");
                        } else {
                            System.out.println(
                                "ID: " + t.getId()
                                + " | Name: " + t.getName()
                                + " | Sport: " + (t.getSport() == null ? "N/A" : t.getSport().getName())
                                + " | Organizer: " + (t.getOrganizer() == null ? "N/A" : t.getOrganizer().getName())
                                + " | Venue: " + (t.getVenue() == null ? "Not assigned" : t.getVenue().getName())
                                + " | Registration: " + t.getRegistrationStartDate() + " to " + t.getRegistrationEndDate()
                                + " | Tournament: " + t.getStartDate() + " to " + t.getEndDate()
                                + " | Max participants: " + t.getMaximumParticipants()
                                + " | Format: " + t.getFormat()
                                + " | Status: " + t.getStatus()
                                + " | Description: " + t.getDescription());
                        }
                    }
                    case 4 -> {
                        Long id = readLong("Enter Tournament ID to update: ");
                        if (id == null) break;
                        Tournament t = tournamentService.getTournament(id);
                        if (t == null) {
                            System.out.println("Tournament not found.");
                            break;
                        }

                        System.out.println("Current name: " + t.getName());
                        System.out.print("Enter new name: ");
                        t.setName(sc.nextLine().trim());

                        System.out.println("Current registration start: " + t.getRegistrationStartDate());
                        System.out.print("Enter new registration start (YYYY-MM-DD): ");
                        t.setRegistrationStartDate(LocalDate.parse(sc.nextLine().trim()));
                        System.out.println("Current registration end: " + t.getRegistrationEndDate());
                        System.out.print("Enter new registration end (YYYY-MM-DD): ");
                        t.setRegistrationEndDate(LocalDate.parse(sc.nextLine().trim()));

                        System.out.println("Current tournament start: " + t.getStartDate());
                        System.out.print("Enter new tournament start (YYYY-MM-DD): ");
                        t.setStartDate(LocalDate.parse(sc.nextLine().trim()));
                        System.out.println("Current tournament end: " + t.getEndDate());
                        System.out.print("Enter new tournament end (YYYY-MM-DD): ");
                        t.setEndDate(LocalDate.parse(sc.nextLine().trim()));

                        System.out.println("Current maximum participants: " + t.getMaximumParticipants());
                        System.out.print("Enter new maximum participants: ");
                        int max = readInt();
                        if (max < 2) {
                            System.out.println("Maximum participants must be at least 2.");
                            break;
                        }
                        t.setMaximumParticipants(max);

                        System.out.println("Current format: " + t.getFormat());
                        System.out.print("Enter new format (KNOCKOUT/LEAGUE/ROUND_ROBIN): ");
                        String format = sc.nextLine().trim().toUpperCase();
                        if (!List.of("KNOCKOUT", "LEAGUE", "ROUND_ROBIN").contains(format)) {
                            System.out.println("Invalid tournament format.");
                            break;
                        }
                        t.setFormat(format);

                        System.out.println("Current status: " + t.getStatus());
                        System.out.print("Enter new status (UPCOMING/REGISTRATION_OPEN/ONGOING/COMPLETED/CANCELLED): ");
                        String status = sc.nextLine().trim().toUpperCase();
                        if (!List.of("UPCOMING", "REGISTRATION_OPEN", "ONGOING", "COMPLETED", "CANCELLED").contains(status)) {
                            System.out.println("Invalid tournament status.");
                            break;
                        }
                        t.setStatus(status);
                        System.out.print("Enter new description: ");
                        t.setDescription(sc.nextLine());

                        tournamentService.updateTournament(t);
                        System.out.println("Tournament updated successfully.");
                    }
                    case 5 -> {
                        Long id = readLong("Enter Tournament ID to delete: ");
                        if (id == null) break;
                        tournamentService.deleteTournament(id);
                        System.out.println("Tournament deleted successfully.");
                    }
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (RuntimeException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }


    // ================= TOURNAMENT PARTICIPANT MENU =================

    private static void tournamentParticipantMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n===== TOURNAMENT PARTICIPANT MANAGEMENT =====");
            System.out.println("1. Register Individual Player");
            System.out.println("2. Register Team");
            System.out.println("3. View All Participants");
            System.out.println("4. Find Participant By ID");
            System.out.println("5. Update Participant Status / Seed");
            System.out.println("6. Delete Participant");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            int choice = readInt();

            try {
                switch (choice) {
                    case 1 -> {
                        Long tournamentId = readLong("Enter Tournament ID: ");
                        if (tournamentId == null) break;
                        Long playerId = readLong("Enter Player ID: ");
                        if (playerId == null) break;
                        tournamentParticipantService.registerPlayer(tournamentId, playerId);
                        System.out.println("Player registered for tournament successfully.");
                    }
                    case 2 -> {
                        Long tournamentId = readLong("Enter Tournament ID: ");
                        if (tournamentId == null) break;
                        Long teamId = readLong("Enter Team ID: ");
                        if (teamId == null) break;
                        tournamentParticipantService.registerTeam(tournamentId, teamId);
                        System.out.println("Team registered for tournament successfully.");
                    }
                    case 3 -> {
                        List<TournamentParticipant> participants =
                                tournamentParticipantService.getAllParticipants();
                        for (TournamentParticipant p : participants) {
                            String name = p.getPlayer() != null
                                    ? p.getPlayer().getName()
                                    : p.getTeam() != null ? p.getTeam().getName() : "Unknown";
                            String type = p.getPlayer() != null ? "PLAYER"
                                    : p.getTeam() != null ? "TEAM" : "INVALID";
                            System.out.println(
                                "ID: " + p.getId()
                                + " | Tournament: " + (p.getTournament() == null ? "N/A" : p.getTournament().getName())
                                + " | Type: " + type
                                + " | Participant: " + name
                                + " | Status: " + p.getStatus()
                                + " | Seed: " + p.getSeedNumber()
                                + " | Registered: " + p.getRegisteredAt());
                        }
                        if (participants.isEmpty()) System.out.println("No tournament participants found.");
                    }
                    case 4 -> {
                        Long id = readLong("Enter Tournament Participant ID: ");
                        if (id == null) break;
                        TournamentParticipant p = tournamentParticipantService.getParticipant(id);
                        if (p == null) {
                            System.out.println("Tournament participant not found.");
                        } else {
                            String name = p.getPlayer() != null
                                    ? p.getPlayer().getName()
                                    : p.getTeam() != null ? p.getTeam().getName() : "Unknown";
                            System.out.println(
                                "ID: " + p.getId()
                                + " | Tournament: " + (p.getTournament() == null ? "N/A" : p.getTournament().getName())
                                + " | Participant: " + name
                                + " | Status: " + p.getStatus()
                                + " | Seed: " + p.getSeedNumber()
                                + " | Registered: " + p.getRegisteredAt());
                        }
                    }
                    case 5 -> {
                        Long id = readLong("Enter Tournament Participant ID to update: ");
                        if (id == null) break;
                        TournamentParticipant p = tournamentParticipantService.getParticipant(id);
                        if (p == null) {
                            System.out.println("Tournament participant not found.");
                            break;
                        }

                        System.out.println("Current status: " + p.getStatus());
                        System.out.print("Enter new status (REGISTERED/CONFIRMED/WITHDRAWN/DISQUALIFIED): ");
                        String status = sc.nextLine().trim().toUpperCase();
                        if (!List.of("REGISTERED", "CONFIRMED", "WITHDRAWN", "DISQUALIFIED").contains(status)) {
                            System.out.println("Invalid participant status.");
                            break;
                        }

                        System.out.println("Current seed number: " + p.getSeedNumber());
                        System.out.print("Enter seed number (0 or blank for no seed): ");
                        String seedInput = sc.nextLine().trim();
                        Integer seed = null;
                        if (!seedInput.isEmpty() && !seedInput.equals("0")) {
                            try {
                                seed = Integer.parseInt(seedInput);
                            } catch (NumberFormatException ex) {
                                System.out.println("Invalid seed number.");
                                break;
                            }
                            if (seed < 1) {
                                System.out.println("Seed number must be positive.");
                                break;
                            }
                        }

                        p.setStatus(status);
                        p.setSeedNumber(seed);
                        tournamentParticipantService.updateParticipant(p);
                        System.out.println("Tournament participant updated successfully.");
                    }
                    case 6 -> {
                        Long id = readLong("Enter Tournament Participant ID to delete: ");
                        if (id == null) break;
                        tournamentParticipantService.deleteParticipant(id);
                        System.out.println("Tournament participant deleted successfully.");
                    }
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (RuntimeException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }


    // ================= TOURNAMENT MATCH MENU =================

    private static void tournamentMatchMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n===== TOURNAMENT MATCH MANAGEMENT =====");
            System.out.println("1. Add Existing Match To Tournament");
            System.out.println("2. View All Tournament Matches");
            System.out.println("3. Find Tournament Match By ID");
            System.out.println("4. Update Tournament Match");
            System.out.println("5. Remove Match From Tournament");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            int choice = readInt();

            try {
                switch (choice) {
                    case 1 -> {
                        Long tournamentId = readLong("Enter Tournament ID: ");
                        if (tournamentId == null) break;
                        Long matchId = readLong("Enter existing Match ID: ");
                        if (matchId == null) break;

                        System.out.print("Enter round number (1, 2, 3...): ");
                        int roundNumber = readInt();
                        System.out.print("Enter match number within tournament: ");
                        int matchNumber = readInt();
                        if (roundNumber < 1 || matchNumber < 1) {
                            System.out.println("Round and match numbers must be positive.");
                            break;
                        }

                        System.out.print("Enter round name (e.g. Quarter-final/Final): ");
                        String roundName = sc.nextLine().trim();
                        System.out.print("Enter bracket position (optional): ");
                        String bracket = sc.nextLine().trim();
                        if (bracket.isEmpty()) bracket = null;

                        tournamentMatchService.addMatch(
                            tournamentId, matchId, roundNumber, matchNumber,
                            roundName.isEmpty() ? null : roundName, bracket);
                        System.out.println("Match added to tournament successfully.");
                    }
                    case 2 -> {
                        List<TournamentMatch> records =
                                tournamentMatchService.getAllTournamentMatches();
                        for (TournamentMatch tm : records) {
                            System.out.println(
                                "ID: " + tm.getId()
                                + " | Tournament: " + (tm.getTournament() == null ? "N/A" : tm.getTournament().getName())
                                + " | Match: " + (tm.getMatch() == null ? "N/A" : tm.getMatch().getTitle())
                                + " | Round: " + tm.getRoundNumber()
                                + " (" + tm.getRoundName() + ")"
                                + " | Match number: " + tm.getMatchNumber()
                                + " | Bracket: " + tm.getBracketPosition()
                                + " | Status: " + tm.getStatus());
                        }
                        if (records.isEmpty()) System.out.println("No tournament matches found.");
                    }
                    case 3 -> {
                        Long id = readLong("Enter Tournament Match ID: ");
                        if (id == null) break;
                        TournamentMatch tm = tournamentMatchService.getTournamentMatch(id);
                        if (tm == null) {
                            System.out.println("Tournament match not found.");
                        } else {
                            System.out.println(
                                "ID: " + tm.getId()
                                + " | Tournament: " + (tm.getTournament() == null ? "N/A" : tm.getTournament().getName())
                                + " | Match: " + (tm.getMatch() == null ? "N/A" : tm.getMatch().getTitle())
                                + " | Round: " + tm.getRoundNumber()
                                + " (" + tm.getRoundName() + ")"
                                + " | Match number: " + tm.getMatchNumber()
                                + " | Bracket: " + tm.getBracketPosition()
                                + " | Status: " + tm.getStatus());
                        }
                    }
                    case 4 -> {
                        Long id = readLong("Enter Tournament Match ID to update: ");
                        if (id == null) break;
                        TournamentMatch tm = tournamentMatchService.getTournamentMatch(id);
                        if (tm == null) {
                            System.out.println("Tournament match not found.");
                            break;
                        }

                        System.out.println("Current round number: " + tm.getRoundNumber());
                        System.out.print("Enter new round number: ");
                        int round = readInt();
                        System.out.println("Current match number: " + tm.getMatchNumber());
                        System.out.print("Enter new match number: ");
                        int number = readInt();
                        if (round < 1 || number < 1) {
                            System.out.println("Round and match numbers must be positive.");
                            break;
                        }

                        System.out.print("Enter new round name: ");
                        String roundName = sc.nextLine().trim();
                        System.out.print("Enter new bracket position (optional): ");
                        String bracket = sc.nextLine().trim();
                        if (bracket.isEmpty()) bracket = null;

                        System.out.print("Enter status (SCHEDULED/IN_PROGRESS/COMPLETED/CANCELLED): ");
                        String status = sc.nextLine().trim().toUpperCase();
                        if (!List.of("SCHEDULED", "IN_PROGRESS", "COMPLETED", "CANCELLED").contains(status)) {
                            System.out.println("Invalid tournament match status.");
                            break;
                        }

                        tm.setRoundNumber(round);
                        tm.setMatchNumber(number);
                        tm.setRoundName(roundName.isEmpty() ? null : roundName);
                        tm.setBracketPosition(bracket);
                        tm.setStatus(status);

                        tournamentMatchService.updateTournamentMatch(tm);
                        System.out.println("Tournament match updated successfully.");
                    }
                    case 5 -> {
                        Long id = readLong("Enter Tournament Match ID to remove: ");
                        if (id == null) break;
                        tournamentMatchService.deleteTournamentMatch(id);
                        System.out.println("Match removed from tournament successfully.");
                    }
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (RuntimeException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }


}
