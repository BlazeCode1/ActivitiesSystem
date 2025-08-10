package org.example.activitiessystem.Repository;

import org.example.activitiessystem.Model.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {
    List<Challenge> findByDistrictAndIsActiveTrue(String district);
}

