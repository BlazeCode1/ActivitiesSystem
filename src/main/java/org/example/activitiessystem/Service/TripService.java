package org.example.activitiessystem.Service;

import lombok.RequiredArgsConstructor;
import org.example.activitiessystem.Api.ApiException;
import org.example.activitiessystem.Model.Place;
import org.example.activitiessystem.Model.Trip;
import org.example.activitiessystem.Model.User;
import org.example.activitiessystem.Repository.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PlaceRepository placeRepository;


    public List<Trip> getTrips(){
        return tripRepository.findAll();
    }

    public Trip getTrip(Integer id){
        Trip t = tripRepository.findTripById(id);
        if( t== null) throw new ApiException("Trip Not Found");
        return t;
    }
    public Trip getTripsByUser(Integer id){
        Trip t = tripRepository.findTripByUserId(id);
        if(t == null)
            throw new ApiException("Trip Not Found");
        return t;
    }

    public void createTrip(Trip trip){
        if(userRepository.findUserById(trip.getUserId())==null)
            throw new ApiException("User ID not found");

        if(categoryRepository.findCategoryById(trip.getCategoryId()) == null){
            throw new ApiException("Category Not Found For Trip");
        }
                if(trip.getSelectedPlaceId() != null){
                    Place place = placeRepository.findPlaceById(trip.getSelectedPlaceId());
                    if(place == null) throw new ApiException("Selected Place Not Found");
                    if(!place.getCategoryId().equals(trip.getCategoryId()))
                        throw new ApiException("Selected Place does not match Category");
                    place.setCount_visits(place.getCount_visits() + 1);
                    place.setCount_current_visitors(place.getCount_visits() + 1);
                }

        tripRepository.save(trip);
    }

    public void finishTrip(Integer tripId,Integer userId){
        Trip trip = tripRepository.findTripById(tripId);
        if(trip == null)
            throw new ApiException("Trip Not Found");
        User user = userRepository.findUserById(userId);
        if(user == null)
            throw new ApiException("User Not found");
        if(!userId.equals(trip.getUserId()))
            throw new ApiException("User Id Not Correct for trip id");

        Place place = placeRepository.findPlaceById(trip.getSelectedPlaceId());

        if(place == null)
            throw new ApiException("Trip did not select a place id");
        place.setCount_current_visitors(place.getCount_current_visitors() - 1);
        placeRepository.save(place);

    }

    public Map<String, String> shareTrip(Integer tripId) {
        Trip trip = tripRepository.findTripById(tripId);
        if (trip == null) throw new ApiException("Trip not found");

        // already shared? return existing link
        if (Boolean.TRUE.equals(trip.getIsPublic()) && trip.getShareToken() != null) {
            return Map.of("shareUrl", trip.getShareToken());
        }

        // generate unique token
        String token = generateUniqueToken();
        trip.setIsPublic(true);
        trip.setShareToken(token);
        trip.setPublishedAt(Instant.now());

        tripRepository.save(trip);

        return Map.of("shareUrl",  token);
    }

    private String generateUniqueToken() {
        String token;
        do {
            token = randomToken(8); // 8-char short code
        } while (tripRepository.existsByShareToken(token));
        return token;
    }

    private String randomToken(int len) {
        final String ALPHA = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // skip confusing chars
        ThreadLocalRandom r = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) sb.append(ALPHA.charAt(r.nextInt(ALPHA.length())));
        return sb.toString();
    }

    public void updateTrip(Integer id, Trip trip){
        Trip old = tripRepository.findTripById(id);
        if (old == null) throw new ApiException("Trip not found");
        if(trip.getCategoryId() == null || categoryRepository.findCategoryById(trip.getCategoryId()) == null){
            throw new ApiException("Category Not Found For Trip");
        }
        if(trip.getSelectedPlaceId() != null){
            Place place = placeRepository.findPlaceById(trip.getSelectedPlaceId());
            if(place == null) throw new ApiException("Selected Place Not Found");
            if(!place.getCategoryId().equals(trip.getCategoryId()))
                throw new ApiException("Selected Place does not match Category");
        }
        old.setSelectedPlaceId(trip.getSelectedPlaceId());
        old.setDistrict(trip.getDistrict());
        old.setImageUrl(trip.getImageUrl());
        old.setUserId(trip.getUserId());
        tripRepository.save(old);
    }

    public void deleteTrip(Integer id){
        Trip t = tripRepository.findTripById(id);
        if (t == null) throw new ApiException("Trip not found");
        tripRepository.delete(t);
    }
    public Trip getSharedTrip(String token) {
        Trip trip = tripRepository.findByShareToken(token);
        if (trip == null || !Boolean.TRUE.equals(trip.getIsPublic())) {
            throw new ApiException("Shared trip not found");
        }
        return trip;
    }


    public List<Trip> getSeasonalTrips(String district) {
        List<Integer> seasonalPlaceIds = placeRepository.findByDistrict(district).stream()
                .filter(p -> p.getCount_visits() > 50 || p.getCount_current_visitors() > 10)
                .map(Place::getId)
                .toList();

        return tripRepository.findBySelectedPlaceIdIn(seasonalPlaceIds);
    }



    private void ensureSubscriber(Integer userId){
        User u = userRepository.findUserById(userId);
        if (u == null) throw new ApiException("User not found");
        if (u.getIsSubscriber() == null || !u.getIsSubscriber())
            throw new ApiException("Subscription required");
    }


    public Place premiumMystery(Integer userId){
        ensureSubscriber(userId);

        User u = userRepository.findUserById(userId);
        String district = u.getDistrict();

        // Top-rated candidates in user’s district (favor partners first)
        List<Place> candidates = placeRepository.findTopMysteryCandidates(district);

        // Avoid places visited by this user in last 60 days
        Instant cutoff = Instant.now().minus(java.time.Duration.ofDays(60));

        for (Place p : candidates){
            boolean revisitedRecently = tripRepository
                    .existsByUserIdAndSelectedPlaceIdAndStartAfter(userId, p.getId(), cutoff);
            if (!revisitedRecently){
                return p; // first good match
            }
        }
        if (candidates.isEmpty()) throw new ApiException("No candidates found in your district");
        return candidates.get(0); // fallback
    }





}
