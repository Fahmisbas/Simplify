package com.fahmisbas.simplify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fahmisbas.simplify.database.ContractDB;
import com.fahmisbas.simplify.database.HelperDB;
import com.fahmisbas.simplify.utils.ImplicitIntents;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import de.cketti.mailto.EmailIntentBuilder;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private SQLiteDatabase database;
    public static NoteAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);

        setToolbar();
        initDatabase();
        navigationView();
        floatingActionButton();
        iniRecyclerView();
        onNoteItemClick();
        onLongNoteItemClick();
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void initDatabase() {
        HelperDB helperDB = new HelperDB(this);
        database = helperDB.getWritableDatabase();
    }

    private void navigationView() {
        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_send:
                        ImplicitIntents.emailIntent(getApplicationContext());
                        break;

                    case R.id.nav_settings:
                        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_share:
                        ImplicitIntents.share(getApplicationContext());
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        actionbarToogle();

    }

    private void actionbarToogle() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void floatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditNote.class);
                startActivity(intent);

            }
        });
    }

    private void iniRecyclerView() {
        RecyclerView rvNote = findViewById(R.id.rvNote);
        adapter = new NoteAdapter(getAllItem());
        rvNote.setAdapter(adapter);

    }

    private void onNoteItemClick() {
        adapter.setOnNoteItemClick(new NoteAdapter.OnNoteItemClick() {
            @Override
            public void onNoteItemClickListener(String title, String note, long id) {
                Intent intent = new Intent(getApplicationContext(), EditNote.class);
                intent.putExtra("title", title);
                intent.putExtra("note", note);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    private void onLongNoteItemClick(){
        adapter.setOnNoteItemLongClick(new NoteAdapter.OnNoteItemLongClick() {
            @Override
            public void onNoteItemLongClickListener(View view,long id) {
               dialogDeleteItemPermission(id);

            }
        });
    }

    private void dialogDeleteItemPermission(final long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Delete This Item")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Are you sure you want to delete this?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData(id);
                    }
                }).setNegativeButton("No",null);
        builder.show();
    }

    private void deleteData(long id) {
        HelperDB helperDB = new HelperDB(this);
        database = helperDB.getWritableDatabase();
        database.delete(ContractDB.EntryDB.TABLE_NAME,
                ContractDB.EntryDB._ID + "=" + id,null);
        MainActivity.adapter.swapCursor(getAllItem());
    }

    private Cursor getAllItem() {
        return database.query(ContractDB.EntryDB.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ContractDB.EntryDB.TIMESTAMP + " DESC");
    }
}
