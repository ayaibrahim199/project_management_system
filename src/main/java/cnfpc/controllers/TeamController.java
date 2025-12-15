package cnfpc.controllers;

import cnfpc.entities.Teams;
import cnfpc.entities.Users;
import cnfpc.services.TeamService;
import cnfpc.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;
    private final UserService userService;

    public TeamController(TeamService teamService, UserService userService) {
        this.teamService = teamService;
        this.userService = userService;
    }

    // List all teams
    @GetMapping
    public String listTeams(Model model) {
        model.addAttribute("teams", teamService.findAllTeams());
        return "teams/list";
    }

    // Show team details
    @GetMapping("/{id}")
    public String showTeamDetails(@PathVariable Long id, Model model) {
        Teams team = teamService.findTeamById(id);
        model.addAttribute("team", team);
        model.addAttribute("members", team.getMembers());
        model.addAttribute("projects", team.getProjects());
        model.addAttribute(
    "availableUsers",
    teamService.getAvailableUsersForTeam(id)
);
        return "teams/details";
    }

    // Show create team form
    @GetMapping("/new")
    public String showCreateTeamForm(Model model) {
        model.addAttribute("team", new Teams());
        model.addAttribute("users", userService.findAllUsers());
        return "teams/team_form";
    }

    // Save new team
    @PostMapping
    public String saveTeam(@RequestParam(required = false) Long teamLeadId,
                          @RequestParam(value = "memberIds", required = false) List<Long> memberIds,
                          @Valid @ModelAttribute("team") Teams team,
                          BindingResult result,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.findAllUsers());
            return "teams/team_form";
        }

        // Validate member selection
        if (memberIds == null || memberIds.isEmpty()) {
            model.addAttribute("users", userService.findAllUsers());
            model.addAttribute("memberError", "Please select at least one team member.");
            return "teams/team_form";
        }

        try {
            if (teamLeadId != null) {
                Users lead = userService.findUserById(teamLeadId);
                team.setTeamLead(lead);
            }
            // Fetch members from IDs
            List<Users> members = new java.util.ArrayList<>();
            if (memberIds != null) {
                for (Long id : memberIds) {
                    Users user = userService.findUserById(id);
                    if (!members.contains(user)) {
                        members.add(user);
                    }
                }
            }
            // Ensure team lead is a member
            if (team.getTeamLead() != null && !members.contains(team.getTeamLead())) {
                members.add(team.getTeamLead());
            }
            team.setMembers(members);
            Teams savedTeam = teamService.createTeamWithMembers(team.getName(), team.getDescription(), team.getTeamLead().getId(), members);
            redirectAttributes.addFlashAttribute("message", "Team created successfully!");
            return "redirect:/teams/" + savedTeam.getId();
        } catch (Exception e) {
            model.addAttribute("users", userService.findAllUsers());
            model.addAttribute("errors", java.util.List.of(e.getMessage()));
            return "teams/team_form";
        }
    }

    // Edit team
    @GetMapping("/edit/{id}")
    public String showEditTeamForm(@PathVariable Long id, Model model) {
        Teams team = teamService.findTeamById(id);
        model.addAttribute("team", team);
        model.addAttribute("users", userService.findAllUsers());
        return "teams/team_form";
    }

    // Update team
    @PostMapping("/{id}")
    public String updateTeam(@PathVariable Long id,
                            @RequestParam(required = false) Long teamLeadId,
                            @RequestParam(value = "memberIds", required = false) List<Long> memberIds,
                            @Valid @ModelAttribute("team") Teams teamForm,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.findAllUsers());
            return "teams/team_form";
        }
        if (memberIds == null || memberIds.isEmpty()) {
            model.addAttribute("users", userService.findAllUsers());
            model.addAttribute("memberError", "Please select at least one team member.");
            return "teams/team_form";
        }
        List<Users> members = new ArrayList<>();
        for (Long memberId : memberIds) {
            Users user = userService.findUserById(memberId);
            if (!members.contains(user)) {
                members.add(user);
            }
        }
        try {
            teamService.updateTeam(id, teamForm.getName(), teamForm.getDescription(), teamLeadId, members);
            redirectAttributes.addFlashAttribute("message", "Team updated successfully!");
            return "redirect:/teams/" + id;
        } catch (Exception e) {
            model.addAttribute("users", userService.findAllUsers());
            model.addAttribute("errors", java.util.List.of(e.getMessage()));
            return "teams/team_form";
        }
    }
    

    // Add member to team
    @PostMapping("/{teamId}/members/add")
    public String addMember(@PathVariable Long teamId,
                           @RequestParam Long userId,
                           RedirectAttributes redirectAttributes) {
        try {
            teamService.addMemberToTeam(teamId, userId);
            redirectAttributes.addFlashAttribute("message", "Member added to team!");
            return "redirect:/teams/" + teamId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add member: " + e.getMessage());
            return "redirect:/teams/" + teamId;
        }
    }

    // Remove member from team
    @PostMapping("/{teamId}/members/remove/{userId}")
    public String removeMember(@PathVariable Long teamId,
                              @PathVariable Long userId,
                              RedirectAttributes redirectAttributes) {
        try {
            teamService.removeMemberFromTeam(teamId, userId);
            redirectAttributes.addFlashAttribute("message", "Member removed from team!");
            return "redirect:/teams/" + teamId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/teams/" + teamId;
        }
    }

    // Delete team
    @PostMapping("/delete/{id}")
    public String deleteTeam(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            teamService.deleteTeamById(id);
            redirectAttributes.addFlashAttribute("message", "Team deleted successfully!");
            return "redirect:/teams";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete team: " + e.getMessage());
            return "redirect:/teams/" + id;
        }
    }
}
