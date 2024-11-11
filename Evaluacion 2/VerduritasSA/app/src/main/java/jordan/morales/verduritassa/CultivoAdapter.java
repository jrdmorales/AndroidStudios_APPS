package jordan.morales.verduritassa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.nombre.setText(cultivo.getAlias()); // Usar getAlias() en lugar de getNombre()
        holder.fecha.setText(cultivo.getFechaSiembra()); // Usar getFechaSiembra() en lugar de getFecha()

        holder.ajustes.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, holder.ajustes);
            popup.inflate(R.menu.cultivo_menu);
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_editar) {
                    listener.onEditClick(cultivo);
                    return true;
                } else if (item.getItemId() == R.id.menu_eliminar) {
                    listener.onDeleteClick(cultivo);
                    return true;
                }
                return false;
            });
            popup.show();
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