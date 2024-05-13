package ru.tinkoff.fintech.meowle.kaspressoScreens

import android.view.View
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import ru.tinkoff.fintech.meowle.R

class RatingScreen : BaseScreen() {

    private val tabRating = KEditText { withId(R.id.tab_btn_rating) }
    private val dislikedHeader = KTextView { withText("Топ по дизлайкам") }
    private val catsList = KRecyclerView(
        builder = { withId(R.id.rv_cats_list) },
        itemTypeBuilder = { itemType(::CatCardRating) }
    )

    fun clickTabRating() {
        tabRating.click()
    }

    fun clickTopDislikes() {
        dislikedHeader.click()
    }

    fun clickOnCat(position: Int) {
        catsList.childAt<CatCardRating>(position) {
            this.catName.click()
        }
    }

    fun checkCatName(catName: String, position: Int) {
        catsList.childAt<CatCardRating>(position) {
            this.catName.containsText(catName)
        }
    }

    fun checkCatsListSize(size: Int) {
        catsList.hasSize(size)
    }
}

private class CatCardRating(matcher: Matcher<View>) : KRecyclerItem<CatCardRating>(matcher) {
    val catName = KTextView(matcher) { withId(R.id.cat_name) }
}

