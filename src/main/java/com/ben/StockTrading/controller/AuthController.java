package com.ben.StockTrading.controller;

import com.ben.StockTrading.entity.TwoFactorOtp;
import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.repo.UserRepo;
import com.ben.StockTrading.response.AuthResponse;
import com.ben.StockTrading.security.CustomUserDetailsService;
import com.ben.StockTrading.security.JwtProvider;
import com.ben.StockTrading.service.EmailService;
import com.ben.StockTrading.service.TwoFactorOtpService;
import com.ben.StockTrading.service.WatchListService;
import com.ben.StockTrading.utils.OtpUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final UserRepo userRepo;
    private final CustomUserDetailsService customUserDetailsService;
    private final TwoFactorOtpService twoFactorOtpService;
    private final EmailService emailService;
    private final WatchListService watchListService;

    public AuthController(UserRepo userRepo, CustomUserDetailsService customUserDetailsService, TwoFactorOtpService twoFactorOtpService, EmailService emailService, WatchListService watchListService) {
        this.userRepo = userRepo;
        this.customUserDetailsService = customUserDetailsService;
        this.twoFactorOtpService = twoFactorOtpService;
        this.emailService = emailService;
        this.watchListService = watchListService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody User user) throws Exception {

        User existingUser = userRepo.findByEmail(user.getEmail());

        if (existingUser != null) {
            throw new Exception("User already exists");
        }

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());


        User savedUser = userRepo.save(newUser);

        watchListService.createWatchList(savedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(),
                savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setStatus(true);
        authResponse.setMessage("User registered successfully");

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);

    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {

        String email = user.getEmail();
        String password = user.getPassword();


        Authentication authentication = authenticate(email, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);

        User authUser = userRepo.findByEmail(email);

        if(user.getTwoFactorAuth().isEnabled()) {
            AuthResponse auth = new AuthResponse();
            auth.setMessage("Two Factor authentication enabled");
            auth.setTwoFactorAuthEnabled(true);
            String otp = OtpUtils.generateOtp();

            TwoFactorOtp existing = twoFactorOtpService.findByUser(authUser.getId());

            if(existing != null) {
                twoFactorOtpService.deleteOtp(existing);
            }

            TwoFactorOtp newOtp = twoFactorOtpService.createOtp(authUser, otp, jwt);

            emailService.sendVerificationOtpEmail(email, otp);

            auth.setSession(newOtp.getId());

            return new ResponseEntity<>(auth, HttpStatus.ACCEPTED);
        }

        AuthResponse authResponse = new AuthResponse();

        authResponse.setJwt(jwt);
        authResponse.setStatus(true);
        authResponse.setMessage("User login successfully");

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    private Authentication authenticate(String username, String password) throws Exception {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if(userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }

        if(!password.equals(userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @PostMapping("/two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifyOtp(
            @PathVariable String otp,@RequestParam String id) throws Exception {

        TwoFactorOtp twoFactorOtp = twoFactorOtpService.findById(id);

        if (twoFactorOtpService.verifyOtp(twoFactorOtp, otp)) {
            AuthResponse auth = new AuthResponse();
            auth.setMessage("two factor authentication successful");
            auth.setTwoFactorAuthEnabled(true);
            auth.setJwt(twoFactorOtp.getJwt());
            return new ResponseEntity<>(auth, HttpStatus.ACCEPTED);
        }

        throw new Exception("Invalid OTP");
    }
}
