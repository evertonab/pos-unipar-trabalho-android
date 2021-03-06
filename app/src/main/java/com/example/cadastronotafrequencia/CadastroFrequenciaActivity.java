package com.example.cadastronotafrequencia;


import android.os.Bundle;
import android.util.Log;
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
import id.ionbit.ionalert.IonAlert;

import java.util.ArrayList;
import java.util.List;


public class CadastroFrequenciaActivity extends AppCompatActivity {

    private MaterialSpinner spTurma;
    private AutoCompleteTextView edNomeAluno;
    private TextInputEditText edPcFrequencia;
    private LinearLayout lnPrincipal;

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

        lnPrincipal = findViewById(R.id.lnPrincipal);
        edNomeAluno = findViewById(R.id.edNomeAluno);
        edNomeAluno.setVisibility(View.GONE);

        edPcFrequencia = findViewById(R.id.edPcFrequencia);

        iniciaSpinners();
        iniciaListaAlunos();
    }

    private void iniciaSpinners() {
        spTurma = findViewById(R.id.spTurma);

        listTurma = TurmaDAO.retornaTurma("", new String[]{}, "nome asc");

        ArrayAdapter adapterTurma = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listTurma);

        spTurma.setAdapter(adapterTurma);
        spTurma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < 0) {
                    turmaSelecionada = new Turma();
                    edNomeAluno.setVisibility(View.GONE);
                } else {
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
        } else {
            listaAluno = AlunoDAO.retornaAlunos("id_turma = ?", new String[]{String.valueOf(turmaSelecionada.getId())}, "nome asc");
        }

        nomes = new String[listaAluno.size()];
        for (int i = 0; i < listaAluno.size(); i++) {
            Aluno aluno = listaAluno.get(i);
            nomes[i] = aluno.getNome() + ", RA: " + aluno.getRa();
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nomes);
        edNomeAluno.setAdapter(adapter);

    }

    private void iniciaListaAlunos() {
        atualizaSelect();
        edNomeAluno.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                raAlunoSelecionado = Integer.parseInt(adapter.getItem(position).substring(adapter.getItem(position).indexOf("RA: ") + 4, adapter.getItem(position).length()));
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
        //Valida Turma
        try {
            if (turmaSelecionada.getId() != null) {
                if (turmaSelecionada.getId() <= 0) {
                    spTurma.setError("Informe a Turma do Aluno");
                    spTurma.requestFocus();

                    return;
                }
            } else {
                spTurma.setError("Informe a Turma do Aluno");
                spTurma.requestFocus();

                return;
            }

        } catch (Exception e) {
            Log.e("Frequencia", "Erro ao buscar a turma selecionada");
        }
        try {
            if (edNomeAluno.getText().toString().isEmpty()) {
                edNomeAluno.setError("Informe o Aluno");
                edNomeAluno.requestFocus();

                return;
            } else {
                raAlunoSelecionado = 0;

                String textCampoAluno = edNomeAluno.getText().toString();
                raAlunoSelecionado = Integer.parseInt(textCampoAluno.substring(textCampoAluno.indexOf("RA: ") + 4, textCampoAluno.length()));

                Aluno aluno = AlunoDAO.retornaPorRA(raAlunoSelecionado);

                if (aluno == null) {
                    edNomeAluno.setError("N??o encontrado Aluno com RA (" + raAlunoSelecionado + "), Verifique !");
                    edNomeAluno.requestFocus();

                    return;
                }
            }
        } catch (Exception e) {
            Log.e("Frequencia", "Erro ao extrair RA do campo de Aluno = " + e.getMessage());
        }

        if (raAlunoSelecionado == 0) {
            edNomeAluno.setError("Informe o Aluno");
            edNomeAluno.requestFocus();

            return;
        }

        //Valida o campo de Frequencia
        if (edPcFrequencia.getText().toString().isEmpty()) {
            edPcFrequencia.setError("Informe o Percentual de frequ??ncia");
            edPcFrequencia.requestFocus();

            return;
        } else if (Integer.parseInt(edPcFrequencia.getText().toString()) > 100) {
            edPcFrequencia.setError("O percentual de ferqu??ncia n??o pode ser maior que 100");
            edPcFrequencia.requestFocus();

            return;
        }

        salvarFrequencia();
    }


    public void salvarFrequencia() {

        Aluno alunoSalvar = new Aluno();
        alunoSalvar = AlunoDAO.retornaPorRA(raAlunoSelecionado);

        List<Frequencia> freqExist = FrequenciaDAO.retornaFrequencia("id_aluno = ? and id_turma = ?", new String[]{String.valueOf(alunoSalvar.getId()),
                String.valueOf(turmaSelecionada.getId())}, "");
        if (freqExist.size() > 0) {
            edNomeAluno.setError("J?? existe lan??amento de frequ??ncia para o aluno");
            edNomeAluno.requestFocus();

            return;
        }

        Frequencia frequencia = new Frequencia();

        frequencia.setIdAluno(alunoSalvar.getId());
        frequencia.setIdTurma(turmaSelecionada.getId());
        frequencia.setPcFrequencia(Integer.parseInt(edPcFrequencia.getText().toString()));

        if (FrequenciaDAO.salvar(frequencia) > 0) {
            new IonAlert(this, IonAlert.SUCCESS_TYPE)
                    .setTitleText("Frequencia salva com sucesso")
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
                    .setTitleText("Erro ao salvar frequencia, entre em contato com o suporte")
                    .show();
        }
    }
}