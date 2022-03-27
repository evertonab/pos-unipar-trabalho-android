package com.example.cadastronotafrequencia.ui.lista;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cadastronotafrequencia.CadastroDisciplinaActivity;
import com.example.cadastronotafrequencia.CadastroProfessorActivity;
import com.example.cadastronotafrequencia.R;
import com.example.cadastronotafrequencia.adapters.DisciplinaAdapter;
import com.example.cadastronotafrequencia.adapters.ProfessorAdapter;
import com.example.cadastronotafrequencia.dao.DisciplinaDAO;
import com.example.cadastronotafrequencia.dao.ProfessorDAO;
import com.example.cadastronotafrequencia.model.Disciplina;
import com.example.cadastronotafrequencia.model.Professor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListaDisciplinaFragment extends Fragment {

    private RecyclerView recyclerView;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lista, container, false);

        atualizaListaDisciplina();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.btCadastro);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   abrirCadastroDisciplina();
            }
        });

        return view;
    }

    private void abrirCadastroDisciplina() {
        Intent intent = new Intent(view.getContext(), CadastroDisciplinaActivity.class);
        startActivity(intent);
    }

    public void atualizaListaDisciplina() {
        List<Disciplina> listaDisciplina = new ArrayList<>();
        listaDisciplina = DisciplinaDAO.retornaDisciplina("", new String[]{}, "nome asc");

        recyclerView = (RecyclerView) view.findViewById(R.id.rvLista);

        DisciplinaAdapter adapter = new DisciplinaAdapter(view.getContext(), listaDisciplina);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        atualizaListaDisciplina();
    }
}