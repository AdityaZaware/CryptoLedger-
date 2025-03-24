package com.ben.StockTrading.controller;

import com.ben.StockTrading.entity.ForgotPassword;
import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.entity.VerificationCode;
import com.ben.StockTrading.enums.VeriFicationType;
import com.ben.StockTrading.request.ForgotPasswordRequest;
import com.ben.StockTrading.request.ResetPasswordRequest;
import com.ben.StockTrading.response.ApiResponse;
import com.ben.StockTrading.response.AuthResponse;
import com.ben.StockTrading.service.EmailService;
import com.ben.StockTrading.service.ForgotPasswordService;
import com.ben.StockTrading.service.UserService;
import com.ben.StockTrading.service.VerificationCodeService;
import com.ben.StockTrading.utils.OtpUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    private final VerificationCodeService verificationCodeService;
    private final ForgotPasswordService forgotPasswordService;

    public UserController(UserService userService, EmailService emailService, VerificationCodeService verificationCodeService, ForgotPasswordService forgotPasswordService) {
        this.userService = userService;
        this.emailService = emailService;
        this.verificationCodeService = verificationCodeService;
        this.forgotPasswordService = forgotPasswordService;
    }

    @GetMapping("/api/user/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/api/user/verfication/{verficationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(
            @RequestHeader("Authorization") String jwt,
            @PathVariable VeriFicationType verficationType) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode  verificationCode =
                verificationCodeService.getVerificationCodeByUserId(user.getId());

        if(verificationCode == null){
            verificationCode =
                    verificationCodeService.sendVerificationCode(user, verficationType);
        }

        if(verficationType.equals(VeriFicationType.EMAIL)){
            emailService.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtp());
        }

        return new ResponseEntity<>("Verification code sent", HttpStatus.OK);
    }

    @PatchMapping("/api/user/enableTwoFactorAuth/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuth(@RequestHeader("Authorization") String jwt,
    @PathVariable String otp) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUserId(user.getId());

        String sendTo = verificationCode.getVeriFicationType().
                equals(VeriFicationType.EMAIL) ? verificationCode.getEmail() : verificationCode.getMobile();

        boolean isVerified = verificationCode.getOtp().equals(otp);

        if(isVerified) {
            User updatedUser = userService
                    .enableTwoFactorAuth(verificationCode.getVeriFicationType(), sendTo, user);

            verificationCodeService.deleteVerificationCode(verificationCode);

            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }

        throw new Exception("Invalid OTP");
    }

    @PostMapping("/auth/user/resetPassword/send-otp/{verficationType}")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp(
            @PathVariable VeriFicationType verficationType,
            @RequestBody ForgotPasswordRequest request) throws Exception {

        User user = userService.findUserProfileByEmail(request.getSendTo());

        String otp = OtpUtils.generateOtp();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        ForgotPassword forgotPassword = forgotPasswordService.findByUser(user.getId());

        if (forgotPassword == null) {
            forgotPasswordService.createToken(user, id, otp,
                    request.getVeriFicationType(), request.getSendTo());
        }

        Long userid = user.getId();
        if(verficationType.equals(VeriFicationType.EMAIL)){
            emailService.sendVerificationOtpEmailForgotPassword(user.getEmail(), otp, userid);
        }

        AuthResponse authResponse = new AuthResponse();
        authResponse.setSession(forgotPassword.getId());
        authResponse.setMessage("Otp sent successfully");

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PatchMapping("/auth/user/resetPassword/{email}/verify-otp")
    public ResponseEntity<ApiResponse> resetPassword(
            @RequestParam Long otp,
            @RequestBody ResetPasswordRequest request,
            @PathVariable String email) throws Exception {


        ForgotPassword forgotPassword = forgotPasswordService.findByEmail(email);

        boolean isVerified = forgotPassword.getOtp().equals(request.getOtp());

        if(isVerified) {
            userService.updatePassword(forgotPassword.getUser(), request.getNewPassword());

            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage("Password updated successfully");

            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        }

        throw new Exception("Invalid OTP");
    }
}
