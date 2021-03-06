package com.example.cadastronotafrequencia;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.cadastronotafrequencia.dao.ProfessorDAO;
import com.example.cadastronotafrequencia.model.Professor;
import com.example.cadastronotafrequencia.util.CpfMask;
import com.example.cadastronotafrequencia.util.Util;
import com.google.android.material.textfield.TextInputEditText;
import id.ionbit.ionalert.IonAlert;

import java.util.Calendar;

public class CadastroProfessorActivity extends AppCompatActivity {


    private TextInputEditText edRaProfessor;
    private TextInputEditText edNomeProfessor;
    private TextInputEditText edCpfProfessor;
    private TextInputEditText edDtNasc;


    private int vAno;
    private int vMes;
    private int vDia;
    private View dataSelecionada;

    private LinearLayout lnPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_professor);

        edRaProfessor = findViewById(R.id.edRaProfessor);
        edNomeProfessor = findViewById(R.id.edNomeProfessor);
        edCpfProfessor = findViewById(R.id.edCpfProfessor);
        edDtNasc = findViewById(R.id.edDtNasc);


        edDtNasc.setFocusable(false);

        lnPrincipal = findViewById(R.id.lnPrincipal);

        edCpfProfessor.addTextChangedListener(CpfMask.insert(edCpfProfessor));

        setDataAtual();
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

    private void setDataAtual() {
        final Calendar calendar = Calendar.getInstance();
        vDia = calendar.get(Calendar.DAY_OF_MONTH);
        vMes = calendar.get(Calendar.MONTH);
        vAno = calendar.get(Calendar.YEAR);
    }

    //Valida????o dos Campos
    private void validaCampos() {
        //Valida o campo RA do Aluno
        if (edRaProfessor.getText().toString().isEmpty()) {
            edRaProfessor.setError("Informe o RA do Professor");
            edRaProfessor.requestFocus();

            return;
        } else {
            Professor profExist = ProfessorDAO.retornaPorRA(Integer.parseInt(edRaProfessor.getText().toString()));
            if (profExist != null) {
                if (profExist.getRa() > 0) {
                    edRaProfessor.setError("Ra j?? cadatrado para o professor: " + profExist.getNome());
                    edRaProfessor.requestFocus();

                    return;
                }
            }
        }

        //Valida o campo Nome do Aluno
        if (edNomeProfessor.getText().toString().isEmpty()) {
            edNomeProfessor.setError("Informe o Nome do Professor");
            edNomeProfessor.requestFocus();

            return;
        }

        //Valida o campo CPF do Aluno
        if (edCpfProfessor.getText().toString().isEmpty()) {
            edCpfProfessor.setError("Informe o CPF do Professor");
            edCpfProfessor.requestFocus();

            return;
        }

        //Valida o campo Data de Nascimento do Aluno
        if (edDtNasc.getText().toString().isEmpty()) {
            edDtNasc.setError("Informe a Data de Nascimento do Professor");
            edDtNasc.requestFocus();

            return;
        }
        salvarProfessor();
    }


    public void salvarProfessor() {
        Professor professor = new Professor();
        professor.setRa(Integer.parseInt(edRaProfessor.getText().toString()));
        professor.setNome(edNomeProfessor.getText().toString());
        professor.setCpf(edCpfProfessor.getText().toString());
        professor.setDtNascimento(edDtNasc.getText().toString());

        if (ProfessorDAO.salvar(professor) > 0) {
            new IonAlert(this, IonAlert.SUCCESS_TYPE)
                    .setTitleText("Professor cadastrado com sucesso")
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
                    .setTitleText("Erro ao salvar professor, entre em contato com o suporte")
                    .show();
        }
    }

    private void limparCampos() {
        edRaProfessor.setText("");
        edNomeProfessor.setText("");
        edCpfProfessor.setText("");
        edDtNasc.setText("");
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