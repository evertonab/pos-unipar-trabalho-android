package com.example.cadastronotafrequencia.ui.lista;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cadastronotafrequencia.CadastroTurmaActivity;
import com.example.cadastronotafrequencia.R;
import com.example.cadastronotafrequencia.adapters.TurmaAdapter;
import com.example.cadastronotafrequencia.dao.TurmaDAO;
import com.example.cadastronotafrequencia.model.Turma;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListaTurmaFragment extends Fragment {

    private RecyclerView recyclerView;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lista, container, false);

        atualizaListaTurma();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.btCadastro);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Funciona agora !!!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                abrirCadastroTurma();
            }
        });

        return view;
    }

    private void abrirCadastroTurma() {
        Intent intent = new Intent(view.getContext(), CadastroTurmaActivity.class);
        startActivityForResult(intent, 1);
    }

    public void atualizaListaTurma() {
        List<Turma> listaTurma = new ArrayList<>();
        listaTurma = TurmaDAO.retornaTurma("", new String[]{}, "nome asc");

        recyclerView = (RecyclerView) view.findViewById(R.id.rvLista);

        TurmaAdapter adapter = new TurmaAdapter(view.getContext(), listaTurma);

        //recyclerView = view.findViewById(R.id.card_view_aluno);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        atualizaListaTurma();
    }
}
