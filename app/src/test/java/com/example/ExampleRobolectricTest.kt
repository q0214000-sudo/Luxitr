package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.db.AppDatabase
import com.example.data.model.PerfumeCategory
import com.example.data.repository.PerfumeRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read app name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("LUXITR", appName)
    }

    @Test
    fun `verify perfume catalog contains key luxury scents`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AppDatabase.getDatabase(context)
        val repository = PerfumeRepository(db.favoriteDao())

        val catalog = repository.catalog
        assertTrue("Catalog should contain fragrances", catalog.isNotEmpty())

        val hawas = repository.getPerfumeById("hawas-ice")
        assertNotNull("Hawas ICE should exist", hawas)
        assertEquals("Hawas ICE", hawas?.name)

        val godOfFire = repository.getPerfumeById("god-of-fire")
        assertNotNull("God of Fire should exist", godOfFire)

        val bdc = repository.getPerfumeById("bleu-de-chanel")
        assertNotNull("Bleu De Chanel should exist", bdc)
    }

    @Test
    fun `verify quiz calculation returns matching perfume`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AppDatabase.getDatabase(context)
        val repository = PerfumeRepository(db.favoriteDao())

        val result = repository.calculateQuizResult(
            vibe = "Fresh",
            occasion = "Office / Work",
            season = "Summer / Hot",
            intensity = "Balanced & Noticeable"
        )

        assertNotNull(result.primaryMatch)
        assertTrue(result.matchPercentage in 85..99)
        assertTrue(result.primaryMatch.searchUrl.contains("luxitr.com"))
    }
}
