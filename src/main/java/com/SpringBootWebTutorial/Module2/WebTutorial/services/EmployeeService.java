package com.SpringBootWebTutorial.Module2.WebTutorial.services;

import com.SpringBootWebTutorial.Module2.WebTutorial.dto.EmployeeDTO;
import com.SpringBootWebTutorial.Module2.WebTutorial.entities.EmployeeEntity;
import com.SpringBootWebTutorial.Module2.WebTutorial.exceptions.ResourceNotFoundException;
import com.SpringBootWebTutorial.Module2.WebTutorial.repositories.EmployeeRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepo employeeRepo;

    private final ModelMapper modelMapper;

    public EmployeeService(EmployeeRepo employeeRepo, ModelMapper modelMapper) {
        this.employeeRepo = employeeRepo;
        this.modelMapper = modelMapper;
    }

    public Optional<EmployeeDTO> getEmployee(Long employeeID) {
//       Optional<EmployeeEntity> employeeEntity =  employeeRepo.findById(employeeID);
//       return employeeEntity.map(employeeEntity1 -> modelMapper.map(employeeEntity1, EmployeeDTO.class));

        return employeeRepo.findById(employeeID).map(employee -> modelMapper.map(employee, EmployeeDTO.class));
    }

    public List<EmployeeDTO> getAllEmployees() {
        List<EmployeeEntity> employeeEntities = employeeRepo.findAll();
        return employeeEntities
                .stream()
                .map(employeeEntity -> modelMapper.map(employeeEntity, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    public EmployeeDTO addEmployee(EmployeeDTO myEmployee) {
        // here we are trying to insert the EmployeeDTO in the EmployeeRepo but it only takes EmployeeEntity so we have to map it here again from EmployeeDTO to EmployeeEntity
        EmployeeEntity toSaveEntity = modelMapper.map(myEmployee, EmployeeEntity.class);
        EmployeeEntity employeeEntity = employeeRepo.save(toSaveEntity);
        return modelMapper.map(employeeEntity, EmployeeDTO.class);
    }

    public EmployeeDTO upadateEmployee(Long employeeId, EmployeeDTO myEmployee) {
        boolean exists = employeeRepo.existsById(employeeId);
        if (!exists) throw new ResourceNotFoundException("Employee with this id " + employeeId + " does not exist");
        EmployeeEntity employeeEntity = modelMapper.map(myEmployee, EmployeeEntity.class);
        employeeEntity.setEmployeeId(employeeId);
        EmployeeEntity updatedEmployee = employeeRepo.save(employeeEntity);
        return modelMapper.map(updatedEmployee, EmployeeDTO.class);
    }

    public boolean deleteEmployee(Long EmployeeId) {
        boolean exists = employeeRepo.existsById(EmployeeId);
        if (!exists) throw new ResourceNotFoundException("Employee with this id " + EmployeeId + " does not exist");
        employeeRepo.deleteById(EmployeeId);
        return true;

    }

    public EmployeeDTO patchEmployee(Long employeeId, Map<String, Object> updates) {
        boolean exists = employeeRepo.existsById(employeeId);
        if (!exists) throw new ResourceNotFoundException("Employee with this id " + employeeId + " does not exist");
        EmployeeEntity employeeEntity = employeeRepo.findById(employeeId).get();
        updates.forEach((field, value) -> {
            Field fieldToBeUpdated = ReflectionUtils.findField(EmployeeEntity.class, field);
            fieldToBeUpdated.setAccessible(true);
            ReflectionUtils.setField(fieldToBeUpdated, employeeEntity, value);
        });
        return modelMapper.map(employeeRepo.save(employeeEntity), EmployeeDTO.class);
    }
}
