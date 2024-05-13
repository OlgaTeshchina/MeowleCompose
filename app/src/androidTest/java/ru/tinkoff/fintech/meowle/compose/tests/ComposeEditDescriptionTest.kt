package ru.tinkoff.fintech.meowle.compose.tests

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.stubbing.Scenario
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.fintech.meowle.PreferenceRuleCompose
import ru.tinkoff.fintech.meowle.compose.screens.ComposeDetailsScreen
import ru.tinkoff.fintech.meowle.compose.screens.ComposeRatingScreen
import ru.tinkoff.fintech.meowle.compose.screens.ComposeTabBarElement
import ru.tinkoff.fintech.meowle.presentation.MainActivity
import ru.tinkoff.fintech.meowle.wiremock.WireMockHelper.crutchVerify
import ru.tinkoff.fintech.meowle.wiremock.WireMockHelper.fileToString

class ComposeEditDescriptionTest {

    @get:Rule
    val prefs = PreferenceRuleCompose()

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get: Rule
    val mock = WireMockRule(5000)

    @Test
    fun checkEditDescription() {
        val checkEditCatDescription = "Проверка изменения описания в карточке котика"

        stubFor(
            get("/api/likes/cats/rating")
                .inScenario(checkEditCatDescription)
                .whenScenarioStateIs(Scenario.STARTED)
                .willSetStateTo("Step 2")
                .willReturn(
                    ok(fileToString("rating_response.json"))
                )
        )

        stubFor(
            get(urlEqualTo("/api/photos/cats/15480/photos"))
                .inScenario(checkEditCatDescription)
                .whenScenarioStateIs("Step 2")
                .willSetStateTo("Step 3")
                .willReturn(
                    ok()
                )
        )

        stubFor(
            get(urlEqualTo("/api/core/cats/get-by-id?id=15480"))
                .inScenario(checkEditCatDescription)
                .whenScenarioStateIs("Step 2")
                .willReturn(
                    ok(fileToString("description_cat_details.json"))
                )
        )

        stubFor(
            WireMock.post(urlEqualTo("/api/core/cats/save-description"))
                .inScenario(checkEditCatDescription)
                .whenScenarioStateIs("Step 3")
                .willReturn(
                    ok(fileToString("description_response.json"))
                )
        )

        ComposeTabBarElement(composeTestRule)
            .navigateToRatingTab()
        with(ComposeRatingScreen(composeTestRule)) {
            clickOnCat("Терех")
        }

        with(ComposeDetailsScreen(composeTestRule)) {
            clickChangBtn()
            checkDescription("Котик, который любит покушать")
            enterDescription("Самый голодный котик")
            clickSaveBtn()
            checkDescription("Самый голодный котик")
        }

        crutchVerify(1,
            postRequestedFor(urlEqualTo("/api/core/cats/save-description"))
                .withRequestBody(equalToJson(fileToString("description_request.json")))
        )
    }
}