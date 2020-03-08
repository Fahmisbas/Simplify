package com.revelatestudio.simplify.adapter;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.revelatestudio.simplify.R;
import com.revelatestudio.simplify.database.ContractDB;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private Cursor cursor;
    private OnNoteItemClick onNoteItemClick;
    private OnNoteItemLongClick onNoteItemLongClick;
    private OnTypeFaceChange onTypeFaceChange;


    public NoteAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    public void setOnNoteItemClick(OnNoteItemClick onNoteItemClick) {
        this.onNoteItemClick = onNoteItemClick;
    }

    public void setOnNoteItemLongClick(OnNoteItemLongClick onNoteItemLongClick) {
        this.onNoteItemLongClick = onNoteItemLongClick;
    }

    public void setOnTypeFaceChange(OnTypeFaceChange onTypeFaceChange) {
        this.onTypeFaceChange = onTypeFaceChange;
    }

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NoteAdapter.ViewHolder holder, final int position) {

        if (!cursor.moveToPosition(position)) {
            return;
        }

        final String title = cursor.getString(cursor.getColumnIndex(ContractDB.EntryDB.COLUMN_TITLE));
        final String note = cursor.getString(cursor.getColumnIndex(ContractDB.EntryDB.COLUMN_NOTE));
        final String timeStamp = cursor.getString(cursor.getColumnIndex(ContractDB.EntryDB.TIMESTAMP));
        final long id = cursor.getLong(cursor.getColumnIndex(ContractDB.EntryDB._ID));

        holder.title.setText(title);
        holder.note.setText(note);
        holder.itemView.setTag(id);

        onTypeFaceChange.typfaceChange(holder.title,holder.note);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNoteItemClick.onNoteItemClickListener(title, note,timeStamp, id);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onNoteItemLongClick.onNoteItemLongClickListener(holder.itemView);
                return true;
            }
        });

        if (holder.title.getText().toString().equals("")){
            holder.title.setText(note);
        }else if (holder.note.getText().toString().equals("")){
            holder.note.setText(title);
        }
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, note;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            note = itemView.findViewById(R.id.tv_note);
        }
    }

    public interface OnNoteItemClick {
        void onNoteItemClickListener(String title, String note,String timestamp, long id);
    }

    public interface OnNoteItemLongClick {
        void onNoteItemLongClickListener(View view);
    }

    public interface OnTypeFaceChange {
        void typfaceChange(TextView title, TextView note);
    }
}
