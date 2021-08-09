package br.com.jet.app.todonotes.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.jet.app.todonotes.R;
import br.com.jet.app.todonotes.database.TaskDatabase;
import br.com.jet.app.todonotes.model.EmptyState;
import br.com.jet.app.todonotes.model.Task;
import br.com.jet.app.todonotes.ui.fragment.BlankFragment;
import br.com.jet.app.todonotes.ui.fragment.TasksListFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "mainactivity";

    private ArrayList<Task> tasks;
    private TaskDatabase database;

    private FloatingActionButton newTaskButton;

    private BlankFragment blankFragment;
    private TasksListFragment tasksListFragment;

    private FragmentManager supportFragmentManager;

    private SparseIntArray sparseIntArray = new SparseIntArray();
    private int layoutType;

    private Bundle args = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sparseIntArray.append(0, R.drawable.ic_dashboard_24dp);
        sparseIntArray.append(1, R.drawable.ic_view_list_24dp);
        sparseIntArray.append(2, R.drawable.ic_view_module_24dp);

        supportFragmentManager = getSupportFragmentManager();

        database = new TaskDatabase(this);
        //database.clear();

        tasks = new ArrayList<>();

        newTaskButton = findViewById(R.id.insert_task_fab);
        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TaskEditorActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

        blankFragment = new BlankFragment();
        tasksListFragment = new TasksListFragment();

        tasks = database.queryAll();

        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();

        if (tasks.isEmpty()) {
            args.putSerializable("empty_state", new EmptyState(R.drawable.ic_add_circle_128dp, getResources().getString(R.string.empty_state_title), getResources().getString(R.string.empty_state_message)));
            blankFragment.setArguments(args);
            fragmentTransaction.replace(R.id.empty_state_fragment, blankFragment);
            fragmentTransaction.commit();
        } else {
            Bundle args = new Bundle();
            args.putSerializable("tasks", tasks);
            tasksListFragment.setArguments(args);
            fragmentTransaction.replace(R.id.empty_state_fragment, tasksListFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.getItem(0);
        SearchView searchView = (SearchView) searchItem.getActionView();

        MenuItem layoutItem = menu.getItem(1);
        layoutItem.setIcon(R.drawable.ic_view_list_24dp);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                onResume();
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                tasks = database.queryBy(s);
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                if (!tasks.isEmpty()) {
                    tasksListFragment.updateList(tasks);
                    onResume();
                } else {
                    BlankFragment noneResultState = new BlankFragment();
                    args.putSerializable("empty_state", new EmptyState(R.drawable.ic_pageview_128dp, getResources().getString(R.string.empty_state_search_title), getResources().getString(R.string.empty_state_search_message)));
                    noneResultState.setArguments(args);

                    fragmentTransaction.replace(R.id.empty_state_fragment, noneResultState);
                    fragmentTransaction.commit();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                tasks = database.queryBy(s);
                if (!tasks.isEmpty()) {
                    tasksListFragment.updateList(tasks);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(this, "This feature is not implemented yet", Toast.LENGTH_SHORT).show();
                break;
            case R.id.order_by:
                if (!tasks.isEmpty()) {
                    ++layoutType;
                    if (layoutType <= 2) {
                        if (layoutType == 1) {
                            item.setIcon(sparseIntArray.get(layoutType));
                        } else if (layoutType == 2) {
                            item.setIcon(sparseIntArray.get(layoutType));
                        }
                        tasksListFragment.changeListLayout(layoutType);
                    } else {
                        layoutType = 0;
                        tasksListFragment.changeListLayout(layoutType);
                        item.setIcon(sparseIntArray.get(layoutType));
                    }
                } else {
                    Toast.makeText(this, getResources().getString(R.string.none_to_order), Toast.LENGTH_SHORT).show();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void callOnResume() {
        onResume();
    }

}
