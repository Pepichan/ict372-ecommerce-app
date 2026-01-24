package com.example.ict372ecommerceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private OnCartItemChangeListener listener;

    public interface OnCartItemChangeListener {
        void onQuantityChanged(CartItem item, int newQuantity);
        void onItemDeleted(CartItem item);
    }

    public CartAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void setOnCartItemChangeListener(OnCartItemChangeListener listener) {
        this.listener = listener;
    }

    public void updateItems(List<CartItem> newItems) {
        this.cartItems = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.tvProductName.setText(item.productName);
        holder.tvProductPrice.setText(item.getSubtotalFormatted());
        holder.tvQuantity.setText(String.valueOf(item.quantity));

        // Set product image
        if (item.imageResId != 0) {
            holder.imgProduct.setImageResource(item.imageResId);
        }

        // Decrease quantity
        holder.btnDecrease.setOnClickListener(v -> {
            if (listener != null) {
                int newQuantity = item.quantity - 1;
                listener.onQuantityChanged(item, newQuantity);
            }
        });

        // Increase quantity
        holder.btnIncrease.setOnClickListener(v -> {
            if (listener != null) {
                int newQuantity = item.quantity + 1;
                listener.onQuantityChanged(item, newQuantity);
            }
        });

        // Delete item
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemDeleted(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName;
        TextView tvProductPrice;
        CardView btnDecrease;
        TextView tvQuantity;
        CardView btnIncrease;
        CardView btnDelete;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgCartProduct);
            tvProductName = itemView.findViewById(R.id.tvCartProductName);
            tvProductPrice = itemView.findViewById(R.id.tvCartProductPrice);
            btnDecrease = itemView.findViewById(R.id.btnDecreaseCartQuantity);
            tvQuantity = itemView.findViewById(R.id.tvCartQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncreaseCartQuantity);
            btnDelete = itemView.findViewById(R.id.btnDeleteItem);
        }
    }
}
