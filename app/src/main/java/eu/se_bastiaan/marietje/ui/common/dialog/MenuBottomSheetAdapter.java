package eu.se_bastiaan.marietje.ui.common.dialog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.se_bastiaan.marietje.R;

public class MenuBottomSheetAdapter extends RecyclerView.Adapter<MenuBottomSheetAdapter.ViewHolder> {

    private List<String> items;
    private Listener listener;

    public MenuBottomSheetAdapter(List<String> items, Listener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sheet_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemTextView.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_text)
        TextView itemTextView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> {
                listener.onItemClick(getAdapterPosition(), items.get(getAdapterPosition()));
            });
        }

    }

    public interface Listener {
        void onItemClick(int position, String item);
    }
}
