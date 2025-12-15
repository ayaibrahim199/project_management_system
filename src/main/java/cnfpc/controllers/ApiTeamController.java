package cnfpc.controllers;

import cnfpc.entities.Teams;
import cnfpc.services.TeamService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teams")
public class ApiTeamController {

    private final TeamService teamService;

    public ApiTeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    // Simple DTO to shape JSON returned to the frontend
    public static class TeamDto {
        public Long id;
        public String name;
        public String description;
        public int members;
        public int projects;
        public String lead;

        public TeamDto(Teams t) {
            this.id = t.getId();
            this.name = t.getName();
            this.description = t.getDescription();
            this.members = t.getMemberCount();
            this.projects = t.getProjects() == null ? 0 : t.getProjects().size();
            this.lead = t.getTeamLead() == null ? null : t.getTeamLead().getUsername();
        }
    }

    @GetMapping
    public List<TeamDto> listTeams() {
        List<Teams> teams = teamService.findAllTeams();
        return teams.stream().map(TeamDto::new).collect(Collectors.toList());
    }
}
