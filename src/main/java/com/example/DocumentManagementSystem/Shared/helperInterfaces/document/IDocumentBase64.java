package com.example.DocumentManagementSystem.Shared.helperInterfaces.document;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.Document;

import java.io.IOException;

public interface IDocumentBase64 {

    String convertToBase64(Document documnet) throws IOException;
}
