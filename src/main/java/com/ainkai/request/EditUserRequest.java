package com.ainkai.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditUserRequest {

  private Long UserId;
  private String firstName;
  private String lastName;
  private String email;
  private String mobile;



}
