package ru.tinkoff.fintech.meowle.kaspresso.tests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.fintech.meowle.PreferenceRuleKaspresso
import ru.tinkoff.fintech.meowle.kaspresso.screens.SearchCatsScreen
import ru.tinkoff.fintech.meowle.presentation.MainActivity
import ru.tinkoff.fintech.meowle.wiremock.WireMockHelper.fileToString

class SearchCatsSuccessTest : TestCase() {

    @get: Rule
    val prefs = PreferenceRuleKaspresso()

    @get: Rule
    val mock = WireMockRule(5000)

    @get: Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun checkSearchCatSuccess() = run {
        stubFor(
            post(urlPathEqualTo("/api/core/cats/search"))
                .willReturn(
                    ok(fileToString("search_response.json"))
                )
        )

        with(SearchCatsScreen()) {
            findCat("Йок")
            checkCatsListSize(4)
            checkFoundCatName("Йок", 0)
        }
    }
}



