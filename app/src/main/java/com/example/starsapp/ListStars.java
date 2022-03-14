package com.example.starsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.starsapp.adapter.StarAdapter;
import com.example.starsapp.beans.Star;
import com.example.starsapp.service.StarService;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListStars extends AppCompatActivity {

    private  List<Star> stars;
    private RecyclerView recyclerView;
    private StarAdapter starAdapter = null;
    private StarService service;
    private static boolean b=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stars);
        stars = new ArrayList<>();
        stars.clear();
        service = StarService.getInstance();
        if(b){
            init();
            b=false;

        }
        stars=service.findAll();
        recyclerView = findViewById(R.id.recycle_view);
        starAdapter = new StarAdapter(this, service.findAll());
        recyclerView.setAdapter(starAdapter);

        recyclerView.removeAllViews();
        LinearLayoutManager l =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(l);

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    Star celibSupprimer=null;
    ItemTouchHelper.SimpleCallback simpleCallback= new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT |
            ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position= viewHolder.getAbsoluteAdapterPosition();

            switch (direction){
                case ItemTouchHelper.LEFT:
                    View popup = LayoutInflater.from(ListStars.this).inflate(R.layout.star_rating, null,
                            false);
                     ImageView img = popup.findViewById(R.id.img);
                     RatingBar bar = popup.findViewById(R.id.ratingBar);
                     TextView idss = popup.findViewById(R.id.idss);

                     //idss.setText("hello" + service.findById(position+1).getId());
                    Glide.with(ListStars.this)
                            .asBitmap()
                            .load(service.findById(position+1).getImg())
                            .apply(new RequestOptions().override(100, 100))
                            .into(img);

                    bar.setRating(service.findById(position+1).getStar());
                    idss.setText(service.findById(position+1).getId()+"");
                    AlertDialog dialog = new AlertDialog.Builder(ListStars.this)
                            .setTitle("Notez : ")
                            .setMessage("Donner une note entre 1 et 5 :")
                            .setView(popup)
                            .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    float s = bar.getRating();
                                    int ids = Integer.parseInt(idss.getText().toString());
                                    Star star = StarService.getInstance().findById(ids);
                                    star.setStar(s);
                                    StarService.getInstance().update(star);
                                    starAdapter.notifyItemChanged(position);

                                }
                            })
                            .setNegativeButton("Annuler", null)
                            .create();
                    dialog.show();
                    break;
                case ItemTouchHelper.RIGHT:
                    Toast.makeText(ListStars.this, stars.get(position).getName(), Toast.LENGTH_SHORT).show();
                    celibSupprimer=stars.get(position);
                    //Toast.makeText(ListStars.this, stars.get(position).getName(), Toast.LENGTH_SHORT).show();
                    service.delete(stars.remove(position));
                    starAdapter.notifyItemRemoved(position);
                    Snackbar.make(recyclerView,celibSupprimer.getName().toString()+" est supprimer", BaseTransientBottomBar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            stars.add(position,celibSupprimer);
                            starAdapter.notifyItemInserted(position);
                        }
                    }).show();
                    break;
            }
        }
    };


    public void init() {
        service.create(new Star("Jennifer Lawrence", "https://res.cloudinary.com/ybmedia/image/upload/c_crop,h_1331,w_2000,x_0,y_0/c_scale,f_auto,q_auto,w_700/v1/m/9/e/9e38d0d82d4eac75ab08b067c05056103e5a4d4f/GettyImages-1153799673.jpg", 1));
        service.create(new Star("Brad Pitt", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQJBntbqy_AhBhpkcGci8VP79LSwcheGgaj4BEeWLy9pUK3KOy7", 3.5f));
        service.create(new Star("Alexis Bledel", "https://media.glamour.com/photos/5984b8959569bf339151b91b/4:3/w_2784,h_2088,c_limit/ALEXIS-BLEDEL.jpg", 1));
        service.create(new Star("Asa Butterfield ", "https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AAKMAnI.img?h=997&w=1438&m=6&q=60&o=f&l=f&x=415&y=302", 1));
        service.create(new Star("Zazie Beetz", "https://res.cloudinary.com/ybmedia/image/upload/c_crop,h_1331,w_2000,x_0,y_0/c_scale,f_auto,q_auto,w_700/v1/m/d/3/d3adaf30c482533a171ccb56d9ab965e253b9d94/GettyImages-1205119616.jpg", 3.5f));
        service.create(new Star("Johnny Depp", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStpy5AJ7T5ogRTjgXmakPRf0SaxtG5fA-7YtfI7aqZKjBCSi7R", 3));
        service.create(new Star("Will Smith", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQbuF86tSHODHWHJRusio04zBWZHRNgFJdu-jyiWgkIbBC4-tuT", 5));
        service.create(new Star(" Cole Sprouse ", "https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AAKMppa.img?h=958&w=1438&m=6&q=60&o=f&l=f&x=414&y=357", 1));
        service.create(new Star("Margot Robbie", "https://res.cloudinary.com/ybmedia/image/upload/c_crop,h_1429,w_2000,x_0,y_0/c_scale,f_auto,q_auto,w_700/v1/m/0/e/0ea2423afab7ae3d74299f1882a2ce294bab319f/GettyImages-909094868.jpg", 5));
        service.create(new Star("Lucas Hedges ", "https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AAKMybn.img?h=958&w=1438&m=6&q=60&o=f&l=f&x=550&y=419", 1));
        service.create(new Star("Iain Armitage", "https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AAKMtQQ.img?h=1750&w=1438&m=6&q=60&o=f&l=f&x=387&y=441", 1));
        service.create(new Star("Emma Roberts", "https://res.cloudinary.com/ybmedia/image/upload/c_crop,h_1334,w_2000,x_0,y_0/c_scale,f_auto,q_auto,w_700/v1/m/7/f/7f2523e872b7c80251661cc01ee33a37aa64fa94/GettyImages-1145515499.jpg", 1));
        service.create(new Star("Hailee Steinfeld", "https://res.cloudinary.com/ybmedia/image/upload/c_crop,h_1176,w_1769,x_0,y_0/c_scale,f_auto,q_auto,w_700/v1/m/3/a/3a390e0b8a98311acd6bed1cebcbceef00816fb6/GettyImages-1172209958.jpg", 1));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
            return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (starAdapter != null){
                    starAdapter.getFilter().filter(newText);
                }
                return true;
            }

        });
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // We are using switch case because multiple icons can be kept
        switch (item.getItemId()) {
            case R.id.shareButton:

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);

                // type of the content to be shared
                sharingIntent.setType("text/plain");

                // Body of the content
                String shareBody = "Your Body Here";

                // subject of the content. you can share anything
                String shareSubject = "Your Subject Here";

                // passing body of the content
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

                // passing subject of the content
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "helloooo", Toast.LENGTH_SHORT).show();
    }
}