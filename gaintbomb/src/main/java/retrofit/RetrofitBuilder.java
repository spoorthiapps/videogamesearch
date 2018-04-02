package retrofit;


import com.gamesearch.gaintbomb.api.GiantBombAPI;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    private final boolean isDebug;
    private static final String BASE_URL = "https://www.giantbomb.com/api/";
    private static final String API_KEY = "api_key";
    private static final String API_VALUE = "32fe555ab822a34a4b44ab4b9bdc7f7597ac71d9";
    private static final String RESPONSE_FORMAT_KEY = "format";
    private static final String RESPONSE_FORMAT_VALUE = "json";

    public RetrofitBuilder(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public GiantBombAPI getGiantBombAPI() {
        Retrofit retrofit = createRetrofit();
        return retrofit.create(GiantBombAPI.class);
    }

    private Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build();
    }

    private OkHttpClient createOkHttpClient() {
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                final Request original = chain.request();
                final HttpUrl originalHttpUrl = original.url();

                final HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter(API_KEY, API_VALUE)
                        .addQueryParameter(RESPONSE_FORMAT_KEY, RESPONSE_FORMAT_VALUE)
                        .build();

                final Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                final Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        setUpLogging(httpClient);

        return httpClient.build();
    }

    private void setUpLogging(OkHttpClient.Builder httpClient) {
        if (isDebug) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }
    }

}
