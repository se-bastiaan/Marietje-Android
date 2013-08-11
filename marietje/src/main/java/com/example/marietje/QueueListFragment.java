package com.example.marietje;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marietje.QueueXMLParser.Entry;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class QueueListFragment extends ListFragment {
    private final String QUEUE_URL = "http://marietje.marie-curie.nl:8080/requests";
    private final int UPDATE_DELAY = 2000;
    private final int UPDATE_DELAY_ERROR = 10000;
    private boolean doUpdate;

    public void startUpdate() {
        final Handler handler = new Handler();
        doUpdate = true;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (doUpdate) {
                    Log.i("", "Doing an update...");

                    ArrayAdapter<String[]> adapter = null;

                    QueueXMLParser parser = new QueueXMLParser();

                    try {
                        InputStream stream = Utils.downloadUrl(QUEUE_URL);
                        List<Entry> entries = parser.parse(stream);
                        stream.close();

                        final List<String[]> data = new ArrayList<String[]>();

                        for (Entry entry : entries) {
                            data.add(new String[]{entry.title, entry.artist, entry.requester});
                        }

                        if (getActivity() == null) {
                            Log.e(QueueListFragment.class.getName(),
                                    "Application closing, so do not update queue");

                        } else {

                            // FIXME: create this here on in onPostExecute? This
                            // avoids getActivity() problems
                            adapter = new ArrayAdapter<String[]>(getActivity(),
                                    android.R.layout.simple_list_item_2,
                                    android.R.id.text1, data) {
                                @Override
                                public View getView(int position,
                                                    View convertView,
                                                    android.view.ViewGroup parent) {
                                    View row;
                                    if (convertView == null) {
                                        LayoutInflater inflater = (LayoutInflater) getActivity()
                                                .getSystemService(
                                                        Context.LAYOUT_INFLATER_SERVICE);
                                        row = inflater
                                                .inflate(
                                                        R.layout.queue_layout,
                                                        null);
                                    } else {
                                        row = convertView;
                                    }

                                    TextView v = (TextView) row
                                            .findViewById(R.id.text1); // fixme: android.R?
                                    v.setText(data.get(position)[0]);
                                    v = (TextView) row
                                            .findViewById(R.id.text2);
                                    v.setText(data.get(position)[1]);
                                    v = (TextView) row
                                            .findViewById(R.id.text3);
                                    v.setText(data.get(position)[2]);

                                    return row;
                                }

                                ;
                            };

                        }

                    } catch (XmlPullParserException e) {
                        Log.e(QueueListFragment.class.getName(),
                                "XML parsing of queue failed");
                    } catch (IOException e) {
                        Log.e(QueueListFragment.class.getName(),
                                "Could not read queue, connection errors?");
                    }

                    final ArrayAdapter<String[]> ada = adapter;

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            if (ada != null) {
                                // restore view position
                                int index = getListView().getFirstVisiblePosition();
                                View v = getListView().getChildAt(0);
                                int top = (v == null) ? 0 : v.getTop();

                                setListAdapter(ada);
                                ada.notifyDataSetChanged();
                                getListView().setSelectionFromTop(index, top);

                                Log.i("", "Finished update");
                            } else {
                                // display error
                                if (getActivity() != null) {
                                    Toast.makeText(getActivity(),
                                            "Kon de queue niet laden",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });

                    try {
                        if (ada == null) {
                            Thread.sleep(UPDATE_DELAY_ERROR);
                        } else {
                            Thread.sleep(UPDATE_DELAY);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        new Thread(r).start(); // start updating
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setEmptyText("Bezig met laden queue...");
        setHasOptionsMenu(true);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            Log.i("", "Visible");
            startUpdate();
        }
        else {
            Log.i("", "Queue updates disabled");
            doUpdate = false; // FIXME: does this guarantee that the thread stops?
        }

    }
}
