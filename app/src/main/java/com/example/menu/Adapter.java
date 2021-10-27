package com.example.menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.transition.TransitionInflater;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    Context context;
    ArrayList<helperClass> arrayList;
    Activity activity;
    String phone,address,email,username,uid;

    public Adapter(Context context, Activity activity,ArrayList<helperClass> arrayList,String phone,String email,String address,String username,String uid) {
        this.context = context;
        this.activity = activity;
        this.arrayList = arrayList;
        this.username=username;
        this.address=address;
        this.phone=phone;
        this.email=email;
        this.uid=uid;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_design, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        helperClass helperClass=arrayList.get(position);
        holder.imageView.setImageResource(helperClass.getImageView());
        holder.title.setText(helperClass.getTitle());
        holder.description.setText(helperClass.getDescription().trim());
        holder.small.setText(helperClass.getSmall());
        holder.large.setText(helperClass.getLarge());
        if (!helperClass.getPrice().equals("")){
            holder.price1.setText(helperClass.getPrice()+" Tk");
        }
        else {
            holder.price1.setText(helperClass.getPrice());
        }
        if (!helperClass.getPrice().equals("")){
            holder.price2.setText(helperClass.getPrice2()+" Tk");
        }
        else {
            holder.price2.setText(helperClass.getPrice2());
        }

        ViewCompat.setTransitionName(holder.title,helperClass.getTitle());
        int cases=helperClass.getImageView();
        holder.layout.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NonConstantResourceId")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

            @Override
            public void onClick(View view) {
               switch (cases){
                   case R.drawable.hot:
                       Intent intent=new Intent(context,HOT.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       intent.putExtra("previousactivity",activity.toString());
                       intent.putExtra("adapter","adapter");
                       intent.putExtra("order_id",helperClass.getOrder_id());
                       intent.putExtra("phone",phone);
                       intent.putExtra("username",username);
                       intent.putExtra("email",email);
                       intent.putExtra("deliver_address",address);
                       intent.putExtra("uid",uid);
                       activity.startActivity(intent);
                      // activity.overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                       activity.overridePendingTransition(R.anim.animate_slide_up_enter, 0);
                       activity.finish();
                       break;

                   case R.drawable.iced:
                       Intent intent1=new Intent(context,ICED.class);
                       intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       intent1.putExtra("previousactivity",activity.toString());
                       intent1.putExtra("adapter","adapter");
                       intent1.putExtra("order_id",helperClass.getOrder_id());
                       intent1.putExtra("phone",phone);
                       intent1.putExtra("username",username);
                       intent1.putExtra("email",email);
                       intent1.putExtra("deliver_address",address);
                       intent1.putExtra("uid",uid);
                       activity.startActivity(intent1);
                       activity.overridePendingTransition(R.anim.animate_slide_up_enter, 0);
                       activity.finish();
                       break;
                   case R.drawable.classic:
                       Intent intent2=new Intent(context,Classic.class);
                       intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       intent2.putExtra("previousactivity",activity.toString());
                       intent2.putExtra("adapter","adapter");
                       intent2.putExtra("order_id",helperClass.getOrder_id());
                       intent2.putExtra("phone",phone);
                       intent2.putExtra("username",username);
                       intent2.putExtra("email",email);
                       intent2.putExtra("deliver_address",address);
                       intent2.putExtra("uid",uid);
                       activity.startActivity(intent2);
                       activity.overridePendingTransition(R.anim.animate_slide_up_enter, 0);
                       activity.finish();
                       break;
                   case R.drawable.specials:
                       Intent intent3=new Intent(context,Specials.class);
                       intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       intent3.putExtra("order_id",helperClass.getOrder_id());
                      // holder.title.setTransitionName("text_transition1");
                       intent3.putExtra("previousactivity",activity.toString());
                       intent3.putExtra("adapter","adapter");
                       intent3.putExtra("phone",phone);
                       intent3.putExtra("username",username);
                       intent3.putExtra("email",email);
                       intent3.putExtra("deliver_address",address);
                       intent3.putExtra("uid",uid);
                       activity.startActivity(intent3);
                       activity.overridePendingTransition(R.anim.animate_slide_up_enter, 0);
                       activity.finish();
                       break;
                   case R.drawable.dessert:
                       Intent intent4=new Intent(context,Dessert.class);
                       intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       intent4.putExtra("order_id",helperClass.getOrder_id());
/*
                       holder.title.setTransitionName("text_transition4");
*/
                       intent4.putExtra("previousactivity",activity.toString());
                       intent4.putExtra("adapter","adapter");
                       intent4.putExtra("phone",phone);
                       intent4.putExtra("username",username);
                       intent4.putExtra("email",email);
                       intent4.putExtra("deliver_address",address);
                       intent4.putExtra("uid",uid);
                       activity.startActivity(intent4);
                       activity.overridePendingTransition(R.anim.animate_slide_up_enter, 0);
                       activity.finish();

                       break;
                   case R.drawable.snacks:
                       Intent intent5=new Intent(context,Others.class);
                       intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       intent5.putExtra("previousactivity",activity.toString());
                       intent5.putExtra("adapter","adapter");
                       intent5.putExtra("order_id",helperClass.getOrder_id());
                       intent5.putExtra("phone",phone);
                       intent5.putExtra("username",username);
                       intent5.putExtra("email",email);
                       intent5.putExtra("deliver_address",address);
                       intent5.putExtra("uid",uid);
                       activity.startActivity(intent5);
                       activity.overridePendingTransition(R.anim.animate_slide_up_enter, 0);
                       activity.finish();
                       break;

                   case R.drawable.tea:
                       Intent intent6=new Intent(context,TEA.class);
                       intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                      // holder.title.setTransitionName("text_transition6");
                       intent6.putExtra("adapter","adapter");
                       intent6.putExtra("order_id",helperClass.getOrder_id());
                       intent6.putExtra("previousactivity",activity.toString());
                       intent6.putExtra("phone",phone);
                       intent6.putExtra("username",username);
                       intent6.putExtra("email",email);
                       intent6.putExtra("deliver_address",address);
                       intent6.putExtra("uid",uid);
                       activity.startActivity(intent6);
                       activity.overridePendingTransition(R.anim.animate_slide_up_enter, 0);
                       activity.finish();
                       break;
                   case R.drawable.cafefredo:
                       Intent intent7=new Intent(context,Freddo.class);
                       intent7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       //holder.title.setTransitionName("text_transition7");
                       intent7.putExtra("previousactivity",activity.toString());
                       intent7.putExtra("adapter","adapter");
                       intent7.putExtra("order_id",helperClass.getOrder_id());
                       intent7.putExtra("phone",phone);
                       intent7.putExtra("username",username);
                       intent7.putExtra("email",email);
                       intent7.putExtra("deliver_address",address);
                       intent7.putExtra("uid",uid);

                       activity.startActivity(intent7);
                       activity.overridePendingTransition(R.anim.animate_slide_up_enter, 0);
                       activity.finish();
                       break;
                   case R.drawable.hazelnut:
                   case R.drawable.caramello:
                   case R.drawable.mini:
                   case R.drawable.chailatte:
                   case R.drawable.hotchoc:
                   case R.drawable.kids:
                   case R.drawable.hazelnut_creamfreddo:
                   case R.drawable.strawberry_freddos:
                   case R.drawable.chocolate_freddos:
                   case R.drawable.white_choc:
                   case R.drawable.cafe_freddo:
                   case R.drawable.vanilla_freddo:
                   case R.drawable.whitemocha:
                   case R.drawable.raspberry:
                   case R.drawable.mocha_brownie:
                   case R.drawable.red_velvet_waffle:
                   case R.drawable.banana_waffle:
                   case R.drawable.nutty_pumpkin_waffles:
                   case R.drawable.wafflenutella:
                   case R.drawable.cookie_waffle:
                   case R.drawable.berry_waffle:
                   case R.drawable.tiramisuwafflejpg:
                   case R.drawable.americano:
                   case R.drawable.cappuccino:
                   case R.drawable.latte:
                   case R.drawable.classic1:
                   case R.drawable.machiato:
                       Intent intent8=new Intent(context,Order.class);
                       intent8.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       //holder.title.setTransitionName("text_transition8");
                       //holder.imageView.setTransitionName("image_transition8");
                       intent8.putExtra("food_image",helperClass.getImageView());
                       intent8.putExtra("food_name",helperClass.getTitle());
                       intent8.putExtra("food_description",helperClass.getDescription());
                       intent8.putExtra("food_price",helperClass.getPrice());
                       intent8.putExtra("regular",helperClass.getSmall());
                       intent8.putExtra("large",helperClass.getLarge());
                       intent8.putExtra("add_ons_visibility",helperClass.getAdd_ons_visibility());
                       intent8.putExtra("radio",helperClass.getRadio_groups_visibility());
                       intent8.putExtra("regular_price",helperClass.getPrice());
                       intent8.putExtra("large_price",helperClass.getPrice2());
                       intent8.putExtra("activity",activity.toString());
                       intent8.putExtra("order_id",helperClass.getOrder_id());
                       intent8.putExtra("layout",1);
                       intent8.putExtra("username",username);
                       intent8.putExtra("email",email);
                       intent8.putExtra("deliver_address",address);
                       intent8.putExtra("phone",phone);
                       intent8.putExtra("uid",uid);
                       try{
                          activity.startActivity(intent8);
                           activity.overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);

                           activity.finish();
                           }
                       catch (Exception e){
                           Toast.makeText(context,"adapter try error",Toast.LENGTH_LONG).show();
                       }
                       break;
               }

            }


        });

    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title,description,small,large,price1,price2;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.image);
            title=itemView.findViewById(R.id.title);
            description=itemView.findViewById(R.id.description);
            layout=itemView.findViewById(R.id.layout_card);
            small=itemView.findViewById(R.id.small);
            large=itemView.findViewById(R.id.large);
            price1=itemView.findViewById(R.id.price);
            price2=itemView.findViewById(R.id.price2);


        }
    }
}
