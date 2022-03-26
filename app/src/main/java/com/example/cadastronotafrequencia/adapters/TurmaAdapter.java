package com.example.cadastronotafrequencia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cadastronotafrequencia.R;
import com.example.cadastronotafrequencia.dao.AlunoDAO;
import com.example.cadastronotafrequencia.model.Turma;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class TurmaAdapter extends RecyclerView.Adapter<TurmaAdapter.TurmaViewHolder> {
    private List<Turma> listaTurmas;
    private Context context;

    public TurmaAdapter( Context context, List<Turma> listaTurmas) {
        this.listaTurmas = listaTurmas;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return listaTurmas.size();
    }

    @Override
    public TurmaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_turma, parent, false);
        TurmaViewHolder viewHolder = new TurmaViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TurmaAdapter.TurmaViewHolder holder, int position) {
        Turma turma = listaTurmas.get(position);
        holder.edNomeTurma       .setText(turma.getNome());
        holder.edPeriodoTurma    .setText(turma.getPeriodo());
        holder.edDisciplinaTurma .setText(turma.getDisciplina());

        int quantTurma = AlunoDAO.retornaAlunos("id_turma = ?", new String[]{String.valueOf(turma.getId())}, "").size();

        holder.edQtAlunosTurma   .setText(String.valueOf(quantTurma));
        holder.edRegimeTurma     .setText(turma.getRegime());
    }


    public class TurmaViewHolder extends RecyclerView.ViewHolder {
        TextInputEditText edNomeTurma;
        TextInputEditText edDisciplinaTurma;
        TextInputEditText edPeriodoTurma;
        TextInputEditText edQtAlunosTurma;
        TextInputEditText edRegimeTurma;
        public TurmaViewHolder(@NonNull View itemView) {
            super(itemView);

            edNomeTurma        = (TextInputEditText)itemView.findViewById(R.id.edNomeTurma);
            edDisciplinaTurma  = (TextInputEditText)itemView.findViewById(R.id.edDisciplinaTurma);
            edPeriodoTurma     = (TextInputEditText)itemView.findViewById(R.id.edPeriodoTurma);
            edQtAlunosTurma    = (TextInputEditText)itemView.findViewById(R.id.edQtAlunos);
            edRegimeTurma      = (TextInputEditText)itemView.findViewById(R.id.edRegimeTurma);
        }
    }
}
