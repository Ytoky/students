package com.example.students.adapter;

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
import com.example.students.activity.UpdateStudentActivity;
import com.example.students.common.enums.StudentsColumnName;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private Context context;
    private List<String> studentIds;
    private List<String> studentName;
    private List<String> studentSurname;
    private List<String> studentCod;
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
        holder.studentIdTxt.setText(String.valueOf(studentIds.get(position)));
        holder.studentSurnameTxt.setText(String.valueOf(studentSurname.get(position)));
        holder.studentNameTxt.setText(String.valueOf(studentName.get(position)));
        holder.studentCodTxt.setText(String.valueOf(studentCod.get(position)));
        holder.mainLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, UpdateStudentActivity.class);
            intent.putExtra(StudentsColumnName.ID.getName(), String.valueOf(studentIds.get(position)));
            intent.putExtra(StudentsColumnName.NAME.getName(), String.valueOf(studentName.get(position)));
            intent.putExtra(StudentsColumnName.SURNAME.getName(), String.valueOf(studentSurname.get(position)));
            intent.putExtra(StudentsColumnName.COD.getName(), String.valueOf(studentCod.get(position)));
            activity.startActivityForResult(intent, 1);
        });
    }

    @Override
    public int getItemCount() {
        return studentIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentIdTxt;
        TextView studentNameTxt;
        TextView studentSurnameTxt;
        TextView studentCodTxt;
        LinearLayout mainLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentIdTxt = itemView.findViewById(R.id.student_id_txt);
            studentNameTxt = itemView.findViewById(R.id.student_name_txt);
            studentSurnameTxt = itemView.findViewById(R.id.student_surname_txt);
            studentCodTxt = itemView.findViewById(R.id.student_cod_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            Animation translateAnim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translateAnim);
        }
    }
}
