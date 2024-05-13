package ru.tinkoff.fintech.meowle.composeScreens

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ru.tinkoff.fintech.meowle.presentation.MainActivity

class ComposeSearchScreen(
    private val composeTestRule : AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>) {

    private val searchInput = composeTestRule.onNodeWithTag("search")
    private val catsFind = composeTestRule.onAllNodesWithTag("catName", true)
    private val tagCatList = "catCard"

    fun findCat(catName: String) {
        with(searchInput) {
            performTextInput(catName)
            performImeAction()
        }
    }

    fun checkNameOfFirstCat(expectedText: String) {
        waitListToLoud()
        catsFind
            .onFirst()
            .assertTextContains(expectedText, true)
    }

    fun clickOnFirstCat() {
        waitListToLoud()
        catsFind
            .onFirst()
            .performClick()
    }

    @OptIn(ExperimentalTestApi::class)
    private fun waitListToLoud() {
        composeTestRule
            .waitUntilAtLeastOneExists(hasTestTag(tagCatList), 3_000)
    }
}
