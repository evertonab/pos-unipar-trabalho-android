package com.example.cadastronotafrequencia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cadastronotafrequencia.R;
import com.example.cadastronotafrequencia.dao.ProfessorDAO;
import com.example.cadastronotafrequencia.model.Disciplina;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class DisciplinaAdapter extends RecyclerView.Adapter<DisciplinaAdapter.DisciplinaViewHolder> {

    private List<Disciplina> listaDisciplina;
    private Context context;

    public DisciplinaAdapter( Context context,List<Disciplina> listaDisciplina) {
        this.listaDisciplina = listaDisciplina;
        this.context = context;
    }

    @Override
    public DisciplinaAdapter.DisciplinaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_disciplina, parent, false);
        DisciplinaAdapter.DisciplinaViewHolder viewHolder = new DisciplinaAdapter.DisciplinaViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DisciplinaAdapter.DisciplinaViewHolder holder, int position) {
        Disciplina disciplina = listaDisciplina.get(position);

        holder.edNomeDisciplina    .setText(disciplina.getNome());
        holder.edNomeProfessor.setText(ProfessorDAO.retornaPorRA(disciplina.getRaProfessor()).getNome());

    }

    @Override
    public int getItemCount() {
        return listaDisciplina.size();
    }

    public class DisciplinaViewHolder extends RecyclerView.ViewHolder {

        TextInputEditText edNomeDisciplina;
        TextInputEditText edNomeProfessor;

        public DisciplinaViewHolder(@NonNull View itemView) {
            super(itemView);

            edNomeDisciplina    = (TextInputEditText)itemView.findViewById(R.id.edNomeDisciplina);
            edNomeProfessor    = (TextInputEditText)itemView.findViewById(R.id.edNomeProfessor);

        }
    }
}
