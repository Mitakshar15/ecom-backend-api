package com.ainkai.repository;

import com.ainkai.model.Address;
import com.ainkai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepo extends JpaRepository<Address,Long> {

  boolean existsByStreetAddressAndCityAndStateAndZipCodeAndUser(String streetAddress, String city, String state, String zipCode,User user);
  Optional<Address> findByStreetAddressAndCityAndStateAndZipCodeAndUser(String streetAddress, String city, String state, String zipCode,User user);


  @Query("SELECT a FROM Address a WHERE a.user.id=:userId")
  public List<Address>getAllByUser(@Param("userId") Long userId);

}
