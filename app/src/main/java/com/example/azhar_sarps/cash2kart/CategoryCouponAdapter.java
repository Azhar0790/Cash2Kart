package com.example.azhar_sarps.cash2kart;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.example.azhar_sarps.cash2kart.pojo.CouponMessage;


import java.util.List;

import android.content.ClipData;
import android.content.ClipboardManager;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by azhar-sarps on 31-May-17.
 */

public class CategoryCouponAdapter extends RecyclerView.Adapter<CategoryCouponAdapter.ViewHolder> {
    Context context;
    List<CouponMessage> list;
    View itemView;
    String x;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_validation,tv_coupon_code,tv_url,txtheading;
        ExpandableTextView tv_description;

        public ViewHolder(View itemView) {
            super(itemView);
            txtheading=(TextView)itemView.findViewById(R.id.txtheading);
            tv_coupon_code = (TextView) itemView.findViewById(R.id.tv_coupon_code);
            tv_validation = (TextView) itemView.findViewById(R.id.tv_validation);
            tv_description = (ExpandableTextView) itemView.findViewById(R.id.tv_description);
            tv_url = (TextView) itemView.findViewById(R.id.tv_url);
        }
    }

    public CategoryCouponAdapter(Context context, List<CouponMessage> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public CategoryCouponAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_coupon_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtheading.setText(list.get(position).getcStore());
        holder.tv_description.setText(list.get(position).getCDesc());
//        makeTextViewResizable(holder.tv_description, 3, "View More", true);


        holder.tv_validation.setText("Expiry date: "+list.get(position).getCValidity());
        holder.tv_coupon_code.setText(list.get(position).getCCouponCode());
        holder.tv_url.setText(list.get(position).getCUrl());

        holder.tv_coupon_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text=list.get(position).getCCouponCode();
                if(text.equals("NO COUPON")) {
                    System.out.println("text:-" + text);
                    Intent i = new Intent(context, WebviewActivity.class);
                    i.putExtra("url", holder.tv_url.getText().toString());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }else{
                    System.out.println("text:-" + text);
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("simple text", text);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, "coupon code copied to Clipboard", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(context, WebviewActivity.class);
                    i.putExtra("url", holder.tv_url.getText().toString());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {

                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "View Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, "View More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }


}