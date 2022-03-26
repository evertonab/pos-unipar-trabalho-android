package com.example.cadastronotafrequencia.ui.lista;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cadastronotafrequencia.CadastroAlunoActivity;
import com.example.cadastronotafrequencia.R;
import com.example.cadastronotafrequencia.adapters.AlunoAdapter;
import com.example.cadastronotafrequencia.dao.AlunoDAO;
import com.example.cadastronotafrequencia.model.Aluno;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListaAlunoFragment extends Fragment {

    private RecyclerView rvListaAlunos;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lista, container, false);

        atualizaListaAluno();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.btCadastro);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCadastroAluno();
            }
        });

        return view;
    }

    private void abrirCadastroAluno() {
        Intent intent = new Intent(view.getContext(),  CadastroAlunoActivity.class);
        startActivityForResult(intent, 1);
    }

    public void atualizaListaAluno() {
        List<Aluno> listaAluno = new ArrayList<>();
        listaAluno = AlunoDAO.retornaAlunos("", new String[]{}, "nome asc");

        rvListaAlunos = (RecyclerView) view.findViewById(R.id.rvLista);

        AlunoAdapter adapter = new AlunoAdapter(view.getContext(), listaAluno);

        rvListaAlunos.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvListaAlunos.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        atualizaListaAluno();
    }


}
