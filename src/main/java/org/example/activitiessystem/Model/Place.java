package org.example.activitiessystem.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Check(constraints = "price_level in ('cheap','medium','expensive') AND avg_rating >=0")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Place Name Cannot Be Empty")
    @Column(columnDefinition = "varchar(255) not null")
    private String name;

    @NotEmpty(message = "Place Description Cannot Be Empty")
    @Column(columnDefinition = "text not null")
    private String description;

    @NotEmpty(message = "Place District Cannot Be Empty")
    @Column(columnDefinition = "varchar(100) not null")
    private String district;

    @NotNull(message = "Place CategoryId Cannot Be Empty")
    @Column(columnDefinition = "int not null")
    private Integer categoryId;

    @NotNull(message = "Place Created by id cannot be null")
    @Column(columnDefinition = "int not null")
    private Integer created_by_id;

    //google map url
    @NotEmpty(message = "Place Location Cannot Be Empty")
    @Column(columnDefinition = "text not null")
    private String location;


    @NotNull(message = "Place open time cannot be empty")
    @JsonFormat(pattern = "HH:mm:ss")
    @Column(columnDefinition = "time not null")
    private LocalTime open_time;

    @NotNull(message = "Place close time cannot be empty")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime close_time;

    @NotEmpty(message = "Place")
    private String contact_number;

    @Column(columnDefinition = "text")
    private String image_url;

    @NotEmpty(message = "Place price level cannot Be Empty")
    @Column(columnDefinition = "varchar(40) not null")
    private String price_level;


    @Column(name = "avg_rating", columnDefinition = "double")
    private Double avgRating;


    @CreationTimestamp
    private Instant created_at;

    @Column(columnDefinition = "boolean not null default false")
    private Boolean is_partner = false;

    @Column(columnDefinition = "int not null default 0")
    private Integer deal_percent = 0;

    @Column(columnDefinition = "timestamp(6) null")
    private Instant deal_until;

    @Column(columnDefinition = "int default 0")
    private Integer count_visits;
    @Column(columnDefinition = "int default 0")
    private Integer count_current_visitors;


}

