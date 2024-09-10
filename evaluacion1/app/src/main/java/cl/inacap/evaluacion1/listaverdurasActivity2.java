package cl.inacap.evaluacion1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class listaverdurasActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listaverduras2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        List<Verdura> verduritas = new ArrayList<>();

        // Recuperar datos de la página anterior
        Intent contexto = getIntent();
        String fecha = contexto.getStringExtra("fecha");
        String tipo = contexto.getStringExtra("tipo");

        // Transformar la fecha
        Calendar aux = Calendar.getInstance();
        int dia = Integer.parseInt(fecha.substring(0, 2));
        int mes = Integer.parseInt(fecha.substring(3, 5));
        int years = Integer.parseInt(fecha.substring(6, 10));
        aux.set(years, mes - 1, dia); // Mes es 0-based en Calendar

        // Rescatar cantidad de días a sumar (ejemplo: 30 días)
        int diasASumar = 30; // Asume que quieres sumar 30 días
        aux.add(Calendar.DAY_OF_YEAR, diasASumar);

        Date inicio = aux.getTime();
        Date fin = new Date(); // Reemplaza esto con tu lógica para determinar la fecha de fin

        // Guardar el elemento
        verduritas.add(new Verdura(tipo, 20, inicio, fin));

        ListView lista = findViewById(R.id.listView);

        // Pintar datos de la lista sobre ListView
        ArrayList<String> elementos = new ArrayList<>();
        for (Verdura verdura : verduritas) {
            elementos.add("La " + verdura.tipo + " tendrá la cosecha en " + fecha);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, elementos);
        lista.setAdapter(adapter);
    }
}
