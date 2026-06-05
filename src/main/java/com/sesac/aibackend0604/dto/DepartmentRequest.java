package com.sesac.aibackend0604.dto;

import com.sesac.aibackend0604.domain.Department;
import jakarta.validation.constraints.NotBlank;

public record DepartmentRequest(
        @NotBlank String departmentName
) {
    public Department toEntity() {
        return Department.builder().name(departmentName).build();
    }
}
