package edu.upc.dsa.proyectodsatest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST("usuario/adduser/")
    Call<User> addUser(@Body User u);

}
