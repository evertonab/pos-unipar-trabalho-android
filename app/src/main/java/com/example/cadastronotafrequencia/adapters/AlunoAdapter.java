package com.example.cadastronotafrequencia.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cadastronotafrequencia.R;
import com.example.cadastronotafrequencia.dao.FrequenciaDAO;
import com.example.cadastronotafrequencia.dao.NotaDAO;
import com.example.cadastronotafrequencia.dao.TurmaDAO;
import com.example.cadastronotafrequencia.model.Aluno;
import com.example.cadastronotafrequencia.model.Frequencia;
import com.example.cadastronotafrequencia.model.Nota;
import com.example.cadastronotafrequencia.model.Turma;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

public class AlunoAdapter extends RecyclerView.Adapter<AlunoAdapter.AlunoViewHolder> {

    private List<Aluno> listaAlunos;
    private Context context;


    public AlunoAdapter(Context context, List<Aluno> listaAlunos) {
        this.listaAlunos = listaAlunos;
        this.context = context;
    }

    public class AlunoViewHolder extends RecyclerView.ViewHolder {

        TextInputEditText edRaAluno;
        TextInputEditText edNomeAluno;
        TextInputEditText edCpfAluno;
        TextInputEditText edTurma;
        TextInputEditText edDtMatricula;
        TextInputEditText edDtNascimento;
        TextInputEditText edAprovacao;
        TextInputEditText edFrequencia;
        TextInputEditText edNota;

        public AlunoViewHolder(@NonNull View itemView) {
            super(itemView);

            edRaAluno = (TextInputEditText) itemView.findViewById(R.id.edRaAluno);
            edNomeAluno = (TextInputEditText) itemView.findViewById(R.id.edNomeAluno);
            edCpfAluno = (TextInputEditText) itemView.findViewById(R.id.edCpfAluno);
            edTurma = (TextInputEditText) itemView.findViewById(R.id.edTurma);
            edDtMatricula = (TextInputEditText) itemView.findViewById(R.id.edDtMatricula);
            edDtNascimento = (TextInputEditText) itemView.findViewById(R.id.edDtNasc);
            edAprovacao = (TextInputEditText) itemView.findViewById(R.id.edAprovacao);
            edFrequencia = (TextInputEditText) itemView.findViewById(R.id.edFrequencia);
            edNota = (TextInputEditText) itemView.findViewById(R.id.edNota);

        }
    }

    @Override
    public AlunoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_aluno, parent, false);

        AlunoAdapter.AlunoViewHolder viewHolder = new AlunoViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlunoViewHolder holder, int position) {
        Aluno aluno = listaAlunos.get(position);

        holder.edRaAluno.setText(String.valueOf(aluno.getRa()));
        holder.edCpfAluno.setText(aluno.getCpf());
        holder.edNomeAluno.setText(aluno.getNome());

        Turma turma = TurmaDAO.retornaPorID(Long.parseLong(aluno.getIdTurma()));
        holder.edTurma.setText(turma.getNome());

        holder.edDtMatricula.setText(aluno.getDtMatricula());
        holder.edDtNascimento.setText(aluno.getDtNasc());

        aprovacaoNotaFrequencia(aluno, holder);

    }

    private void aprovacaoNotaFrequencia(Aluno aluno, AlunoViewHolder holder) {

        List<Nota> list = new ArrayList<>();
        Float media = Float.valueOf(0);
        int soma = 0;
        int pcfrequencia = 0;
        boolean valida = true;

        try {
            List<Frequencia> freq = FrequenciaDAO.retornaFrequencia("id_aluno = ? and id_turma = ?", new String[]{String.valueOf(aluno.getId()), aluno.getIdTurma()}, "");
            if (freq.size() > 0) {pcfrequencia = freq.get(0).getPcFrequencia(); } else valida = false;

        } catch (Exception e) {
            Log.e("AlunoAdapter", "Erro ao buscar frequência do aluno, Erro = " + e.getMessage());
        }


        try {
            list = NotaDAO.retornaNota("id_aluno = ? AND id_turma = ?", new String[]{String.valueOf(aluno.getId()), String.valueOf(aluno.getIdTurma())}, "");

            Turma turma = TurmaDAO.retornaPorID(Long.parseLong(aluno.getIdTurma()));

            if (turma.getRegime().toUpperCase().equals("ANUAL")) {
                if (list.size() >= 4) {
                    for (Nota not : list) {
                        soma = soma + not.getnota();
                    }
                    media = Float.valueOf(soma / 4);
                } else
                    valida = false;
            } else {
                if (list.size() >= 2) {
                    for (Nota not : list) {
                        soma = soma + not.getnota();
                    }
                    media = Float.valueOf(soma / 2);

                } else
                    valida = false;
            }

        } catch (Exception ex) {
            Log.e("Erro", "Erro ao buscar as notas: " + ex.getMessage());
        }

        if(valida){
            if (pcfrequencia < 70 && media < 70) {
                holder.edAprovacao.setText("Aluno Reprovado por Frequencia e Media");
                holder.edAprovacao.setTextColor(Color.RED);
                holder.edFrequencia.setText(pcfrequencia + "%");
                holder.edNota.setText(media.toString());
            } else if (pcfrequencia > 70 && media < 70) {
                holder.edAprovacao.setText("Aluno Reprovado por Média");
                holder.edAprovacao.setTextColor(Color.RED);
                holder.edFrequencia.setText(pcfrequencia + "%");
                holder.edNota.setText(media.toString());
            }else if (pcfrequencia < 70 && media > 70){
                holder.edAprovacao.setText("Aluno Reprovado por Frequencia");
                holder.edAprovacao.setTextColor(Color.RED);
                holder.edFrequencia.setText(pcfrequencia + "%");
                holder.edNota.setText(media.toString());
            }else{
                holder.edAprovacao.setText("Aluno Aprovado");
                holder.edAprovacao.setTextColor(Color.parseColor("#37AC3C"));
                holder.edFrequencia.setText(pcfrequencia + "%");
                holder.edNota.setText(media.toString());
            }
        } else {
            holder.edAprovacao.setText("Em Análise");
            if(pcfrequencia != 0 )  holder.edFrequencia.setText(pcfrequencia + "%");
            if(media != 0) holder.edNota.setText(media.toString());
        }


    }

    @Override
    public int getItemCount() {
        return listaAlunos.size();
    }
}
