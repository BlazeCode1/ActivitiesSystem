package org.example.activitiessystem.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ratings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotNull(message = "Rating User Id Cannot Be Empty")
    @Column(columnDefinition = "int not null")
    private Integer userId;


    @NotNull(message = "Rating Place Id Cannot Be Empty")
    @Column(columnDefinition = "int not null")
    private Integer placeId;


    @NotNull(message = "Rating Score Cannot Be Empty")
    @Column(columnDefinition = "int not null")
    private Integer score;


    @NotNull(message = "Rating Score_cleanliness Cannot Be Empty")
    @Column(columnDefinition = "int not null")
    private Integer score_cleanliness;


    @NotNull(message = "Rating Score_Service Cannot Be Empty")
    @Column(columnDefinition = "int not null")
    private Integer score_service;


    @NotNull(message = "Rating Score_price Cannot Be Empty")
    @Column(columnDefinition = "int not null")
    private Integer score_price;

    @NotEmpty(message = "rating comment Cannot Be Empty")
    @Column(columnDefinition = "text not null")
    private String comment;

    @CreationTimestamp
    private Instant created_at;
}

//    ratings {
//        INT id PK
//        INT user_id
//        INT place_id
//        INT score
//        INT score_cleanliness
//        INT score_service
//        INT score_price
//        TEXT comment
//        TEXT photo_url
//        DATETIME created_at
//    }
