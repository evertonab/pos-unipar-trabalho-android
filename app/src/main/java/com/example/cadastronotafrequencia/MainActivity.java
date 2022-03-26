package com.example.cadastronotafrequencia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cadastronotafrequencia.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cadPessoa, R.id.nav_cadDisciplina, R.id.nav_cadTurma, R.id.nav_cadAluno)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void lancarFrequencia(View view) {
        Intent intent = new Intent(view.getContext(), CadastroFrequenciaActivity.class);
        startActivity(intent);
    }

    public void lancarNotas(View view) {
        Intent intent = new Intent(view.getContext(), CadastroNotaActivity.class);
        startActivity(intent);
    }

    public void professores(View view) {
        Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_cadPessoa);
    }

    public void alunos(View view) {
        Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_cadAluno);
    }

    public void disciplinas(View view) {
        Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_cadDisciplina);
    }

    public void turmas(View view) {
        Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_cadTurma);
    }
}