package br.com.jet.app.todonotes.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.jet.app.todonotes.R;
import br.com.jet.app.todonotes.model.EmptyState;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    private int resDrawable;
    private ImageView iconState;
    private TextView title, content;

    private EmptyState emptyState;

    public BlankFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        emptyState = (EmptyState) getArguments().getSerializable("empty_state");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View itemView = inflater.inflate(R.layout.fragment_blank, container, false);

        iconState = itemView.findViewById(R.id.state_icon);
        title = itemView.findViewById(R.id.state_title);
        content = itemView.findViewById(R.id.state_content);

        if (emptyState != null) {
            iconState.setImageResource(emptyState.getResDrawable());
            title.setText(emptyState.getTitle());
            content.setText(emptyState.getContent());
        }

        return itemView;

    }

}
