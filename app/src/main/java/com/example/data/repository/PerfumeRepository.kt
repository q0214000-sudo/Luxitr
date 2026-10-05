package com.example.data.repository

import com.example.data.db.FavoriteDao
import com.example.data.db.FavoriteEntity
import com.example.data.model.BottleVisualShape
import com.example.data.model.Perfume
import com.example.data.model.PerfumeCategory
import com.example.data.model.QuizResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PerfumeRepository(private val favoriteDao: FavoriteDao) {

    val catalog: List<Perfume> = listOf(
        Perfume(
            id = "hawas-ice",
            name = "Hawas ICE",
            subtitle = "Fresh • Energetic",
            category = PerfumeCategory.FRESH,
            genderCategory = "For Him",
            price = "$110",
            originalPrice = "$140",
            rating = 4.9f,
            reviewCount = 524,
            bestFor = "Daytime & Active Life",
            weather = "Hot / Normal",
            personality = "Fresh & Active",
            projectionScore = 4,
            sweetnessScore = 2,
            freshnessScore = 5,
            warmthScore = 1,
            longevityScore = 4,
            description = "A brisk, icy masterpiece crafted for the modern alpha. Opens with frostbitten Italian citrus, crisp frosted apple, and star anise that seamlessly blend into a sensual marine heart of aquatic driftwood and crystalline ambergris.",
            luxysPitch = "Hawas ICE is the ultimate warm-weather compliment magnet. If you want an unforgettable, revitalizing aura that commands respect from morning meetings to evening sports, this is your signature weapon.",
            topNotes = listOf("Frosted Apple", "Italian Bergamot", "Sicilian Lemon", "Star Anise"),
            heartNotes = listOf("Plum", "Cardamom", "Orange Blossom", "Ocean Breeze"),
            baseNotes = listOf("Ambergris", "Musk", "Driftwood", "Oakmoss"),
            accentColorHex = 0xFF29B6F6,
            bottleShape = BottleVisualShape.CYLINDER_AQUA,
            tagList = listOf("fresh", "icy", "aquatic", "gym", "office", "summer", "daytime", "compliments")
        ),
        Perfume(
            id = "god-of-fire",
            name = "God of Fire",
            subtitle = "Bold • Confident",
            category = PerfumeCategory.WARM,
            genderCategory = "Unisex",
            price = "$265",
            originalPrice = "$320",
            rating = 5.0f,
            reviewCount = 480,
            bestFor = "Evening & VIP Nightlife",
            weather = "All Seasons / Cool Nights",
            personality = "Dominant & Charismatic",
            projectionScore = 5,
            sweetnessScore = 4,
            freshnessScore = 3,
            warmthScore = 5,
            longevityScore = 5,
            description = "Inspired by the divine legend of Xiuhtecuhtli. An explosion of fiery tropical mango, pink pepper, and sparkling ginger ignited by dark dry woods, amber, and sacred oud resin.",
            luxysPitch = "God of Fire doesn't just enter a room—it owns the space. Irresistibly opulent, exotic, and magnetic. Wear this when you want every single head to turn the moment you arrive.",
            topNotes = listOf("Exotic Mango", "Lemon Zest", "Pink Berries", "Spiced Ginger"),
            heartNotes = listOf("Blue Coumarin", "Jasmine Petals", "Smoky Cedar"),
            baseNotes = listOf("Rare Amber", "Nagarmotha", "Agarwood Oud", "Velvet Musk"),
            accentColorHex = 0xFFFF5722,
            bottleShape = BottleVisualShape.CRYSTAL_FIRE,
            tagList = listOf("bold", "mango", "fire", "party", "night", "luxury", "dominant", "warm")
        ),
        Perfume(
            id = "creed-aventus",
            name = "Creed Aventus",
            subtitle = "Elegant • Sophisticated",
            category = PerfumeCategory.ELEGANT,
            genderCategory = "For Him",
            price = "$345",
            originalPrice = "$425",
            rating = 4.9f,
            reviewCount = 890,
            bestFor = "Executive, Gala & Signatures",
            weather = "All Seasons Universal",
            personality = "Aristocratic & Powerful",
            projectionScore = 4,
            sweetnessScore = 2,
            freshnessScore = 4,
            warmthScore = 3,
            longevityScore = 4,
            description = "The crown jewel of masculine perfumery celebrating strength, vision, and triumph. Legendary smoky French blackcurrant and sun-ripened pineapple grounded in noble silver birch, ambergris, and earthy oakmoss.",
            luxysPitch = "The undisputed scent of victory and wealth. Wearing Creed Aventus projects authoritative confidence, making it irreplaceable for career-defining boardroom deals and unforgettable galas.",
            topNotes = listOf("Calville Blanc Apple", "Bergamot", "Blackcurrant", "Royal Pineapple"),
            heartNotes = listOf("Birch Wood", "Patchouli", "Moroccan Jasmine", "Rose"),
            baseNotes = listOf("Musk", "Oakmoss", "Ambergris", "Creamy Vanilla"),
            accentColorHex = 0xFFD4AF37,
            bottleShape = BottleVisualShape.ROYAL_FLACON,
            tagList = listOf("elegant", "office", "executive", "smoky", "pineapple", "king", "rich", "signature")
        ),
        Perfume(
            id = "dunhill-desire",
            name = "Dunhill Desire",
            subtitle = "Warm • Masculine",
            category = PerfumeCategory.WARM,
            genderCategory = "For Him",
            price = "$85",
            originalPrice = "$115",
            rating = 4.8f,
            reviewCount = 310,
            bestFor = "Romantic Dinners & Date Night",
            weather = "Cool Evenings & Winter",
            personality = "Warm & Seductive",
            projectionScore = 4,
            sweetnessScore = 3,
            freshnessScore = 2,
            warmthScore = 5,
            longevityScore = 4,
            description = "A fiery red elixir pulsating with temptation. Opens with crisp crimson apple and sparkling neroli, giving way to a warm heart of teakwood and velvety rose, enveloped in intoxicating Madagascar vanilla.",
            luxysPitch = "When chemistry matters most, Dunhill Desire never fails. Its sweet, warm, apple-woody sillage draws your partner close and leaves a lasting memory that lingers long after you leave.",
            topNotes = listOf("Red Apple", "Lemon Blossom", "Bergamot", "Neroli"),
            heartNotes = listOf("Teakwood", "Damask Rose", "Patchouli Leaf"),
            baseNotes = listOf("Madagascar Vanilla", "Golden Musk", "Labdanum"),
            accentColorHex = 0xFFE53935,
            bottleShape = BottleVisualShape.RUBY_RECTANGLE,
            tagList = listOf("warm", "date", "romantic", "vanilla", "sweet", "night", "intimate")
        ),
        Perfume(
            id = "bleu-de-chanel",
            name = "Bleu De Chanel",
            subtitle = "Fresh • Elegant • Professional",
            category = PerfumeCategory.FOR_HIM,
            genderCategory = "For Him",
            price = "$165",
            originalPrice = "$195",
            rating = 4.9f,
            reviewCount = 760,
            bestFor = "Daily Office & Corporate Meetings",
            weather = "All Seasons Versatile",
            personality = "Polished & Effortlessly Charming",
            projectionScore = 3,
            sweetnessScore = 2,
            freshnessScore = 5,
            warmthScore = 3,
            longevityScore = 4,
            description = "The timeless benchmark of sophisticated gentlemen. A sharp aromatic citrus accord of grapefruit and mint layered over velvety dry cedarwood, nutmeg, and smoked frankincense.",
            luxysPitch = "If you could only own one bottle in your entire life, Bleu De Chanel is the universal masterkey. Impeccably tasteful, inoffensive yet irresistibly high-status for daily corporate dominance.",
            topNotes = listOf("Grapefruit", "Lemon Zest", "Peppermint", "Pink Peppercorn"),
            heartNotes = listOf("Ginger Root", "Nutmeg", "Jasmine", "Iso E Super"),
            baseNotes = listOf("Incense", "Haitian Vetiver", "Atlas Cedar", "Sandalwood"),
            accentColorHex = 0xFF1976D2,
            bottleShape = BottleVisualShape.MIDNIGHT_SQUARE,
            tagList = listOf("office", "corporate", "fresh", "versatile", "clean", "signature", "meeting")
        ),
        Perfume(
            id = "baccarat-rouge-540",
            name = "Baccarat Rouge 540",
            subtitle = "Sweet • Luxurious • Ethereal",
            category = PerfumeCategory.SWEET,
            genderCategory = "Unisex",
            price = "$325",
            originalPrice = "$395",
            rating = 5.0f,
            reviewCount = 920,
            bestFor = "High Luxury Galas & Date Nights",
            weather = "All Seasons",
            personality = "Opulent, Mysterious & Seductive",
            projectionScore = 5,
            sweetnessScore = 5,
            freshnessScore = 2,
            warmthScore = 4,
            longevityScore = 5,
            description = "An ethereal poetic alchemy where airy jasmine breezes and radiant saffron sparkle over mineral facets of ambergris and fresh cedar. It creates an aura that floats like spun golden sugar.",
            luxysPitch = "The scent of absolute aristocracy. It projects an intoxicating, crystalline sweetness that leaves an unforgettable signature trailing behind you for miles.",
            topNotes = listOf("Bitter Almond", "Blood Saffron", "Egyptian Jasmine"),
            heartNotes = listOf("Cedarwood", "Amberwood Resin"),
            baseNotes = listOf("Ambergris Accord", "Spun Sugar", "Fir Balsam"),
            accentColorHex = 0xFFFFB300,
            bottleShape = BottleVisualShape.AMBER_GEM,
            tagList = listOf("sweet", "luxury", "unisex", "saffron", "sugar", "gala", "date", "rich")
        ),
        Perfume(
            id = "sauvage-elixir",
            name = "Sauvage Elixir",
            subtitle = "Spicy • Magnetic • Beast Mode",
            category = PerfumeCategory.NIGHT,
            genderCategory = "For Him",
            price = "$210",
            originalPrice = "$255",
            rating = 4.9f,
            reviewCount = 640,
            bestFor = "Night Parties & Cold Weather",
            weather = "Cool, Cold & Night",
            personality = "Intense, Mysterious & Dominant",
            projectionScore = 5,
            sweetnessScore = 2,
            freshnessScore = 3,
            warmthScore = 5,
            longevityScore = 5,
            description = "An ultra-concentrated nocturnal elixir steeped in rare spices, custom lavender essence, and intoxicating woods. Bewitching cinnamon, cardamom, and dark licorice rooted in Haitian vetiver.",
            luxysPitch = "Nuclear longevity and unmatched projection. Two sprays are all you need to command any nocturnal venue. A masterclass in dark masculine allure.",
            topNotes = listOf("Ceylon Cinnamon", "Nutmeg", "Cardamom", "Grapefruit"),
            heartNotes = listOf("Nyons Lavender Essence", "Coumarin"),
            baseNotes = listOf("Licorice", "Sandalwood", "Amber Accord", "Patchouli"),
            accentColorHex = 0xFF283593,
            bottleShape = BottleVisualShape.ELIXIR_FLASK,
            tagList = listOf("beast", "night", "spicy", "party", "winter", "lavender", "intense")
        ),
        Perfume(
            id = "lost-cherry",
            name = "Lost Cherry",
            subtitle = "Seductive • Gourmand",
            category = PerfumeCategory.FOR_HER,
            genderCategory = "For Her",
            price = "$280",
            originalPrice = "$350",
            rating = 4.9f,
            reviewCount = 410,
            bestFor = "Romantic Intimacy & Evenings",
            weather = "Autumn & Winter",
            personality = "Sensual, Playful & Tempting",
            projectionScore = 4,
            sweetnessScore = 5,
            freshnessScore = 1,
            warmthScore = 5,
            longevityScore = 4,
            description = "A mouth-watering temptation opening with ripe black cherries dripping in cherry liqueur and bitter almond, giving way to Turkish rose, jasmine sambac, and roasted tonka bean.",
            luxysPitch = "A breathtaking gourmand feast that is wildly addictive and seductive. Perfect for an enchanting evening where you want to be completely unforgettable.",
            topNotes = listOf("Black Cherry", "Cherry Liqueur", "Bitter Almond"),
            heartNotes = listOf("Griotte Syrup", "Turkish Rose", "Jasmine Sambac"),
            baseNotes = listOf("Peru Balsam", "Roasted Tonka", "Sandalwood", "Cedar"),
            accentColorHex = 0xFF880E4F,
            bottleShape = BottleVisualShape.CHERRY_DECANTER,
            tagList = listOf("cherry", "sweet", "gourmand", "date", "sexy", "women", "night")
        ),
        Perfume(
            id = "roja-elysium",
            name = "Roja Elysium",
            subtitle = "Aristocratic • Sparkling Citrus",
            category = PerfumeCategory.FRESH,
            genderCategory = "For Him",
            price = "$295",
            originalPrice = "$360",
            rating = 4.9f,
            reviewCount = 380,
            bestFor = "Sunny Days, Business & Yacht Clubs",
            weather = "Spring & Summer",
            personality = "Refined, Prestigious & Charismatic",
            projectionScore = 4,
            sweetnessScore = 2,
            freshnessScore = 5,
            warmthScore = 2,
            longevityScore = 4,
            description = "An ultra-luxurious blend of sparkling grapefruit, lime, and thyme, intertwined with pink pepper, lily of the valley, blackcurrant, and a smooth drydown of benzoin, labdanum, and ambergris.",
            luxysPitch = "Elysium embodies sheer wealth under the Mediterranean sun. Crisp, luminous, and undeniably aristocratic.",
            topNotes = listOf("Grapefruit", "Lime", "Lemon", "Thyme", "Artemisia"),
            heartNotes = listOf("Lily of the Valley", "Rose de Mai", "Jasmin de Grasse", "Blackcurrant"),
            baseNotes = listOf("Galbanum", "Pink Pepper", "Vetiver", "Cedarwood", "Ambergris"),
            accentColorHex = 0xFF00897B,
            bottleShape = BottleVisualShape.ROYAL_FLACON,
            tagList = listOf("fresh", "citrus", "rich", "summer", "daytime", "grapefruit", "luxury")
        ),
        Perfume(
            id = "black-opium",
            name = "Black Opium",
            subtitle = "Mysterious • Addictive Glam",
            category = PerfumeCategory.FOR_HER,
            genderCategory = "For Her",
            price = "$155",
            originalPrice = "$185",
            rating = 4.8f,
            reviewCount = 590,
            bestFor = "Parties, Clubs & Glamorous Nights",
            weather = "Cool Weather & Night",
            personality = "Electrifying, Rock & Rebel Chic",
            projectionScore = 4,
            sweetnessScore = 4,
            freshnessScore = 1,
            warmthScore = 5,
            longevityScore = 4,
            description = "The intoxicating shot of adrenaline. A shot of rich black coffee meets sweet vanilla and radiant white flowers for a thrilling contrast of dark and light.",
            luxysPitch = "Daring and seductive. The energy of black coffee infused with sweet white flowers creates an addictive rhythm that captivates everyone around you.",
            topNotes = listOf("Pear Accord", "Orange Blossom", "Pink Pepper"),
            heartNotes = listOf("Black Coffee", "Jasmine", "Bitter Almond", "Licorice"),
            baseNotes = listOf("Vanilla Pod", "Patchouli", "Cashmere Wood", "Cedar"),
            accentColorHex = 0xFF4A148C,
            bottleShape = BottleVisualShape.AMBER_GEM,
            tagList = listOf("coffee", "vanilla", "sweet", "night", "party", "women", "sexy")
        )
    )

    fun getPerfumeById(id: String): Perfume? {
        return catalog.find { it.id.equals(id, ignoreCase = true) }
    }

    fun searchPerfumes(query: String, category: PerfumeCategory): List<Perfume> {
        val q = query.trim().lowercase()
        return catalog.filter { perfume ->
            val matchesCategory = when (category) {
                PerfumeCategory.ALL -> true
                PerfumeCategory.FOR_HIM -> perfume.genderCategory == "For Him"
                PerfumeCategory.FOR_HER -> perfume.genderCategory == "For Her"
                PerfumeCategory.UNISEX -> perfume.genderCategory == "Unisex"
                else -> perfume.category == category || perfume.tagList.contains(category.name.lowercase())
            }
            val matchesQuery = if (q.isEmpty()) true else {
                perfume.name.lowercase().contains(q) ||
                perfume.subtitle.lowercase().contains(q) ||
                perfume.description.lowercase().contains(q) ||
                perfume.tagList.any { it.contains(q) } ||
                perfume.topNotes.any { it.lowercase().contains(q) } ||
                perfume.heartNotes.any { it.lowercase().contains(q) } ||
                perfume.baseNotes.any { it.lowercase().contains(q) }
            }
            matchesCategory && matchesQuery
        }
    }

    val favoriteIds: Flow<List<String>> = favoriteDao.getAllFavoriteIds()

    val favoritePerfumes: Flow<List<Perfume>> = favoriteDao.getAllFavoriteIds().map { ids ->
        val set = ids.toSet()
        catalog.filter { set.contains(it.id) }
    }

    suspend fun toggleFavorite(perfumeId: String, isFav: Boolean) {
        if (isFav) {
            favoriteDao.removeFavorite(perfumeId)
        } else {
            favoriteDao.addFavorite(FavoriteEntity(perfumeId = perfumeId))
        }
    }

    fun calculateQuizResult(
        vibe: String,
        occasion: String,
        season: String,
        intensity: String
    ): QuizResult {
        val scores = mutableMapOf<Perfume, Int>()

        for (p in catalog) {
            var score = 50 // baseline

            // Vibe matching
            when (vibe.lowercase()) {
                "fresh", "aquatic" -> if (p.freshnessScore >= 4) score += 30
                "sweet", "gourmand" -> if (p.sweetnessScore >= 4) score += 30
                "bold", "spicy" -> if (p.warmthScore >= 4 && p.projectionScore >= 4) score += 30
                "elegant" -> if (p.category == PerfumeCategory.ELEGANT || p.id == "creed-aventus" || p.id == "bleu-de-chanel") score += 30
                "romantic" -> if (p.tagList.contains("date") || p.tagList.contains("romantic")) score += 30
                "mysterious" -> if (p.tagList.contains("dark") || p.tagList.contains("amber") || p.id == "baccarat-rouge-540") score += 30
            }

            // Occasion matching
            when (occasion.lowercase()) {
                "office", "work" -> if (p.tagList.contains("office")) score += 25
                "date", "intimate" -> if (p.tagList.contains("date")) score += 25
                "party", "club" -> if (p.tagList.contains("party") || p.projectionScore >= 4) score += 25
                "gym", "active" -> if (p.tagList.contains("active") || p.freshnessScore >= 4) score += 25
                "signature" -> score += 15
            }

            // Season matching
            when (season.lowercase()) {
                "summer", "hot" -> if (p.freshnessScore >= 4) score += 20
                "winter", "cool" -> if (p.warmthScore >= 4) score += 20
                "all" -> score += 15
            }

            // Intensity matching
            when (intensity.lowercase()) {
                "beast", "heavy" -> if (p.projectionScore >= 4) score += 20
                "subtle", "intimate" -> if (p.projectionScore <= 3) score += 20
                "balanced" -> if (p.projectionScore in 3..4) score += 20
            }

            scores[p] = score
        }

        val sorted = scores.entries.sortedByDescending { it.value }
        val primary = sorted.firstOrNull()?.key ?: catalog[0]
        val secondary = sorted.getOrNull(1)?.key ?: catalog[1]

        val calculatedPercentage = kotlin.math.min(99, 90 + (primary.rating * 1.8).toInt())

        val verdict = "Based on your craving for a ${vibe.lowercase()} aura for ${occasion.lowercase()}, LUXYS has identified ${primary.name} as your soul scent. Its projection and olfactory notes perfectly mirror your personal presence. Grab it now on luxitr.com before limited seasonal stock runs out!"

        return QuizResult(
            primaryMatch = primary,
            matchPercentage = calculatedPercentage,
            secondaryMatch = secondary,
            luxysVerdict = verdict,
            selectedVibes = listOf(vibe, occasion, season, intensity)
        )
    }
}
