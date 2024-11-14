package jordan.morales.verduritassa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;  // Código de solicitud para inicio de sesión con Google
    private EditText emailEditText, passwordEditText;
    private Button loginButton ;
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private LinearLayout  googleSignInButton ;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializa las vistas
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        googleSignInButton   = findViewById(R.id.googleSignInButton);

        // Inicializa FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Verifica si el usuario ya está autenticado
        if (mAuth.getCurrentUser() != null) {
            updateUI(mAuth.getCurrentUser());
            return;
        }

        // Configura el inicio de sesión con Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))  // Asegúrate de que esta cadena esté configurada en strings.xml
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Configura el listener para el botón de Google
        findViewById(R.id.googleSignInButton).setOnClickListener(v -> signInWithGoogle());

        // Configura el listener para el botón de inicio de sesión con correo electrónico
        loginButton.setOnClickListener(v -> loginUser());

        // Configura el enlace para la pantalla de registro
        TextView registerTextView = findViewById(R.id.registerTextView);
        if (registerTextView != null) {
            registerTextView.setOnClickListener(v -> {
                Intent intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
                startActivity(intent);
            });
        }
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validar que el correo y la contraseña no estén vacíos
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Introduce un email válido");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Introduce la contraseña");
            return;
        }

        // Intentar iniciar sesión con los datos proporcionados
        Log.d("LoginActivity", "Iniciando sesión con correo: " + email);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Si el inicio de sesión fue exitoso, redirigir al MainActivity
                Log.d("LoginActivity", "Inicio de sesión exitoso");
                Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                updateUI(mAuth.getCurrentUser());
            } else {
                // Si hubo un error, mostrar un mensaje adecuado
                Log.e("LoginActivity", "Error al iniciar sesión: " + task.getException().getMessage());
                Toast.makeText(LoginActivity.this, "Error al iniciar sesión: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para iniciar sesión con Google
    private void signInWithGoogle() {
        Log.d("LoginActivity", "Iniciando sesión con Google");
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Maneja el resultado de la actividad de inicio de sesión con Google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Autenticación con Google exitosa
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.e("LoginActivity", "Error en inicio de sesión con Google: " + e.getStatusCode());
                Toast.makeText(this, "Error en inicio de sesión con Google", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Autenticación con Firebase usando la cuenta de Google
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("LoginActivity", "Autenticando con Firebase usando Google");
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso
                        Log.d("LoginActivity", "Inicio de sesión con Google exitoso");
                        updateUI(mAuth.getCurrentUser());
                    } else {
                        Log.e("LoginActivity", "Error en la autenticación con Firebase: " + task.getException().getMessage());
                        Toast.makeText(this, "Error en la autenticación", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();  // Finaliza la actividad de login
        }
    }
}
