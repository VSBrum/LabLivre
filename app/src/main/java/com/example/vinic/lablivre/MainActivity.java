package com.example.vinic.lablivre;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    ProgressBar progressBar;
    ListView listView;
    List<Laboratory> labList;
    List<Laboratory> listaLab;
    Button btnFilter;
    Button btnLimpar;
    Switch btnLabsLivre;
    CheckBox a2;
    CheckBox b6;
    CheckBox b7;
    CheckBox b8;
    List<String> filtrarBloco = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnOpenFilter = (Button) findViewById(R.id.btnOpenFilter);
        final RelativeLayout filterBlock = (RelativeLayout) findViewById(R.id.filterBlock);
        final RelativeLayout containerPrincipal = (RelativeLayout) findViewById(R.id.containerPrincipal);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnLabsLivre = (Switch) findViewById(R.id.freeLabsOnly);
        btnFilter = (Button) findViewById(R.id.btnFilter);
        btnLimpar = (Button) findViewById(R.id.btnCancel);
        a2 = (CheckBox) findViewById(R.id.check1);
        b6 = (CheckBox) findViewById(R.id.check2);
        b7 = (CheckBox) findViewById(R.id.check3);
        b8 = (CheckBox) findViewById(R.id.check4);
        listView = (ListView) findViewById(R.id.listViewLabs);
        labList = new ArrayList<>();
        listaLab = new ArrayList<>();
        btnLimpar.setEnabled(false);

        btnOpenFilter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                float screenHeight;
                screenHeight = containerPrincipal.getMeasuredHeight();

                if (filterBlock.getHeight() != 0) {
                    filterBlock.getLayoutParams().height = 0;
                    filterBlock.requestLayout();
                } else {
                    filterBlock.getLayoutParams().height = Math.round(screenHeight);
                    filterBlock.requestLayout();
                }
            }
        });

        if(btnLimpar.getBackground().equals(R.color.md_blue_200)){
            btnLimpar.setEnabled(false);
        }else{
            btnLimpar.setEnabled(true);
        }
        readLaboratories();
    }

    private void readLaboratories() {
        PerformNetworkRequest request = new PerformNetworkRequest("https://pastebin.com/raw/uXfyvpEY", null, CODE_GET_REQUEST);
        request.execute();
    }

    //Link Atual(com problema) http://localhost/LuaPI/v1/Api.php?apicall=getLab
    // Antigo link https://pastebin.com/raw/AJ26zqFm
    // Link com JSON que criei a mão https://pastebin.com/raw/sYMTMeu0

    private void refreshLabsList(final JSONArray labs) throws JSONException {
        final RelativeLayout filterBlock = (RelativeLayout) findViewById(R.id.filterBlock);
        labList.clear();

        for (int i = 0; i < labs.length(); i++) {
            JSONObject obj = labs.getJSONObject(i);
            listaLab.add(new Laboratory(
                    obj.getInt("cod_lab"),
                    obj.getString("nome"),
                    obj.getInt("quantComputadores"),
                    obj.getInt("Status"),
                    obj.getString("bloco_cod_bloco"),
                    obj.getJSONArray("Reservas")
            ));
        }

        for (int i = 0; i < labs.length(); i++) {
            labList.add(listaLab.get(i));
        }
        LabAdapter adapter = new LabAdapter(labList);
        listView.setAdapter(adapter);

        btnLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnLabsLivre.isChecked() | a2.isChecked() | b6.isChecked() | b7.isChecked() | b8.isChecked()) {
                    labList.clear();
                    btnLimpar.setBackgroundResource(R.color.md_blue_200);
                    for (int i = 0; i < labs.length(); i++) {
                        labList.add(listaLab.get(i));
                    }
                    LabAdapter adapter = new LabAdapter(labList);
                    listView.setAdapter(adapter);
                }
                btnLabsLivre.setChecked(false);
                a2.setChecked(false);
                b6.setChecked(false);
                b7.setChecked(false);
                b8.setChecked(false);
                Toast.makeText(getApplicationContext(), "Successfully Cleaned", Toast.LENGTH_SHORT).show();
                filterBlock.getLayoutParams().height = 0;
                filterBlock.requestLayout();
                btnLimpar.setEnabled(false);

            }
        });

        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a2.isChecked()) {
                    filtrarBloco.add("A2");
                    btnLimpar.setBackgroundResource(R.color.colorPrimaryDark);
                    btnLimpar.setEnabled(true);
                } else {
                    for (int x = 0; x < filtrarBloco.size(); x++) {
                        if (filtrarBloco.get(x).equals("A2"))
                            filtrarBloco.remove(x);
                    }
                    btnLimpar.setEnabled(false);
                    if(!btnLabsLivre.isChecked() & !a2.isChecked() & !b6.isChecked() & !b7.isChecked() & !b8.isChecked()){
                        btnLimpar.setBackgroundResource(R.color.md_blue_200);
                    }
                }
            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b6.isChecked()) {
                    filtrarBloco.add("B6");
                    btnLimpar.setBackgroundResource(R.color.colorPrimaryDark);
                    btnLimpar.setEnabled(true);
                } else {
                    for (int x = 0; x < filtrarBloco.size(); x++) {
                        if (filtrarBloco.get(x).equals("B6"))
                            filtrarBloco.remove(x);
                    }
                    btnLimpar.setEnabled(false);
                    if(!btnLabsLivre.isChecked() & !a2.isChecked() & !b6.isChecked() & !b7.isChecked() & !b8.isChecked()){
                        btnLimpar.setBackgroundResource(R.color.md_blue_200);
                    }
                }
            }
        });

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b7.isChecked()) {
                    filtrarBloco.add("B7");
                    btnLimpar.setBackgroundResource(R.color.colorPrimaryDark);
                    btnLimpar.setEnabled(true);
                } else {
                    for (int x = 0; x < filtrarBloco.size(); x++) {
                        if (filtrarBloco.get(x).equals("B7"))
                            filtrarBloco.remove(x);
                    }
                    btnLimpar.setEnabled(false);
                    if(!btnLabsLivre.isChecked() & !a2.isChecked() & !b6.isChecked() & !b7.isChecked() & !b8.isChecked()){
                        btnLimpar.setBackgroundResource(R.color.md_blue_200);
                    }
                }
            }
        });

        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b8.isChecked()) {
                    filtrarBloco.add("B8");
                    btnLimpar.setBackgroundResource(R.color.colorPrimaryDark);
                    btnLimpar.setEnabled(true);
                } else {
                    for (int x = 0; x < filtrarBloco.size(); x++) {
                        if (filtrarBloco.get(x).equals("B8"))
                            filtrarBloco.remove(x);
                    }
                    btnLimpar.setEnabled(false);
                    if(!btnLabsLivre.isChecked() & !a2.isChecked() & !b6.isChecked() & !b7.isChecked() & !b8.isChecked()){
                        btnLimpar.setBackgroundResource(R.color.md_blue_200);
                    }
                }
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                filterBlock.getLayoutParams().height = 0;
                filterBlock.requestLayout();
                labList.clear();
                Toast.makeText(getApplicationContext(), "Successfully Filtered", Toast.LENGTH_SHORT).show();
                btnLimpar.setBackgroundResource(R.color.colorPrimaryDark);
                for (int x = 0; x < labs.length(); x++) {
                    try {
                        JSONObject labAtual = labs.getJSONObject(x);
                        boolean inserir = false;
                        for (int y = 0; y < filtrarBloco.size(); y++) {
                            if (labAtual.getString("bloco_cod_bloco").toString().equals(filtrarBloco.get(y).toString())) {
                                inserir = true;
                            }
                        }
                        if (btnLabsLivre.isChecked()) {
                            if (inserir & labAtual.getInt("Status") == 0) {
                                labList.add(listaLab.get(x));
                                LabAdapter adapter = new LabAdapter(labList);
                                listView.setAdapter(adapter);
                                btnLimpar.setEnabled(true);
                            }
                        } else {
                            if (inserir) {
                                labList.add(listaLab.get(x));
                                LabAdapter adapter = new LabAdapter(labList);
                                listView.setAdapter(adapter);
                                /*Toast.makeText(getApplicationContext(), "Successfully Filtered", Toast.LENGTH_SHORT).show();
                                btnLimpar.setBackgroundResource(R.color.colorPrimaryDark);*/
                                btnLimpar.setEnabled(true);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (btnLabsLivre.isChecked() & !a2.isChecked() & !b6.isChecked() & !b7.isChecked() & !b8.isChecked()) {
                    labList.clear();
                    for (int i = 0; i < labs.length(); i++) {
                        JSONObject obj;
                        try {
                            obj = labs.getJSONObject(i);
                            if (obj.getInt("Status") == 0) {
                                labList.add(listaLab.get(i));
                                LabAdapter adapter = new LabAdapter(labList);
                                listView.setAdapter(adapter);
                                btnLimpar.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(GONE);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshLabsList(object.getJSONArray("labs"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            if (requestCode == CODE_POST_REQUEST) {
                return requestHandler.sendPostRequest(url, params);
            }
            if (requestCode == CODE_GET_REQUEST) {
                return requestHandler.sendGetRequest(url);
            }
            return null;
        }
    }

    class LabAdapter extends ArrayAdapter<Laboratory> {
        List<Laboratory> labList;

        LabAdapter(List<Laboratory> labList) {
            super(MainActivity.this, R.layout.layout_labs_list, labList);
            this.labList = labList;
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            String msg = "Livre";

            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint({"ViewHolder", "InflateParams"})
            View listViewItem = inflater.inflate(R.layout.layout_labs_list, null, true);

            TextView textViewLab = (TextView) listViewItem.findViewById(R.id.textViewLab);
            TextView textViewBlock = (TextView) listViewItem.findViewById(R.id.textViewBlock);
            TextView textViewComputers = (TextView) listViewItem.findViewById(R.id.textViewComputers);
            TextView textViewStatus = (TextView) listViewItem.findViewById(R.id.textViewStatus);
            TextView txt = (TextView) listViewItem.findViewById(R.id.textView2);

            Laboratory laboratory = labList.get(position);

            textViewLab.setText(laboratory.getNome());
            textViewBlock.setText("Bloco: " + laboratory.getBloco_cod_bloco());
            textViewComputers.setText(laboratory.getQuantComputadores() + " computadores");

            try {
                String dataInicio = laboratory.getReservas().getJSONObject(0).getString("dataHoraInicio");
                String dataFim = laboratory.getReservas().getJSONObject(0).getString("dataHoraFim");
                String data = "dd/MM";

                java.util.Date agora = new java.util.Date();
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat formata = new SimpleDateFormat(data);
                String DataAgora = formata.format(agora);

                String DataInicial = dataInicio.substring(8, 10) + "/" + dataInicio.substring(5, 7);
                String HorarioInicial = dataInicio.substring(11, 13) + ":" + dataInicio.substring(14, 16);
                String HorarioFinal = dataFim.substring(11, 13) + ":" + dataFim.substring(14, 16);

                if (DataInicial.equals(DataAgora)) {
                    msg = "Ocupado";
                    textViewLab.setBackgroundResource(R.color.colorDanger);
                    textViewStatus.setText("Dia: " + DataInicial + "\nReservado de " + HorarioInicial + " as " + HorarioFinal);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            txt.setText(msg);
            return listViewItem;
        }
    }
}