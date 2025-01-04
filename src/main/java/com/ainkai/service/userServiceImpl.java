package com.ainkai.service;

import com.ainkai.config.JwtProvider;
import com.ainkai.exceptions.UserException;
import com.ainkai.mapper.EcomApiUserMapper;
import com.ainkai.model.Address;
import com.ainkai.model.User;
import com.ainkai.model.dtos.EditProfileRequest;
import com.ainkai.repository.AddressRepo;
import com.ainkai.repository.UserRepo;
import com.ainkai.request.AddressRequest;
import com.ainkai.request.EditUserRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class userServiceImpl implements UserService{

    private UserRepo userRepo;
    private AddressRepo addressRepo;
    private JwtProvider jwtProvider;
    private EcomApiUserMapper mapper;




    @Override
    public User findUserById(Long userId) throws UserException {

        Optional<User>user = userRepo.findById(userId);
        if(user.isPresent()){
            return user.get();
        }


        throw new UserException("ERROR KEY",":: USER NOT FOUND WITH ID ::"+ userId);
    }

    @Override
    public User findUserProfileByJwt(String jwt) {

        String email = jwtProvider.getEmailFromJwtToken(jwt);
        User user  =userRepo.findByEmail(email);
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = userRepo.findAllByOrderByCreatedAtDesc();
        return users;
    }

    @Override
    public User editUser(EditProfileRequest userRequest) {

        Optional<User> opt = userRepo.findById(userRequest.getId());

        if(opt.isPresent()){

            opt.get().setFirstName(userRequest.getFirstName());
            opt.get().setLastName(userRequest.getLastName());
            opt.get().setMobile(userRequest.getMobile());
            opt.get().setEmail(userRequest.getEmail());

        }
        User updatedUser  = userRepo.save(opt.get());

        return updatedUser;

    }

    @Override
    public Address addNewAddress(com.ainkai.model.dtos.AddressRequest addressRequest) {
        Optional<User> user = userRepo.findById(addressRequest.getUserId());
        //TODO:Remove the old package for AddressRequest
          Address address = mapper.toAddressEntity(addressRequest);
          address.setUser(user.get());
//        address.setFirstName(addressRequest.getFirstName());
//        address.setLastName(addressRequest.getLastName());
//        address.setStreetAddress(addressRequest.getStreetAddress());
//        address.setCity(addressRequest.getCity());
//        address.setState(addressRequest.getState());
//        address.setZipCode(addressRequest.getZipCode());
//        address.setMobile(addressRequest.getMobile());
        if(addressRepo.existsByStreetAddressAndCityAndStateAndZipCodeAndUser(address.getStreetAddress(),address.getCity(),address.getState(),address.getZipCode(),address.getUser())){
            throw new RuntimeException("ADDR ALREADY EXISTS");
        }
        else{
            return addressRepo.save(address);
        }



    }
    @Override
    public void deleteAddress(Long addressId){

        addressRepo.deleteById(addressId);

    }

    @Override
    public Address editAddress(AddressRequest addressRequest, Long addressId) {

        Optional<User> user = userRepo.findById(addressId);
        Optional<Address> opt = addressRepo.findById(addressId);
        Address address = opt.get();
        address.setFirstName(addressRequest.getFirstName());
        address.setLastName(addressRequest.getLastName());
        address.setStreetAddress(addressRequest.getStreetAddress());
        address.setCity(addressRequest.getCity());
        address.setState(addressRequest.getState());
        address.setZipCode(addressRequest.getZipCode());
        address.setMobile(addressRequest.getMobile());

        return  addressRepo.save(address);
    }

    @Override
    public List<Address> getAllUserAddresses(Long userId) {

        List<Address>addressList = addressRepo.getAllByUser(userId);
        if(addressList.size()>0){
            return addressList;
        }
        else{
            return List.of();
        }
    }


}
