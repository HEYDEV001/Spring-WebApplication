package com.SpringBootWebTutorial.Module2.WebTutorial.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {

    int id;

    @NotBlank
    String title;

    @AssertTrue
    boolean isActive;

    
    LocalDate createdAt;
}
