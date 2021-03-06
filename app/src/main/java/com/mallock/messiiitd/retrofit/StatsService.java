package com.mallock.messiiitd.retrofit;


import retrofit.Call;
import retrofit.http.GET;

public interface StatsService {

    @GET("getMostLikedFood.php/")
    Call<LikedFoodGet> getMostLikedFood();

    @GET("getLeastLikedFood.php/")
    Call<LikedFoodGet> getLeastLikedFood();

    @GET("getTotalLikes.php/")
    Call<TotalLikesGet> getTotalLikes();

    @GET("getTotalDislikes.php/")
    Call<TotalLikesGet> getTotalDislikes();
}
