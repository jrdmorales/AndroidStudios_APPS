package jordan.morales.verduritassa;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextAlias,FechaSiembra;
    private Spinner spinnerPlanta;
    private Button btnGuardarCultivo;

    public static ArrayList<String> cultivosList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        spinnerPlanta = findViewById(R.id.spinnerPlanta);
        FechaSiembra = findViewById(R.id.FechaSiembra);
        btnGuardarCultivo = findViewById(R.id.btnGuardarCultivo);
        editTextAlias = findViewById(R.id.editTextAlias);

        // Configurando el Spinner con los tipos de cultivos
        String[] tiposCultivos = {"Tomates (80 días)", "Cebollas (120 días)", "Lechugas (60 días)", "Apio (85 días)", "Choclo (90 días)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiposCultivos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlanta.setAdapter(adapter);

        btnGuardarCultivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarCultivo();
            }
        });

        // Configurar el DatePickerDialog
        FechaSiembra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

    }
    private void showDatePickerDialog() {
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear el DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                // Ajustar la fecha seleccionada en el formato deseado (DD/MM/YYYY)
                String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                FechaSiembra.setText(date);
            }
        }, year, month, day);

        // Mostrar el DatePickerDialog
        datePickerDialog.show();
    }

    private void registrarCultivo() {
        String alias = editTextAlias.getText().toString();
        String tipoCultivo = spinnerPlanta.getSelectedItem().toString();
        int diasCosecha = obtenerDiasDeCosecha(tipoCultivo);

        // Obtener fecha seleccionada del EditText (FechaSiembra)
        String fechaSiembraStr = FechaSiembra.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar fechaSiembra = Calendar.getInstance();

        try {
            fechaSiembra.setTime(sdf.parse(fechaSiembraStr));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al obtener la fecha de siembra", Toast.LENGTH_SHORT).show();
            return; // Salir si la fecha es inválida
        }

        // Calcular la fecha de cosecha
        Calendar fechaCosecha = (Calendar) fechaSiembra.clone();
        fechaCosecha.add(Calendar.DAY_OF_MONTH, diasCosecha);

        String fechaCosechaStr = sdf.format(fechaCosecha.getTime());

        // Crear el objeto Cultivo
        Cultivo nuevoCultivo = new Cultivo(alias, tipoCultivo, fechaSiembraStr, fechaCosechaStr);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("nuevoCultivo", nuevoCultivo);
        setResult(RESULT_OK, resultIntent);

        Toast.makeText(RegisterActivity.this, "Cultivo registrado: " + tipoCultivo + " - Fecha de Cosecha: " + fechaCosechaStr, Toast.LENGTH_SHORT).show();
        finish();
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
