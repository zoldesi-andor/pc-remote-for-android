package hu.za.pc_remote.ui.remotecontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import hu.za.pc_remote.R;
import hu.za.pc_remote.layoutsmanagement.FileManager;
import hu.za.pc_remote.layoutsmanagement.LayoutListItem;

import java.util.List;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 10/23/11
 * Time: 8:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class SelectLayout extends Activity {

    private ArrayAdapter<LayoutListItem> adapter;
    private TextView empty;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectlayout);

        adapter = new ArrayAdapter<LayoutListItem>(this, R.layout.simplelistitem);

        empty = (TextView) findViewById(R.id.SelectLayoutNoResult);

        ListView listView = (ListView) findViewById(R.id.SelectLayoutListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ItemClieckedListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshAdapter();
    }

    private void refreshAdapter() {
        new Thread() {
            @Override
            public void run() {
                final List<LayoutListItem> list = FileManager.listFiles();
                runOnUiThread(
                        new Runnable() {
                            public void run() {
                                adapter.clear();
                                for (LayoutListItem item : list) {
                                    adapter.add(item);
                                }
                                empty.setVisibility(adapter.getCount() == 0 ? VISIBLE : INVISIBLE);
                            }
                        }
                );
            }
        }.start();
    }

    private class ItemClieckedListener implements ListView.OnItemClickListener {
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            final LayoutListItem item = adapter.getItem(i);
            Intent starterIntent = new Intent(SelectLayout.this, RemoteControl.class);
            starterIntent.putExtra(RemoteControl.LayoutItemKey, item);
            startActivity(starterIntent);
        }
    }
}