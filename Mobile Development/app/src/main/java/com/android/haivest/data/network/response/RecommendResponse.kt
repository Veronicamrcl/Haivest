package com.android.haivest.data.network.response

import com.google.gson.annotations.SerializedName

data class RecommendResponse (
    @SerializedName("DFT")
    val dft: Dft? = null,

    @SerializedName("NFT")
    val nft: Dft? = null,

    @SerializedName("Nama tanaman")
    val namaTanaman: String? = null,

    @SerializedName("Tips")
    val tips: List<String>? = null
)

data class Dft (
    @SerializedName("Flow Rate")
    val flowRate: String? = null,

    @SerializedName("Nutrient Solution")
    val nutrientSolution: String? = null,

    @SerializedName("Plant Spacing")
    val plantSpacing: String? = null,

    @SerializedName("Reservoir Depth")
    val reservoirDepth: String? = null,

    @SerializedName("Channel Slope")
    val channelSlope: String? = null
)
