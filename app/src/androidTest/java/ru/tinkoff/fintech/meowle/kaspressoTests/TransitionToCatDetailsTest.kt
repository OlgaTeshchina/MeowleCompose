package ru.tinkoff.fintech.meowle.kaspressoTests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.stubbing.Scenario
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.fintech.meowle.PreferenceRuleKaspresso
import ru.tinkoff.fintech.meowle.presentation.MainActivity
import ru.tinkoff.fintech.meowle.kaspressoScreens.DetailsScreen
import ru.tinkoff.fintech.meowle.kaspressoScreens.SearchCatsScreen
import ru.tinkoff.fintech.meowle.wiremock.WireMockHelper.fileToString

class TransitionToCatDetailsTest : TestCase() {

    @get: Rule
    val prefs = PreferenceRuleKaspresso()

    @get: Rule
    val mock = WireMockRule(5000)

    @get: Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun checkCatDetails() = run {
        val catDetails = "Проверка информации о котике в его карточке"

        stubFor(
            post(urlPathEqualTo("/api/core/cats/search"))
                .inScenario(catDetails)
                .whenScenarioStateIs(Scenario.STARTED)
                .willSetStateTo("Step 2")
                .willReturn(
                    ok(fileToString("search_response.json"))
                )
        )

        stubFor(
            get(urlEqualTo("/api/photos/cats/17888/photos"))
                .inScenario(catDetails)
                .whenScenarioStateIs("Step 2")
                .willReturn(
                    ok()
                )
        )

        stubFor(
            get(urlEqualTo("/api/core/cats/get-by-id?id=17888"))
                .inScenario(catDetails)
                .whenScenarioStateIs("Step 2")
                .willReturn(
                    ok()
                )
        )

        with(SearchCatsScreen()) {
            findCat("Йок")
            clickOnCat(0)
        }

        with(DetailsScreen()) {
            checkCatName("Йокульчик")
            checkCatDescription("Самый милый котик из всех котиков")
            checkCatLikes("9")
            checkCatDislikes("0")
        }
    }
}

