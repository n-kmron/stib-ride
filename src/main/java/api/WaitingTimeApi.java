package api;

import api.format.WaitingTimeResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface WaitingTimeApi {
    @GET("search/")
    Call<WaitingTimeResponse> getWaitingTimes(
            @Query("dataset") String dataset,
            @Query("q") String query,
            @Query("rows") int rows,
            @Query("refine.pointid") int pointId
    );
}
