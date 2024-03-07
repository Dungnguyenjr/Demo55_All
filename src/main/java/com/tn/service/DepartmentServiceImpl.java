package com.tn.service;

import com.tn.entity.Department;
import com.tn.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentRepository departmentRepo;

    public DepartmentServiceImpl(DepartmentRepository departmentRepo) {
        this.departmentRepo = departmentRepo;
    }

    @Override
    public List<Department> getAll() {
        List<Department> departments = departmentRepo.findAll();
        return departments;
    }

    @Override
    public Department getById(Integer id) {
        Department department = departmentRepo.findById(id).get();
        return department;
    }

}
