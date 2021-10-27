package com.example.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter2 extends RecyclerView.Adapter<Adapter2.ViewHolder2> {
    Context context;
    ArrayList<textview> arrayList;
    Activity activity;
    String username,phone,email,address,uid;



    public Adapter2(Context context, ArrayList<textview> arrayList,Activity activity,String username,String phone, String email, String address, String uid) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity=activity;
        this.username=username;
        this.phone=phone;
        this.email=email;
        this.address=address;
        this.uid=uid;
    }


    @NonNull
    @Override
    public Adapter2.ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.textview, parent, false);
        ViewHolder2 viewHolder2= new ViewHolder2(v);

        return viewHolder2;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder2 holder, int position) {
        textview textview=arrayList.get(position);
        holder.textView.setText(textview.getTextView());
        String cases=textview.getTextView();

        //holder.textView.setTypeface(null,Typeface.NORMAL);

        if(activity.toString().contains("com.example.menu.MainMenu")&& position==0){
            holder.textView.setTypeface(null,Typeface.BOLD);
            //Toast.makeText(context,"bind view holder  0",Toast.LENGTH_LONG).show();

        }

        else {
            if (activity.toString().contains("com.example.menu.Classic") && position == 1) {
                holder.textView.setTypeface(null, Typeface.BOLD);
               // Toast.makeText(context,"bind view holder  1",Toast.LENGTH_LONG).show();
            }else {
                if (activity.toString().contains("com.example.menu.Specials") && position == 2) {
                    holder.textView.setTypeface(null, Typeface.BOLD);
                   // Toast.makeText(context,"bind view holder  2",Toast.LENGTH_LONG).show();
                }else {
                    if (activity.toString().contains("com.example.menu.HOT") && position == 3) {
                        holder.textView.setTypeface(null, Typeface.BOLD);
                       // Toast.makeText(context,"bind view holder  3",Toast.LENGTH_LONG).show();
                    }else {
                        if (activity.toString().contains("com.example.menu.ICED") && position == 4) {
                            holder.textView.setTypeface(null, Typeface.BOLD);
                          //  Toast.makeText(context,"bind view holder  4",Toast.LENGTH_LONG).show();
                        }else {
                            if (activity.toString().contains("com.example.menu.TEA") && position == 5) {
                                holder.textView.setTypeface(null, Typeface.BOLD);
                               // Toast.makeText(context,"bind view holder  5",Toast.LENGTH_LONG).show();
                            }else {
                                if (activity.toString().contains("com.example.menu.Freddo") && position == 6) {
                                    holder.textView.setTypeface(null, Typeface.BOLD);
                                   // Toast.makeText(context,"bind view holder  6",Toast.LENGTH_LONG).show();
                                }else {
                                    if (activity.toString().contains("com.example.menu.Dessert") && position == 7) {
                                        holder.textView.setTypeface(null, Typeface.BOLD);
                                       // Toast.makeText(context,"bind view holder  7",Toast.LENGTH_LONG).show();
                                    }else {
                                        if (activity.toString().contains("com.example.menu.Others") && position == 8) {
                                            holder.textView.setTypeface(null, Typeface.BOLD);
                                            //Toast.makeText(context,"bind view holder  8",Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            //Toast.makeText(context,"bind view holder else",Toast.LENGTH_LONG).show();
                                            // holder.textView.setTypeface(null,Typeface.NORMAL);

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=activity.getIntent();
                int order_id=0;
                order_id=i.getIntExtra("order_id",0);

                holder.textView.setTypeface(null, Typeface.BOLD);
                switch (cases){

                    case "All":
                        Intent intent=new Intent(context,MainMenu.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("previousactivity",activity.toString());
                        intent.putExtra("order_id",order_id);
                        intent.putExtra("phone",phone);
                        intent.putExtra("uid",uid);
                        intent.putExtra("username",username);
                        intent.putExtra("email",email);
                        intent.putExtra("deliver_address",address);
                        context.startActivity(intent);
                        activity.overridePendingTransition(R.anim.animate_slide_up_enter, 0);
                        activity.finish();
                        break;
                    case "Classic":
                        Intent intent1=new Intent(context,Classic.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.putExtra("previousactivity",activity.toString());
                        intent1.putExtra("order_id",order_id);
                        intent1.putExtra("phone",phone);
                        intent1.putExtra("uid",uid);
                        intent1.putExtra("username",username);
                        intent1.putExtra("email",email);
                        intent1.putExtra("deliver_address",address);
                        context.startActivity(intent1);
                        activity.overridePendingTransition(R.anim.animate_slide_up_enter, 0);
                        activity.finish();
                        break;
                    case "Specials":
                        Intent intent2=new Intent(context,Specials.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent2.putExtra("previousactivity",activity.toString());
                        intent2.putExtra("order_id",order_id);
                        intent2.putExtra("phone",phone);
                        intent2.putExtra("uid",uid);
                        intent2.putExtra("username",username);
                        intent2.putExtra("email",email);
                        intent2.putExtra("deliver_address",address);
                        context.startActivity(intent2);
                        activity.overridePendingTransition(R.anim.animate_slide_up_enter, 0);
                        activity.finish();
                        break;
                    case "Hot Drinks":
                        Intent intent3=new Intent(context,HOT.class);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent3.putExtra("previousactivity",activity.toString());
                        intent3.putExtra("order_id",order_id);
                        intent3.putExtra("phone",phone);
                        intent3.putExtra("uid",uid);
                        intent3.putExtra("username",username);
                        intent3.putExtra("email",email);
                        intent3.putExtra("deliver_address",address);
                        context.startActivity(intent3);
                        activity.overridePendingTransition(R.anim.animate_slide_up_enter, 0);
                        activity.finish();
                        break;
                    case "Iced Drinks":
                        Intent intent4=new Intent(context,ICED.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent4.putExtra("previousactivity",activity.toString());
                        intent4.putExtra("order_id",order_id);
                        intent4.putExtra("phone",phone);
                        intent4.putExtra("uid",uid);
                        intent4.putExtra("username",username);
                        intent4.putExtra("email",email);
                        intent4.putExtra("deliver_address",address);
                        context.startActivity(intent4);
                        activity.overridePendingTransition(R.anim.animate_slide_up_enter, 0);
                        activity.finish();
                        break;
                    case "Tea":
                        Intent intent5=new Intent(context,TEA.class);
                        intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent5.putExtra("previousactivity",activity.toString());
                        intent5.putExtra("order_id",order_id);
                        intent5.putExtra("phone",phone);
                        intent5.putExtra("uid",uid);
                        intent5.putExtra("username",username);
                        intent5.putExtra("email",email);
                        intent5.putExtra("deliver_address",address);
                        context.startActivity(intent5);
                        activity.overridePendingTransition(R.anim.animate_slide_up_enter, 0);
                        activity.finish();
                        break;
                    case "Freddos":
                        Intent intent6=new Intent(context,Freddo.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent6.putExtra("previousactivity",activity.toString());
                        intent6.putExtra("order_id",order_id);
                        intent6.putExtra("phone",phone);
                        intent6.putExtra("uid",uid);
                        intent6.putExtra("username",username);
                        intent6.putExtra("email",email);
                        intent6.putExtra("deliver_address",address);
                        context.startActivity(intent6);
                        activity.overridePendingTransition(R.anim.animate_slide_up_enter, 0);
                        activity.finish();
                        break;
                    case "Waffles":
                        Intent intent7=new Intent(context,Dessert.class);
                        intent7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent7.putExtra("previousactivity",activity.toString());
                        intent7.putExtra("order_id",order_id);
                        intent7.putExtra("phone",phone);
                        intent7.putExtra("uid",uid);
                        intent7.putExtra("username",username);
                        intent7.putExtra("email",email);
                        intent7.putExtra("deliver_address",address);
                        context.startActivity(intent7);
                        activity.overridePendingTransition(R.anim.animate_slide_up_enter, 0);
                        activity.finish();
                        break;
                    case "Others":
                        Intent intent8=new Intent(context,Others.class);
                        intent8.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent8.putExtra("previousactivity",activity.toString());
                        intent8.putExtra("order_id",order_id);
                        intent8.putExtra("phone",phone);
                        intent8.putExtra("uid",uid);
                        intent8.putExtra("username",username);
                        intent8.putExtra("email",email);
                        intent8.putExtra("deliver_address",address);
                        context.startActivity(intent8);
                        activity.overridePendingTransition(R.anim.animate_slide_up_enter, 0);
                        activity.finish();
                        break;
                }

            }
        });

    }





    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    public static class ViewHolder2 extends RecyclerView.ViewHolder {

        TextView textView;
        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.text);




        }
    }

}
