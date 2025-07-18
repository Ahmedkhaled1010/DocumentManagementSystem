package com.example.DocumentManagementSystem.Shared.DataTransferModel.DocumentDto;

import lombok.Data;

@Data
public class DocumentSearchCriteria {
    private String fileName;
    private String fileType;
    private String nationalID;
    private String tag;
    private String privacy;
    private String workspaceId;
    private long size;
    private Boolean isDeleted;

    private String sortField = "fileName";
    private String sortDirection = "asc";

    private int pageNum = 1;
    private int pageSize = 10;
}
