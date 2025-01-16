package com.ainkai.service;

import com.ainkai.config.JwtProvider;
import com.ainkai.exceptions.UserException;
import com.ainkai.mapper.EcomApiUserMapper;
import com.ainkai.model.Address;
import com.ainkai.model.User;
import com.ainkai.model.dtos.AddressRequest;
import com.ainkai.model.dtos.EditProfileRequest;
import com.ainkai.repository.AddressRepo;
import com.ainkai.repository.UserRepo;;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class userServiceImpl implements UserService{

    private final UserRepo userRepo;
    private final AddressRepo addressRepo;
    private final JwtProvider jwtProvider;
    private final EcomApiUserMapper mapper;

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
    public Address addNewAddress(AddressRequest addressRequest) {
        Optional<User> user = userRepo.findById(addressRequest.getUserId());
        //TODO:Remove the old package for AddressRequest
          Address address = mapper.toAddressEntity(addressRequest);
          address.setUser(user.get());
        if(addressRepo.existsByStreetAddressAndCityAndStateAndZipCodeAndUser(address.getStreetAddress(),address.getCity(),address.getState(),address.getZipCode(),address.getUser())){
            throw new RuntimeException("ADDRESS ALREADY EXISTS");
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
        User user = userRepo.findById(addressRequest.getUserId()).get();
        Optional<Address> opt = addressRepo.findById(addressId);
        Address address = opt.get();
        address = mapper.toAddressEntity(addressRequest);
        address.setId(addressId);
        address.setUser(user);
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
