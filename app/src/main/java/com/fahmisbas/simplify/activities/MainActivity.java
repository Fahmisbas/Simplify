package com.fahmisbas.simplify.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fahmisbas.simplify.R;
import com.fahmisbas.simplify.adapter.NoteAdapter;
import com.fahmisbas.simplify.database.Crud;
import com.fahmisbas.simplify.database.HelperDB;
import com.fahmisbas.simplify.utils.FontTypes;
import com.fahmisbas.simplify.utils.ImplicitIntents;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private SQLiteDatabase database;
    public static NoteAdapter adapter;
    private Crud crud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        crud = new Crud(getApplicationContext());

        setup();

    }

    private void setup() {
        setToolbar();
        initDatabase();
        navigationView();
        floatingActionButton();
        iniRecyclerView();
        onNoteItemClick();
        onLongNoteItemClick();
        setTypefacePreference();
    }

    private void setTypefacePreference() {
        adapter.setOnTypeFaceChange(new NoteAdapter.OnTypeFaceChange() {
            @Override
            public void typfaceChange(TextView title, TextView note) {
                SharedPreferences sharedPreferences = getSharedPreferences("com.fahmisbas.simplify", MODE_PRIVATE);
                String chosenTf = sharedPreferences.getString("font_preference", null);
                if (chosenTf != null) {
                    switch (chosenTf) {
                        case "Roboto":
                            new FontTypes(getApplicationContext()).roboto(title, note);
                            note.setTextSize(15);
                            break;
                        case "Open Sans":
                            new FontTypes(getApplicationContext()).openSans(title, note);
                            title.setTextSize(18);
                            note.setTextSize((float) 17.5);
                            break;
                        case "Monospace":
                            new FontTypes(getApplicationContext()).monospace(title, note);
                            note.setTextSize(15);
                            break;
                        case "Raleway":
                            new FontTypes(getApplicationContext()).raleway(title, note);
                            break;
                    }
                }
            }
        });
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
        final ImplicitIntents impIntent = new ImplicitIntents(getApplicationContext());
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_send:
                        impIntent.emailIntent("fahmisulaimanbas@gmail.com", "Simplify - Minimal Note");
                        break;

                    case R.id.nav_settings:
                        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_share:
                        impIntent.share("Download this app : ", "Simple, Minimal note app");
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
                Intent intent = new Intent(getApplicationContext(), EditNoteActivity.class);
                startActivity(intent);

            }
        });
    }

    private void iniRecyclerView() {
        RecyclerView rvNote = findViewById(R.id.rvNote);
        adapter = new NoteAdapter(crud.readData());
        rvNote.setAdapter(adapter);

    }

    private void onNoteItemClick() {
        adapter.setOnNoteItemClick(new NoteAdapter.OnNoteItemClick() {
            @Override
            public void onNoteItemClickListener(String title, String note, long id) {
                Intent intent = new Intent(getApplicationContext(), EditNoteActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("note", note);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    private void onLongNoteItemClick() {
        adapter.setOnNoteItemLongClick(new NoteAdapter.OnNoteItemLongClick() {
            @Override
            public void onNoteItemLongClickListener(View view) {
                dialogDeleteItemPermission((Long) view.getTag());

            }
        });
    }

    private void dialogDeleteItemPermission(final long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Delete this note")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Are you sure you want to delete this?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        crud.deleteData(id);
                    }
                }).setNegativeButton("No", null);
        builder.show();
    }
}
