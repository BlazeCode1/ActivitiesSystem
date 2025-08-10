package org.example.activitiessystem.Repository;

import org.example.activitiessystem.Model.Place;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlaceRepository  extends JpaRepository<Place,Integer> {

    Boolean existsByNameAndDistrict(String name, String district);

    @Query("select p from Place p where p.id=?1")
    Place findPlaceById(Integer id);

    List<Place> findByDistrictIgnoreCaseOrderByAvgRatingAsc(String district);
    List<Place> findByCategoryIdOrderByAvgRatingDesc(Integer categoryId);
    List<Place> findByDistrictIgnoreCaseAndCategoryIdOrderByAvgRatingDesc(String district, Integer categoryId);
    @Query("SELECT p FROM Place p WHERE p.district = :district ORDER BY p.avgRating DESC")
    List<Place> findTopPlacesByDistrict(String district, Pageable pageable);

    @Query("SELECT p FROM Place p " +
            "WHERE p.district = :district " +
            "AND (p.count_visits >= :visitThreshold " +
            "OR p.count_current_visitors >= :currentVisitorsThreshold)")
    List<Place> findByDistrictAndSeasonal(
             String district,
             int visitThreshold,
             int currentVisitorsThreshold
    );


    List<Place> findByDistrict(String district);
}
