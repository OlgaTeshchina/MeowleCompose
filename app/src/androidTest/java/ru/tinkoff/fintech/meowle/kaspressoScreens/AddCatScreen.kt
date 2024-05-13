package ru.tinkoff.fintech.meowle.kaspressoScreens

import android.os.Build
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import ru.tinkoff.fintech.meowle.R
import ru.tinkoff.fintech.meowle.ToastMatcherAPI29

class AddCatScreen: BaseScreen() {

    private val tabAddCat = KEditText { withId(R.id.tab_btn_add) }
    private val nameCatText = KEditText { withId(R.id.et_name) }
    private val genderCat = KTextView { withHint("Пол") }
    private val femaleGenderText = KTextView { withText(R.string.gender_female) }
    private val descriptionCatText = KEditText { withId(R.id.cat_description) }
    private val addCatBtn = KButton { withId(R.id.btn_add) }
    private val successMessage = KTextView { withText(R.string.add_snackbar_success_message) }

    fun clickTabAddCat() {
        tabAddCat.click()
    }

    fun clickAddBtn() {
        addCatBtn.click()
    }

    fun enterName(nameCat: String) {
        nameCatText.replaceText(nameCat)
    }

    fun enterDescription(catDescription: String) {
        descriptionCatText.replaceText(catDescription)
    }

    fun chooseGenderFemale() {
        genderCat.click()
        with(femaleGenderText) {
            inRoot { isPlatformPopup() }
            click()
        }
    }

    fun checkSuccessMessage() {
        if (Build.VERSION.SDK_INT <= 29) {
            successMessage.inRoot { ToastMatcherAPI29() }
            successMessage.isDisplayed()
        }
    }
}
