package com.example.DocumentManagementSystem.Shared.helperInterfaces.document;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.Document;

public interface IDocumentDeletionServices {
     void softDeleteWorkSpace(Document documnet, boolean isDeleted);
}
