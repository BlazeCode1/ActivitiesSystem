package org.example.activitiessystem.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "عنوان التحدي لا يمكن أن يكون فارغ")
    @Column(columnDefinition = "varchar(255) not null")
    private String title;

    @NotEmpty(message = "وصف التحدي لا يمكن أن يكون فارغ")
    @Column(columnDefinition = "text not null")
    private String description;

    @NotEmpty(message = "اسم الحي لا يمكن أن يكون فارغ")
    @Column(columnDefinition = "varchar(100) not null")
    private String district;

    @NotEmpty(message = "الفترة أو الموسم لا يمكن أن يكون فارغ")
    @Column(columnDefinition = "varchar(50) not null")
    private String season;

    @NotNull(message = "Challenge Points Cannot Be Empty")
    @Column(columnDefinition = "int not null")
    private Integer point;


    @NotNull(message = "تاريخ البداية لا يمكن أن يكون فارغ")
    private LocalDate startDate;

    @NotNull(message = "تاريخ النهاية لا يمكن أن يكون فارغ")
    private LocalDate endDate;

    @Column(columnDefinition = "boolean not null default true")
    private Boolean isActive = true;
}

