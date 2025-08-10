package org.example.activitiessystem.Service;

import lombok.RequiredArgsConstructor;
import org.example.activitiessystem.Api.ApiException;
import org.example.activitiessystem.Repository.CategoryRepository;
import org.example.activitiessystem.Repository.TripRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TripLeaderboardService {

    private final TripRepository tripRepository;
    private final CategoryRepository categoryRepository;



    public List<Map<String,Object>> topTripsByDistrict(String district){

        List<Object[]> rows = tripRepository.topTripsByDistrict(district, PageRequest.of(0,3));
        return buildList(rows, 3, true);
    }

    public List<Map<String,Object>> topTripsByDistrictCategory(String district, Integer categoryId){
        if (categoryRepository.findCategoryById(categoryId) == null)
            throw new ApiException("Category not found");

        List<Object[]> rows = tripRepository.topTripsByDistrictCategory(district, categoryId, PageRequest.of(0,6));
        return buildList(rows, 3, true); // نجيب 6 ونصفّي لتفادي تكرار نفس المكان
    }

    private List<Map<String,Object>> buildList(List<Object[]> rows, int limit, boolean dedupeByPlace){
        ArrayList<Map<String,Object>> out = new ArrayList<>();
        HashSet<Integer> seenPlaces = new HashSet<>();

        for (Object[] r : rows) {
            Integer tripId     = (Integer) r[0];
            Integer userId     = (Integer) r[1];
            Integer placeId    = (Integer) r[2];
            String  placeName  = (String)  r[3];
            Double  avgRating  = (Double)  r[4];
            Instant createdAt  = (Instant) r[5];
            String  imageUrl   = (String)  r[6];
            Integer categoryId = (Integer) r[7];

            if (dedupeByPlace && seenPlaces.contains(placeId)) continue;

            HashMap<String,Object> m = new HashMap<>();
            m.put("tripId", tripId);
            m.put("userId", userId);
            m.put("placeId", placeId);
            m.put("placeName", placeName);
            m.put("avgRating", (avgRating == null) ? 0.0 : avgRating);
            m.put("districtCategoryId", categoryId);
            m.put("createdAt", createdAt);
            m.put("imageUrl", imageUrl);

            out.add(m);
            if (dedupeByPlace) seenPlaces.add(placeId);
            if (out.size() == limit) break;
        }
        return out;
    }
}
