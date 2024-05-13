package ru.tinkoff.fintech.meowle.kaspressoScreens

import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.edit.KTextInputLayout
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import ru.tinkoff.fintech.meowle.R

class DetailsScreen : BaseScreen() {

    private val catName = KEditText { withId(R.id.cat_name) }
    private val catDescription = KTextView { withId(R.id.cat_description) }
    private val catDescriptionForChange = KTextInputLayout { withId(R.id.til_desc) }
    private val countLikes = KEditText { withId(R.id.tw_likes) }
    private val likeBtn = KEditText { withId(R.id.ib_like) }
    private val countDislikes = KEditText { withId(R.id.tw_dislikes) }
    private val editBtn = KButton { withId(R.id.btn_edit) }
    private val confirmBtn = KButton { withText(R.string.details_bottom_sheet_save_button) }

    fun checkCatName(expectedName: String) {
        catName.hasText(expectedName)
    }

    fun checkCatDescription(expectedDescription: String) {
        catDescription.hasText(expectedDescription)
    }

    fun checkCatLikes(expectedLikes: String) {
        countLikes.hasText(expectedLikes)
    }

    fun checkCatDislikes(expectedDislikes: String) {
        countDislikes.hasText(expectedDislikes)
    }

    fun clickEditBtn() {
        editBtn.click()
    }

    fun clickConfirmBtn() {
        confirmBtn.isDisplayed()
        confirmBtn.click()
    }

    fun clickOnLike() {
        likeBtn.click()
    }

    fun enterNewDescription(descriptionText: String) {
        with(catDescriptionForChange) {
            edit.isDisplayed()
            edit.replaceText(descriptionText)
        }
    }
}