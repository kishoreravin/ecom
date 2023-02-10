package com.example.ecom.pages.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecom.databinding.AdapterVariantsBinding
import com.example.ecom.models.ProductModel
import com.example.ecom.utils.dp
import com.example.ecom.utils.setColor
import com.example.ecom.utils.toColorListState
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

class VariantsAdapter(
    private val variants: List<ProductModel>,
    private val currentProductId: String,
    private val onVariantSelected: (id: String) -> Unit
) : RecyclerView.Adapter<VariantsAdapter.VariantsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantsViewHolder {
        val binding = AdapterVariantsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VariantsViewHolder(binding)
    }

    override fun getItemCount(): Int = variants.size

    override fun onBindViewHolder(holder: VariantsViewHolder, position: Int) {
        holder.bindData(position)
    }

    inner class VariantsViewHolder(private val binding: AdapterVariantsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(position: Int) {
            val variant = variants[position]
            binding.variantName.text = variant.name
            val backgroundShapeModel: ShapeAppearanceModel = ShapeAppearanceModel.builder()
                .setAllCornerSizes(80F)
                .build()
            binding.variantName.background = MaterialShapeDrawable(backgroundShapeModel).apply {
                fillColor = if (currentProductId == variant.id) "#FFF8F3".toColorListState() else "#ffffff".toColorListState()
                strokeColor = if (currentProductId == variant.id) "#FF6B00".toColorListState() else "#FFE7D6".toColorListState()
                strokeWidth = 4f
            }
            if (currentProductId == variant.id) {
                binding.variantName.setColor("#FF6B00")
            }
            binding.variantName.setOnClickListener {
                variant.id?.let(onVariantSelected)
            }
        }

    }
}