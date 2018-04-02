package dagger;


import com.gamesearch.giantbomb.SearchActivity;

import javax.inject.Singleton;

@Singleton
@Component(modules = APIModule.class)
public interface APIComponent {

    void inject(SearchActivity searchActivity);
}
