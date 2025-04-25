package com.example.learningenglishapp.api;

import com.example.learningenglishapp.model.LoginRequest;
import com.example.learningenglishapp.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("v1/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}