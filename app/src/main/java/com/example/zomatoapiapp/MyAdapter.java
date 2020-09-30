package com.example.zomatoapiapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements Filterable {
private List<ListItem>listItems;
    private List<ListItem> listItemListFull;
private Context context;

    public MyAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
        listItemListFull= new ArrayList<ListItem>(listItems);
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_main,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ListItem listitem=listItems.get(position);
        holder.textName.setPaintFlags( Paint.UNDERLINE_TEXT_FLAG);
        holder.textName.setText(listitem.getName()+"\n");
        holder.textCuisines.setText("Cuisines:-\t"+listitem.getCuisines()+"\n");
        holder.textTimings.setText("Timings:-\t"+listitem.getTimings()+"\n");
        holder.textRatings.setText("Ratings:-\t"+listitem.getUser_rating()+"\n");
        holder.textPhoneNumbers.setText("Phone Number:-\t"+listitem.getPhone_number()+"\n");
        holder.No_Rating.setText("Total Number of People Voted:-\t"+listitem.getNo_Rating()+"\n");
        holder.address.setText("Address:-\t"+listitem.getAddress()+"\n");
        holder.city.setText("City-\t"+listitem.getCity()+"\n");
//Log.i("URl",listitem.getUrl());
holder.linearLayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Log.i("URl",listitem.getUrl());
        Intent intent=new Intent(context,Web_View_Zomato.class);
        intent.putExtra("url",listitem.getUrl());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);

        context.startActivity(intent);



    }
});
    }
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textName;
        public TextView textCuisines;
        public TextView textTimings;
        public TextView textRatings;
        public TextView textPhoneNumbers;
         public TextView No_Rating;
         public LinearLayout linearLayout;
public TextView address;
public TextView city;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName=itemView.findViewById(R.id.textName);
            textCuisines=itemView.findViewById(R.id.textCuisines);
            textTimings=itemView.findViewById(R.id.textTimings);
            textRatings=itemView.findViewById(R.id.textRatings);
            textPhoneNumbers=itemView.findViewById(R.id.textPhoneNumbers);
            No_Rating=itemView.findViewById(R.id.No_Rating);
            address=itemView.findViewById(R.id.address);
            city=itemView.findViewById(R.id.city);
            linearLayout=itemView.findViewById(R.id.linearLayout);
        }
    }

    private Filter exampleFilter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ListItem> filteredListx=new ArrayList<>();
            if (constraint==null||constraint.length()==0)
            {
                filteredListx.addAll(listItemListFull);
            }
            else
            {
                String filterPattern=constraint.toString().toLowerCase().trim();//trim =empty space remover
                for (ListItem item:listItemListFull)
                {
                    if (item.getName().toLowerCase().startsWith(filterPattern))
                    {
                        filteredListx.add(item);
                    }

                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredListx;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listItems.clear();
            listItems.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}
