package br.com.jet.app.todonotes.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.jet.app.todonotes.R;
import br.com.jet.app.todonotes.model.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> tasks;
    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public TaskAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TaskViewHolder(LayoutInflater.from(context).inflate(R.layout.task_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int i) {
        taskViewHolder.bind(tasks.get(i));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private CardView cardLayout;
        private TextView taskTitle, taskContent;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            cardLayout = itemView.findViewById(R.id.card_layout);
            taskTitle = itemView.findViewById(R.id.task_title);
            taskContent = itemView.findViewById(R.id.task_content);

            itemView.setOnCreateContextMenuListener(this);

            if (onItemClick != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClick.onItemClick(getAdapterPosition());
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemClick.onLongItemClick(getAdapterPosition());
                        return false;
                    }
                });
            }

        }

        public void bind(Task task) {

            //TODO - Change this switch by putting this on Task Object
            switch (task.getPriority()) {
                case 0:
                    cardLayout.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.highPriority));
                    break;

                case 1:
                    cardLayout.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.mediumPriority));
                    break;

                case 2:
                    cardLayout.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.lowPriority));
                    break;
            }

            taskTitle.setText(task.getTitle());
            taskContent.setText(task.getContent());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, 0, 0, R.string.share);
            menu.add(0, 1, 1, R.string.delete);
        }
    }


    public void updateList(ArrayList<Task> taskList) {
        if (taskList != null) {
            tasks.clear();
            tasks.addAll(taskList);
            this.notifyDataSetChanged();
        }
    }
}
