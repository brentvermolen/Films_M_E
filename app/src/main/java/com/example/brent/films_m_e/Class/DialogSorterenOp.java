package com.example.brent.films_m_e.Class;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.example.brent.films_m_e.R;

import java.util.ArrayList;
import java.util.List;

public class DialogSorterenOp extends AlertDialog.Builder {

    private Context mContext;
    public Spinner spinner;
    public CheckBox chkOmdraaien;

    List<String> data = new ArrayList<>();

    public DialogSorterenOp(Context context, int currentSort, boolean currentDesc){
        super(context);
        mContext = context;

        setDialog(currentSort, currentDesc);
    }

    private void setDialog(int currentSort, boolean currentDesc) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_sort, null);

        spinner = view.findViewById(R.id.cboSorterenOp);
        chkOmdraaien = view.findViewById(R.id.chkDescending);

        data.add("Naam");
        data.add("Uitgebracht Op");
        data.add("Duur");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chkOmdraaien.setVisibility(View.VISIBLE);
                switch (position){
                    case 0:
                        chkOmdraaien.setText("Z-A");
                        break;
                    case 1:
                        chkOmdraaien.setText("Nieuwste eerste");
                        break;
                    case 2:
                        chkOmdraaien.setText("Langste eerst");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                chkOmdraaien.setVisibility(View.GONE);
            }
        });

        spinner.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, data));

        chkOmdraaien.setChecked(currentDesc);
        spinner.setSelection(currentSort);

        this.setView(view);
    }

    public int getSpinnerValue(){
        return spinner.getSelectedItemPosition();
    }

    public boolean getDescending(){
        return chkOmdraaien.isChecked();
    }
}
