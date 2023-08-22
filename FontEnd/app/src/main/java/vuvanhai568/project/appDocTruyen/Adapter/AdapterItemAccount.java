package vuvanhai568.project.appDocTruyen.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import vuvanhai568.project.appDocTruyen.Activity.Detail_Posts;
import vuvanhai568.project.appDocTruyen.Activity.EditPost;
import vuvanhai568.project.appDocTruyen.Modal.Book;
import vuvanhai568.project.appDocTruyen.R;
import vuvanhai568.project.appDocTruyen.onClickAccount;

public class AdapterItemAccount extends RecyclerView.Adapter<AdapterItemAccount.ViewHolder> {
    ArrayList<Book> arrayList;
    onClickAccount clickAccount;

    Context context;

    public AdapterItemAccount(Context context,onClickAccount clickAccount) {
        this.context = context;
        this.clickAccount = clickAccount;

    }

    public void setData(ArrayList<Book> arr) {
        arrayList = arr;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookaccount, parent, false);
        return new AdapterItemAccount.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (arrayList == null) {
            return;
        }
        Book book = arrayList.get(position);
        holder.author.setText(book.getAuthor());
        holder.title.setText(book.getTitle());
        holder.comment.setText(String.valueOf(book.getBinhLuanCount()));

        loadImageFromUrl(book.getImage(), holder.img);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Detail_Posts.class);

                Bundle b = new Bundle();
                String id = book.getId();
                b.putString("id", id);
                i.putExtra("dataBook", b);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, EditPost.class);

                Bundle b = new Bundle();
                String id = book.getId();
                b.putString("id", id);
                i.putExtra("dataBook", b);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAccount.delete(book.getId(),book.getTitle());
            }
        });
    }

    private void loadImageFromUrl(String imageUrl, ImageView imageView) {
        ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    Log.d("zzz", "Bitmap is null");
                }
            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Xử lý phản hồi lỗi nếu cần thiết
            }
        });

        Volley.newRequestQueue(context).add(imageRequest);
    }


    @Override
    public int getItemCount() {
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img,edit,delete;
        TextView author, title, comment;
        LinearLayout item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imgBookAC);
            title = itemView.findViewById(R.id.txtTitleAC);
            author = itemView.findViewById(R.id.txtAuthorAC);
            comment = itemView.findViewById(R.id.commentAC);
            item = itemView.findViewById(R.id.itemAC);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
