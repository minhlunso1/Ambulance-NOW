package pinride.minhna.submission.ambulancenow.compo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import pinride.minhna.submission.ambulancenow.R;
import pinride.minhna.submission.ambulancenow.module.Individual;

/**
 * Created by Minh on 3/26/2016.
 */
public class AmbulanceAdapter extends ArrayAdapter<Individual> {

    private Context context;
    private ItemClickListener listener;

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public AmbulanceAdapter(Context context, ArrayList<Individual> items, ItemClickListener listener) {
        super(context, 0, items);
        this.context=context;
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Individual item = getItem(position);
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_driver_request_new, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tv_ambulance);
        TextView tvAddress = (TextView) convertView.findViewById(R.id.tv_ambulance_addr);
        Button btnReq = (Button) convertView.findViewById(R.id.btn_request);

        btnReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);
            }
        });
        tvName.setText(item.getName());
        tvAddress.setText(item.getAddress());

        return convertView;
    }
}
