package ru.tinkoff.fintech.meowle.compose.tests

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.stubbing.Scenario
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.fintech.meowle.PreferenceRuleCompose
import ru.tinkoff.fintech.meowle.compose.screens.ComposeDetailsScreen
import ru.tinkoff.fintech.meowle.compose.screens.ComposeSearchScreen
import ru.tinkoff.fintech.meowle.presentation.MainActivity
import ru.tinkoff.fintech.meowle.wiremock.WireMockHelper.fileToString

class ComposeTransitionToDetailsTest {

    @get: Rule
    val mock = WireMockRule(5000)

    @get:Rule
    val prefs = PreferenceRuleCompose()

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun checkDetails() {
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

        with(ComposeSearchScreen(composeTestRule)) {
            findCat("Йок")
            clickOnFirstCat()
        }

        with(ComposeDetailsScreen(composeTestRule)) {
            checkName("Йокульчик, ")
            checkDescription("Самый милый котик из всех котиков")
            checkLikes("9")
            checkDislikes("0")
        }
    }
}