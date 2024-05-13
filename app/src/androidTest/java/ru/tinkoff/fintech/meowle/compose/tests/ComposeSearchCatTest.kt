package ru.tinkoff.fintech.meowle.compose.tests

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.fintech.meowle.PreferenceRuleCompose
import ru.tinkoff.fintech.meowle.compose.screens.ComposeSearchScreen
import ru.tinkoff.fintech.meowle.presentation.MainActivity
import ru.tinkoff.fintech.meowle.wiremock.WireMockHelper.fileToString

class ComposeSearchCatTest {

    @get:Rule
    val prefs = PreferenceRuleCompose()

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get: Rule
    val mock = WireMockRule(5000)

    @Test
    fun searchCatSuccess() {
        stubFor(
            post(WireMock.urlPathEqualTo("/api/core/cats/search"))
                .willReturn(
                    ok(fileToString("search_response.json"))
                )
        )

        with(ComposeSearchScreen(composeTestRule)) {
            findCat("Йок")
            checkNameOfFirstCat("Йок")
        }
    }
}