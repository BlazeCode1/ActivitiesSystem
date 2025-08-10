package org.example.activitiessystem.Controller;

import lombok.RequiredArgsConstructor;
import org.example.activitiessystem.Service.TripLeaderboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
public class TripLeaderboardController {

    private final TripLeaderboardService tripLeaderboardService;

    // GET /api/v1/trips/leaderboard/{district}
    @GetMapping("/leaderboard/{district}")
    public ResponseEntity<?> topTripsByDistrict(@PathVariable String district){
        return ResponseEntity.ok(tripLeaderboardService.topTripsByDistrict( district));
    }

    // GET /api/v1/trips/leaderboard{district}/{categoryId}
    @GetMapping("/leaderboard/{district}/{categoryId}")
    public ResponseEntity<?> topTripsByDistrictCategory(
                                                        @PathVariable String district,
                                                        @PathVariable Integer categoryId){
        return ResponseEntity.ok(tripLeaderboardService.topTripsByDistrictCategory( district, categoryId));
    }
}
