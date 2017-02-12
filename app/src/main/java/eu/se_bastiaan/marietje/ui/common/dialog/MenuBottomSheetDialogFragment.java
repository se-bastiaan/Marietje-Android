package eu.se_bastiaan.marietje.ui.common.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import eu.se_bastiaan.marietje.R;
import eu.se_bastiaan.marietje.ui.common.decorator.DividerItemDecoration;

public class MenuBottomSheetDialogFragment extends BottomSheetDialogFragment implements MenuBottomSheetAdapter.Listener {

    private static final String ARG_ITEMS = "items";
    private Listener listener;

    public static MenuBottomSheetDialogFragment newInstance(List<Integer> items, Listener listener) {
        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_ITEMS, new ArrayList<>(items));
        MenuBottomSheetDialogFragment fragment = new MenuBottomSheetDialogFragment();
        fragment.setListener(listener);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sheet_menu, container, false);

        MenuBottomSheetAdapter adapter = new MenuBottomSheetAdapter(getArguments().getIntegerArrayList(ARG_ITEMS), this);

        RecyclerView recyclerView = ButterKnife.findById(v, R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        return v;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(int position, int item) {
        if (listener != null) {
            dismiss();
            listener.onItemClick(position, item);
        }
    }

    public interface Listener {
        void onItemClick(int position, @StringRes int item);
    }

}
