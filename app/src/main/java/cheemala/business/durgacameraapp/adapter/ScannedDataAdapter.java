package cheemala.business.durgacameraapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import cheemala.business.durgacameraapp.R;
import cheemala.business.durgacameraapp.model.ScannedTextData;
import cheemala.business.durgacameraapp.utils.DatabaseCallbacks;

public class ScannedDataAdapter extends RecyclerView.Adapter<ScannedDataAdapter.ViewDataHolder> {

    private List<ScannedTextData> alData;
    private Context context;
    private DatabaseCallbacks dbCallBcks;

    public ScannedDataAdapter(Context context,List<ScannedTextData> alData,DatabaseCallbacks dbCallBcks){
        this.context = context;
        this.alData = alData;
        this.dbCallBcks = dbCallBcks;
    }

    @NonNull
    @Override
    public ViewDataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.scaned_data_lst_item,viewGroup,false);
        return new ViewDataHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewDataHolder viewDataHolder, final int position) {
        viewDataHolder.timStmpTxt.setText(alData.get(position).getTimeStamp());
        viewDataHolder.scandTxt.setText(alData.get(position).getScannedTxt());
        viewDataHolder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to delete?");
                builder.setCancelable(true);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbCallBcks.deleteRecord(alData.get(position).getId(),position);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return alData.size();
    }

    public class ViewDataHolder extends RecyclerView.ViewHolder {

        private TextView timStmpTxt,scandTxt;
        private ImageView deleteImg;

        public ViewDataHolder(@NonNull View itemView) {
            super(itemView);
            timStmpTxt = itemView.findViewById(R.id.tim_stmp_txt);
            scandTxt = itemView.findViewById(R.id.scand_txt);
            deleteImg = itemView.findViewById(R.id.delete_img);
            scandTxt.setPaintFlags(scandTxt.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
        }

    }

    public void userDeleted(int postion){
        alData.remove(postion);
        notifyDataSetChanged();
    }

}
