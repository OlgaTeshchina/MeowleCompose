package ru.tinkoff.fintech.meowle.kaspressoScreens

import android.view.View
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import ru.tinkoff.fintech.meowle.R

class SearchCatsScreen : BaseScreen() {

    private val searchInput = KEditText { withId(R.id.et_search) }
    private val catsList = KRecyclerView(
        builder = { withId(R.id.rv_search_result_list) },
        itemTypeBuilder = { itemType(::CatCard) }
    )

    fun findCat(catName: String) {
        searchInput.replaceText(catName)
        searchInput.pressImeAction()
    }

    fun checkFoundCatName(catName: String, position: Int) {
        catsList.childAt<CatCard>(position) {
            this.catName.containsText(catName)
        }
    }

    fun checkCatsListSize(size: Int) {
        catsList.hasSize(size)
    }

    fun clickOnCat(position: Int) {
        catsList.childAt<CatCard>(position) {
            this.catName.click()
        }
    }
}

private class CatCard(matcher: Matcher<View>) : KRecyclerItem<CatCard>(matcher) {
    val catName = KTextView(matcher) { withId(R.id.cat_name) }
}

