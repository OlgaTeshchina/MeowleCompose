package ru.tinkoff.fintech.meowle.compose.tests

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.fintech.meowle.presentation.MainActivity
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.stubbing.Scenario
import ru.tinkoff.fintech.meowle.PreferenceRule
import ru.tinkoff.fintech.meowle.compose.screens.ComposeDetailsScreen
import ru.tinkoff.fintech.meowle.compose.screens.ComposeRatingScreen
import ru.tinkoff.fintech.meowle.compose.screens.ComposeTabBarElement
import ru.tinkoff.fintech.meowle.wiremock.WireMockHelper.crutchVerify
import ru.tinkoff.fintech.meowle.wiremock.WireMockHelper.fileToString

class ComposeLikeTopCatTest {

    @get:Rule
    val prefs = PreferenceRule(true, true)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get: Rule
    val mock = WireMockRule(5000)

    @Test
    fun likeInTopCatDislike() {
        val checkCatLike = "Проверка метода с запросом, в котором передался лайк"

        stubFor(
            get("/api/likes/cats/rating")
                .inScenario(checkCatLike)
                .whenScenarioStateIs(Scenario.STARTED)
                .willSetStateTo("Step 2")
                .willReturn(
                    ok(fileToString("rating_response.json"))
                )
        )

        stubFor(
            get("/api/likes/cats/rating")
                .inScenario(checkCatLike)
                .whenScenarioStateIs("Step 2")
                .willSetStateTo("Step 3")
                .willReturn(
                    ok(fileToString("rating_response.json"))
                )
        )

        stubFor(
            get(urlEqualTo("/api/photos/cats/14016/photos"))
                .inScenario(checkCatLike)
                .whenScenarioStateIs("Step 3")
                .willSetStateTo("Step 4")
                .willReturn(
                    ok()
                )
        )

        stubFor(
            get(urlEqualTo("/api/core/cats/get-by-id?id=14016"))
                .inScenario(checkCatLike)
                .whenScenarioStateIs("Step 3")
                .willReturn(
                    ok()
                )
        )

        stubFor(
            post(urlEqualTo("/api/likes/cats/14016/likes"))
                .inScenario(checkCatLike)
                .whenScenarioStateIs("Step 4")
                .willReturn(
                    ok(fileToString("like_response.json"))
                )
        )

        ComposeTabBarElement(composeTestRule)
            .navigateToRatingTab()
        with(ComposeRatingScreen(composeTestRule)) {
            clickTopDislike()
            checkFirstCatName("Звезда")
            clickOnCat("Звезда")
        }

        with(ComposeDetailsScreen(composeTestRule)) {
            clickOnLike()
            checkLikes("1")
        }

        crutchVerify(1,
            postRequestedFor(urlEqualTo("/api/likes/cats/14016/likes"))
                .withRequestBody(equalToJson(fileToString("like_request.json")))
        )
    }
}