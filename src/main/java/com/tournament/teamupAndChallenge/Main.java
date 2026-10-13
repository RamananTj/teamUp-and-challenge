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
    
    
}
