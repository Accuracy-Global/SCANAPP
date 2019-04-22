package cheemala.business.durgacameraapp.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cheemala.business.durgacameraapp.R;
import cheemala.business.durgacameraapp.adapter.ScannedDataAdapter;
import cheemala.business.durgacameraapp.database.AppDb;
import cheemala.business.durgacameraapp.model.ScannedTextData;
import cheemala.business.durgacameraapp.utils.DatabaseCallbacks;

public class DisplayDataActivity extends AppCompatActivity {

    private AppDb appDb;
    private TextView titlTxt;
    private ImageView historyImg;
    private RecyclerView scandLstRecyclrVw;
    private ScannedDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);
        initialiseViews();
    }

    private void initialiseViews() {
        appDb = new AppDb(this);
        scandLstRecyclrVw = findViewById(R.id.scand_list_rcyclrVw);
        titlTxt = findViewById(R.id.titl_txt);
        titlTxt.setText(getString(R.string.titl_hstry_txt));
        historyImg = findViewById(R.id.history_img);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (historyImg.getVisibility() == View.VISIBLE){
            historyImg.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchDtaFrmDbToDisplay();
    }

    private void fetchDtaFrmDbToDisplay() {

        List<ScannedTextData> alData = appDb.fetchAllScannedDta();
        if (alData != null && alData.size() > 0){
            adapter = new ScannedDataAdapter(DisplayDataActivity.this, alData, new DatabaseCallbacks() {
                @Override
                public void deleteRecord(String dBpostion, int listPosition) {
                    deleteUser(dBpostion,listPosition);
                }

                @Override
                public void editRecord(String position) {}
            });
            scandLstRecyclrVw.setLayoutManager(new LinearLayoutManager(this));
            scandLstRecyclrVw.setHasFixedSize(true);
            scandLstRecyclrVw.setAdapter(adapter);
        }else {
            Toast.makeText(DisplayDataActivity.this,"No data to display",Toast.LENGTH_LONG).show();
        }

    }

    private void deleteUser(String dBId, int listPosition) {
        if (appDb != null){
            if (appDb.deleteUser(dBId) != 0){
                Toast.makeText(DisplayDataActivity.this,"Item deleted successfully!",Toast.LENGTH_LONG).show();
                updateRecyclerVw(listPosition);
            }else {
                Toast.makeText(DisplayDataActivity.this,"Something went wrong!",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void updateRecyclerVw(int listPosition) {
        adapter.userDeleted(listPosition);
    }

}
