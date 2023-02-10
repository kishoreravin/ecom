package com.example.ecom.pages.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecom.R
import com.example.ecom.databinding.AdapterVariantsBinding
import com.example.ecom.models.ProductModel
import com.example.ecom.utils.CommonUtils
import com.example.ecom.utils.toColorListState
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class VariantsAdapter(
    private val variants: List<ProductModel>,
    private val currentProductId: String,
    private val onVariantSelected: (id: String) -> Unit
) : RecyclerView.Adapter<VariantsAdapter.VariantsViewHolder>(), KoinComponent {
    val commonUtils by inject<CommonUtils>()
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
                fillColor = if (currentProductId == variant.id) commonUtils.getColor(R.color.colorOrangeWhite)
                    .toColorListState() else commonUtils.getColor(R.color.colorPrimary)
                    .toColorListState()
                strokeColor =
                    if (currentProductId == variant.id) commonUtils.getColor(R.color.colorAccent)
                        .toColorListState() else commonUtils.getColor(R.color.colorLightAccent).toColorListState()
                strokeWidth = 4f
            }
            if (currentProductId == variant.id) {
                binding.variantName.setTextColor(binding.variantName.context.getColor(R.color.colorAccent))
            }
            binding.variantName.setOnClickListener {
                variant.id?.let(onVariantSelected)
            }
        }

    }
}