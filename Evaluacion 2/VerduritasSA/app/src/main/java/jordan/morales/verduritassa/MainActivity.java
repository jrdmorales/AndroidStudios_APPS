package jordan.morales.verduritassa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CultivoAdapter.OnCultivoClickListener {

    private static final int REQUEST_CODE_REGISTER_CULTIVO = 1;

    private TextView tvWelcome;
    private ImageView ivLogout, btnAddCultivo;
    private RecyclerView recyclerViewCosechas;
    private CultivoAdapter adapter;
    private List<Cultivo> cultivos;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Comprobar si el usuario está autenticado
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            return;
        }

        Log.d("MainActivity", "Usuario autenticado: " + currentUser.getEmail());

        setContentView(R.layout.activity_main);

        // Inicializar Firebase Auth y Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Inicializar lista y adaptador
        cultivos = new ArrayList<>();
        adapter = new CultivoAdapter(this, cultivos, this);

        // Configurar RecyclerView
        recyclerViewCosechas = findViewById(R.id.recyclerViewCosechas);
        recyclerViewCosechas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCosechas.setAdapter(adapter);

        // Configurar elementos de la interfaz
        tvWelcome = findViewById(R.id.tvWelcome);
        ivLogout = findViewById(R.id.ivLogout);
        btnAddCultivo = findViewById(R.id.btnAddCultivo);

        // Configurar saludo de bienvenida
        String userEmail = currentUser.getEmail();
        tvWelcome.setText(userEmail != null ? "Bienvenido " + userEmail : "Bienvenido, Usuario");

        // Cargar cultivos del usuario
        cargarCultivosUsuario();

        // Configurar botones
        btnAddCultivo.setOnClickListener(v -> agregarCultivo());
        ivLogout.setOnClickListener(v -> cerrarSesion());
    }

    private void cargarCultivosUsuario() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("users").document(userId).collection("cultivos")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            cultivos.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String alias = document.getString("alias");
                                String planta = document.getString("planta");
                                String fechaSiembra = document.getString("fechaSiembra");
                                String fechaCosecha = document.getString("fechaCosecha");
                                cultivos.add(new Cultivo(alias, planta, fechaSiembra, fechaCosecha));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "Error al cargar cultivos", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }

    private void cerrarSesion() {
        auth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
    }

    private void agregarCultivo() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REQUEST_CODE_REGISTER_CULTIVO);
    }

    @Override
    public void onEditClick(Cultivo cultivo) {
        Toast.makeText(this, "Editar " + cultivo.getAlias(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(Cultivo cultivo) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Eliminar el cultivo en Firestore
            db.collection("users").document(userId).collection("cultivos")
                    .whereEqualTo("alias", cultivo.getAlias())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                            }
                            cultivos.remove(cultivo);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(this, cultivo.getAlias() + " eliminado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Error al eliminar cultivo", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_REGISTER_CULTIVO && resultCode == RESULT_OK && data != null) {
            Cultivo nuevoCultivo = (Cultivo) data.getSerializableExtra("nuevoCultivo");
            if (nuevoCultivo != null) {
                guardarCultivoEnFirestore(nuevoCultivo);
            }
        }
    }

    private void guardarCultivoEnFirestore(Cultivo cultivo) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Crear un mapa para el cultivo
            Map<String, Object> cultivoData = new HashMap<>();
            cultivoData.put("alias", cultivo.getAlias());
            cultivoData.put("planta", cultivo.getPlanta()); // Cambia a getPlanta si tienes el método
            cultivoData.put("fechaSiembra", cultivo.getFechaSiembra()); // Asegúrate de tener este método
            cultivoData.put("fechaCosecha", cultivo.getFechaCosecha()); // Asegúrate de tener este método

            // Guardar en Firestore y actualizar la lista local
            db.collection("users").document(userId).collection("cultivos")
                    .add(cultivoData)
                    .addOnSuccessListener(documentReference -> {
                        cultivos.add(cultivo);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "Cultivo agregado exitosamente", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error al agregar cultivo", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }
}