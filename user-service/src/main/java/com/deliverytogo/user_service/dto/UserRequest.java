package com.deliverytogo.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class UserRequest {

    @NotBlank
    private String fullName;

    @Email
    private String email;

    private List<AddressRequest> addresses;

    @Data
    public static class AddressRequest {
        private String street;
        private String city;
        private String zip;
    }
}
