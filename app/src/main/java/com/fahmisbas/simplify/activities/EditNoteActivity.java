package com.fahmisbas.simplify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.fahmisbas.simplify.R;
import com.fahmisbas.simplify.database.Crud;
import com.fahmisbas.simplify.utils.FontTypes;
import com.fahmisbas.simplify.utils.ImplicitIntents;

public class EditNoteActivity extends AppCompatActivity {

    private TextView title;
    private EditText edtTitle, edtNote;
    boolean isEdtTextChanged = false;
    boolean isNewNote = false;
    boolean autoSave = false;
    private Crud crud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        title = findViewById(R.id.title);
        edtTitle = findViewById(R.id.edt_title);
        edtNote = findViewById(R.id.edt_note);
        crud = new Crud(getApplicationContext());
        edtTextTypeFace();
        setToolbar();
        edtTextChange();
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

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    private void edtTextTypeFace() {
        SharedPreferences sharedPreferences = getSharedPreferences("com.fahmisbas.simplify", MODE_PRIVATE);
        String chosenTf = sharedPreferences.getString("font_preference", null);
        if (chosenTf != null) {
            switch (chosenTf) {
                case "Roboto":
                    new FontTypes(this).roboto(edtTitle, edtNote);
                    break;

                case "Open Sans":
                    new FontTypes(this).openSans(edtTitle, edtNote);
                    break;

                case "Monospace":
                    new FontTypes(this).monospace(edtTitle, edtNote);
                    break;

                case "Raleway":
                    new FontTypes(this).raleway(edtTitle, edtNote);
                    break;
            }
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
        if (saveOrUpdateOnBackPressed()) {
            super.onBackPressed();
        }
    }

    private boolean saveOrUpdateOnBackPressed() {
        SharedPreferences sharedPreferences = getSharedPreferences("com.fahmisbas.simplify", MODE_PRIVATE);
        boolean isAutoSave = sharedPreferences.getBoolean("save_preference", false);
        if (isAutoSave) {
            Intent intent = getIntent();
            long id = intent.getLongExtra("id", -1);
            if (id != -1) {
                crud.updateData(id, edtTitle.getText().toString(), edtNote.getText().toString());
                return true;
            } else {
                crud.addData(edtTitle.getText().toString(), edtNote.getText().toString());
                return true;
            }
        } else {
            if (isEdtTextChanged && !autoSave) {
                discardOrSave();
                return false;
            }

            if (isNewNote && !edtTitle.getText().toString().isEmpty() && !edtNote.getText().toString().isEmpty() && !autoSave) {
                saveNewNoteOrNot();
                return false;
            }
            return true;
        }
    }

    private void saveNewNoteOrNot() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Want to save this note?")
                .setMessage("Any unsaved changes will be lost!")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditNoteActivity.super.onBackPressed();
                    }
                }).setNegativeButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        crud.addData(edtTitle.getText().toString(), edtNote.getText().toString());
                        EditNoteActivity.super.onBackPressed();
                    }
                });
        builder.show();
    }

    private void discardOrSave() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Want to save this changes?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Any unsaved changes will be lost!")
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
                        crud.updateData(id, edtTitle.getText().toString(), edtNote.getText().toString());
                        EditNoteActivity.super.onBackPressed();
                    }
                });
        builder.show();
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
                    crud.updateData(id, edtTitle.getText().toString(), edtNote.getText().toString());
                    isEdtTextChanged = false;
                } else {
                    crud.addData(edtTitle.getText().toString(), edtNote.getText().toString());
                    super.onBackPressed();
                }
                break;

            case R.id.delete:
                dialogDeleteItemPermission();
                break;

            case R.id.share:
                new ImplicitIntents(this).share(edtTitle.getText().toString(), edtNote.getText().toString());
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
                        crud.deleteData(id);
                    }
                }).setNegativeButton("No", null);
        builder.show();
    }
}
