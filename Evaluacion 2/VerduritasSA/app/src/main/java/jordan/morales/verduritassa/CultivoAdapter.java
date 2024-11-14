package jordan.morales.verduritassa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class CultivoAdapter extends RecyclerView.Adapter<CultivoAdapter.CultivoViewHolder> {
    private List<Cultivo> cultivos;
    private Context context;
    private OnCultivoClickListener listener;

    public CultivoAdapter(Context context, List<Cultivo> cultivos, OnCultivoClickListener listener) {
        this.context = context;
        this.cultivos = cultivos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CultivoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cultivo_item, parent, false);
        return new CultivoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CultivoViewHolder holder, int position) {
        Cultivo cultivo = cultivos.get(position);
        holder.nombre.setText(cultivo.getAlias());
        holder.fecha.setText(cultivo.getFechaSiembra());

        holder.ajustes.setOnClickListener(v -> {
            // Crear y configurar el BottomSheetDialog
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_menu, null);
            bottomSheetDialog.setContentView(bottomSheetView);

            // Configurar acciones para los botones del Bottom Sheet
            TextView tvEditar = bottomSheetView.findViewById(R.id.tvEditar);
            TextView tvEliminar = bottomSheetView.findViewById(R.id.tvEliminar);

            tvEditar.setOnClickListener(view -> {
                listener.onEditClick(cultivo);
                bottomSheetDialog.dismiss();
            });

            tvEliminar.setOnClickListener(view -> {
                listener.onDeleteClick(cultivo);
                bottomSheetDialog.dismiss();
            });

            // Mostrar el BottomSheetDialog
            bottomSheetDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return cultivos.size();
    }

    public static class CultivoViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, fecha;
        ImageView ajustes;

        public CultivoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvNombreCultivo);
            fecha = itemView.findViewById(R.id.tvFechaCosecha);
            ajustes = itemView.findViewById(R.id.ivConfig);
        }
    }

    public interface OnCultivoClickListener {
        void onEditClick(Cultivo cultivo);
        void onDeleteClick(Cultivo cultivo);
    }
}
