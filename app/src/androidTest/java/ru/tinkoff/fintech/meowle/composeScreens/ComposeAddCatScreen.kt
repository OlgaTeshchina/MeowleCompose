package ru.tinkoff.fintech.meowle.composeScreens

import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ru.tinkoff.fintech.meowle.presentation.MainActivity

class ComposeAddCatScreen(
    private val composeTestRule : AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>) {

    private val catNameText = composeTestRule.onNodeWithText("Введите имя котика")
    private val catDescriptionText = composeTestRule.onNodeWithText("Описание")
    private val catGenderDropdown = composeTestRule.onNodeWithContentDescription("Выберите пол")
    private val catFemaleGender = composeTestRule.onNodeWithText("Женский")
    private val addBtn = composeTestRule.onAllNodesWithText("Добавить", true)
    private val snackBarText = composeTestRule.onNodeWithText("Котик добавлен")
    private val okBtn = composeTestRule.onNodeWithText("Ok")

    fun enterName(catName: String) {
        catNameText.performTextInput(catName)
    }

    fun enterDescription(catDescription: String) {
        catDescriptionText.performTextInput(catDescription)
    }

    fun chooseFemaleGender() {
        catGenderDropdown.performClick()
        catFemaleGender.performClick()
    }

    fun clickAddBtn() {
        addBtn
            .onFirst()
            .performClick()
    }

    fun checkSnackbar() {
        composeTestRule.waitUntil(3_000,) { snackBarText.isDisplayed() }
        okBtn.performClick()
    }
}