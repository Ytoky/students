package com.example.booklibrary.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booklibrary.R;
import com.example.booklibrary.activity.UpdateBookActivity;
import com.example.booklibrary.common.enums.BookColumnName;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private Context context;
    private List<String> bookIds;
    private List<String> bookTitles;
    private List<String> bookAuthor;
    private List<String> bookPages;
    private Activity activity;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bookIdTxt.setText(String.valueOf(bookIds.get(position)));
        holder.bookAuthorTxt.setText(String.valueOf(bookAuthor.get(position)));
        holder.bookTitleTxt.setText(String.valueOf(bookTitles.get(position)));
        holder.bookPagesTxt.setText(String.valueOf(bookPages.get(position)));
        holder.mainLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, UpdateBookActivity.class);
            intent.putExtra(BookColumnName.ID.getName(), String.valueOf(bookIds.get(position)));
            intent.putExtra(BookColumnName.TITLE.getName(), String.valueOf(bookTitles.get(position)));
            intent.putExtra(BookColumnName.AUTHOR.getName(), String.valueOf(bookAuthor.get(position)));
            intent.putExtra(BookColumnName.PAGES.getName(), String.valueOf(bookPages.get(position)));
            activity.startActivityForResult(intent, 1);
        });
    }

    @Override
    public int getItemCount() {
        return bookIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bookIdTxt;
        TextView bookTitleTxt;
        TextView bookAuthorTxt;
        TextView bookPagesTxt;
        LinearLayout mainLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookIdTxt = itemView.findViewById(R.id.book_id_txt);
            bookTitleTxt = itemView.findViewById(R.id.book_title_txt);
            bookAuthorTxt = itemView.findViewById(R.id.book_author_txt);
            bookPagesTxt = itemView.findViewById(R.id.book_pages_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            Animation translateAnim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translateAnim);
        }
    }
}
