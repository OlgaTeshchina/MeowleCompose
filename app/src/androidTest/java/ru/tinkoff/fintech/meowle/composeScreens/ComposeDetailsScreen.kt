package ru.tinkoff.fintech.meowle.composeScreens

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ru.tinkoff.fintech.meowle.presentation.MainActivity

class ComposeDetailsScreen (
    private val composeTestRule : AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>) {

    private val catName = composeTestRule.onNodeWithTag("catName", true)
    private val catDescription = composeTestRule.onNodeWithTag("catDescription", true)
    private val catCountLikes = composeTestRule.onAllNodesWithTag("catCountLikes", true)
    private val catLikeBtn = composeTestRule.onAllNodesWithTag("likeCatBtn", true)
    private val changDescription = composeTestRule.onNodeWithText("Поменять описание котика")
    private val changBtn = composeTestRule.onNodeWithText("Изменить")
    private val saveBtn = composeTestRule.onNodeWithText("Сохранить")

    fun checkName(expectedName: String) {
        catName.assertTextContains(expectedName)
    }

    fun checkDescription(expectedDescription: String) {
        catDescription.assertTextEquals(expectedDescription)
    }

    @OptIn(ExperimentalTestApi::class)
    fun checkLikes(expectedLikes: String) {
        composeTestRule
            .waitUntilAtLeastOneExists(hasText(expectedLikes), 3_000)
        catCountLikes
            .onFirst()
            .assertTextEquals(expectedLikes)

    }

    fun checkDislikes(expectedDislikes: String) {
        catCountLikes
            .onLast()
            .assertTextEquals(expectedDislikes)
    }

    fun clickOnLike() {
        catLikeBtn
            .onFirst()
            .performClick()
    }

    fun clickChangBtn() {
        changBtn.performClick()
    }

    fun clickSaveBtn() {
        saveBtn.performClick()
    }

    fun enterDescription(descriptionText: String) {
        changDescription.performTextInput(descriptionText)
    }
}