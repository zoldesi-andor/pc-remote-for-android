package hu.za.pc_remote.ui.layoutmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import hu.za.pc_remote.R;
import hu.za.pc_remote.layoutsmanagement.FileManager;
import hu.za.pc_remote.layoutsmanagement.LayoutListItem;
import hu.za.pc_remote.layoutsmanagement.NetworkManager;

import java.util.List;

import static android.view.View.*;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 10/20/11
 * Time: 9:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class LayoutDownloader extends Activity {

    private ListView listView = null;
    private TextView empty = null;
    ArrayAdapter<LayoutListItem> adapter = null;
    NetworkManager networkManager = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutdownloader);

        adapter = new ArrayAdapter<LayoutListItem>(LayoutDownloader.this, R.layout.simplelistitem);

        empty = (TextView) findViewById(R.id.LayoutDownloaderNoNewTextView);
        listView = (ListView) findViewById(R.id.LayoutDownloaderListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new SaveClickListener());

        Button refreshButton = (Button) findViewById(R.id.refreshLayoutsButton);
        refreshButton.setOnClickListener(new RefreshListener());

    }

    private void refreshAdapter() {
        if (networkManager == null)
            return;

        final ProgressDialog dialog = ProgressDialog.show(this, "", getString(R.string.loading), true);

        new Thread() {
            @Override
            public void run() {
                final List<LayoutListItem> l = networkManager.listLayouts();
                runOnUiThread(
                        new Runnable() {
                            public void run() {
                                adapter.clear();
                                if (l != null)
                                    for (LayoutListItem item : l) {
                                        adapter.add(item);
                                    }
                                dialog.dismiss();
                                empty.setVisibility(adapter.getCount() == 0 ? VISIBLE : INVISIBLE);
                            }
                        }
                );
            }
        }.start();
    }

    private class RefreshListener implements View.OnClickListener {

        public void onClick(View view) {
            EditText editText = (EditText) findViewById(R.id.UrlEditText);
            final String hostUrl = editText.getText().toString();
            networkManager = new NetworkManager(hostUrl);

            refreshAdapter();
        }
    }

    public class SaveClickListener implements ListView.OnItemClickListener {

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            final LayoutListItem item = adapter.getItem(i);
            if (FileManager.isStorageWritable()) {
                if (networkManager != null) {
                    new Thread() {
                        @Override
                        public void run() {
                            networkManager.saveLayout(item.getId());
                            runOnUiThread(
                                    new Runnable() {
                                        public void run() {
                                            refreshAdapter();
                                            Toast.makeText(
                                                    LayoutDownloader.this,
                                                    getString(R.string.downloadSuccessful),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );
                        }
                    }.start();
                }
            } else {
                adapter.clear();
                empty.setVisibility(VISIBLE);
                Toast.makeText(
                        LayoutDownloader.this,
                        getText(R.string.StorageNotAvailable),
                        Toast.LENGTH_SHORT
                );
            }
        }
    }
}