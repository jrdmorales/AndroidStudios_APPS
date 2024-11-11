package jordan.morales.verduritassa;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, nombreEditText, paisEditText, generoEditText;
    private Button registerButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nombreEditText = findViewById(R.id.nombreEditText);
        paisEditText = findViewById(R.id.paisEditText);
        generoEditText = findViewById(R.id.generoEditText);
        registerButton = findViewById(R.id.registerButton);

        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        TextView textViewLogin = findViewById(R.id.textViewLogin);
        if (textViewLogin != null) {
            textViewLogin.setOnClickListener(v -> {
                Intent intent = new Intent(RegisterUserActivity.this,LoginActivity.class);
                startActivity(intent);
            });
        } else {
            // Esto indica que no se ha encontrado el TextView, puede ayudar a la depuración
            Log.e("RegisterUserActivity", "textViewLogin no se encontró.");
        }
    }



    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String nombre = nombreEditText.getText().toString().trim();
        String pais = paisEditText.getText().toString().trim();
        String genero = generoEditText.getText().toString().trim();

        // Validate the fields
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Introduce un email válido");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            passwordEditText.setError("La contraseña debe tener al menos 6 caracteres");
            return;
        }
        if (TextUtils.isEmpty(nombre)) {
            nombreEditText.setError("Introduce tu nombre");
            return;
        }
        if (TextUtils.isEmpty(pais)) {
            paisEditText.setError("Introduce tu país");
            return;
        }
        if (TextUtils.isEmpty(genero)) {
            generoEditText.setError("Introduce tu género");
            return;
        }

        // Create user in Firebase
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Save additional user information if needed
                    Toast.makeText(RegisterUserActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterUserActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterUserActivity.this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
