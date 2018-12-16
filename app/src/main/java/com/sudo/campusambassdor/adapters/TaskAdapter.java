
package com.sudo.campusambassdor.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sudo.campusambassdor.R;
import com.sudo.campusambassdor.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private ArrayList<Task> mTaskList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public TaskAdapter(ArrayList<Task> items) {
        mTaskList = items;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent,false);
        ViewHolder evh = new ViewHolder(v, mListener);
        return evh;


    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task currentItem = mTaskList.get(position);

        holder.title.setText(currentItem.getmTitle());
        holder.description.setText(currentItem.getmDescription());
        //Button
    }

    public interface ItemListener {
        void onItemClick(Task item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // TODO - Your view members
        public TextView title;
        public TextView description;
        public Button id;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
//            itemView.setOnClickListener(this);
            // TODO instantiate/assign view members
            title = itemView.findViewById(R.id.TaskTitle);
            description = itemView.findViewById(R.id.TaskDesc);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
        }

    }



                                