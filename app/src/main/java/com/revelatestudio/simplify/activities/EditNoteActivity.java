package com.revelatestudio.simplify.activities;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.revelatestudio.simplify.R;
import com.revelatestudio.simplify.database.ContractDB;
import com.revelatestudio.simplify.database.Crud;
import com.revelatestudio.simplify.utils.FontTypes;
import com.revelatestudio.simplify.utils.ImplicitIntents;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EditNoteActivity extends AppCompatActivity {

    private EditText edtTitle, edtNote;
    boolean isEdtTextChanged = false;
    boolean isNewNote = false;
    public TextView tvTimeStamp;
    private Crud crud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        edtTitle = findViewById(R.id.edt_title);
        edtNote = findViewById(R.id.edt_note);
        tvTimeStamp = findViewById(R.id.timestamp);

        crud = new Crud(getApplicationContext());

        setup();
    }

    private void setup() {
        setEdtTextTypeFacePreference();
        setToolbar();
        setEdtText();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.tv_toolbar_title);
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

    private void setEdtTextTypeFacePreference() {
        SharedPreferences sharedPreferences = getSharedPreferences("com.fahmisbas.simplify", MODE_PRIVATE);
        String chosenTf = sharedPreferences.getString("font_preference", null);
        FontTypes fontTypes = new FontTypes(getApplicationContext());
        if (chosenTf != null) {
            switch (chosenTf) {
                case "Roboto":
                    fontTypes.roboto(edtTitle, edtNote, tvTimeStamp);
                    break;

                case "Open Sans":
                    fontTypes.openSans(edtTitle, edtNote, tvTimeStamp);
                    break;

                case "Monospace":
                    fontTypes.monospace(edtTitle, edtNote, tvTimeStamp);
                    break;

                case "Raleway":
                    fontTypes.raleway(edtTitle, edtNote, tvTimeStamp);
                    break;
            }
        }
    }

    private void setEdtText() {
        Intent intent = getIntent();
        long id = intent.getLongExtra("id", -1);
        String title = intent.getStringExtra("title");
        String note = intent.getStringExtra("note");
        String timestamp = intent.getStringExtra("timestamp");

        if (id != -1) {
            edtTitle.setText(title);
            edtNote.setText(note);
            tvTimeStamp.setText(formatDate(timestamp));
            edtTextChangeListener();
        } else {
            isNewNote = true;
        }
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd MMMM yyyy");
            if (date != null) {
                return "Last Update, " + fmtOut.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
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
            addOrUpdate();
            return true;
        } else {
            if (isEdtTextChanged) {
                discardOrSave();
                return false;
            }
            if (isNewNote) {
                if (!edtTitle.getText().toString().isEmpty() || !edtNote.getText().toString().isEmpty()) {
                    saveNewNoteOrNot();
                    isNewNote = false;
                    return false;
                }
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
                        isNewNote = false;
                    }
                }).setNegativeButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        crud.addData(edtTitle.getText().toString(), edtNote.getText().toString());
                        isNewNote = false;
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
                        crud.deleteData(id);
                        crud.addData(edtTitle.getText().toString(), edtNote.getText().toString());
                        EditNoteActivity.super.onBackPressed();
                    }
                });
        builder.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        if (isNewNote) {
            menu.getItem(1).setVisible(false);
        } else menu.getItem(1).setVisible(true);
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
                addOrUpdate();
                break;

            case R.id.delete:
                dialogDeleteItemPermission();
                break;

            case R.id.share:
                new ImplicitIntents(this).share(edtTitle.getText().toString(), edtNote.getText().toString());
                break;
        }
    }

    private void addOrUpdate() {
        Intent intent = getIntent();
        long id = intent.getLongExtra("id", -1);
        if (id != -1) {
            if (isEdtTextChanged) {
                crud.deleteData(id);
                crud.addData(edtTitle.getText().toString(), edtNote.getText().toString());
                isEdtTextChanged = false;
            }
        } else {
            if (!edtTitle.getText().toString().isEmpty() || !edtNote.getText().toString().isEmpty()) {
                crud.addData(edtTitle.getText().toString(), edtNote.getText().toString());
                super.onBackPressed();
            }
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
                        EditNoteActivity.super.onBackPressed();
                    }
                }).setNegativeButton("No", null);
        builder.show();
    }
}
