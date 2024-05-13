package ru.tinkoff.fintech.meowle.kaspresso.tests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.fintech.meowle.PreferenceRuleKaspresso
import ru.tinkoff.fintech.meowle.presentation.MainActivity
import ru.tinkoff.fintech.meowle.kaspresso.screens.DetailsScreen
import ru.tinkoff.fintech.meowle.kaspresso.screens.RatingScreen
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.github.tomakehurst.wiremock.stubbing.Scenario
import ru.tinkoff.fintech.meowle.wiremock.WireMockHelper.fileToString

class LikeTopCatTest : TestCase() {

    @get: Rule
    val prefs = PreferenceRuleKaspresso()

    @get: Rule
    val mock = WireMockRule(5000)

    @get: Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun checkLikeInTopDislike() = run {
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

        with(RatingScreen()) {
            clickTabRating()
            clickTopDislikes()
            checkCatName("Звезда", 0)
            checkCatsListSize(10)
            clickOnCat(0)
        }

        with(DetailsScreen()) {
            clickOnLike()
            checkCatLikes("1")
        }

        verify(
            postRequestedFor(urlEqualTo("/api/likes/cats/14016/likes"))
                .withRequestBody(equalToJson(fileToString("like_request.json")))
        )
    }
}