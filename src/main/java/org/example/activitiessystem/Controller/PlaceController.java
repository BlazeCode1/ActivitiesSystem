package org.example.activitiessystem.Controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.activitiessystem.Api.ApiResponse;
import org.example.activitiessystem.Model.Place;
import org.example.activitiessystem.Service.PlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("/get")
    public ResponseEntity<?> getPlaces(){
        return ResponseEntity.ok(placeService.getPlaces());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPlace(@Valid @RequestBody Place place){
    placeService.addPlace(place);
    return ResponseEntity.ok(new ApiResponse("Place Added Successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePlace(@PathVariable  Integer id,@Valid @RequestBody Place place){
        placeService.updatePlace(id,place);
        return ResponseEntity.ok(new ApiResponse("Place updated Successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePlace(@PathVariable Integer id){
        placeService.deletePlace(id);
        return ResponseEntity.ok(new ApiResponse("Place deleted Successfully"));
    }

    @GetMapping("/{placeId}")
    public ResponseEntity<?> getPlaceById(@PathVariable Integer placeId) {
        return ResponseEntity.ok(placeService.getPlaceById(placeId));
    }

    @GetMapping("/search/{district}/{categoryId}")
    public ResponseEntity<?> searchPlaces(@PathVariable String district, @PathVariable Integer categoryId) {
        return ResponseEntity.ok(placeService.searchPlaces(district, categoryId));
    }

    @GetMapping("/get/top/{userId}")
    public ResponseEntity<?> getTopPlacesByUserDistrict(@PathVariable Integer userId){
        return ResponseEntity.ok(placeService.getTopPlacesInUserDistrict(userId));
    }

    @GetMapping("/events/seasonal/{district}")
    public List<Place> getSeasonalPlaces(@PathVariable String district) {
        return placeService.getSeasonalPlaces(district);
    }

    @GetMapping("/discounts/{userId}/{district}")
    public ResponseEntity<?> subscriberDiscounts(@PathVariable Integer userId,
                                                 @PathVariable String district){
        return ResponseEntity.ok(placeService.subscriberDiscounts(userId, district));
    }

}
