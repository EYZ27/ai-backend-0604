package com.sesac.aibackend0604.dto;

import com.sesac.aibackend0604.domain.Department;
import com.sesac.aibackend0604.domain.Employee;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmployeeRequest(
        @NotBlank String employName,
        String position,
        @NotNull Long departmentId
) {
    public Employee toEntity(Department department) {
        return Employee.builder().name(employName).position(position).department(department).build();
    }
}
