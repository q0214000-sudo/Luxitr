package com.example.data.model

enum class PerfumeCategory(val displayName: String, val icon: String) {
    ALL("All", "✨"),
    FOR_HIM("For Him", "👔"),
    FOR_HER("For Her", "💄"),
    UNISEX("Unisex", "⚜️"),
    FRESH("Fresh", "🍃"),
    SWEET("Sweet", "🌸"),
    WARM("Warm", "🔥"),
    ELEGANT("Elegant", "⚜️"),
    NIGHT("Night", "🌙")
}

enum class BottleVisualShape {
    CYLINDER_AQUA,
    CRYSTAL_FIRE,
    ROYAL_FLACON,
    RUBY_RECTANGLE,
    MIDNIGHT_SQUARE,
    AMBER_GEM,
    ELIXIR_FLASK,
    CHERRY_DECANTER
}

data class Perfume(
    val id: String,
    val name: String,
    val subtitle: String,
    val category: PerfumeCategory,
    val genderCategory: String, // "For Him", "For Her", "Unisex"
    val price: String,
    val originalPrice: String,
    val rating: Float = 4.9f,
    val reviewCount: Int = 340,
    val bestFor: String,
    val weather: String,
    val personality: String,
    val projectionScore: Int, // 1 to 5
    val sweetnessScore: Int,  // 1 to 5
    val freshnessScore: Int,  // 1 to 5
    val warmthScore: Int,     // 1 to 5
    val longevityScore: Int,  // 1 to 5
    val description: String,
    val luxysPitch: String,
    val topNotes: List<String>,
    val heartNotes: List<String>,
    val baseNotes: List<String>,
    val accentColorHex: Long,
    val bottleShape: BottleVisualShape,
    val tagList: List<String>
) {
    val searchUrl: String
        get() = "https://luxitr.com/search?q=" + java.net.URLEncoder.encode(name, "UTF-8")

    val directProductUrl: String
        get() = "https://luxitr.com/products/${id}"
}
