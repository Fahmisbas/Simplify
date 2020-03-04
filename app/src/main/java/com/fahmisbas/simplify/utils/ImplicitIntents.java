package com.fahmisbas.simplify.utils;

import android.content.Context;
import android.content.Intent;

import de.cketti.mailto.EmailIntentBuilder;

public class ImplicitIntents {

    private Context context;

    public ImplicitIntents(Context context){
        this.context = context;
    }


    public void share(String subject, String text) {
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        myIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        myIntent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(myIntent, "Share to"));
    }

    public void emailIntent(String email,String subject) {
        Intent emailIntent = EmailIntentBuilder.from(context)
                .to(email)
                .subject(subject)
                .build();
        context.startActivity(emailIntent);
    }

}
