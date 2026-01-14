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

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    private final List<Product> allProducts; //all products
    private final List<Product> visibleProducts; //products matching the current filter
    private final OnProductClickListener listener; //click listener for product items

    public ProductAdapter(List<Product> products, OnProductClickListener listener) {
        this.allProducts = new ArrayList<>(products);
        this.visibleProducts = new ArrayList<>(products);
        this.listener = listener;
    }

    @NonNull
    @Override

    // ✅ creates each row
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    // ✅ binds product data to each row
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = visibleProducts.get(position);
        holder.tvName.setText(p.name);
        holder.tvPrice.setText(p.price);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(p);
            }
        });
    }

    @Override
    // ✅ returns the count of visible products
    public int getItemCount() {
        return visibleProducts.size();
    }

    // ✅ filters products based on the query
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


    // ✅ ViewHolder class representing each product row
    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
        }
    }
}
