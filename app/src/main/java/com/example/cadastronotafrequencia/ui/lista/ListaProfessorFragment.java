package com.example.cadastronotafrequencia.ui.lista;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cadastronotafrequencia.CadastroProfessorActivity;
import com.example.cadastronotafrequencia.R;
import com.example.cadastronotafrequencia.adapters.ProfessorAdapter;
import com.example.cadastronotafrequencia.dao.ProfessorDAO;
import com.example.cadastronotafrequencia.model.Professor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListaProfessorFragment extends Fragment {

    private RecyclerView recyclerView;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lista, container, false);

        atualizaListaProfessor();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.btCadastro);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCadastroProfessor();
            }
        });

        return view;
    }

    private void abrirCadastroProfessor() {
        Intent intent = new Intent(view.getContext(), CadastroProfessorActivity.class);
        startActivityForResult(intent, 1);
    }

    public void atualizaListaProfessor() {
        List<Professor> listaProfessor = new ArrayList<>();
        listaProfessor = ProfessorDAO.retornaProfessor("", new String[]{}, "nome asc");

        recyclerView = (RecyclerView) view.findViewById(R.id.rvLista);

        ProfessorAdapter adapter = new ProfessorAdapter(view.getContext(), listaProfessor);

        //recyclerView = view.findViewById(R.id.card_view_aluno);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        atualizaListaProfessor();
    }
}
