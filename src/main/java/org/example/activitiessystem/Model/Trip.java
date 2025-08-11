package org.example.activitiessystem.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity


public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "userId cannot be null")
    @Min(value = 1, message = "userId must be >= 1")
    @Column(columnDefinition = "int not null")
    private Integer userId;

    @NotBlank(message = "District cannot be blank")
    @Size(max = 100, message = "District max length is 100")
    @Column(columnDefinition = "varchar(100) not null")
    private String district;

    @Positive(message = "categoryId must be positive")
    @Column(columnDefinition = "int not null")
    private Integer categoryId;

    @Positive(message = "selectedPlaceId must be positive")
    @Column(columnDefinition = "int not null")
    private Integer selectedPlaceId;


    //image proof
    @NotEmpty(message = "Image Url Cannot Be Empty")
    private String imageUrl;

    @CreationTimestamp
    @Column(columnDefinition = "timestamp(6) not null default current_timestamp(6)")
    private Instant start;


    @CreationTimestamp
    @Column(columnDefinition = "timestamp(6) not null default current_timestamp(6)")
    private Instant finish;

    @Column(columnDefinition = "boolean not null default false")
    private Boolean isFinish = false;

    @Column(columnDefinition = "boolean not null default false")
    private Boolean isPublic = false;

    @Column(columnDefinition = "varchar(20) unique")
    private String shareToken;

    @Column(columnDefinition = "timestamp(6) null")
    private Instant publishedAt;


}
