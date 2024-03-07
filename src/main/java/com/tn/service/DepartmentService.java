package com.tn.service;

import com.tn.entity.Department;

import java.util.List;

public interface DepartmentService {

    List<Department> getAll();

    Department getById(Integer id);
}
