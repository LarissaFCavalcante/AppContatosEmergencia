package com.example.fivecontacts.ContatosEmergencia.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.fivecontacts.R;
import com.example.fivecontacts.ContatosEmergencia.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PerfilUsuario_Activity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    EditText edUser;
    EditText edPass;
    EditText edNome;
    EditText edEmail;
    Switch swLogado, swTema2;

    Button btModificar;
    BottomNavigationView bnv;

    User user;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        btModificar = findViewById(R.id.btCriar);
        bnv = findViewById(R.id.bnv);
        bnv.setOnNavigationItemSelectedListener(this);
        bnv.setSelectedItemId(R.id.anvPerfil);

        edUser = findViewById(R.id.edT_Login2);
        edPass = findViewById(R.id.edt_Pass2);
        edNome = findViewById(R.id.edtNome);
        edEmail = findViewById(R.id.edEmail);
        swLogado = findViewById(R.id.swLogado);
        swTema2 = findViewById(R.id.swTema2);
        view = this.getWindow().getDecorView();

        Intent quemChamou = this.getIntent();
        if (quemChamou != null) {
            Bundle params = quemChamou.getExtras();
            if (params != null) {
                //Recuperando o Usuario
                user = (User) params.getSerializable("usuario");
                setTitle("Alterar dados de "+user.getNome());
                if (user.isTema_escuro()){
                    view.setBackgroundColor(Color.BLACK);
                }
            }
        }

        if (user != null) {
            edUser.setText(user.getLogin());
            edPass.setText(user.getSenha());
            edNome.setText(user.getNome());
            edEmail.setText(user.getEmail());
            swLogado.setChecked(user.isManterLogado());
            swTema2.setChecked(user.isTema_escuro());
        }

        btModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setNome(edNome.getText().toString());
                user.setLogin(edUser.getText().toString());
                user.setSenha(edPass.getText().toString());
                user.setEmail(edEmail.getText().toString());
                user.setManterLogado(swLogado.isChecked());
                user.setTema_escuro(swTema2.isChecked());
                salvarModificacoes(user);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Checagem de o Item selecionado é a de mudanças de contatos
        if (item.getItemId() == R.id.anvMudar) {
            //Abertura da Tela de Perfil
            Intent intent = new Intent(this, AlterarContatos_Activity.class);
            intent.putExtra("usuario", user);
            startActivity(intent);

        }
        // Checagem de o Item selecionado é Ligar
        if (item.getItemId() == R.id.anvLigar) {
            //Abertura da Tela Mudar COntatos
            Intent intent = new Intent(this, ListaDeContatos_Activity.class);
            intent.putExtra("usuario", user);
            startActivity(intent);

        }
        return true;
    }

    public void salvarModificacoes(User user){
        SharedPreferences salvaUser= getSharedPreferences("usuarioPadrao", Activity.MODE_PRIVATE);
        SharedPreferences.Editor escritor= salvaUser.edit();

        escritor.putString("nome", user.getNome());
        escritor.putString("senha", user.getSenha());
        escritor.putString("login", user.getLogin());
        escritor.putString("email", user.getEmail());
        escritor.putBoolean("manterLogado", user.isManterLogado());
        escritor.putBoolean("tema", user.isTema_escuro());

        escritor.commit(); //Salva em Disco

        this.user = atualizarUser();

        Toast.makeText(PerfilUsuario_Activity.this,"Modificações Salvas",Toast.LENGTH_LONG).show();

        finish();
    }

    private User atualizarUser() {
        User user = null;
        SharedPreferences temUser= getSharedPreferences("usuarioPadrao", Activity.MODE_PRIVATE);
        String loginSalvo = temUser.getString("login","");
        String senhaSalva = temUser.getString("senha","");
        String nomeSalvo = temUser.getString("nome","");
        String emailSalvo = temUser.getString("email","");
        boolean manterLogado = temUser.getBoolean("manterLogado",false);
        boolean temaEscuro = temUser.getBoolean("tema", false);

        user = new User(nomeSalvo, loginSalvo, senhaSalva, emailSalvo, manterLogado, temaEscuro);
        return user;
    }
}