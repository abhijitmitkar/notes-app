package com.abhijitmitkar.realmdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import io.realm.Realm;

/**
 * Created by Abhijit on 15-06-2016.
 */
public class ActEditNote extends AppCompatActivity {

    public static final String EXTRA_NOTE_ID = "extra_note_id";
    public static final int NEW_NOTE = 0;
    private EditText edtTitle;
    private EditText edtNote;
    private Context context;
    private ProgressDialog progressDialog;
    private Realm realm;
    private long noteId;
    private Note note;
    private android.support.v4.view.ActionProvider shareActionProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit_note);
        AppUtils.setUpToolbar(this, (Toolbar) findViewById(R.id.actEditNote_toolbar), true);
        context = this;
        edtTitle = (EditText) findViewById(R.id.actEditNote_edtTitle);
        edtNote = (EditText) findViewById(R.id.actEditNote_edtNote);
        progressDialog = AppUtils.createProgressDialog(context, null, "Saving");
        noteId = getIntent().getLongExtra(EXTRA_NOTE_ID, NEW_NOTE);

        realm = Realm.getDefaultInstance();

        if (noteId != NEW_NOTE) populate();
    }

    private void populate() {
        note = realm.where(Note.class)
                .equalTo(Note.NOTE_ID, noteId)
                .findFirst();
        if (note != null) {
            edtTitle.setText(note.getTitle());
            edtNote.setText(note.getNote());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_edit_note, menu);
        if (noteId == NEW_NOTE) {
            menu.findItem(R.id.menuitem_actEditNote_delete).setVisible(false);
            menu.findItem(R.id.menuitem_actEditNote_share).setVisible(false);
        }
        return true;
    }

    private Intent getShareIntent() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, edtTitle.getText().toString() + ":\n" + edtNote.getText().toString());
        shareIntent.setType("text/plain");
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menuitem_actEditNote_save:
                saveNote();
                return true;
            case R.id.menuitem_actEditNote_delete:
                askPermissionToDelete();
                return true;
            case R.id.menuitem_actEditNote_share:
                startActivity(Intent.createChooser(getShareIntent(), "Send to"));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        saveNote();
    }

    private void saveNote() {
        if (isValidated()) {
            progressDialog.show();

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    if (noteId != NEW_NOTE && note != null) {
                        note.setTitle(edtTitle.getText().toString().trim());
                        note.setNote(edtNote.getText().toString().trim());

                    } else {
                        Note note = realm.createObject(Note.class);
                        note.setTitle(edtTitle.getText().toString().trim());
                        note.setNote(edtNote.getText().toString().trim());
                        note.setNoteId(System.currentTimeMillis());
                    }

                    progressDialog.dismiss();
                    Toast.makeText(context, "Note saved", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }
    }

    private void deleteNote() {
        progressDialog.show();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                note.deleteFromRealm();
                progressDialog.dismiss();
                Toast.makeText(context, "Note deleted", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void askPermissionToDelete() {
        new AlertDialog.Builder(context)
                .setMessage("Are you sure to delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteNote();
                    }
                })
                .setNegativeButton("No", null)
                .create()
                .show();
    }

    private boolean isValidated() {
        if ((edtTitle.getText().toString().trim().isEmpty())) {
            edtTitle.setError(getString(R.string.validation_required));
            return false;
        } else {
            edtTitle.setError(null);
            if ((edtNote.getText().toString().trim().isEmpty())) {
                edtNote.setError(getString(R.string.validation_required));
                return false;
            } else {
                edtNote.setError(null);
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
