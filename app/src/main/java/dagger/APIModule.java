package dagger;


import com.gamesearch.giantbomb.BuildConfig;

import javax.inject.Singleton;

import retrofit.RetrofitBuilder;

@Module
public class APIModule {

    @Provides
    @Singleton
    RetrofitBuilder getRetrofitAPI() {
        return new RetrofitBuilder(BuildConfig.DEBUG);
    }


}
