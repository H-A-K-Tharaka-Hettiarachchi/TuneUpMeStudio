package com.kshprimeindustries.tuneupmestudio.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kshprimeindustries.tuneupmestudio.R;
import com.kshprimeindustries.tuneupmestudio.TuneUpMeStudioActivityPartnershipRequestItemView;
import com.kshprimeindustries.tuneupmestudio.model.TuneUpMeStudioPartnershipRequestItem;

import java.util.ArrayList;

public class TuneUpMeStudioPartnershipRequestItemAdapter extends RecyclerView.Adapter<TuneUpMeStudioPartnershipRequestItemAdapter.TuneUpMeStudioPartnershipRequestItemViewHolder> {

    static class TuneUpMeStudioPartnershipRequestItemViewHolder extends RecyclerView.ViewHolder {

        TextView textViewPartnershipRequestItemBandName;
        TextView textViewPartnershipRequestItemBandEmail;
        TextView textViewPartnershipRequestItemBandMobile;
        TextView textViewPartnershipRequestItemViewButton;

        public TuneUpMeStudioPartnershipRequestItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewPartnershipRequestItemBandName = itemView.findViewById(R.id.textViewPartnershipRequestItemBandName);
            this.textViewPartnershipRequestItemBandEmail = itemView.findViewById(R.id.textViewPartnershipRequestItemBandEmail);
            this.textViewPartnershipRequestItemBandMobile = itemView.findViewById(R.id.textViewPartnershipRequestItemBandMobile);
            this.textViewPartnershipRequestItemViewButton = itemView.findViewById(R.id.textViewPartnershipRequestItemViewButton);
        }
    }

    ArrayList<TuneUpMeStudioPartnershipRequestItem> tuneUpMeStudioPartnershipRequestItemArrayList;

    public TuneUpMeStudioPartnershipRequestItemAdapter(ArrayList<TuneUpMeStudioPartnershipRequestItem> tuneUpMeStudioPartnershipRequestItemArrayList) {
        this.tuneUpMeStudioPartnershipRequestItemArrayList = tuneUpMeStudioPartnershipRequestItemArrayList;
    }

    @NonNull
    @Override
    public TuneUpMeStudioPartnershipRequestItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.partnership_request_item, parent, false);

        return new TuneUpMeStudioPartnershipRequestItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TuneUpMeStudioPartnershipRequestItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textViewPartnershipRequestItemBandName.setText(tuneUpMeStudioPartnershipRequestItemArrayList.get(position).getName());
        holder.textViewPartnershipRequestItemBandEmail.setText(tuneUpMeStudioPartnershipRequestItemArrayList.get(position).getEmail());
        holder.textViewPartnershipRequestItemBandMobile.setText(tuneUpMeStudioPartnershipRequestItemArrayList.get(position).getMobile());
        holder.textViewPartnershipRequestItemViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCardClick(v);
                Intent intent = new Intent(v.getContext(), TuneUpMeStudioActivityPartnershipRequestItemView.class);
                intent.putExtra("itemId", tuneUpMeStudioPartnershipRequestItemArrayList.get(position).getItemId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tuneUpMeStudioPartnershipRequestItemArrayList.size();
    }

    private void animateCardClick(View view) {
        view.setPressed(true);
        view.invalidate();
        view.postDelayed(() -> view.setPressed(false), 200);

        view.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction(() -> view.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(100))
                .start();
    }

}
