package com.ainkai.service;

import com.ainkai.config.JwtProvider;
import com.ainkai.exceptions.UserException;
import com.ainkai.model.Address;
import com.ainkai.model.User;
import com.ainkai.repository.AddressRepo;
import com.ainkai.repository.UserRepo;
import com.ainkai.request.AddressRequest;
import com.ainkai.request.EditUserRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class userServiceImpl implements UserService{

    private UserRepo userRepo;
    private AddressRepo addressRepo;
    private JwtProvider jwtProvider;

    public  userServiceImpl( UserRepo userRepo, JwtProvider jwtProvider,AddressRepo addressRepo){
      this.userRepo = userRepo;
      this.jwtProvider = jwtProvider;
      this.addressRepo=addressRepo;
    }


    @Override
    public User findUserById(Long userId) throws UserException {

        Optional<User>user = userRepo.findById(userId);
        if(user.isPresent()){
            return user.get();
        }


        throw new UserException(":: USER NOT FOUND WITH ID ::"+ userId);
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {

        String email = jwtProvider.getEmailFromJwtToken(jwt);

        User user  =userRepo.findByEmail(email);

        if(user ==null){
            throw new UserException(":: USER NOT FOUND FOR ::"+ email);
        }


        return user;
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = userRepo.findAllByOrderByCreatedAtDesc();
        return users;
    }

    @Override
    public User editUser(EditUserRequest userRequest) {

        Optional<User> opt = userRepo.findById(userRequest.getUserId());

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

        Address address = new Address();
        address.setUser(user.get());
        address.setFirstName(addressRequest.getFirstName());
        address.setLastName(addressRequest.getLastName());
        address.setStreetAddress(addressRequest.getStreetAddress());
        address.setCity(addressRequest.getCity());
        address.setState(addressRequest.getState());
        address.setZipCode(addressRequest.getZipCode());
        address.setMobile(addressRequest.getMobile());
        if(addressRepo.existsByStreetAddressAndCityAndStateAndZipCodeAndUser(address.getStreetAddress(),address.getCity(),address.getState(),address.getZipCode(),address.getUser())){
            return null;
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
