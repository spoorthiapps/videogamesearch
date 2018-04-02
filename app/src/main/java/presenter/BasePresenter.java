package presenter;


import io.reactivex.disposables.CompositeDisposable;

public abstract class BasePresenter {

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void unsubscribe() {
        compositeDisposable.dispose();
    }

}
