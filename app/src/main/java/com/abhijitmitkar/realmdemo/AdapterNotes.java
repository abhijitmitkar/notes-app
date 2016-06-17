package com.abhijitmitkar.realmdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Abhijit on 15-06-2016.
 */
public class AdapterNotes extends RealmRecyclerViewAdapter<Note, AdapterNotes.ViewHolderNotes> {

    private final OnRecyclerItemClickListener onItemClickListener;

    public AdapterNotes(Context context, OrderedRealmCollection<Note> data, boolean autoUpdate, OnRecyclerItemClickListener onRecyclerItemClickListener) {
        super(context, data, autoUpdate);
        onItemClickListener = onRecyclerItemClickListener;
    }

    @Override
    public ViewHolderNotes onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.listitem_note, parent, false);
        return new ViewHolderNotes(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolderNotes holder, int position) {
        final Note note = getData().get(position);
        holder.txtTitle.setText(note.getTitle());
        holder.txtNote.setText(note.getNote());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClicked(note);
            }
        });
    }

    public class ViewHolderNotes extends RecyclerView.ViewHolder {
        TextView txtTitle, txtNote;

        public ViewHolderNotes(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.listitemNote_txt_title);
            txtNote = (TextView) itemView.findViewById(R.id.listitemNote_txt_note);
        }
    }

    public interface OnRecyclerItemClickListener {
        void onItemClicked(Note note);
    }
}
