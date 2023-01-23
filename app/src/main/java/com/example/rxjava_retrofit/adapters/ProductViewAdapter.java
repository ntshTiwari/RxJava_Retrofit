package com.example.rxjava_retrofit.adapters;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.rxjava_retrofit.R;
import com.example.rxjava_retrofit.models.Product;

import java.util.ArrayList;

public class ProductViewAdapter extends
        RecyclerView.Adapter<ProductViewAdapter.ProductViewHolder> {

    private ArrayList<Product> products;

    public ProductViewAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    public void addNewList(ArrayList<Product> newProducts){
        products.addAll(newProducts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_view_layout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);

        System.out.println(String.valueOf(product.id));
        System.out.println(holder.productId.getText() + String.valueOf(product.id));

        holder.productId.setText("Vendor code: " + String.valueOf(product.id));
        holder.productTitle.setText(product.title);
        holder.productPrice.setText(String.valueOf(product.price) + " $");

        holder.productPreviousPrice.setPaintFlags(holder.productPreviousPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        tryGettingImage(holder, product.image);
    }

    private void tryGettingImage(ProductViewHolder holder, String imageUrl) {
        Glide.with(holder.root)
                .load(imageUrl)
                .apply(
                        new RequestOptions()
                                .error(R.drawable.retry_placeholder)
                                .centerCrop()
                )
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        /// only set listener for failed items
                        holder.productImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.e("IMAGE:", "setOnClickListener called for " + holder.productTitle);
                                tryGettingImage(holder, imageUrl);
                            }
                        });
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //on load success
                        /// remove  set listener for successful items
                        holder.productImageView.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                }
                        );
                        return false;
                    }
                })
                .transition(withCrossFade())
                .into(holder.productImageView);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImageView;
        public TextView productId, productTitle, productPrice, productPreviousPrice;
        public View root;
        public ProductViewHolder(View view){
            super(view);

            productImageView = view.findViewById(R.id.product_image);

            productId = view.findViewById(R.id.product_id);
            productTitle = view.findViewById(R.id.product_title);
            productPrice = view.findViewById(R.id.product_price);
            productPreviousPrice = view.findViewById(R.id.product_previous_price);

            root = view.getRootView();
        }
    }

}
