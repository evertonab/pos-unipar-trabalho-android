package com.example.cadastronotafrequencia;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cadastronotafrequencia.dao.AlunoDAO;
import com.example.cadastronotafrequencia.dao.FrequenciaDAO;
import com.example.cadastronotafrequencia.dao.TurmaDAO;
import com.example.cadastronotafrequencia.model.Aluno;
import com.example.cadastronotafrequencia.model.Frequencia;
import com.example.cadastronotafrequencia.model.Turma;
import com.example.cadastronotafrequencia.util.Util;
import com.google.android.material.textfield.TextInputEditText;
import fr.ganfra.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

public class CadastroFrequenciaActivity extends AppCompatActivity {

    private MaterialSpinner spTurma;
    private AutoCompleteTextView edNomeAluno;
    private TextInputEditText edPcFrequencia;
    private LinearLayout lnFrequencia;


    List<Turma> listTurma = new ArrayList<>();
    List<Aluno> listaAluno = new ArrayList<>();
    String[] nomes;
    ArrayAdapter<String> adapter;
    Turma turmaSelecionada = new Turma();
    int raAlunoSelecionado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_frequencia);

        edNomeAluno    = findViewById(R.id.edNomeAluno);
        edNomeAluno.setVisibility(View.GONE);
        lnFrequencia = findViewById(R.id.lnFrequencia);

        edPcFrequencia = findViewById(R.id.edPcFrequencia);

        iniciaSpinners();
        iniciaListaAlunos();
    }

    private void iniciaSpinners() {
        spTurma = findViewById(R.id.spTurma);

        listTurma = TurmaDAO.retornaTurma("",new String[]{},"nome asc");

        ArrayAdapter adapterTurma = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listTurma);

        spTurma.setAdapter(adapterTurma);
        spTurma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < 0) {
                    turmaSelecionada = new Turma();
                    edNomeAluno.setVisibility(View.GONE);
                }else {
                    turmaSelecionada = listTurma.get((int) (id) - 1);
                    edNomeAluno.setVisibility(View.VISIBLE);
                    atualizaSelect();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void atualizaSelect() {
        if (turmaSelecionada.getId() == null) {
            listaAluno = AlunoDAO.retornaAlunos("", new String[]{}, "nome asc");
        }else {
            listaAluno = AlunoDAO.retornaAlunos("id_turma = ?", new String[]{String.valueOf(turmaSelecionada.getId())}, "nome asc");
        }

        nomes = new String[listaAluno.size()];
        for (int i = 0; i < listaAluno.size(); i++) {
            Aluno aluno = listaAluno.get(i);
            nomes[i] = aluno.getNome() + ", RA: " + aluno.getRa();
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, nomes);
        edNomeAluno.setAdapter(adapter);

    }

    private void iniciaListaAlunos() {
        atualizaSelect();
        edNomeAluno.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                raAlunoSelecionado = Integer.parseInt(adapter.getItem(position).substring(adapter.getItem(position).indexOf("RA: ") + 4,adapter.getItem(position).length()));
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
        spTurma.setSelection(0);
        edPcFrequencia.setText("");
        edNomeAluno.setText("");
    }

    private void validaCampos() {
        //Valida o campo RA do Aluno
        Aluno aluno = new Aluno();
        aluno = AlunoDAO.retornaPorRA(raAlunoSelecionado);
        if (aluno == null) {
            edNomeAluno.setError("Aluno não cadastrado");
            edNomeAluno.requestFocus();
            return;
        }
        salvarFrequencia(aluno);
    }

    public void salvarFrequencia(Aluno aluno) {
        Frequencia frequencia = new Frequencia();

        frequencia.setIdAluno(aluno.getId());
        frequencia.setIdTurma(turmaSelecionada.getId());


        if (FrequenciaDAO.salvar(frequencia) > 0) {
            setResult(RESULT_OK);
            finish();
        } else {
            Util.customSnakeBar(lnFrequencia, "Erro ao salvar frequencia", 0);
        }
    }
}