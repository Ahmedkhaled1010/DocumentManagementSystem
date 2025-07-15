package com.example.DocumentManagementSystem.BusinessLayer.Services;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.WorkSpace;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.jpa.UserRepository;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.mongo.WorkSpaceRepository;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.WorkSpace.WorkSpaceDto;
import com.example.DocumentManagementSystem.Shared.POJO.APIResponse;
import com.example.DocumentManagementSystem.Shared.POJO.ApiResponsePage;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;

import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class WorkSpaceService {

    @Autowired
    WorkSpaceRepository workSpaceRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServices userServices;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MongoTemplate mongoTemplate;

   public ResponseEntity<APIResponse<Map<String, Object>>> createWorkSpace(@RequestBody WorkSpaceDto workSpaceDto, Authentication authentication, BindingResult bindingResult) {
        {
            APIResponse<Map<String, Object>> response;

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();

                bindingResult.getFieldErrors().forEach(error -> {
                    errors.put(error.getField(), error.getDefaultMessage());
                });

                bindingResult.getGlobalErrors().forEach(error -> {
                    errors.put("global", error.getDefaultMessage());
                });
                response = new APIResponse<>("400", "Error", Map.of("error", errors));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            User user =(User) authentication.getPrincipal();
            System.out.println(user);
            WorkSpace workSpace=modelMapper.map(workSpaceDto, WorkSpace.class);
            workSpace.setUserNationalID(user.getNationalID());
            workSpaceRepository.save(workSpace);
            user.getWorkSpaceList().add(workSpace.getId());
            userRepository.save(user);
            response = new APIResponse<>("200", "Success", Map.of("data", workSpace));
            return ResponseEntity.ok(response);



        }
    }

    public ResponseEntity<APIResponse<Object>> deleteWorkSpace(String workSpaceId,Boolean isDeleted)
    {
        APIResponse< Object> response;

        Optional<WorkSpace> workSpace =workSpaceRepository.findById(new ObjectId(workSpaceId));
        WorkSpaceDto workSpaceDto= modelMapper.map(workSpace.get(),WorkSpaceDto.class);
        if (!workSpace.isPresent())
        {
            response = new APIResponse<>("404", "Not Found",  workSpaceDto);
            return ResponseEntity.ok(response);
        }
        else
        {

                Query query =new Query(Criteria.where("_id").is(new ObjectId(workSpaceId)));
                Update update =new Update().set("isDeleted",true);
                UpdateResult result= mongoTemplate.updateFirst(query,update,WorkSpace.class);
                response = new APIResponse<>("200", "Success",result);



            return ResponseEntity.ok(response);
        }
    }

    public  ResponseEntity<APIResponse<Object>> retriveWorkSpace(String workSpaceId, Authentication authentication)
    {

        APIResponse< Object> response;
        User userSignin =(User) authentication.getPrincipal();
        Optional<User> user = userRepository.findByuserId(userSignin.getUserId());

        List<ObjectId> userList =user.get().getWorkSpaceList();

        if (userList.contains(workSpaceId))
        {
            Optional<WorkSpace> workSpace=workSpaceRepository.findById(new ObjectId(workSpaceId));
            if (workSpace.isPresent()) {
                WorkSpaceDto workSpaceDto = modelMapper.map(workSpace, WorkSpaceDto.class);

                response = new APIResponse<>("200", "Success", workSpaceDto);
                return ResponseEntity.ok(response);
            }
        }
        response = new APIResponse<>("404", "Not found",  "Not Authorize");
        return ResponseEntity.ok(response);
    }

    public  ResponseEntity<?> retriveAllWorkSpace(Authentication authentication,int pageNum, int pageSize, String sortField, String sortDir)
    {

        ApiResponsePage<Object> response;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize,
                sortDir.equals("asc") ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending());
        User userSignin =(User) authentication.getPrincipal();
        Optional<User> user = userRepository.findByuserId(userSignin.getUserId());
        Page<WorkSpace> workSpaceList;

        if (user.isPresent())
        {
            workSpaceList=workSpaceRepository.findAll(pageable);
            if (workSpaceList.isEmpty())
            {
                response = new ApiResponsePage<>("401", "Not found workSpace", null,workSpaceList.getNumber()+1,workSpaceList.getTotalPages(),
                        workSpaceList.getTotalElements());
                return ResponseEntity.ok(response);
            }
            List<WorkSpaceDto>  workSpaceDtoList = workSpaceList.getContent().stream()
                    .map(workSpace -> modelMapper.map(workSpace, WorkSpaceDto.class))
                    .toList()  ;
            Page<WorkSpaceDto> workSpaceDtos = new PageImpl<>(workSpaceDtoList, pageable, workSpaceList.getTotalElements());

            response = new ApiResponsePage<>("200", "Success", workSpaceDtos.getContent(),workSpaceDtos.getNumber()+1,workSpaceDtos.getTotalPages(),
                    workSpaceDtos.getTotalElements());

            return ResponseEntity.ok( response
            );

        }

        response = new ApiResponsePage<>("404", "Not found", null,0,0,0L);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<APIResponse<Object>> updateWorkSpace(String workSpaceId,  WorkSpaceDto workSpaceDto, Authentication authentication)
    {
        APIResponse< Object> response;
        User userSignin =(User) authentication.getPrincipal();
        Optional<User> user = userRepository.findByuserId(userSignin.getUserId());
        List<ObjectId> userList =user.get().getWorkSpaceList();
        if (userList.contains(new ObjectId(workSpaceId)))
        {
            Optional<WorkSpace> workSpace=workSpaceRepository.findById(new ObjectId(workSpaceId));
            if (workSpace.isPresent()) {
                if (workSpaceDto.getName()!=null)
                {
                    workSpace.get().setName(workSpaceDto.getName());
                }
                if (workSpaceDto.getDescription()!=null)
                {
                    workSpace.get().setDescription(workSpaceDto.getDescription());
                }
                workSpaceRepository.save(workSpace.get());
                response = new APIResponse<>("200", "Success", workSpace);
                return ResponseEntity.ok(response);
            }
        }
        response = new APIResponse<>("404", "Not found",  "Not Authorize");
        return ResponseEntity.ok(response);
    }


    public  ResponseEntity<?> retriveAllWorkSpaceAllUser(Authentication authentication, int pageNum, int pageSize, String sortField, String sortDir)
    {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize,
                sortDir.equals("asc") ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending());
        ApiResponsePage<Object> response;
        User userSignin =(User) authentication.getPrincipal();
        Optional<User> user = userRepository.findByuserId(userSignin.getUserId());
        Page<WorkSpace> workSpaceList;
        if (user.isPresent())
        {
             workSpaceList=workSpaceRepository.findAll(pageable);
             if (workSpaceList.isEmpty())
             {
                 response = new ApiResponsePage<>("401", "Not found workSpace", null,workSpaceList.getNumber()+1,workSpaceList.getTotalPages(),
                         workSpaceList.getTotalElements());
                 return ResponseEntity.ok(response);
             }
           List<WorkSpaceDto>  workSpaceDtoList = workSpaceList.getContent().stream()
                    .map(workSpace -> modelMapper.map(workSpace, WorkSpaceDto.class))
                    .toList()  ;
            Page<WorkSpaceDto> workSpaceDtos = new PageImpl<>(workSpaceDtoList, pageable, workSpaceList.getTotalElements());

            response = new ApiResponsePage<>("200", "Success", workSpaceDtos.getContent(),workSpaceDtos.getNumber()+1,workSpaceDtos.getTotalPages(),
                    workSpaceDtos.getTotalElements());

            return ResponseEntity.ok( response
            );

        }

        response = new ApiResponsePage<>("404", "Not found", null,0,0,0L);
        return ResponseEntity.ok(response);
    }



}
