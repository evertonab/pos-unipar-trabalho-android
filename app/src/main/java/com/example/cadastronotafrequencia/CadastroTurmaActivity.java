package com.example.cadastronotafrequencia;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cadastronotafrequencia.dao.DisciplinaDAO;
import com.example.cadastronotafrequencia.dao.TurmaDAO;
import com.example.cadastronotafrequencia.model.Disciplina;
import com.example.cadastronotafrequencia.model.Turma;
import com.example.cadastronotafrequencia.util.Util;
import com.google.android.material.textfield.TextInputEditText;
import fr.ganfra.materialspinner.MaterialSpinner;
import id.ionbit.ionalert.IonAlert;

import java.util.ArrayList;
import java.util.List;

public class CadastroTurmaActivity extends AppCompatActivity {

    private TextInputEditText edNomeTurma;
    private TextInputEditText edDisciplinaTurma;
    private TextInputEditText edPeriodoTurma;
    private TextInputEditText edRegimeTurma;
    private MaterialSpinner   spRegime;
    private MaterialSpinner   spDisciplina;
    private MaterialSpinner   spPeriodo;

    private LinearLayout lnPrincipal;

    List<Disciplina> lisDisc = new ArrayList<>();
    Disciplina disciplinaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_turma);

        edNomeTurma       = findViewById(R.id.edNomeTurma);
        edDisciplinaTurma = findViewById(R.id.edDisciplinaTurma);
        edPeriodoTurma    = findViewById(R.id.edPeriodoTurma);
        edRegimeTurma     = findViewById(R.id.edRegimeTurma);

        lnPrincipal = findViewById(R.id.lnPrincipal);

        iniciaSpinners();
    }
    private void iniciaSpinners() {
        spDisciplina = findViewById(R.id.spDisciplinaTurma);
        spPeriodo    = findViewById(R.id.spPeriodoTurma);
        spRegime     = findViewById(R.id.spRegime);

        lisDisc = DisciplinaDAO.retornaDisciplina("", new String[]{}, "");

        String periodos[] = new String[]{"Matutino",
                "Vespertino",
                "Noturno",};

        String regimes[] = new String[]{"Anual",
                "Semestral"
        };

        ArrayAdapter adapterPeriodo    = new ArrayAdapter(this, android.R.layout.simple_list_item_1, periodos);
        ArrayAdapter adapterDisciplina = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lisDisc);
        ArrayAdapter adapterRegime     = new ArrayAdapter(this, android.R.layout.simple_list_item_1, regimes);

        spPeriodo.setAdapter(adapterPeriodo);
        spDisciplina.setAdapter(adapterDisciplina);
        spRegime.setAdapter(adapterRegime);

        spDisciplina.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < 0) {
                    disciplinaSelecionada = new Disciplina();
                }else {
                    disciplinaSelecionada = lisDisc.get((int) (id) - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflaterMenu = getMenuInflater();
        inflaterMenu.inflate(R.menu.menu_cadastro, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mn_limpar:

                limparCampos();

                return true;
            case R.id.mn_save:

                validaCampos();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void limparCampos() {
        edNomeTurma.setText("");
        spPeriodo.setSelection(0);
        spDisciplina.setSelection(0);
        spRegime.setSelection(0);
    }

    private void validaCampos() {

        spDisciplina = findViewById(R.id.spDisciplinaTurma);
        spPeriodo    = findViewById(R.id.spPeriodoTurma);
        spRegime     = findViewById(R.id.spRegime);

        if (edNomeTurma.getText().toString().isEmpty()) {
            edNomeTurma.setError("Informe o Nome da Turma");
            edNomeTurma.requestFocus();

            return;
        }

        if (spPeriodo.getSelectedItem() == null){
            TextView errorText = (TextView)spPeriodo.getSelectedView();
            errorText.setError("Informe o Per??odo!");
            errorText.setTextColor(Color.RED);
            errorText.setText("Informe o Per??odo!");
            errorText.requestFocus();
            return;
        }

        if (disciplinaSelecionada.getNome().length() <= 0){
            spDisciplina.setError("Informe a Disciplina");
            spDisciplina.requestFocus();

            return;
        }
        salvarTurma();
    }

    public void salvarTurma() {
        Turma turma = new Turma();
        turma.setNome(edNomeTurma.getText().toString());
        turma.setPeriodo(spPeriodo.getSelectedItem().toString());
        turma.setRegime(spRegime.getSelectedItem().toString());
        turma.setDisciplina(disciplinaSelecionada.getNome());

        if (TurmaDAO.salvar(turma) > 0) {
            new IonAlert(this, IonAlert.SUCCESS_TYPE)
                    .setTitleText("Turma salva com sucesso")
                    .setConfirmClickListener(new IonAlert.ClickListener() {
                        @Override
                        public void onClick(IonAlert sDialog) {
                            sDialog.dismiss();
                            finish();
                        }
                    })
                    .show();
        } else {
            new IonAlert(this, IonAlert.ERROR_TYPE)
                    .setTitleText("Erro ao salvar turma, entre em contato com o suporte")
                    .show();
        }
    }
}