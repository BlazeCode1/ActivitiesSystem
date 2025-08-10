package org.example.activitiessystem.Service;


import lombok.RequiredArgsConstructor;
import org.example.activitiessystem.Api.ApiException;
import org.example.activitiessystem.Model.Place;
import org.example.activitiessystem.Model.Ratings;
import org.example.activitiessystem.Model.User;
import org.example.activitiessystem.Repository.PlaceRepository;
import org.example.activitiessystem.Repository.RatingsRepository;
import org.example.activitiessystem.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingsService {
    private final RatingsRepository ratingsRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    public List<Ratings> getRatings(){
        return ratingsRepository.findAll();
    }

    public void createRating(Ratings rating){
        User user = userRepository.findUserById(rating.getUserId());
        if(user == null)
            throw new ApiException("User Not Found");

        Place place = placeRepository.findPlaceById(rating.getPlaceId());
        if(place == null)
            throw new ApiException("Place Not Found With given id");
        updatePlaceAverageRating(rating.getPlaceId());
        ratingsRepository.save(rating);
    }

    public void updateRating(Integer id, Ratings updateRating){
        Ratings oldRating = ratingsRepository.findRatingsById(id);
        if(oldRating == null)
            throw new ApiException("Rating id Not Found");

        User user = userRepository.findUserById(updateRating.getUserId());
        if(user == null)
            throw new ApiException("User Not Found");

        Place place = placeRepository.findPlaceById(updateRating.getPlaceId());
        if(place == null)
            throw new ApiException("Place Not Found With given id");

        oldRating.setUserId(updateRating.getUserId());
        oldRating.setPlaceId(updateRating.getPlaceId());
        oldRating.setScore(updateRating.getScore());
        oldRating.setScore_cleanliness(updateRating.getScore_cleanliness());
        oldRating.setScore_service(updateRating.getScore_service());
        oldRating.setScore_price(updateRating.getScore_price());
        oldRating.setComment(updateRating.getComment());

        ratingsRepository.save(oldRating);
    }

    public void deleteRating(Integer id) {
        Ratings rating = ratingsRepository.findRatingsById(id);
        if(rating == null) {
            throw new ApiException("Rating not found");
        }
        ratingsRepository.delete(rating);
    }


    private void updatePlaceAverageRating(Integer placeId) {
        Place place = placeRepository.findPlaceById(placeId);
        if (place == null) {
            throw new ApiException("Place not found");
        }

        List<Ratings> placeRatings = ratingsRepository.findAllByPlaceId(placeId);

        if (placeRatings.isEmpty()) {
            place.setAvgRating(0.0);
        } else {
            double totalScore = 0.0;
            for (Ratings r : placeRatings) {
                double ratingAvg = (r.getScore() +
                        r.getScore_cleanliness() +
                        r.getScore_service() +
                        r.getScore_price()) / 4.0;
                totalScore += ratingAvg;
            }
            // Calculate overall average and round to 1 decimal place
            double avgRating = Math.round((totalScore / placeRatings.size()) * 10.0) / 10.0;
            place.setAvgRating(avgRating);
        }

        placeRepository.save(place);
    }


}
