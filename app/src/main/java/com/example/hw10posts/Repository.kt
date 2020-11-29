package com.example.hw10posts

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.hw10posts.api.API
import com.example.hw10posts.api.AuthRequestParams
import com.example.hw10posts.api.CreatePostRequest
import com.example.hw10posts.api.RegistrationRequestParams
import com.example.hw10posts.api.interceptor.InjectAuthTokenInterceptor
import java.util.concurrent.TimeUnit


object Repository {

    private var retrofit: Retrofit =
            Retrofit.Builder()
                    .baseUrl("https://netology-back-end-post-hw-8.herokuapp.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(
                            OkHttpClient().newBuilder()
                                    .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                                    .connectTimeout(1, TimeUnit.MINUTES)
                                    .writeTimeout(1, TimeUnit.MINUTES)
                                    .readTimeout(1, TimeUnit.MINUTES)
                                    .build()
                    )
                    .build()


    // Добавление interceptor-ов в retrofit клиент. Во все последующие запросы будет добавляться токен
    // и они будут логироваться

    fun createRetrofitWithAuth(authToken: String) {
        val httpLoggerInterceptor = HttpLoggingInterceptor()
        // Указываем, что хотим логировать тело запроса.
        httpLoggerInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
                .addInterceptor(InjectAuthTokenInterceptor(authToken))
                .addInterceptor(httpLoggerInterceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build()

        retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl("https://netology-back-end-post-hw-8.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        //создаем API на основе нового retrofit-клиента
        API = retrofit.create(com.example.hw10posts.api.API::class.java)
    }

    // Ленивое создание API
    private var API: API =
            retrofit.create(com.example.hw10posts.api.API::class.java)


    suspend fun authenticate(login: String, password: String) = API.authenticate(
            AuthRequestParams(login, password)
    )

    suspend fun register(login: String, password: String) =
            API.register(
                    RegistrationRequestParams(
                            login,
                            password
                    )
            )

    suspend fun createPost(content: String) = API.createPost(CreatePostRequest(content = content))

    suspend fun getPosts() = API.getPosts()

    suspend fun likedByMe(id: Int) = API.likedByMe(id)

    suspend fun cancelMyLike(id: Int) = API.cancelMyLike(id)
}