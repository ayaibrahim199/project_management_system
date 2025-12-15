package cnfpc.services;

import cnfpc.entities.Teams;
import cnfpc.entities.Users;
import cnfpc.repositories.TeamRepository;
import cnfpc.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    // Get all teams
    public List<Teams> findAllTeams() {
        return teamRepository.findAll();
    }

    // Get team by ID
    public Teams findTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with ID: " + id));
    }

    // Get team by name
    public Optional<Teams> findTeamByName(String name) {
        return teamRepository.findByName(name);
    }

    // Get teams for a specific user
    public List<Teams> getTeamsForUser(Long userId) {
        return teamRepository.findTeamsByMemberId(userId);
    }

    // Get teams led by a user
    public List<Teams> getTeamsLedBy(Long userId) {
        return teamRepository.findByTeamLeadId(userId);
    }

    // Create a new team with custom members
    public Teams createTeamWithMembers(String name, String description, Long teamLeadId, List<Users> members) {
        if (teamRepository.findByName(name).isPresent()) {
            throw new RuntimeException("Team with name '" + name + "' already exists");
        }
        Users teamLead = userRepository.findById(teamLeadId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + teamLeadId));
        Teams team = new Teams(name, description, teamLead);
        // Ensure team lead is a member
        if (!members.contains(teamLead)) {
            members.add(teamLead);
        }
        team.setMembers(members);
        team.setCreatedAt(LocalDateTime.now());
        team.setUpdatedAt(LocalDateTime.now());
        return teamRepository.save(team);
    }

    // Create a new team
    public Teams createTeam(String name, String description, Long teamLeadId) {
        if (teamRepository.findByName(name).isPresent()) {
            throw new RuntimeException("Team with name '" + name + "' already exists");
        }

        Users teamLead = userRepository.findById(teamLeadId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + teamLeadId));

        Teams team = new Teams(name, description, teamLead);
        // Add team lead as a member
        team.setMembers(new ArrayList<>());
        team.getMembers().add(teamLead);
        return teamRepository.save(team);
    }

    // Create a new team with custom members

    // Update team (robust, single method)
    public Teams updateTeam(Long id, String name, String description, Long teamLeadId, List<Users> members) {
        Teams team = findTeamById(id);
        if (team == null) {
            throw new IllegalArgumentException("Team not found");
        }
        // Check for name uniqueness (exclude self)
        Optional<Teams> existing = teamRepository.findByName(name);
        if (existing.isPresent() && !existing.get().getId().equals(id)) {
            throw new IllegalArgumentException("A team with this name already exists");
        }
        team.setName(name);
        team.setDescription(description);
        if (teamLeadId != null) {
            Users lead = userRepository.findById(teamLeadId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + teamLeadId));
            team.setTeamLead(lead);
        }
        if (members != null && !members.isEmpty()) {
            // Ensure team lead is a member
            if (team.getTeamLead() != null && !members.contains(team.getTeamLead())) {
                members.add(team.getTeamLead());
            }
            team.setMembers(members);
        }
        team.setUpdatedAt(LocalDateTime.now());
        return teamRepository.save(team);
    }

     
    public List<Users> getAvailableUsersForTeam(Long teamId) {

    Teams team = findTeamById(teamId);

    List<Users> allUsers = userRepository.findAll();
    List<Users> teamMembers = team.getMembers();

    List<Users> availableUsers = new ArrayList<>();

    for (Users user : allUsers) {
        if (!teamMembers.contains(user)) {
            availableUsers.add(user);
        }
    }

    return availableUsers;
}

    // Save or update team
    public Teams saveTeam(Teams team) {
        team.setUpdatedAt(LocalDateTime.now());
        return teamRepository.save(team);
    }

    // Add member to team
    public Teams addMemberToTeam(Long teamId, Long userId) {
        Teams team = findTeamById(teamId);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        if (team.isMember(user)) {
            throw new RuntimeException("User is already a member of this team");
        }

        team.getMembers().add(user);
        team.setUpdatedAt(LocalDateTime.now());
        return teamRepository.save(team);
    }

    // Remove member from team
    public Teams removeMemberFromTeam(Long teamId, Long userId) {
        Teams team = findTeamById(teamId);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Prevent removing the team lead
        if (team.getTeamLead().getId().equals(userId)) {
            throw new RuntimeException("Cannot remove the team lead from the team");
        }

        team.getMembers().remove(user);
        team.setUpdatedAt(LocalDateTime.now());
        return teamRepository.save(team);
    }

    // Check if user is member of team
    public boolean isUserMemberOfTeam(Long teamId, Long userId) {
        return teamRepository.isUserMemberOfTeam(teamId, userId);
    }

    // Delete team
    public void deleteTeamById(Long id) {
        teamRepository.deleteById(id);
    }
}
