package com.fahmisbas.simplify.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.TextView;

public class FontTypes {

    private Typeface normalStyle, boldStyle;
    private Context context;

    public FontTypes(Context context) {
        this.context = context;
    }

    public void roboto(EditText edtTitle, EditText edtNote) {
        normalStyle = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_regular.ttf");
        boldStyle = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_bold.ttf");

        edtNote.setTypeface(normalStyle);
        edtTitle.setTypeface(boldStyle);
    }

    public void roboto(TextView tvTitle, TextView tvNote) {
        normalStyle = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_regular.ttf");
        boldStyle = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_bold.ttf");

        tvNote.setTypeface(normalStyle);
        tvTitle.setTypeface(boldStyle);
    }

    public void openSans(EditText edtTitle, EditText edtNote) {
        normalStyle = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        boldStyle = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);

        edtTitle.setTypeface(boldStyle);
        edtNote.setTypeface(normalStyle);
    }

    public void openSans(TextView tvTitle, TextView tvNote) {
        normalStyle = Typeface.createFromAsset(context.getAssets(), "fonts/opensans_regular.ttf");
        boldStyle = Typeface.createFromAsset(context.getAssets(), "fonts/opensans_bold.ttf");

        tvNote.setTypeface(normalStyle);
        tvTitle.setTypeface(boldStyle);
    }


    public void monospace(EditText edtTitle, EditText edtNote) {
        normalStyle = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
        boldStyle = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD);

        edtTitle.setTypeface(boldStyle);
        edtNote.setTypeface(normalStyle);
    }

    public void monospace(TextView tvTitle, TextView tvNote) {
        normalStyle = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
        boldStyle = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD);

        tvTitle.setTypeface(boldStyle);
        tvNote.setTypeface(normalStyle);
    }


    public void raleway(TextView tvTitle, TextView tvNote) {
        normalStyle = Typeface.createFromAsset(context.getAssets(), "fonts/raleway_regular.ttf");
        boldStyle = Typeface.createFromAsset(context.getAssets(), "fonts/raleway_bold.ttf");

        tvNote.setTypeface(normalStyle);
        tvTitle.setTypeface(boldStyle);
    }

    public void raleway(EditText tvTitle, EditText tvNote) {
        normalStyle = Typeface.createFromAsset(context.getAssets(), "fonts/raleway_regular.ttf");
        boldStyle = Typeface.createFromAsset(context.getAssets(), "fonts/raleway_bold.ttf");

        tvNote.setTypeface(normalStyle);
        tvTitle.setTypeface(boldStyle);
    }


}
