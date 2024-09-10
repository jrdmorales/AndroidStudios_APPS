package cl.inacap.evaluacion1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //rescatar los elementos de la vista
        EditText fecha = findViewById(R.id.editTextDate2);
        Spinner tipo = findViewById(R.id.spinner);
        Button guardar = findViewById(R.id.button);
        //agregar  listener al boton (funcionalidad al boton)
            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //rescatar los valores del formulario
                    String fechaStr= fecha.getText().toString();
                    String tipoStr = tipo.getSelectedItem().toString();

                    //empaquetar para la siguiente vista
                    Intent siguientePagina = new Intent(MainActivity.this, listaverdurasActivity2.class);

                    siguientePagina.putExtra("fecha",fechaStr);
                    siguientePagina.putExtra("tipo",fechaStr);

                    //ir a la siguiente vista

                    startActivity(siguientePagina);
                }
            });


    }
}