package com.example.DocumentManagementSystem.Shared.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponsePage<T> {
    private String statusCode;
    private String statusMsg;
    private T data;
    private Integer currentPage;
    private Integer totalPages;
    private Long totalElements;
}
