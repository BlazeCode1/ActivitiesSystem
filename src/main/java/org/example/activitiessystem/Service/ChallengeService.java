package org.example.activitiessystem.Service;

import lombok.RequiredArgsConstructor;
import org.example.activitiessystem.Api.ApiException;
import org.example.activitiessystem.Model.Challenge;
import org.example.activitiessystem.Model.User;
import org.example.activitiessystem.Repository.ChallengeRepository;
import org.example.activitiessystem.Repository.PlaceRepository;
import org.example.activitiessystem.Repository.TripRepository;
import org.example.activitiessystem.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;


    public void addChallenge(Integer adminID, Challenge newChallenge){
        if (!userRepository.findUserAndCheckAdmin(adminID))
            throw new ApiException("Must Be Admin to Add New Challenge");
        challengeRepository.save(newChallenge);
    }



    public List<Challenge> getCurrentChallenges(String district) {
        LocalDate today = LocalDate.now();
        return challengeRepository.findByDistrictAndIsActiveTrue(district)
                .stream()
                .filter(c -> !c.getStartDate().isAfter(today) && !c.getEndDate().isBefore(today))
                .collect(Collectors.toList());
    }

    public void changeChallengeStatus(Integer adminId, Integer challengeId, Boolean isActive) {
        if (!userRepository.findUserAndCheckAdmin(adminId)) {
            throw new ApiException("Must be Admin to change challenge status");
        }

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ApiException("التحدي غير موجود"));

        challenge.setIsActive(isActive);
        challengeRepository.save(challenge);
    }


    public String completeChallenge(Integer userId, Integer challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ApiException("التحدي غير موجود"));
        User user = userRepository.findUserById(userId);

        if (!challenge.getIsActive()) {
            throw new ApiException("التحدي غير مفعّل");
        }

        if (user.getCompletedChallenges() != null &&
                Arrays.asList(user.getCompletedChallenges().split(","))
                        .contains(challengeId.toString())) {
            throw new ApiException("لقد أكملت هذا التحدي من قبل");
        }

        user.setPoints(user.getPoints() + challenge.getPoint());

        String updatedChallenges = (user.getCompletedChallenges() == null || user.getCompletedChallenges().isEmpty())
                ? challengeId.toString()
                : user.getCompletedChallenges() + "," + challengeId;
        user.setCompletedChallenges(updatedChallenges);

        userRepository.save(user);

        return "المستخدم " + userId + " أكمل التحدي: " + challenge.getTitle() + " 🎉";
    }
}
