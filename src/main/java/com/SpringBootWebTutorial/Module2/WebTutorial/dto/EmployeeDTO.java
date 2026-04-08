package com.SpringBootWebTutorial.Module2.WebTutorial.dto;


import com.SpringBootWebTutorial.Module2.WebTutorial.annotations.EmployeeRoleValidation;
import com.SpringBootWebTutorial.Module2.WebTutorial.annotations.Prime;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

//    @NonNull
    Long employeeId;

    @NotBlank(message = "Employee name should not be blank")
            @Size(min = 3,max = 14,message = "the minimum length of the name should be 3 and max length should be 14")
    String name;


//    @AssertTrue(message  = "We only want active Employees")
    @JsonProperty("isActive")
    boolean isActive;

    @Max(value = 80)
    @Min(value =18)
    @Prime
    int age ;

    @NotBlank(message ="Role of the employee can not be blank")
//    @Pattern(regexp = "^(USER|ADMIN)$",message = "role can only be USER or ADMIN")
    @EmployeeRoleValidation
    private String role;

    @Email(message = "Email should be in a proper format")
    String email;

// For Lecture 2.5 Input validation Annotation
}
