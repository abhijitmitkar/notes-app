package com.abhijitmitkar.realmdemo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Abhijit on 15-06-2016.
 */
public class Note extends RealmObject {

    public static final String CLASS = "Note";
    public static final String TITLE = "title";
    public static final String NOTE = "note";
    public static final String NOTE_ID = "noteId";

    private String title;
    private String note;
    @PrimaryKey
    private long noteId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }
}
