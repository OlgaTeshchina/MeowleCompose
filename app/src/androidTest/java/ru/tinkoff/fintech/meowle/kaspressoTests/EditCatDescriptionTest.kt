package ru.tinkoff.fintech.meowle.kaspressoTests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.stubbing.Scenario
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.fintech.meowle.PreferenceRuleKaspresso
import ru.tinkoff.fintech.meowle.presentation.MainActivity
import ru.tinkoff.fintech.meowle.kaspressoScreens.DetailsScreen
import ru.tinkoff.fintech.meowle.kaspressoScreens.RatingScreen
import ru.tinkoff.fintech.meowle.wiremock.WireMockHelper.fileToString
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class EditCatDescriptionTest: TestCase() {

    @get: Rule
    val prefs = PreferenceRuleKaspresso()

    @get: Rule
    val mock = WireMockRule(5000)

    @get: Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun editDescriptionSuccess() = run {
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
            post(urlEqualTo("/api/core/cats/save-description"))
                .inScenario(checkEditCatDescription)
                .whenScenarioStateIs("Step 3")
                .willReturn(
                    ok(fileToString("description_response.json"))
                )
        )

        with(RatingScreen()) {
            clickTabRating()
            clickOnCat(4)
        }

        with(DetailsScreen()) {
            checkCatDescription("Котик, который любит покушать")
            clickEditBtn()
            enterNewDescription("Самый голодный котик")

            runBlocking { delay(200) }

            clickConfirmBtn()
            checkCatDescription("Самый голодный котик")
        }

        verify(
            postRequestedFor(urlEqualTo("/api/core/cats/save-description"))
                .withRequestBody(equalToJson(fileToString("description_request.json")))
        )
    }
}



