package br.com.jet.app.todonotes.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.com.jet.app.todonotes.R;
import br.com.jet.app.todonotes.ui.activity.MainActivity;
import br.com.jet.app.todonotes.ui.activity.TaskEditorActivity;
import br.com.jet.app.todonotes.database.TaskDatabase;
import br.com.jet.app.todonotes.model.Task;
import br.com.jet.app.todonotes.ui.adapter.OnItemClick;
import br.com.jet.app.todonotes.ui.adapter.TaskAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TasksListFragment extends Fragment {

    private static final String TAG = "taskfrag";

    private TaskAdapter adapter;
    private RecyclerView tasksList;
    private RecyclerView.LayoutManager[] layoutManager = new RecyclerView.LayoutManager[]{new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL), new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false), new GridLayoutManager(getContext(), 3)};
    private ArrayList<Task> tasks;

    private TaskDatabase database;

    private View itemView;

    private int index;

    public static final int GRID_LAYOUT = 2;
    public static final int STAGGERED_GRID_LAYOUT = 0;
    public static final int LIST_LAYOUT = 1;

    private int currentLayout;

    public TasksListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = new TaskDatabase(getContext());

        tasks = (ArrayList<Task>) getArguments().getSerializable("tasks");
        //tasks = database.queryAll();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        itemView = inflater.inflate(R.layout.fragment_tasks_list, container, false);

        tasksList = itemView.findViewById(R.id.tasks_list);
        adapter = new TaskAdapter(getContext(), tasks);
        currentLayout = getLayoutSettingFromSharedPreferences();
        tasksList.setLayoutManager(layoutManager[getLayoutSettingFromSharedPreferences()]);
        tasksList.setAdapter(adapter);

        adapter.setOnItemClick(new OnItemClick() {

            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), TaskEditorActivity.class);
                intent.putExtra("task", tasks.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(int position) {
                index = position;
            }
        });

        return itemView;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "TO-DO: " + tasks.get(index).getTitle());
                intent.putExtra(Intent.EXTRA_TEXT, tasks.get(index).getContent());
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_via)));
                break;

            case 1:

                database.delete(tasks.get(index).getId());

                tasks = database.queryAll();

                if (tasks.isEmpty()) {
                    MainActivity activity = (MainActivity) getActivity();
                    if (activity != null) {
                        activity.callOnResume();
                    }
                }

                adapter.updateList(tasks);

                break;
        }
        return super.onContextItemSelected(item);
    }

    public void updateList(ArrayList<Task> t) {
        adapter.updateList(t);
    }

    public void changeListLayout(int layout) {
        saveLayoutSettingOnSharedPreferences(layout);
        tasksList.setLayoutManager(layoutManager[getLayoutSettingFromSharedPreferences()]);
        currentLayout = layout;
    }



    private int getLayoutSettingFromSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("layout_preference_5", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("layout_index_5", 0);
    }

    private void saveLayoutSettingOnSharedPreferences(int layout){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("layout_preference_5", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("layout_index_5", layout);
        editor.apply();
    }

    public int getCurrentLayout() {
        return currentLayout;
    }
}
