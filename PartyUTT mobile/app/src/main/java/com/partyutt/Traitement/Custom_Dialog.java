package com.partyutt.Traitement;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.partyutt.CreateParty;
import com.partyutt.R;

/**
 * Created by Bastien on 08/01/2015.
 */
public class Custom_Dialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public EditText editMail;

    public Custom_Dialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_guest_dialog);
        yes = (Button) findViewById(R.id.buttonAdd);
        no = (Button) findViewById(R.id.buttonCancel);
        editMail = (EditText)findViewById(R.id.editMail);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAdd:
                View inflatedView = getLayoutInflater().inflate(R.layout.activity_create_party,null);
                TableLayout tl = (TableLayout)inflatedView.findViewById(R.id.tablelayout);
                TableRow tr = new TableRow(c);
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                TextView invitation = new TextView(c);
                invitation.setText(editMail.getText().toString());
                invitation.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                tr.addView(invitation);
                tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
                dismiss();
                break;
            case R.id.buttonCancel:
                dismiss();
                break;
            default:
                break;
        }
        //dismiss();
    }
}