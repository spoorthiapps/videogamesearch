package fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gamesearch.giantbomb.R;

public class ErrorFragment extends Fragment{


    private static final String ERROR_MESSAGE_EXTRA = "errorMessage";

    public static ErrorFragment newInstance(String errorMessage) {
        ErrorFragment myFragment = new ErrorFragment();
        Bundle args = new Bundle();
        args.putString(ERROR_MESSAGE_EXTRA, errorMessage);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.error_fragment_layout, container, false);
        Bundle arguments = getArguments();
        if (arguments != null && arguments.getString(ERROR_MESSAGE_EXTRA) != null) {
            ((TextView)view.findViewById(R.id.errorTextView)).setText(arguments.getString(ERROR_MESSAGE_EXTRA));
        }
        return view;
    }
}
