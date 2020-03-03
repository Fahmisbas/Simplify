package com.fahmisbas.simplify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fahmisbas.simplify.R;
import com.fahmisbas.simplify.database.ContractDB;
import com.fahmisbas.simplify.database.HelperDB;
import com.fahmisbas.simplify.utils.FontTypes;

public class EditNoteActivity extends AppCompatActivity {

    private TextView title;
    public EditText edtTitle, edtNote;
    private SQLiteDatabase database;
    boolean isEdtTextChanged = false;
    boolean isNewNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        title = findViewById(R.id.title);
        edtTitle = findViewById(R.id.edt_title);
        edtNote = findViewById(R.id.edt_note);

        edtTextTypeFace();
        setToolbar();
        edtTextChange();
    }

    void edtTextTypeFace() {
        SharedPreferences sharedPreferences = getSharedPreferences("com.fahmisbas.simplify", MODE_PRIVATE);
        String chosenTf = sharedPreferences.getString("font_preference", null);
        if (chosenTf != null) {
            switch (chosenTf) {
                case "Roboto":
                    new FontTypes(this).roboto(edtTitle,edtNote);
                    break;

                case "Sans-Serif":
                    new FontTypes(this).sansSerif(edtTitle,edtNote);
                    break;

                case "Monospace":
                    new FontTypes(this).monospace(edtTitle,edtNote);
                    break;
            }
        }
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            title.setText("Note");
        }
    }

    private void edtTextChange() {
        Intent intent = getIntent();
        long id = intent.getLongExtra("id", -1);
        String title = intent.getStringExtra("title");
        String note = intent.getStringExtra("note");

        if (id != -1) {
            edtTitle.setText(title);
            edtNote.setText(note);
            edtTextChangeListener();
        } else {
            isNewNote = true;
        }
    }

    private void edtTextChangeListener() {
        edtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                isEdtTextChanged = true;
            }
        });
        edtNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                isEdtTextChanged = true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backPressedFlag()) {
            super.onBackPressed();
        }
    }

    private boolean backPressedFlag() {
        if (isEdtTextChanged) {
            discardOrSave();
            return false;
        }
        if (isNewNote && !edtTitle.getText().toString().isEmpty() && !edtNote.getText().toString().isEmpty()) {
            saveNewNoteOrNot();
            return false;
        }
        return true;
    }

    private void saveNewNoteOrNot() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Do you want to save this note?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditNoteActivity.super.onBackPressed();
                    }
                }).setNegativeButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addData();
                        EditNoteActivity.super.onBackPressed();
                    }
                });
        builder.show();
    }

    private void discardOrSave() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Discrad or save?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditNoteActivity.super.onBackPressed();
                    }
                }).setNegativeButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = getIntent();
                        long id = intent.getLongExtra("id", -1);
                        updateData(id, edtTitle.getText().toString(), edtNote.getText().toString());
                        EditNoteActivity.super.onBackPressed();
                    }
                });
        builder.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        selectedItem(item);
        return super.onOptionsItemSelected(item);
    }

    private void selectedItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                Intent intent = getIntent();
                long id = intent.getLongExtra("id", -1);
                if (id != -1) {
                    updateData(id, edtTitle.getText().toString(), edtNote.getText().toString());
                    isEdtTextChanged = false;
                } else {
                    addData();
                    super.onBackPressed();
                }

                break;

            case R.id.delete:
                dialogDeleteItemPermission();
                break;
        }
    }

    private void dialogDeleteItemPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Delete This Item")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Are you sure you want to delete this?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = getIntent();
                        long id = intent.getLongExtra("id", -1);
                        deleteData(id);
                    }
                }).setNegativeButton("No", null);
        builder.show();
    }

    private void deleteData(long id) {
        HelperDB helperDB = new HelperDB(this);
        database = helperDB.getWritableDatabase();
        database.delete(ContractDB.EntryDB.TABLE_NAME,
                ContractDB.EntryDB._ID + "=" + id, null);
        MainActivity.adapter.swapCursor(getAllItem());
        Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void addData() {
        HelperDB helperDB = new HelperDB(this);
        database = helperDB.getWritableDatabase();
        String title = edtTitle.getText().toString();
        String note = edtNote.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(ContractDB.EntryDB.COLUMN_TITLE, title);
        cv.put(ContractDB.EntryDB.COLUMN_NOTE, note);
        database.insert(ContractDB.EntryDB.TABLE_NAME, null, cv);
        MainActivity.adapter.swapCursor(getAllItem());
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    private void updateData(long id, String title, String note) {
        HelperDB helperDB = new HelperDB(this);
        database = helperDB.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ContractDB.EntryDB.COLUMN_TITLE, title);
        cv.put(ContractDB.EntryDB.COLUMN_NOTE, note);
        database.update(ContractDB.EntryDB.TABLE_NAME, cv, "_id = ?", new String[]{String.valueOf(id)});
        MainActivity.adapter.swapCursor(getAllItem());
        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
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
