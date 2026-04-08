package com.SpringBootWebTutorial.Module2.WebTutorial.services;

import com.SpringBootWebTutorial.Module2.WebTutorial.dto.DepartmentDTO;
import com.SpringBootWebTutorial.Module2.WebTutorial.dto.EmployeeDTO;
import com.SpringBootWebTutorial.Module2.WebTutorial.entities.DepartmentEntity;
import com.SpringBootWebTutorial.Module2.WebTutorial.entities.EmployeeEntity;
import com.SpringBootWebTutorial.Module2.WebTutorial.exceptions.ResourceNotFoundException;
import com.SpringBootWebTutorial.Module2.WebTutorial.repositories.DepartmentRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentService {
    private final DepartmentRepo departmentRepo;

    private final ModelMapper modelMapper;

    public DepartmentService(DepartmentRepo departmentRepo, ModelMapper modelMapper) {
        this.departmentRepo = departmentRepo;
        this.modelMapper = modelMapper;
    }

    public List<DepartmentDTO> getAllDepartment() {
        List<DepartmentEntity> departmentEntity = departmentRepo.findAll();
        return departmentEntity
                .stream()
                .map(departmentEntity1 -> modelMapper.map(departmentEntity1, DepartmentDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<DepartmentDTO> getDepartmentById(Long id) {
        boolean exists = departmentRepo.existsById(id);
        if (!exists) throw new ResourceNotFoundException("Department with id " + id + " not found");
        return departmentRepo.findById(id).map(departmentEntity -> modelMapper.map(departmentEntity, DepartmentDTO.class));
    }

    public DepartmentDTO createNewDepartment(DepartmentDTO newDepartmentDTO) {
        DepartmentEntity newDep = modelMapper.map(newDepartmentDTO, DepartmentEntity.class);
        DepartmentEntity depEntity = departmentRepo.save(newDep);
        return modelMapper.map(depEntity, DepartmentDTO.class);
    }

    public DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO) {
        boolean exists = departmentRepo.existsById(id);
        if (!exists) throw new ResourceNotFoundException("Department with this id " + id + " does not exist");
        DepartmentEntity departmentEntity = modelMapper.map(departmentDTO, DepartmentEntity.class);
        departmentEntity.setId(id);
        DepartmentEntity updateDepartment = departmentRepo.save(departmentEntity);
        return modelMapper.map(updateDepartment, DepartmentDTO.class);
    }
}
