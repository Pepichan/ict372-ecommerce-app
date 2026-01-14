package com.example.ict372ecommerceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// ✅ The Adapter creates and binds each product “row/card” in the RecyclerView
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<Product> allProducts; //all products
    private final List<Product> visibleProducts; //products matching the current filter

    public ProductAdapter(List<Product> products) {
        this.allProducts = new ArrayList<>(products);
        this.visibleProducts = new ArrayList<>(products);
    }

    @NonNull
    @Override

    // ✅ creates each row
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    // ✅ binds product data to each row
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = visibleProducts.get(position);
        holder.tvName.setText(p.name);
        holder.tvPrice.setText(p.price);
    }

    @Override
    // ✅ returns the count of visible products
    public int getItemCount() {
        return visibleProducts.size();
    }

    public void filter(String query) {
        visibleProducts.clear();

        if (query == null || query.trim().isEmpty()) {
            visibleProducts.addAll(allProducts);
        } else {
            String q = query.toLowerCase(Locale.ROOT).trim();
            for (Product p : allProducts) {
                if (p.name.toLowerCase(Locale.ROOT).contains(q)) {
                    visibleProducts.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
        }
    }
}
