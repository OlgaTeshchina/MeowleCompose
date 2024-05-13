package ru.tinkoff.fintech.meowle.composeScreens

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ru.tinkoff.fintech.meowle.presentation.MainActivity

class ComposeRatingScreen(
    private val composeTestRule : AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>) {

    private val topCatName = composeTestRule.onAllNodesWithTag("topCatName", true)
    private val topOnDislike = composeTestRule.onNodeWithText("Топ по дизлайкам")

    fun clickTopDislike() {
        composeTestRule.waitUntil(5_000) { topOnDislike.isDisplayed() }
        topOnDislike.performClick()
    }

    @OptIn(ExperimentalTestApi::class)
    fun clickOnCat(catName: String) {
        composeTestRule.waitUntilAtLeastOneExists(hasText(catName), 5_000)
        composeTestRule.onNodeWithText(catName)
            .performClick()
    }

    @OptIn(ExperimentalTestApi::class)
    fun checkFirstCatName(expectedName: String) {
        composeTestRule
            .waitUntilAtLeastOneExists(hasText(expectedName), 5_000)
        topCatName
            .onFirst()
            .assertTextEquals(expectedName)
    }
}