package com.SpringBootWebTutorial.Module2.WebTutorial.controllers;


import com.SpringBootWebTutorial.Module2.WebTutorial.dto.DepartmentDTO;
import com.SpringBootWebTutorial.Module2.WebTutorial.dto.EmployeeDTO;
import com.SpringBootWebTutorial.Module2.WebTutorial.entities.DepartmentEntity;
import com.SpringBootWebTutorial.Module2.WebTutorial.exceptions.ResourceNotFoundException;
import com.SpringBootWebTutorial.Module2.WebTutorial.repositories.DepartmentRepo;
import com.SpringBootWebTutorial.Module2.WebTutorial.services.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    // This is dependency Injection
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartment(@RequestParam(required = false) String title){
        List<DepartmentDTO> departmentDTO = departmentService.getAllDepartment();
        return ResponseEntity.ok(departmentDTO);
    }

    @PostMapping
    public ResponseEntity<DepartmentDTO> createNewDepartment(@RequestBody @Valid DepartmentDTO newDepartmentDTO){
        DepartmentDTO savedDepartment = departmentService.createNewDepartment(newDepartmentDTO);
        return new ResponseEntity<>(savedDepartment, HttpStatus.CREATED);
    }

   @GetMapping("/{Id}")
public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Long Id){
        Optional<DepartmentDTO> departmentDTO = departmentService.getDepartmentById(Id);
        return departmentDTO
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new ResourceNotFoundException("Department not found"));
   }
   @PutMapping("/{Id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable Long Id, @RequestBody @Valid DepartmentDTO departmentDTO){
       return ResponseEntity.ok(departmentService.updateDepartment(Id,departmentDTO));
   }


}
