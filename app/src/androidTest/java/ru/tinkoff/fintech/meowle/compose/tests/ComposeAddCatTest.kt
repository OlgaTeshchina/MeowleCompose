package ru.tinkoff.fintech.meowle.compose.tests

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.stubbing.Scenario
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.fintech.meowle.PreferenceRule
import ru.tinkoff.fintech.meowle.compose.screens.ComposeAddCatScreen
import ru.tinkoff.fintech.meowle.compose.screens.ComposeTabBarElement
import ru.tinkoff.fintech.meowle.presentation.MainActivity
import ru.tinkoff.fintech.meowle.wiremock.WireMockHelper.fileToString

class ComposeAddCatTest {

    @get:Rule
    val prefs = PreferenceRule(true, true)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get: Rule
    val mock = WireMockRule(5000)

    @Test
    fun checkAddCatSuccess() {

        val checkAddCat = "Проверка метода с запросом, в котором передаются данные кота из теста"

        stubFor(
            post("/api/core/cats/add")
                .inScenario(checkAddCat)
                .whenScenarioStateIs(Scenario.STARTED)
                .willSetStateTo("Step 2")
                .willReturn(
                    ok(fileToString("add_cat_response.json"))
                )
        )

        stubFor(
            post(urlPathEqualTo("/api/core/cats/search"))
                .inScenario(checkAddCat)
                .whenScenarioStateIs("Step 2")
                .willReturn(
                    ok(fileToString("add_search_response.json"))
                )
        )

        ComposeTabBarElement(composeTestRule)
            .navigateToAddTab()
        with(ComposeAddCatScreen(composeTestRule)) {
            enterName("Жужик")
            chooseFemaleGender()
            enterDescription("Котик")
            clickAddBtn()
            checkSnackbar()
        }

        verify(
            postRequestedFor(urlEqualTo("/api/core/cats/add"))
                .withRequestBody(equalToJson(fileToString("add_cat_request.json")))
        )
    }
}