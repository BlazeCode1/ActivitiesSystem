package org.example.activitiessystem.Service;

import lombok.RequiredArgsConstructor;
import org.example.activitiessystem.Api.ApiException;
import org.example.activitiessystem.Model.User;
import org.example.activitiessystem.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public void addUser(User user){
        userRepository.save(user);
    }
    public void updateUser(Integer id, User user){
        User oldUser = userRepository.findUserById(id);

        if(oldUser == null)
            throw new ApiException("User not found");

        oldUser.setUsername(user.getUsername());
        oldUser.setEmail(user.getEmail());
        userRepository.save(oldUser);
    }

    public void deleteUser(Integer id){
        User user = userRepository.findUserById(id);
        if(user == null)
            throw new ApiException("User not found");
        userRepository.delete(user);
    }

    private User requireUser(Integer userId){
        User u = userRepository.findUserById(userId);
        if (u == null) throw new ApiException("User not found");
        return u;
    }

    private void requireAdmin(Integer adminId){
        Boolean ok = userRepository.findUserAndCheckAdmin(adminId);
        if (ok == null || !ok) throw new ApiException("Admin privileges required");
    }


    // ===== Subscription with POINTS =====
    public void subscribeWithPoints(Integer userId){

        User u = requireUser(userId);

        int pts = (u.getPoints() == null) ? 0 : u.getPoints();
        if (pts < 200) throw new ApiException("Not enough points");

        u.setPoints(pts - 200);
        u.setIsSubscriber(true);
        userRepository.save(u);
    }

    public void unsubscribe(Integer userId){
        User u = requireUser(userId);
        u.setIsSubscriber(false);
        userRepository.save(u);
    }

    public void grantSubscriptionByAdmin(Integer adminId, Integer targetUserId){
        requireAdmin(adminId);
        User u = requireUser(targetUserId);
        u.setIsSubscriber(true);
        userRepository.save(u);
    }

    public void revokeSubscriptionByAdmin(Integer adminId, Integer targetUserId){
        requireAdmin(adminId);
        User u = requireUser(targetUserId);
        u.setIsSubscriber(false);
        userRepository.save(u);
    }

    public Map<String,Object> subscriptionStatus(Integer userId){
        User u = requireUser(userId);
        HashMap<String,Object> m = new HashMap<String,Object>();
        m.put("userId", u.getId());
        m.put("isSubscriber", u.getIsSubscriber() != null && u.getIsSubscriber());
        m.put("points", (u.getPoints() == null) ? 0 : u.getPoints());
        return m;
    }
}




