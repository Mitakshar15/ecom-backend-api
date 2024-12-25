package com.ainkai.controller;


import com.ainkai.dto.AddressResponseDTO;
import com.ainkai.dto.UserResponseDTO;
import com.ainkai.exceptions.UserException;
import com.ainkai.model.Address;
import com.ainkai.model.User;
import com.ainkai.repository.AddressRepo;
import com.ainkai.repository.UserRepo;
import com.ainkai.request.AddressRequest;
import com.ainkai.request.EditUserRequest;
import com.ainkai.response.ApiResponse;
import com.ainkai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getUserProfileHandler(@RequestHeader("Authorization") String jwt)throws UserException{

        System.out.println("INSIDE GET USER PROFILE HANDLER CONTROLLER ::");
        User user = userService.findUserProfileByJwt(jwt);
        return  new ResponseEntity<>(UserResponseDTO.fromEntity(user), HttpStatus.ACCEPTED);

    }

    @GetMapping("/")
    public ResponseEntity<List<User>> findAllUsersHandler(@RequestHeader("Authorization") String jwt) throws UserException{

        List<User> users = userService.findAllUsers();
        return  new ResponseEntity<>(users,HttpStatus.ACCEPTED);

    }

    @PutMapping("/editProfile")
    public ResponseEntity<UserResponseDTO>updateUserDetailHandler(@RequestHeader("Authorization") String jwt, @RequestBody EditUserRequest userRequest) throws UserException {

        User loggedUser = userService.findUserProfileByJwt(jwt);
        if(Objects.equals(loggedUser.getId(), userRequest.getUserId())){
            User updatedUser = userService.editUser(userRequest);
            return new ResponseEntity<UserResponseDTO>(UserResponseDTO.fromEntity(updatedUser),HttpStatus.OK);
        }
        else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/address/edit")
    public ResponseEntity<Address>addDeliveryAddress(@RequestHeader("Authorization") String jwt,@RequestBody AddressRequest request) throws UserException {

       User user  = userService.findUserProfileByJwt(jwt);
       if(Objects.equals(user.getId(), request.getUserId())){
           Address newAddress = userService.addNewAddress(request);
           if(newAddress!=null){
               return new ResponseEntity<>(newAddress,HttpStatus.CREATED);
           }
           else {
               return ResponseEntity.notFound().build();
           }

       }
       else{
           return ResponseEntity.notFound().build();
       }

    }

    @DeleteMapping("/address/delete/{addressId}")
    public ResponseEntity<ApiResponse> deleteDeliveryAddress(@RequestHeader("Authorization") String jwt,@PathVariable Long addressId) throws UserException {

        User user = userService.findUserProfileByJwt(jwt);
        ApiResponse apiResponse = new ApiResponse();
        if(user!=null){
            userService.deleteAddress(addressId);
            apiResponse.setStatus(true);
            apiResponse.setMessage("ADDRESS DELETED SUCCESFULLY");
            return  new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        else {
            apiResponse.setMessage("FAILED TO DELETE");
            apiResponse.setStatus(false);
            return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/address/edit/{addressId}")
    public ResponseEntity<Address> editAddressHandler(@RequestHeader("Authorization") String jwt,@PathVariable Long addressId,@RequestBody AddressRequest addressRequest) throws UserException {

        User user = userService.findUserProfileByJwt(jwt);
        if(Objects.equals(user.getId(),addressRequest.getUserId())){
            Address editedAddress = userService.editAddress(addressRequest,addressId);
            return new ResponseEntity<>(editedAddress,HttpStatus.ACCEPTED);
        }
        else{

            return ResponseEntity.notFound().build();

        }

    }

    @GetMapping("/address")
  public ResponseEntity<List<AddressResponseDTO>> getDeliveryAddress(@RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.findUserProfileByJwt(jwt);
        List<AddressResponseDTO> addressResponseDTOList= new ArrayList<>();
        if(user==null){
          return ResponseEntity.notFound().build();
        }
        else{

            List<Address> addressList = userService.getAllUserAddresses(user.getId());
            addressResponseDTOList = AddressResponseDTO.fromEntityList(addressList);

            return new ResponseEntity<>(addressResponseDTOList,HttpStatus.ACCEPTED);

        }

  }



}
