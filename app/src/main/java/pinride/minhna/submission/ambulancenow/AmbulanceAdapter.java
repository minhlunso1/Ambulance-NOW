package pinride.minhna.submission.ambulancenow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import pinride.minhna.submission.ambulancenow.module.Individual;

/**
 * Created by Minh on 3/26/2016.
 */
public class AmbulanceAdapter extends ArrayAdapter<Individual> {

    private Context context;

    public AmbulanceAdapter(Context context, ArrayList<Individual> items) {
        super(context, 0, items);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Individual item = getItem(position);
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_driver_request_new, parent, false);
        }

        CircleImageView img = (CircleImageView) convertView.findViewById(R.id.img_avatar);
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_ambulance);

        Picasso.with(context)
                .load(item.getImgUrl())
                .placeholder(R.drawable.fa_ambulance_256_0_ffe57f_none)
                .into(img);
        tvName.setText(item.getName());

        return convertView;
    }
}
