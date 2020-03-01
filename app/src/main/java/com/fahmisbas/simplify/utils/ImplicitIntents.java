package com.fahmisbas.simplify.utils;

import android.content.Context;
import android.content.Intent;

import de.cketti.mailto.EmailIntentBuilder;

public class ImplicitIntents {


    public static void share(Context context) {
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        String shareBody = "Download : .....";
        String shareSub = "Simpliffy - Minimal Note App";
        myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
        myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(myIntent, "Share to"));
    }

    public static void emailIntent(Context context) {
        Intent emailIntent = EmailIntentBuilder.from(context)
                .to("fahmisulaimanbas@gmail.com")
                .subject("Simplify Note App")
                .build();
        context.startActivity(emailIntent);
    }

}
