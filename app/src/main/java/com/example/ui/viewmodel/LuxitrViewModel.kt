package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.LuxysAiEngine
import com.example.data.db.AppDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.Perfume
import com.example.data.model.PerfumeCategory
import com.example.data.model.QuizResult
import com.example.data.model.SenderType
import com.example.data.repository.PerfumeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ScreenDestination {
    data object Splash : ScreenDestination()
    data object Home : ScreenDestination()
    data object Chat : ScreenDestination()
    data object Collection : ScreenDestination()
    data class Detail(val perfumeId: String) : ScreenDestination()
    data object Quiz : ScreenDestination()
    data class InAppWeb(val url: String, val perfumeName: String? = null) : ScreenDestination()
    data object Favorites : ScreenDestination()
}

class LuxitrViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    val repository = PerfumeRepository(database.favoriteDao())
    private val aiEngine = LuxysAiEngine(repository)

    // Navigation Stack
    private val _currentScreen = MutableStateFlow<ScreenDestination>(ScreenDestination.Splash)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    private val screenStack = mutableListOf<ScreenDestination>()

    // Catalog & Search State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(PerfumeCategory.ALL)
    val selectedCategory: StateFlow<PerfumeCategory> = _selectedCategory.asStateFlow()

    private val _filteredPerfumes = MutableStateFlow<List<Perfume>>(repository.catalog)
    val filteredPerfumes: StateFlow<List<Perfume>> = _filteredPerfumes.asStateFlow()

    // Favorites
    val favoriteIds: StateFlow<List<String>> = repository.favoriteIds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoritePerfumes: StateFlow<List<Perfume>> = repository.favoritePerfumes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Chat State with LUXYS
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = SenderType.LUXYS,
                text = "Welcome to LUXYS ✨\nYour personal fragrance intelligence for LUXITR.\n\nTell me what vibe or occasion you're searching for—like 'office', 'date night', or 'something sweet and fresh'—and I will unveil your perfect signature fragrance.",
                recommendedPerfumeId = "hawas-ice",
                whyPoints = listOf(
                    "Bespoke olfactory matching",
                    "Hand-selected luxury oil concentration",
                    "100% authentic on luxitr.com"
                ),
                bestForTags = listOf("✨ Signature Curation", "⚜️ Official LUXITR", "🚀 Fast Shipping"),
                suggestionChips = listOf(
                    "💼 Best for Office",
                    "💖 Best for Date",
                    "🍃 Fresh Perfumes",
                    "✨ Find My Perfume"
                )
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isBotTyping = MutableStateFlow(false)
    val isBotTyping: StateFlow<Boolean> = _isBotTyping.asStateFlow()

    // Quiz State
    private val _currentQuizStep = MutableStateFlow(1)
    val currentQuizStep: StateFlow<Int> = _currentQuizStep.asStateFlow()

    private val _selectedVibe = MutableStateFlow("Fresh")
    val selectedVibe: StateFlow<String> = _selectedVibe.asStateFlow()

    private val _selectedOccasion = MutableStateFlow("Office / Work")
    val selectedOccasion: StateFlow<String> = _selectedOccasion.asStateFlow()

    private val _selectedSeason = MutableStateFlow("Summer / Hot")
    val selectedSeason: StateFlow<String> = _selectedSeason.asStateFlow()

    private val _selectedIntensity = MutableStateFlow("Balanced & Noticeable")
    val selectedIntensity: StateFlow<String> = _selectedIntensity.asStateFlow()

    private val _quizResult = MutableStateFlow<QuizResult?>(null)
    val quizResult: StateFlow<QuizResult?> = _quizResult.asStateFlow()

    init {
        // Automatically transition from splash to home after delay
        viewModelScope.launch {
            delay(2000)
            if (_currentScreen.value is ScreenDestination.Splash) {
                navigateTo(ScreenDestination.Home, clearBackstack = true)
            }
        }
    }

    // Navigation Methods
    fun navigateTo(destination: ScreenDestination, clearBackstack: Boolean = false) {
        if (clearBackstack) {
            screenStack.clear()
        } else {
            screenStack.add(_currentScreen.value)
        }
        _currentScreen.value = destination
    }

    fun navigateBack(): Boolean {
        if (screenStack.isNotEmpty()) {
            val prev = screenStack.removeAt(screenStack.size - 1)
            _currentScreen.value = prev
            return true
        }
        if (_currentScreen.value !is ScreenDestination.Home) {
            _currentScreen.value = ScreenDestination.Home
            return true
        }
        return false
    }

    // Search and Filter
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterCatalog()
    }

    fun selectCategory(category: PerfumeCategory) {
        _selectedCategory.value = category
        filterCatalog()
    }

    private fun filterCatalog() {
        _filteredPerfumes.value = repository.searchPerfumes(
            _searchQuery.value,
            _selectedCategory.value
        )
    }

    // Favorites
    fun toggleFavorite(perfumeId: String) {
        viewModelScope.launch {
            val isFav = favoriteIds.value.contains(perfumeId)
            repository.toggleFavorite(perfumeId, isFav)
        }
    }

    // Buy Now Action - Opens luxitr.com
    fun openBuyNow(context: Context, perfume: Perfume) {
        val searchUrl = "https://luxitr.com/search?q=" + java.net.URLEncoder.encode(perfume.name, "UTF-8")
        // Navigate to In-App Web Viewer first for seamless immersion
        navigateTo(ScreenDestination.InAppWeb(url = searchUrl, perfumeName = perfume.name))
    }

    fun openDirectExternalBrowser(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // fallback
        }
    }

    // Chat Actions
    fun sendMessageToLuxys(messageText: String) {
        if (messageText.isBlank()) return

        val userMessage = ChatMessage(
            sender = SenderType.USER,
            text = messageText.trim()
        )
        _chatMessages.value = _chatMessages.value + userMessage
        _isBotTyping.value = true

        viewModelScope.launch {
            // Small authentic thinking delay for elegance
            delay(600)
            val botResponse = aiEngine.generateResponse(userMessage.text, _chatMessages.value)
            _isBotTyping.value = false
            _chatMessages.value = _chatMessages.value + botResponse
        }
    }

    fun handleQuickCategory(categoryName: String) {
        when (categoryName) {
            "✨ Find My Perfume" -> {
                startQuiz()
            }
            "▦ Explore All Perfumes" -> {
                selectCategory(PerfumeCategory.ALL)
                navigateTo(ScreenDestination.Collection)
            }
            else -> {
                navigateTo(ScreenDestination.Chat)
                sendMessageToLuxys("I'm looking for the $categoryName")
            }
        }
    }

    // Quiz flow
    fun startQuiz() {
        _currentQuizStep.value = 1
        _quizResult.value = null
        navigateTo(ScreenDestination.Quiz)
    }

    fun selectQuizVibe(vibe: String) { _selectedVibe.value = vibe }
    fun selectQuizOccasion(occasion: String) { _selectedOccasion.value = occasion }
    fun selectQuizSeason(season: String) { _selectedSeason.value = season }
    fun selectQuizIntensity(intensity: String) { _selectedIntensity.value = intensity }

    fun nextQuizStep() {
        if (_currentQuizStep.value < 4) {
            _currentQuizStep.value += 1
        } else {
            // Calculate final match
            val result = repository.calculateQuizResult(
                vibe = _selectedVibe.value,
                occasion = _selectedOccasion.value,
                season = _selectedSeason.value,
                intensity = _selectedIntensity.value
            )
            _quizResult.value = result
            _currentQuizStep.value = 5 // Result screen
        }
    }

    fun prevQuizStep() {
        if (_currentQuizStep.value > 1) {
            _currentQuizStep.value -= 1
        } else {
            navigateBack()
        }
    }
}
