package com.abhijitmitkar.realmdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ActMain extends AppCompatActivity {

    private Realm realm;
    private Context context;
    private RealmResults<Note> realmResults;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        context = this;
        AppUtils.setUpToolbar(this, (Toolbar) findViewById(R.id.actMain_toolbar), false);
        emptyView = findViewById(R.id.actMain_empty_view);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToEditActivity(0);
                }
            });

        realm = Realm.getDefaultInstance();

        // Find all notes
        realmResults = realm.where(Note.class).findAllAsync();
        realmResults.addChangeListener(new RealmChangeListener<RealmResults<Note>>() {
            @Override
            public void onChange(RealmResults<Note> element) {
                if(element.size() > 0) emptyView.setVisibility(View.GONE);
                else emptyView.setVisibility(View.VISIBLE);
            }
        });

        // RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.actMain_recycler_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new AdapterNotes(context, realmResults, true, new AdapterNotes.OnRecyclerItemClickListener() {
            @Override
            public void onItemClicked(Note note) {
                Log.i(context.getClass().getSimpleName(), "<><><> NoteId:" + note.getNoteId());
                goToEditActivity(note.getNoteId());
            }
        }));
    }

    private void goToEditActivity(long noteId) {
        Intent intent = new Intent(this, ActEditNote.class);
        intent.putExtra(ActEditNote.EXTRA_NOTE_ID, noteId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuitem_actMain_about) {
            AppUtils.showSnackbar(findViewById(R.id.actMain_coordinator), getString(R.string.about_text), "", null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
