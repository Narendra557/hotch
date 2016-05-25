package au.com.hellopeople.hotch.register.interests;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import au.com.hellopeople.hotch.R;

/**
 * Created by DELL on 3/10/2016.
 */
public class SportsAdapter extends ArrayAdapter{

    List list = new ArrayList();

    public SportsAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(Sports  object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row;
        row = convertView;
        ProductHolder productHolder;
        productHolder = new ProductHolder();

        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_main_interest, parent, false);
            productHolder.lbl_main = (TextView)row.findViewById(R.id.listTitle);

            row.setTag(productHolder);
        }
        else {
            productHolder = (ProductHolder)row.getTag();
        }

        Sports sports = (Sports)this.getItem(position);
        productHolder.lbl_main.setText(sports.getMain());

        return row;
    }

    static class ProductHolder{
        TextView lbl_main;
    }
}
