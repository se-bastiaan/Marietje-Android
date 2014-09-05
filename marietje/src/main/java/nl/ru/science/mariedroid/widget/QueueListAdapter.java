package nl.ru.science.mariedroid.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import nl.ru.science.mariedroid.R;
import nl.ru.science.mariedroid.network.objects.Request;

/**
 * Created by Sebastiaan on 05-09-14.
 */
public class QueueListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<Request> mRequests;

    class ViewHolder {
        public ViewHolder(View v) {
            ButterKnife.inject(this, v);
        }

        @InjectView(R.id.text1)
        TextView text1;
        @InjectView(R.id.text2)
        TextView text2;
        @InjectView(R.id.text3)
        TextView text3;
    }

    public QueueListAdapter(Context context, ArrayList<Request> requests) {
        super();
        mContext = context;
        mRequests = requests;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mRequests.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Request getItem(int position) {
        return mRequests.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_queue_listitem, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Request request = getItem(position);

        holder.text1.setText(request.title);
        holder.text2.setText(request.artist);
        holder.text3.setText(request.requester);

        if(position == 0) {
            convertView.setBackgroundResource(R.color.now_playing);
        } else {
            convertView.setBackgroundResource(android.R.color.transparent);
        }

        return convertView;
    }

}
