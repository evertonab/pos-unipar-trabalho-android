package com.example.cadastronotafrequencia;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cadastronotafrequencia.dao.DisciplinaDAO;
import com.example.cadastronotafrequencia.dao.ProfessorDAO;
import com.example.cadastronotafrequencia.model.Disciplina;
import com.example.cadastronotafrequencia.model.Professor;
import com.example.cadastronotafrequencia.util.Util;
import com.google.android.material.textfield.TextInputEditText;
import fr.ganfra.materialspinner.MaterialSpinner;
import id.ionbit.ionalert.IonAlert;

import java.util.ArrayList;
import java.util.List;

public class CadastroDisciplinaActivity extends AppCompatActivity {

    private TextInputEditText edNomeDisciplina;
    private MaterialSpinner spProfessor;
    private LinearLayout lnDisciplina;

    List<Professor> lisPro = new ArrayList<>();
    Professor profSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_disciplina);

        edNomeDisciplina = findViewById(R.id.edNomeDisciplina);
        lnDisciplina     = findViewById(R.id.lnDisciplina);

        iniciaSpinners();
    }

    private void iniciaSpinners() {
        spProfessor = findViewById(R.id.spProfessor);

        lisPro = ProfessorDAO.retornaProfessor("", new String[]{}, "");

        ArrayAdapter adapterProfessor = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lisPro);

        spProfessor.setAdapter(adapterProfessor);

        spProfessor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if (position < 0) {
                   profSelecionado = new Professor();
               }else {
                   profSelecionado = lisPro.get((int) (id) - 1);
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
        edNomeDisciplina.setText("");
        spProfessor.setSelection(0);

    }

    private void validaCampos(){
        //Valida o campo RA do Aluno
        if (edNomeDisciplina.getText().toString().isEmpty()) {
            edNomeDisciplina.setError("Informe o nome da Disciplina");
            edNomeDisciplina.requestFocus();

            return;
        }

        if (profSelecionado.getRa() <= 0) {
            spProfessor.setError("Informe o professor");
            spProfessor.requestFocus();

            return;
        }

        salvarDisciplina();
    }

    public void salvarDisciplina() {
        Disciplina disciplina = new Disciplina();
        disciplina.setNome(edNomeDisciplina.getText().toString());
        disciplina.setRaProfessor(profSelecionado.getRa());

        if (DisciplinaDAO.salvar(disciplina) > 0) {
            new IonAlert(this, IonAlert.SUCCESS_TYPE)
                    .setTitleText("Disciplina Salva com sucesso")
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
                    .setTitleText("Erro ao salvar a disciplina, entre em contato com o suporte")
                    .show();
        }
    }

}