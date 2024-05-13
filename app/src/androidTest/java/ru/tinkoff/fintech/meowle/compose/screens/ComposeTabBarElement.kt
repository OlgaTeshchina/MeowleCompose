package ru.tinkoff.fintech.meowle.compose.screens

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ru.tinkoff.fintech.meowle.presentation.MainActivity

class ComposeTabBarElement(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>) {
    fun navigateToSearchTab() {
        navigateToTab("Поиск")
    }

    fun navigateToRatingTab() {
        navigateToTab("Рейтинг")
    }

    fun navigateToAddTab() {
        navigateToTab("Добавить")
    }

    fun navigateToFavoritesTab() {
        navigateToTab("Избранное")
    }

    fun navigateToSettingsTab() {
        navigateToTab("Настройки")
    }

    private fun navigateToTab(tabName: String) {
        composeTestRule
            .onNodeWithContentDescription(tabName, useUnmergedTree = true)
            .performClick()
    }
}