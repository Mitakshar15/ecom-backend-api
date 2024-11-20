//this class Handles login and signup with authentication


package com.ainkai.controller;

import com.ainkai.config.JwtProvider;
import com.ainkai.exceptions.UserException;
import com.ainkai.model.Cart;
import com.ainkai.model.User;
import com.ainkai.repository.UserRepo;
import com.ainkai.request.LoginRequest;
import com.ainkai.response.AuthResponse;
import com.ainkai.service.CartService;
import com.ainkai.service.CustomUserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/auth")
public class AuthController {
    //These are the services and repositories used by the controller for various operations.
    private UserRepo userRepo;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;
    private CustomUserServiceImpl customUserService;
    private CartService cartService;
    
    //This constructor uses constructor-based dependency injection to initialize the controller's dependencies.
    public  AuthController(UserRepo userRepo,CustomUserServiceImpl customUserService , PasswordEncoder passwordEncoder,JwtProvider jwtProvider,CartService cartService){
        this.userRepo = userRepo;
        this.customUserService = customUserService;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.cartService = cartService;
    }
     
    // Handles POST requests to "/auth/signup".
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user)throws UserException{
        //Extracts user details from the request.
        
        String email = user.getEmail();
        String password = user.getPassword();
        String firstName=user.getFirstName();
        String lastName = user.getLastName();
        String mobile = user.getMobile();
        String role = user.getRole();

        //Checks if the email already exists.
        User isEmailExist = userRepo.findByEmail(email);
         
        if(isEmailExist != null){
            throw  new UserException(":: EMAIL IS ALREDY USED WITH ANOTHER ACCOUNT ::");
        }

        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setFirstName(firstName);
        createdUser.setLastName(lastName);
        createdUser.setMobile(mobile);
        createdUser.setRole(role);
        User saveduser = userRepo.save(createdUser);
        Cart cart = cartService.createCart(saveduser);


        Authentication authentication = new UsernamePasswordAuthenticationToken(saveduser.getEmail(),saveduser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("SIGN UP SUCCESS");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse>loginUserHandler(@RequestBody LoginRequest loginRequest){

        String userName = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(userName,password);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse(token,"::SIGN-IN SUCCESS::");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);



    }

    public Authentication authenticate(String username, String password){
        UserDetails userDetails = customUserService.loadUserByUsername(username);
        if(userDetails == null){
            throw new BadCredentialsException(":: INVALID CREDENTIALS (USERNAME) ::");
        }
       if(!passwordEncoder.matches(password,userDetails.getPassword())){
           throw new BadCredentialsException(":: INVALID CREDENTIALS (PASSWORD) ::");
        }
       return  new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}
