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

    public void sansSerif(EditText edtTitle, EditText edtNote) {
        normalStyle = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        boldStyle = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);

        edtTitle.setTypeface(boldStyle);
        edtNote.setTypeface(normalStyle);
    }

    public void monospace(EditText edtTitle, EditText edtNote) {
        normalStyle = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
        boldStyle = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD);

        edtTitle.setTypeface(boldStyle);
        edtNote.setTypeface(normalStyle);
    }

    public void roboto(TextView tvTitle, TextView tvNote) {
        normalStyle = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_regular.ttf");
        boldStyle = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_bold.ttf");

        tvNote.setTypeface(normalStyle);
        tvTitle.setTypeface(boldStyle);
    }

    public void sansSerif(TextView tvTitle, TextView tvNote) {
        normalStyle = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        boldStyle = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);

        tvTitle.setTypeface(boldStyle);
        tvNote.setTypeface(normalStyle);
    }

    public void monospace(TextView tvTitle, TextView tvNote) {
        normalStyle = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
        boldStyle = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD);

        tvTitle.setTypeface(boldStyle);
        tvNote.setTypeface(normalStyle);
    }

}
