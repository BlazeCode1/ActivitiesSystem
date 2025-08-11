package org.example.activitiessystem.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.activitiessystem.Api.ApiResponse;
import org.example.activitiessystem.Model.Trip;
import org.example.activitiessystem.Service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/trip")
@RequiredArgsConstructor
public class TripController {
    private final TripService tripService;

    @GetMapping("/get")
    public ResponseEntity<?> getTrips(){
        return ResponseEntity.ok(tripService.getTrips());
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<?> getTrip(@PathVariable Integer id){
        return ResponseEntity.ok(tripService.getTrip(id));
    }

    @GetMapping("/get/user/{userId}")
    public ResponseEntity<?> getTripsByUser(@PathVariable Integer userId){
        return ResponseEntity.ok(tripService.getTripsByUser(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTrip(@Valid @RequestBody Trip trip){
        tripService.createTrip(trip);
        return ResponseEntity.status(201).body(new ApiResponse("Trip Created Successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTrip(@PathVariable Integer id, @RequestBody Trip patch){
        tripService.updateTrip(id, patch);
        return ResponseEntity.ok(new ApiResponse("Trip Updated Successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTrip(@PathVariable Integer id){
        tripService.deleteTrip(id);
        return ResponseEntity.ok(new ApiResponse("Trip Deleted Successfully"));
    }

    @PostMapping("/{tripId}/share")
    public ResponseEntity<?> share(@PathVariable Integer tripId) {
        return ResponseEntity.ok(tripService.shareTrip(tripId));
    }

    // public view: GET /api/v1/trip/shared/{token}
    @GetMapping("/shared/{token}")
    public ResponseEntity<?> viewShared(@PathVariable String token) {
        return ResponseEntity.ok(tripService.getSharedTrip(token));
    }

    @GetMapping("/trips/seasonal/{district}")
    public ResponseEntity<?> getSeasonalTrips(@PathVariable String district) {
        return ResponseEntity.ok(tripService.getSeasonalTrips(district));
    }

    @GetMapping("/mystery/premium/{userId}")
    public ResponseEntity<?> premiumMystery(@PathVariable Integer userId){
        return ResponseEntity.ok(tripService.premiumMystery(userId));
    }


    @GetMapping("/finish/{tripId}/{userId}")
    public ResponseEntity<?> finishTrip(@PathVariable Integer tripId,@PathVariable Integer userId){
        tripService.finishTrip(tripId,userId);
        return ResponseEntity.ok(new ApiResponse("Trip Finished!"));
    }
}
