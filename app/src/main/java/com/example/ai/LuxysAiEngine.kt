package com.example.ai

import com.example.BuildConfig
import com.example.data.model.ChatMessage
import com.example.data.model.Perfume
import com.example.data.model.SenderType
import com.example.data.repository.PerfumeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class LuxysAiEngine(private val repository: PerfumeRepository) {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    suspend fun generateResponse(
        userMessage: String,
        conversationHistory: List<ChatMessage>
    ): ChatMessage = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }
        val hasValidKey = apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY"

        if (hasValidKey) {
            try {
                val apiResponse = callGeminiRestApi(apiKey, userMessage, conversationHistory)
                if (apiResponse != null) {
                    return@withContext apiResponse
                }
            } catch (e: Exception) {
                // Fallback smoothly to luxury offline heuristic fragrance intelligence
            }
        }

        // Intelligent local luxury fragrance engine
        return@withContext generateLocalLuxysResponse(userMessage)
    }

    private fun callGeminiRestApi(
        apiKey: String,
        userMessage: String,
        history: List<ChatMessage>
    ): ChatMessage? {
        val systemPrompt = """
            You are LUXYS, the elite personal fragrance intelligence and connoisseur for the luxury perfume house LUXITR.
            Your tone is suave, charming, prestigious, and deeply knowledgeable in perfumery (notes, projection, sillage, season, occasions).
            You can fluently reply in English or Bengali (if user types in Bangla or Banglish).
            Your mission is to understand user preference and persuasively convince them to buy a specific LUXITR perfume from luxitr.com.
            Our catalog includes: Hawas ICE, God of Fire, Creed Aventus, Dunhill Desire, Bleu De Chanel, Baccarat Rouge 540, Sauvage Elixir, Lost Cherry, Roja Elysium, Black Opium.
            
            IMPORTANT:
            If you recommend a perfume, mention its EXACT name from:
            [Hawas ICE, God of Fire, Creed Aventus, Dunhill Desire, Bleu De Chanel, Baccarat Rouge 540, Sauvage Elixir, Lost Cherry, Roja Elysium, Black Opium].
            Explain Why (bullet points), Also consider (alternatives), and Best for tags. Always urge them to click BUY NOW to order authentically on luxitr.com.
        """.trimIndent()

        val contentsArray = JSONArray()

        // Include recent history (max 4 turns)
        val recentHistory = history.takeLast(4)
        for (msg in recentHistory) {
            val role = if (msg.sender == SenderType.USER) "user" else "model"
            val turn = JSONObject().apply {
                put("role", role)
                put("parts", JSONArray().put(JSONObject().put("text", msg.text)))
            }
            contentsArray.put(turn)
        }

        // Add current user prompt
        contentsArray.put(
            JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().put(JSONObject().put("text", userMessage)))
            }
        )

        val rootJson = JSONObject().apply {
            put("contents", contentsArray)
            put(
                "systemInstruction",
                JSONObject().put("parts", JSONArray().put(JSONObject().put("text", systemPrompt)))
            )
            put(
                "generationConfig",
                JSONObject().apply {
                    put("temperature", 0.7)
                    put("maxOutputTokens", 500)
                }
            )
        }

        val requestBody = rootJson.toString().toRequestBody("application/json".toMediaType())
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = httpClient.newCall(request).execute()
        if (!response.isSuccessful) return null

        val responseBodyString = response.body?.string() ?: return null
        val json = JSONObject(responseBodyString)
        val candidates = json.optJSONArray("candidates") ?: return null
        val firstCandidate = candidates.optJSONObject(0) ?: return null
        val content = firstCandidate.optJSONObject("content") ?: return null
        val parts = content.optJSONArray("parts") ?: return null
        val replyText = parts.optJSONObject(0)?.optString("text") ?: return null

        // Detect recommended perfume inside Gemini's response
        val matchedPerfume = findPerfumeInText(replyText) ?: findPerfumeInText(userMessage)

        return ChatMessage(
            sender = SenderType.LUXYS,
            text = replyText,
            recommendedPerfumeId = matchedPerfume?.id,
            whyPoints = listOf(
                "Authentic luxury blend crafted by LUXITR",
                "High sillage and 10+ hours longevity",
                "Guaranteed compliments on luxitr.com"
            ),
            bestForTags = listOf("Exclusive LUXITR", "Official Scent", "Free Fast Shipping"),
            suggestionChips = listOf("✨ More options", "Other occasions", "Order on luxitr.com")
        )
    }

    private fun generateLocalLuxysResponse(input: String): ChatMessage {
        val q = input.lowercase().trim()
        val isBengali = input.any { it in '\u0980'..'\u09FF' } ||
                q.contains("valo") || q.contains("jonno") || q.contains("konta") || q.contains("ki") || q.contains("lagbe")

        // 1. Office / Corporate queries
        if (q.contains("office") || q.contains("work") || q.contains("corporate") || q.contains("meeting") || q.contains("job")) {
            val bdc = repository.getPerfumeById("bleu-de-chanel")!!
            val text = if (isBengali) {
                "Office-এর জন্য আমার প্রথম পছন্দ BLEU DE CHANEL পারফিউমটি।\n\nএটি clean, elegant এবং professional vibe-এর জন্য নিখুঁত match! সহকর্মী ও ক্লায়েন্টদের কাছে আপনার ব্যক্তিত্বকে অত্যন্ত অভিজাতভাবে উপস্থাপন করবে।"
            } else {
                "For the office, my top recommendation is BLEU DE CHANEL.\n\nIt strikes the perfect balance of crisp citrus elegance and smooth woody prestige. It projects effortless authority without overwhelming the boardroom."
            }

            return ChatMessage(
                sender = SenderType.LUXYS,
                text = text,
                recommendedPerfumeId = bdc.id,
                alternativePerfumeIds = listOf("creed-aventus", "roja-elysium"),
                whyPoints = listOf(
                    if (isBengali) "Clean & sophisticated vibe" else "Clean & sophisticated vibe",
                    if (isBengali) "Professional & versatile" else "Professional & highly versatile",
                    if (isBengali) "Daytime use-এর জন্য perfect" else "Perfect for 9-to-5 daytime presence"
                ),
                bestForTags = listOf("💼 Office / Business", "☀️ Daytime", "❄️ All seasons"),
                suggestionChips = listOf("✨ More options", "Other occasions", "Details on luxitr.com")
            )
        }

        // 2. Date / Romantic queries
        if (q.contains("date") || q.contains("romantic") || q.contains("love") || q.contains("dinner") || q.contains("girlfriend") || q.contains("boyfriend") || q.contains("crush")) {
            val desire = repository.getPerfumeById("dunhill-desire")!!
            val text = if (isBengali) {
                "Date night-এর জন্য সবচেয়ে captivating পছন্দ হলো DUNHILL DESIRE!\n\nএর sweet red apple এবং warm vanilla-র মোহনীয় মেলবন্ধন সঙ্গীকে কাছে টেনে নেবে। রোমান্টিক আবহ তৈরিতে এর কোনো তুলনা নেই।"
            } else {
                "For an unforgettable Date Night, nothing commands intimacy quite like DUNHILL DESIRE.\n\nIts warm, seductive blend of crisp red apple, velvety teakwood, and intoxicating vanilla creates a magnetic sillage that draws your date impossibly close."
            }

            return ChatMessage(
                sender = SenderType.LUXYS,
                text = text,
                recommendedPerfumeId = desire.id,
                alternativePerfumeIds = listOf("lost-cherry", "baccarat-rouge-540"),
                whyPoints = listOf(
                    if (isBengali) "Warm & irresistible intimacy" else "Warm, magnetic sensual aura",
                    if (isBengali) "অসাধারণ vanilla & woody drydown" else "Addictive Madagascar vanilla drydown",
                    if (isBengali) "Late night রোমান্সের জন্য সেরা" else "Clinically proven compliment generator"
                ),
                bestForTags = listOf("💖 Romantic Dates", "🌙 Evenings", "🔥 Intimate Proximity"),
                suggestionChips = listOf("✨ More options", "Sweet perfumes", "Buy from luxitr.com")
            )
        }

        // 3. Bold / Party / Nightlife
        if (q.contains("party") || q.contains("club") || q.contains("bold") || q.contains("night") || q.contains("alpha") || q.contains("attention") || q.contains("hangout")) {
            val gof = repository.getPerfumeById("god-of-fire")!!
            val text = if (isBengali) {
                "Party বা বিশেষ রাতের আসরে সকলের মধ্যমণি হতে GOD OF FIRE ছাড়া ভাবাই যায় না!\n\nএর juicy tropical mango এবং fiery amber প্রজেকশন পুরো রুমে আপনার আগমন ঘোষণা করবে।"
            } else {
                "When you want to dominate the room and be the undisputed center of attention, choose GOD OF FIRE.\n\nA nuclear burst of exotic mango, fiery ginger, and smoked oud amber that commands awe from across the venue."
            }

            return ChatMessage(
                sender = SenderType.LUXYS,
                text = text,
                recommendedPerfumeId = gof.id,
                alternativePerfumeIds = listOf("sauvage-elixir", "black-opium"),
                whyPoints = listOf(
                    if (isBengali) "Beast Mode প্রজেকশন (৫/৫)" else "Beast-mode room-filling projection",
                    if (isBengali) "Exotic mango ও smoky oud কম্বিনেশন" else "Exotic fiery mango & noble amber",
                    if (isBengali) "১২ ঘণ্টারও বেশি দীর্ঘস্থায়ী" else "12+ hours extreme party longevity"
                ),
                bestForTags = listOf("👥 Parties & VIP Lounges", "🔥 High Energy", "👑 Alpha Presence"),
                suggestionChips = listOf("✨ More options", "Night perfumes", "Claim on luxitr.com")
            )
        }

        // 4. Fresh / Summer / Gym
        if (q.contains("fresh") || q.contains("summer") || q.contains("gym") || q.contains("gorom") || q.contains("ice") || q.contains("clean") || q.contains("water") || q.contains("sport")) {
            val hawas = repository.getPerfumeById("hawas-ice")!!
            val text = if (isBengali) {
                "তীব্র গরম ও চনমনে সতেজতার জন্য HAWAS ICE-এর কোনো বিকল্প নেই!\n\nবরফশীতল ফ্রস্টেড আপেল ও ইতালিয়ান সাইট্রাসের সাথে মেরিন উইন্ড আপনার শরীর ও মনে তাৎক্ষণিক প্রাণ সঞ্চার করবে।"
            } else {
                "For hot weather and an invigorating high-energy aura, HAWAS ICE is legendary.\n\nIt wraps you in a glacier-chilled blast of frosted green apple, sparkling bergamot, and salty ocean breeze that stays crystal crisp all day."
            }

            return ChatMessage(
                sender = SenderType.LUXYS,
                text = text,
                recommendedPerfumeId = hawas.id,
                alternativePerfumeIds = listOf("roja-elysium", "bleu-de-chanel"),
                whyPoints = listOf(
                    if (isBengali) "অত্যন্ত সতেজ ফ্রোজেন সাইট্রাস নোট" else "Ultra-refreshing frozen apple & bergamot",
                    if (isBengali) "গরমের দিনেও দুর্দান্ত পারফরম্যান্স" else "Unstoppable high-heat performance",
                    if (isBengali) "জিম ও দৈনন্দিন ব্যবহারের উপযোগী" else "Active lifestyle & daytime excellence"
                ),
                bestForTags = listOf("🍃 Fresh & Invigorating", "☀️ Summer Heat", "⚡ Active Energy"),
                suggestionChips = listOf("✨ More options", "Office perfumes", "Order on luxitr.com")
            )
        }

        // 5. Sweet / Gourmand / Luxury
        if (q.contains("sweet") || q.contains("sugar") || q.contains("misti") || q.contains("vanilla") || q.contains("gourmand") || q.contains("luxury") || q.contains("baccarat")) {
            val br540 = repository.getPerfumeById("baccarat-rouge-540")!!
            val text = if (isBengali) {
                "মিষ্টি এবং চরম আভিজাত্যের প্রতীক হিসেবে BACCARAT ROUGE 540-এর জাদুকরী সুবাস অপরিসীম।\n\nজাফরান, জুঁই এবং ক্রিস্টাল অ্যাম্বারউডের এই অমৃত সুবাস আপনাকে রাজকীয় মর্যাদায় ভূষিত করবে।"
            } else {
                "If you crave sweet opulence, BACCARAT ROUGE 540 is the holy grail of haute perfumery.\n\nA luminous veil of golden saffron, crystal jasmine, and mineral ambergris that floats like pure spun golden luxury."
            }

            return ChatMessage(
                sender = SenderType.LUXYS,
                text = text,
                recommendedPerfumeId = br540.id,
                alternativePerfumeIds = listOf("lost-cherry", "god-of-fire"),
                whyPoints = listOf(
                    if (isBengali) "বিশ্বজুড়ে সর্বাধিক প্রশংসিত লাক্সারি সুবাস" else "The world's most desired luxury scent",
                    if (isBengali) "১০/১০ ক্রিস্টালাইন সুইট সিয়েজ" else "Ethereal sillage that hangs in the air",
                    if (isBengali) "নারী ও পুরুষ উভয়ের জন্যই আদর্শ" else "Flawless unisex aristocracy"
                ),
                bestForTags = listOf("👑 Royal Aristocracy", "🌸 Sweet Ethereal", "💎 Pure Luxury"),
                suggestionChips = listOf("✨ More options", "Explore Catalog", "Order on luxitr.com")
            )
        }

        // 6. Executive / Wealth / King / Status
        if (q.contains("rich") || q.contains("king") || q.contains("boss") || q.contains("status") || q.contains("aventus") || q.contains("money") || q.contains("expensive") || q.contains("vip")) {
            val aventus = repository.getPerfumeById("creed-aventus")!!
            val text = if (isBengali) {
                "সাফল্য ও বিজয়ের অপর নাম CREED AVENTUS!\n\nধোঁয়াটে আনারস ও রাজকীয় বার্চ উডের এই সুবাস আপনাকে একজন সফল নেতার মর্যাদা দান করবে। LUXITR থেকে আজই সংগ্রহ করুন।"
            } else {
                "The undisputed scent of victory and boundless ambition: CREED AVENTUS.\n\nCrisp French pineapple, smoky birchwood, and noble ambergris announce that a leader has arrived. Uncompromising prestige in every drop."
            }

            return ChatMessage(
                sender = SenderType.LUXYS,
                text = text,
                recommendedPerfumeId = aventus.id,
                alternativePerfumeIds = listOf("bleu-de-chanel", "god-of-fire"),
                whyPoints = listOf(
                    if (isBengali) "বিজয়ের প্রতীকী সুবাস" else "Emblem of wealth and executive power",
                    if (isBengali) "স্মোকি আনারস ও কাঠের আভিজাত্য" else "Signature smoky blackcurrant & birch",
                    if (isBengali) "উচ্চ সম্মোহনী প্রজেকশন" else "High-status aura for career & galas"
                ),
                bestForTags = listOf("⚜️ Executive Triumph", "💼 Power Meetings", "👑 King of Fragrances"),
                suggestionChips = listOf("✨ More options", "Explore All", "Buy on luxitr.com")
            )
        }

        // Default intelligent luxury intro
        val defaultPerfume = repository.getPerfumeById("hawas-ice")!!
        val defaultText = if (isBengali) {
            "নমস্কার! আমি LUXYS ✨ LUXITR-এর আপনার ব্যক্তিগত সুগন্ধি উপদেষ্টা।\n\nআপনি কোন উপলক্ষ্যে সুগন্ধি খুঁজছেন? (যেমন: অফিস, ডেট নাইট, পার্টি, গরমের দিনের রিফ্রেশমেন্ট)?\nআমাদের সংগ্রহে প্রতিটি মুহূর্তকে স্মরণীয় করার সেরা পারফিউম রয়েছে।"
        } else {
            "Greetings! I am LUXYS ✨ your personal fragrance intelligence for the luxury house of LUXITR.\n\nTell me: what vibe or occasion are you dressing for? Whether it's executive boardroom confidence, romantic seduction, or vibrant party energy, I'll reveal your soul scent."
        }

        return ChatMessage(
            sender = SenderType.LUXYS,
            text = defaultText,
            recommendedPerfumeId = defaultPerfume.id,
            whyPoints = listOf(
                "100% Authentic Luxury Oil Concentrations",
                "Crafted for Exceptional 10+ Hour Sillage",
                "Direct Seamless Delivery via luxitr.com"
            ),
            bestForTags = listOf("✨ Bespoke Curation", "⚜️ LUXITR Signature", "🚀 Express Delivery"),
            suggestionChips = listOf("💼 Best for Office", "💖 Best for Date", "🍃 Fresh Perfumes", "✨ Find My Perfume")
        )
    }

    private fun findPerfumeInText(text: String): Perfume? {
        val lower = text.lowercase()
        return repository.catalog.find { perfume ->
            lower.contains(perfume.name.lowercase()) ||
            lower.contains(perfume.id.replace("-", " ")) ||
            lower.contains(perfume.id)
        }
    }
}
