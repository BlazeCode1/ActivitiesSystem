package org.example.activitiessystem.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.activitiessystem.Api.ApiResponse;
import org.example.activitiessystem.Model.User;
import org.example.activitiessystem.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> getUsers(){
        if(userService.getUsers().isEmpty())
            return ResponseEntity.ok(new ApiResponse("No Users Available"));
        return ResponseEntity.ok(userService.getUsers());
    }


    @PostMapping("/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody User user){
        userService.addUser(user);
        return ResponseEntity.ok(new ApiResponse("User Added Successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id,@Valid @RequestBody User user){
        userService.updateUser(id,user);
        return ResponseEntity.ok(new ApiResponse("User added Successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse("User deleted Successfully"));
    }

    // Subscribe using points
    @PostMapping("/{userId}/subscribe/points")
    public ResponseEntity<?> subscribeWithPoints(@PathVariable Integer userId){
        userService.subscribeWithPoints(userId);
        return ResponseEntity.ok(new ApiResponse("Subscribed with points"));
    }

    @PostMapping("/{userId}/unsubscribe")
    public ResponseEntity<?> unsubscribe(@PathVariable Integer userId){
        userService.unsubscribe(userId);
        return ResponseEntity.ok(new ApiResponse("Unsubscribed"));
    }

    // Admin grant / revoke
    @PostMapping("/{adminId}/grant-sub/{targetUserId}")
    public ResponseEntity<?> grantSub(@PathVariable Integer adminId,
                                      @PathVariable Integer targetUserId){
        userService.grantSubscriptionByAdmin(adminId, targetUserId);
        return ResponseEntity.ok(new ApiResponse("Subscription granted"));
    }

    @PostMapping("/{adminId}/revoke-sub/{targetUserId}")
    public ResponseEntity<?> revokeSub(@PathVariable Integer adminId,
                                       @PathVariable Integer targetUserId){
        userService.revokeSubscriptionByAdmin(adminId, targetUserId);
        return ResponseEntity.ok(new ApiResponse("Subscription revoked"));
    }

    // Status
    @GetMapping("/{userId}/sub/status")
    public ResponseEntity<?> subStatus(@PathVariable Integer userId){
        return ResponseEntity.ok(userService.subscriptionStatus(userId));
    }


}
