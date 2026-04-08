package com.SpringBootWebTutorial.Module2.WebTutorial.controllers;


import com.SpringBootWebTutorial.Module2.WebTutorial.dto.EmployeeDTO;
import com.SpringBootWebTutorial.Module2.WebTutorial.entities.EmployeeEntity;
import com.SpringBootWebTutorial.Module2.WebTutorial.exceptions.ResourceNotFoundException;
import com.SpringBootWebTutorial.Module2.WebTutorial.repositories.EmployeeRepo;
import com.SpringBootWebTutorial.Module2.WebTutorial.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;


// If we want to create a parent means
// jaise ki yha ham Employee ke liye url endpoints bana rhe h to kyu na employee ko parent bana diya jaaye
// jisse baki ke jo bhi endpoints h bo uske baad append ho jaaye.
// Ye ham kr skte h @RequestMapping ke through like
// to ab yha employee ke baad jo bhi apan path denge bo uss endpoint pe pahucha dega
// ab agar greet pe jana  h to "employee/greet" dena padega
@RestController
@RequestMapping(path= "/employee")
public class EmployeeController {

    @GetMapping(path= "/greet")
    public String greet(){
        return "Greetings from Spring Boot!";
    }

    // This is how the mapping is done in the spring, it is connecting by the Component scan of the Project done by the spring.
    // @GetMapping is used to create endPoints in the Spring.
    // We have given here as the path because the @GetMapping can multiple parameters.
    @GetMapping(path = "/getSecretMessage")
    public String getSecretMessege(){
        return "Secret Messege: Kisi ko mat batana." ;
    }

//    private final EmployeeRepo employeeRepo;
//
//    public EmployeeController(EmployeeRepo employeeRepo) {
//        this.employeeRepo = employeeRepo;
//    }

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
// There are to ways in which we inject the variables
    // 1. Path variables.
    // 2. request Params.

    // if we want to inject the variables in the URL then we can do like this but we have to define those as path variables by using @PathVariables

//    @GetMapping( path = "/{EmployeeID}")
//    public EmployeeDTO getEmployee(@PathVariable Long EmployeeID){
//        // here we have returned an object of the EmployeeDTO and Injected the EmployeeID from the Url itself
//        return new EmployeeDTO(EmployeeID,"dev pathak", true,19,"dev@gamil.com");
//    }

    // Lecture 2.3
//    @GetMapping( path = "/{EmployeeID}")
//    public EmployeeEntity getEmployee(@PathVariable Long EmployeeID){
//        return employeeRepo.findById(EmployeeID).orElse(null);
//    }


    // This is for lecture 2.4
    @GetMapping(path = "/{EmployeeID}")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable @Valid Long EmployeeID) {
        Optional<EmployeeDTO> employeeDTO = employeeService.getEmployee(EmployeeID);
        return employeeDTO
                .map(employeeDTO1 -> ResponseEntity.ok(employeeDTO1)) // or we can write like this (.map(ResponseEntity::ok))
                .orElseThrow(()-> new ResourceNotFoundException("Employee Not Found with Id:"+ EmployeeID));// This message will not be displayed.
    }


    // This is the Request params method in this you can give the url in the format (employee?age=10) Then it will reach that endpoint
//    @GetMapping
//    public String getAllEmployees(@RequestParam(required = false) Integer age){
//        return "All employees" +age;
//    }


    // this is for the lecture 2.3
//    @GetMapping
//    public List<EmployeeEntity> getAllEmployees(@RequestParam(required = false) Integer age){
//        return employeeRepo.findAll();
//    }

    // This is for lecture 2.4
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(@RequestParam(required = false) Integer age){
        List<EmployeeDTO> employeeDTO =employeeService.getAllEmployees();
         return ResponseEntity.ok(employeeDTO);
    }

    // EXCEPTION HANDLING
    // This is Handler is only for this controller, but we have to make this accessible for other controller then we have to make it global.
//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity<String> HandleEmployeeNotFound(NoSuchElementException exception){
//        return  new ResponseEntity<>("Employee Not Found",HttpStatus.NOT_FOUND);// The message given here will be shown.
////    /   return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee Not Found");
//    }

    // This is used to add data to the Application

    // yha ham pura ka pura object hi de rhe h parameter me by the use of @RequestBody.
    // @RequestBody is used for all the mapping and conversion of the formats and the getting the whole object.

//    @PostMapping
//    public EmployeeDTO addEmployee(@RequestBody EmployeeDTO myEmployee){
//        myEmployee.setEmployeeId(100l);
//        return myEmployee;
//    }


    // this is for lecture 2.3
//    @PostMapping
//    public EmployeeEntity addEmployee(@RequestBody EmployeeEntity myEmployee){
//        return employeeRepo.save(myEmployee);
//    }


    // This is for lecture 2.4

    // ResponseEntity is for the Status codes
    @PostMapping
    public ResponseEntity<EmployeeDTO> addEmployee(@RequestBody @Valid EmployeeDTO myEmployee){
        EmployeeDTO savedEmployee = employeeService.addEmployee(myEmployee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }
    // This is for lecture 2.5
    // this is used to update the data
    @PutMapping(path = "/{EmployeeId}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable   Long EmployeeId, @RequestBody EmployeeDTO myEmployee){
        return ResponseEntity.ok(employeeService.upadateEmployee(EmployeeId,myEmployee));
    }

    @DeleteMapping(path = "/{EmployeeId}")
    public ResponseEntity<Boolean> deleteEmployee(@PathVariable  Long EmployeeId){
      boolean deletedEmployee =employeeService.deleteEmployee(EmployeeId);
      if(deletedEmployee){
          return ResponseEntity.ok(true);
      }
      return ResponseEntity.notFound().build();
    }

    @PatchMapping(path = "/{EmployeeId}")
    public EmployeeDTO patchEmployee(@PathVariable Long EmployeeId, @RequestBody Map<String,Object> updates){
        return employeeService.patchEmployee(EmployeeId,updates);
    }
}

