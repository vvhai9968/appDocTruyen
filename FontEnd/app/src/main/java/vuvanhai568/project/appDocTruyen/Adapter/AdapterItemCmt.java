package vuvanhai568.project.appDocTruyen.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vuvanhai568.project.appDocTruyen.Modal.Book;
import vuvanhai568.project.appDocTruyen.R;
import vuvanhai568.project.appDocTruyen.onClickComment;

public class AdapterItemCmt extends RecyclerView.Adapter<AdapterItemCmt.ViewHolder> {

    ArrayList<Book.Comment> arrayList;
    Context context;
    String username;
    onClickComment onClickComment;


    public AdapterItemCmt(Context context, vuvanhai568.project.appDocTruyen.onClickComment onClickComment) {
        this.context = context;
        this.onClickComment = onClickComment;
    }

    public void setDataCmt(ArrayList<Book.Comment> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cmt,parent,false);
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
         username = sharedPreferences.getString("username", "");
        return new AdapterItemCmt.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Book.Comment comment = arrayList.get(position);

        holder.name.setText(comment.getFullName());
        holder.cmt.setText(comment.getCmt());
        Log.d("zzz", "onBindViewHolder: 123 " + comment.getIdPerson());
        Log.d("zzz", "onBindViewHolder: 456 " + username);

       if (username.equals(comment.getIdPerson())){
           holder.edit.setVisibility(View.VISIBLE);
           holder.delete.setVisibility(View.VISIBLE);

           holder.delete.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   onClickComment.DeleteComment( comment.getIdPerson(), comment.get_id(),comment.getCmt());
               }
           });
           holder.edit.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   onClickComment.EditComment( comment.getIdPerson(), comment.get_id(),comment.getCmt());
               }
           });


       } else {
           holder.edit.setVisibility(View.INVISIBLE);
           holder.delete.setVisibility(View.INVISIBLE);
       }

    }

    @Override
    public int getItemCount() {
        if (arrayList !=null){
            return  arrayList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,cmt;
        ImageView edit,delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             name = itemView.findViewById(R.id.idName);
             cmt = itemView.findViewById(R.id.idcmt);
             edit = itemView.findViewById(R.id.idcmEdit);
             delete = itemView.findViewById(R.id.idcmtDelete);
        }
    }
}
