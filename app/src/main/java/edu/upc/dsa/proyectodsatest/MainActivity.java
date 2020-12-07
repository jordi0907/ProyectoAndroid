package edu.upc.dsa.proyectodsatest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    //crear retrofit
    Retrofit retrofit;

    //Para crear el recycler
    private RecyclerView recyclerView;
    //  private RecyclerView.Adapter mAdapter;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //para la interfaz
    UserService userService;

    User usuario;
    List<Objetos> objetosList;

    public TextView editTextName, editTextPassword ;
    public String nameUser,passwordUser;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Esto es configuracion del recycler
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        editTextName = this.findViewById(R.id.editTextName);
        editTextPassword = this.findViewById(R.id.editTextPassword);


        //Configuracion del retrofit
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //Attaching Interceptor to a client
        OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/dsaApp/")
//                .baseUrl("http://147.83.7.204:8080/dsaApp/")
                // .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        userService = retrofit.create(UserService.class);
    }

    //Notifica mensajes
    private void NotifyUser(String MSG){
        Toast toast = Toast.makeText(MainActivity.this,MSG,Toast.LENGTH_SHORT);
        toast.show();
    }
    public void onButtonRegistrarClick (View view) {
        //Retrofit Implementation on Button Press
        //Adding Interceptor
        nameUser = editTextName.getText().toString();
        passwordUser = editTextPassword.getText().toString();
        User usuariotmp = new User();
        usuariotmp.setUsername(nameUser);
        usuariotmp.setPassword(passwordUser);
        try {
            Call<User> usersCall = userService.addUser(usuariotmp);
            /* Android Doesn't allow synchronous execution of Http Request and so we must put it in queue*/
            usersCall.enqueue(new Callback<User>() {

                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 201) {
                        NotifyUser("Successful");
                        MainActivity.this.usuario = response.body();
                        NotifyUser("Usuario" +usuario);
                        objetosList = usuario.objetosList;
                        NotifyUser("objetos" + objetosList);
                        Log.d("MYAPP", "La lista de objetos es"+objetosList);
                    /*    mAdapter = new MyAdapter(objetosList);
                        recyclerView.setAdapter(mAdapter);*/
                        buildRecyclerView();
                        //Lanzar una nueva actividad con otra pantalla
                    }
                    if (response.code() == 409) {NotifyUser("User Duplicado , Inserta de nuevo");}
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    NotifyUser("Error Server");
                }

            });
        } catch (Exception e) {
            NotifyUser("Exception: " + e.toString());
        }


    }

    private void buildRecyclerView(){
        // define an adapter y le paso el contenido que tiene que adaptar
        mAdapter = new MyAdapter(objetosList);
        recyclerView.setAdapter(mAdapter);
        //para coger la posicion del item que he clicado y llamoa la interfaz del adaptador

        mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                NotifyUser("Posicion clickada" +position);
                //lanzarias el Objetos Activity que tendra la descripcion detallada del objeto
                //ObjetosActivity(position,false);
            }


        });
    }














}