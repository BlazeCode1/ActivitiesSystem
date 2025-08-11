package org.example.activitiessystem.Service;

import lombok.RequiredArgsConstructor;
import org.example.activitiessystem.Api.ApiException;
import org.example.activitiessystem.Model.Category;
import org.example.activitiessystem.Model.Place;
import org.example.activitiessystem.Model.User;
import org.example.activitiessystem.Repository.CategoryRepository;
import org.example.activitiessystem.Repository.PlaceRepository;
import org.example.activitiessystem.Repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor

public class PlaceService {

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;


    public List<Place> getPlaces(){
        return placeRepository.findAll();
    }

    public Place getPlaceById(Integer id){
        Place place = placeRepository.findPlaceById(id);
        if(place == null)
            throw new ApiException("Place Not Found");
        return place;
    }
    public List<Place> getTopPlacesInUserDistrict(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) throw new ApiException("User not found");
        return placeRepository.findTopPlacesByDistrict(user.getDistrict(), PageRequest.of(0, 5));
    }

    public List<Place> searchPlaces(String district, Integer categoryId) {
        if (district == null && categoryId == null) {
            return placeRepository.findAll(Sort.by(Sort.Direction.DESC, "avgRating"));
        } else if (district != null && categoryId == null) {
            return placeRepository.findByDistrictIgnoreCaseOrderByAvgRatingAsc(district);
        } else if (district == null) {
            return placeRepository.findByCategoryIdOrderByAvgRatingDesc(categoryId);
        }
        return placeRepository.findByDistrictIgnoreCaseAndCategoryIdOrderByAvgRatingDesc(district, categoryId);
    }

    public void addPlace(Place place){
        // Normalize
        place.setName(place.getName().trim());
        place.setDistrict(place.getDistrict().trim());
        if (place.getContact_number() != null) place.setContact_number(place.getContact_number().trim());
        if (place.getLocation() != null) place.setLocation(place.getLocation().trim());

        // Check refs
        if (categoryRepository.findCategoryById(place.getCategoryId()) == null)
            throw new ApiException("Category not found");

        User user = userRepository.findUserById(place.getCreated_by_id());
        if (user == null) throw new ApiException("User not found");
        if (!"admin".equalsIgnoreCase(user.getRole()))
            throw new ApiException("Unauthorized: only admin can add places");

        // Business validations
        if (place.getOpen_time() != null && place.getClose_time() != null) {
            // allow same-day only; if you support overnight, handle that separately
            if (!place.getClose_time().isAfter(place.getOpen_time()))
                throw new ApiException("close_time must be after open_time");
        }

        // Price level whitelist (defense in depth)
        String pl = place.getPrice_level();
        if (pl == null || !(pl.equalsIgnoreCase("cheap") ||
                pl.equalsIgnoreCase("medium") ||
                pl.equalsIgnoreCase("expensive"))) {
            throw new ApiException("Invalid price_level (cheap|medium|expensive)");
        }

        // Duplicate checks (case-insensitive)
        if (placeRepository.existsByNameIgnoreCaseAndDistrictIgnoreCase(place.getName(), place.getDistrict()))
            throw new ApiException("Place already added (name,district)");

        if (place.getLocation() != null &&
                placeRepository.existsByLocation(place.getLocation()))
            throw new ApiException("Place already added (location)");

        // Defaults
        if (place.getAvgRating() == null) place.setAvgRating(0.0);
        if (place.getCount_visits() == null) place.setCount_visits(0);
        if (place.getCount_current_visitors() == null) place.setCount_current_visitors(0);

        // Persist
        placeRepository.save(place);
    }



    public void updatePlace(Integer id,Place place){
        Place oldPlace = placeRepository.findPlaceById(id);

        if(oldPlace == null)
            throw new ApiException("Place Not Found");

        oldPlace.setName(place.getName());
        oldPlace.setDescription(place.getDescription());
        oldPlace.setDistrict(place.getDistrict());
        oldPlace.setCategoryId(place.getCategoryId());
        oldPlace.setLocation(place.getLocation());
        oldPlace.setOpen_time(place.getOpen_time());
        oldPlace.setClose_time(place.getClose_time());
        oldPlace.setContact_number(place.getContact_number());
        oldPlace.setImage_url(place.getImage_url());
        oldPlace.setPrice_level(place.getPrice_level());
        placeRepository.save(oldPlace);
    }

    public void deletePlace(Integer id){
        Place place = placeRepository.findPlaceById(id);
        if(place == null)
            throw new ApiException("Place Not Found");
        placeRepository.delete(place);
    }



    public List<Place> getSeasonalPlaces(String district) {
        int seasonalVisitThreshold = 50;
        int seasonalCurrentVisitorsThreshold = 10;

        return placeRepository.findByDistrictAndSeasonal(
                district,
                seasonalVisitThreshold,
                seasonalCurrentVisitorsThreshold
        );
    }

    private void ensureSubscriber(Integer userId){
        User u = userRepository.findUserById(userId);
        if (u == null) throw new ApiException("User not found");
        if (u.getIsSubscriber() == null || !u.getIsSubscriber())
            throw new ApiException("Subscription required");
    }



    public List<Place> subscriberDiscounts(Integer userId, String district){
        ensureSubscriber(userId);
        Instant now = Instant.now();
        return placeRepository.findByDistrictAndIsPartnerTrueAndDealPercentGreaterThanAndActive(district, 0, now);
    }

}
