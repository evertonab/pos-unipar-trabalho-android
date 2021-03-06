package com.example.cadastronotafrequencia;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.cadastronotafrequencia.dao.AlunoDAO;
import com.example.cadastronotafrequencia.dao.ProfessorDAO;
import com.example.cadastronotafrequencia.dao.TurmaDAO;
import com.example.cadastronotafrequencia.model.Aluno;
import com.example.cadastronotafrequencia.model.Professor;
import com.example.cadastronotafrequencia.model.Turma;
import com.example.cadastronotafrequencia.util.CpfMask;
import com.example.cadastronotafrequencia.util.Util;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import fr.ganfra.materialspinner.MaterialSpinner;
import id.ionbit.ionalert.IonAlert;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CadastroAlunoActivity extends AppCompatActivity {

    private TextInputEditText edRaAluno;
    private TextInputEditText edNomeAluno;
    private TextInputEditText edCpfAluno;
    private TextInputEditText edDtNasc;
    private TextInputEditText edDtMatricula;
    private MaterialSpinner spTurma;

    private int vAno;
    private int vMes;
    private int vDia;
    private View dataSelecionada;

    private LinearLayout lnPrincipal;

    List<Turma> listTurma = new ArrayList<>();
    Turma turmaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_aluno);

        edRaAluno = findViewById(R.id.edRaAluno);
        edNomeAluno = findViewById(R.id.edNomeAluno);
        edCpfAluno = findViewById(R.id.edCpfAluno);
        edDtNasc = findViewById(R.id.edDtNasc);
        edDtMatricula = findViewById(R.id.edDtMatricula);

        edDtNasc.setFocusable(false);
        edDtMatricula.setFocusable(false);

        lnPrincipal = findViewById(R.id.lnPrincipal);

        edCpfAluno.addTextChangedListener(CpfMask.insert(edCpfAluno));

        iniciaSpinners();

        setDataAtual();
    }

    private void setDataAtual() {
        final Calendar calendar = Calendar.getInstance();
        vDia = calendar.get(Calendar.DAY_OF_MONTH);
        vMes = calendar.get(Calendar.MONTH);
        vAno = calendar.get(Calendar.YEAR);
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
                }else {
                    turmaSelecionada = listTurma.get((int) (id) - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //A????o ao Selecionar o Item da Lista
       /*
        spPeriodo.setVisibility(View.GONE);

        spCursos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0){

                    Button btADS = new Button(getBaseContext());
                    btADS.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    btADS.setText("Bot??o ADS");
                    btADS.setBackgroundColor(getColor(R.color.teal_200));

                    llPrincipal.addView(btADS);


                    spPeriodo.setVisibility(View.VISIBLE);
                } else
                    spPeriodo.setVisibility(View.GONE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });*/
    }

    //Valida????o dos Campos
    private void validaCampos() {
        //Valida o campo RA do Aluno
        if (edRaAluno.getText().toString().isEmpty()) {
            edRaAluno.setError("Informe o RA do Aluno");
            edRaAluno.requestFocus();

            return;
        }else {
            Aluno alunoExist = AlunoDAO.retornaPorRA(Integer.parseInt(edRaAluno.getText().toString()));
            if (alunoExist != null) {
                if (alunoExist.getRa() > 0) {
                    edRaAluno.setError("Ra j?? cadatrado para o Aluno: " + alunoExist.getNome());
                    edRaAluno.requestFocus();

                    return;
                }
            }
        }


        //Valida o campo Nome do Aluno
        if (edNomeAluno.getText().toString().isEmpty()) {
            edNomeAluno.setError("Informe o Nome do Aluno");
            edNomeAluno.requestFocus();

            return;
        }

        //Valida o campo CPF do Aluno
        if (edCpfAluno.getText().toString().isEmpty()) {
            edCpfAluno.setError("Informe o CPF do Aluno");
            edCpfAluno.requestFocus();

            return;
        }

        //Valida o campo Data de Nascimento do Aluno
        if (edDtNasc.getText().toString().isEmpty()) {
            edDtNasc.setError("Informe a Data de Nascimento do Aluno");
            edDtNasc.requestFocus();

            return;
        }

        //Valida o campo Data de Matricula do Aluno
        if (edDtMatricula.getText().toString().isEmpty()) {
            edDtMatricula.setError("Informe a Data de Matricula do Aluno");
            edDtMatricula.requestFocus();

            return;
        }

        salvarAluno();
    }

    public void salvarAluno() {
        Aluno aluno = new Aluno();
        aluno.setRa(Integer.parseInt(edRaAluno.getText().toString()));
        aluno.setNome(edNomeAluno.getText().toString());
        aluno.setCpf(edCpfAluno.getText().toString());
        aluno.setDtNasc(edDtNasc.getText().toString());
        aluno.setDtMatricula(edDtMatricula.getText().toString());
        aluno.setIdTurma(String.valueOf(turmaSelecionada.getId()));

        if (AlunoDAO.salvar(aluno) > 0) {
            new IonAlert(this, IonAlert.SUCCESS_TYPE)
                    .setTitleText("Aluno Cadastrado com sucesso")
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
                    .setTitleText("Erro ao salvar o aluno, entre em contato com o suporte")
                    .show();
        }
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
        edRaAluno.setText("");
        edNomeAluno.setText("");
        edCpfAluno.setText("");
        edDtNasc.setText("");
        edDtMatricula.setText("");
    }

    public void selecionarData(View view) {

        dataSelecionada = view;
        setDataAtual();
        showDialog(0);

    }

    private DatePickerDialog.OnDateSetListener setDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            vAno = year;
            vMes = month;
            vDia = day;

            atualizaData();
        }
    };

    private void atualizaData() {
        TextInputEditText edit = (TextInputEditText) dataSelecionada;
        edit.setText(new StringBuilder().append(Util.padLeftZeros(String.valueOf(vDia), 2)).append("/").append(Util.padLeftZeros(String.valueOf(vMes + 1), 2)).append("/").append(vAno));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, setDatePicker, vAno, vMes, vDia);
    }

}