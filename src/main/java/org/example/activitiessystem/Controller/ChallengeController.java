package org.example.activitiessystem.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.activitiessystem.Api.ApiResponse;
import org.example.activitiessystem.Model.Challenge;
import org.example.activitiessystem.Service.ChallengeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping("/create/{adminId}")
    public ResponseEntity<?> createChallenge(@PathVariable Integer adminId,@Valid @RequestBody Challenge challenge){
        challengeService.addChallenge(adminId,challenge);
        return ResponseEntity.ok(new ApiResponse("Challenge Created"));
    }

    @PostMapping("/current/{district}")
    public ResponseEntity<?> getCurrentChallenges(@PathVariable String district) {
        return ResponseEntity.ok(challengeService.getCurrentChallenges(district));
    }

    @PostMapping("/complete/{userId}/{challengeId}")
    public ResponseEntity<String> completeChallenge(@PathVariable Integer userId, @PathVariable Integer challengeId) {
        return ResponseEntity.ok(challengeService.completeChallenge(userId, challengeId));
    }

    @PutMapping("/status/{adminId}/{challengeId}/{isActive}")
    public ResponseEntity<?> changeChallengeStatus(@PathVariable Integer adminId, @PathVariable Integer challengeId, @PathVariable Boolean isActive) {
        challengeService.changeChallengeStatus(adminId, challengeId, isActive);
        return ResponseEntity.ok(new ApiResponse("Challenge status updated successfully"));
    }

}
