package com.example.DocumentManagementSystem.Shared.Specifications.Document;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.Documnet;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class DocumentSpecification {
    @Autowired
    MongoTemplate mongoTemplate;
    public Page<Documnet> specificDocument(String workSpaceId, String nationalID, Map<String,Object> params, Pageable pageable)
    {
        List<Criteria> criteria = new ArrayList<>();

        if (workSpaceId!=null)
        {
            criteria.add(Criteria.where("workspaceId").is(new ObjectId(workSpaceId)));

        }
        if (nationalID!=null)
        {
            criteria.add(Criteria.where("nationalID").is(nationalID));
           // params.put("nationalId",nationalID);
            params.put("isDeleted",false);
        }
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                 if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")
                        || key.toLowerCase().startsWith("is")) {
                   Boolean isValue =Boolean.parseBoolean(value.toString());
                    criteria.add(Criteria.where(key).is(isValue));

                }
               else if (isNumeric(value)) {
                   int longvalue =Integer.parseInt(value.toString());
                    criteria.add(Criteria.where(key).is(longvalue));

                }
    else {
                    criteria.add(Criteria.where(key).regex(".*" + value + ".*", "i"));
                }
            }
        Query query=new Query();
        if (criteria.size() != 0) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }
        long total = mongoTemplate.count(query, Documnet.class);

        query.with(pageable);

        List<Documnet> documents=mongoTemplate.find(query,Documnet.class);

        return new PageImpl<>(documents,pageable,total);
    }
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
