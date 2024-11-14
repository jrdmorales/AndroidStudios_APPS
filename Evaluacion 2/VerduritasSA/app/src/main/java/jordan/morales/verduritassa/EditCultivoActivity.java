package jordan.morales.verduritassa;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditCultivoActivity extends AppCompatActivity {

    private EditText editTextAlias, FechaSiembra;
    private Spinner spinnerPlanta;
    private Button btnGuardarCultivo;
    private FirebaseFirestore db;
    private String userId;
    private ImageView backArrow;

    // Declaramos la variable cultivo a nivel de clase
    private Cultivo cultivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cultivo);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Obtener el cultivo enviado desde la MainActivity
        cultivo = (Cultivo) getIntent().getSerializableExtra("cultivo");

        // Referencias a los elementos de la interfaz
        editTextAlias = findViewById(R.id.editTextAlias);
        FechaSiembra = findViewById(R.id.FechaSiembra);
        spinnerPlanta = findViewById(R.id.spinnerPlanta);
        btnGuardarCultivo = findViewById(R.id.btnGuardarCultivo);
        backArrow = findViewById(R.id.backArrow);

        // Inicializar el userId
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (cultivo != null) {
            // Asignamos el alias y fecha de siembra al UI
            editTextAlias.setText(cultivo.getAlias());
            FechaSiembra.setText(cultivo.getFechaSiembra());

            // Configurar el spinner con los valores actuales
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                    new String[]{"Tomates (80 días)", "Cebollas (120 días)", "Lechugas (60 días)", "Apio (85 días)", "Choclo (90 días)"});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPlanta.setAdapter(adapter);
            spinnerPlanta.setSelection(adapter.getPosition(cultivo.getPlanta()));
        }

        // Configurar el botón de guardar
        btnGuardarCultivo.setOnClickListener(v -> guardarCambios());

        // Configurar el DatePickerDialog
        FechaSiembra.setOnClickListener(v -> showDatePickerDialog());

        // Acción de retroceso
        backArrow.setOnClickListener(v -> back());
    }

    private void back() {
        finish(); // Finaliza la actividad y vuelve a la anterior
    }

    private void showDatePickerDialog() {
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear el DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            // Ajustar la fecha seleccionada en el formato deseado (DD/MM/YYYY)
            String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            FechaSiembra.setText(date);
        }, year, month, day);

        // Mostrar el DatePickerDialog
        datePickerDialog.show();
    }

    private void guardarCambios() {
        String alias = editTextAlias.getText().toString();
        String tipoCultivo = spinnerPlanta.getSelectedItem().toString(); // Obtener planta seleccionada del spinner
        int diasCosecha = obtenerDiasDeCosecha(tipoCultivo);
        String fechaSiembra = FechaSiembra.getText().toString();

        // Obtener fecha seleccionada del EditText (FechaSiembra)
        String fechaSiembraStr = FechaSiembra.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar fechaSiembraCalendar = Calendar.getInstance();

        // Calcular la fecha de cosecha
        Calendar fechaCosechaCalendar = (Calendar) fechaSiembraCalendar.clone();
        fechaCosechaCalendar.add(Calendar.DAY_OF_MONTH, diasCosecha);

        String fechaCosechaStr = sdf.format(fechaCosechaCalendar.getTime());
        try {
            fechaSiembraCalendar.setTime(sdf.parse(fechaSiembraStr));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al obtener la fecha de siembra", Toast.LENGTH_SHORT).show();
            return; // Salir si la fecha es inválida
        }


        // Asegúrate de que el objeto 'cultivo' no sea null
        if (cultivo != null) {
            // Obtener el ID del cultivo
            String cultivoId = cultivo.getId();  // Ahora puedes acceder al 'id' del cultivo

            // Crear un mapa de los datos actualizados
            Map<String, Object> cultivoData = new HashMap<>();
            cultivoData.put("alias", alias);
            cultivoData.put("planta", tipoCultivo);
            cultivoData.put("fechaSiembra", fechaSiembra);

            // Actualizar en Firestore
            db.collection("users").document(userId).collection("cultivos")
                    .document(cultivoId)  // Utilizamos el ID del cultivo
                    .update(cultivoData)
                    .addOnSuccessListener(aVoid -> {
                        // Enviar el resultado de vuelta a MainActivity
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("cultivoActualizado", new Cultivo(cultivoId, alias, tipoCultivo, fechaSiembra, cultivo.getFechaCosecha()));
                        Toast.makeText(EditCultivoActivity.this, "Cultivo registrado: " + tipoCultivo + " - Fecha de Cosecha: " + fechaCosechaStr, Toast.LENGTH_SHORT).show();

                        setResult(RESULT_OK, resultIntent);
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error al actualizar cultivo", Toast.LENGTH_SHORT).show());


        }

    }

    private int obtenerDiasDeCosecha(String planta) {
        switch (planta) {
            case "Tomates (80 días)":
                return 80;
            case "Cebollas (120 días)":
                return 120;
            case "Lechugas (60 días)":
                return 60;
            case "Apio (85 días)":
                return 85;
            case "Choclo (90 días)":
                return 90;
            default:
                return 0;
        }
    }
}
