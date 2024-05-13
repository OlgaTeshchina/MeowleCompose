package ru.tinkoff.fintech.meowle.kaspresso.tests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.stubbing.Scenario
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.fintech.meowle.PreferenceRuleKaspresso
import ru.tinkoff.fintech.meowle.presentation.MainActivity
import ru.tinkoff.fintech.meowle.kaspresso.screens.AddCatScreen
import ru.tinkoff.fintech.meowle.wiremock.WireMockHelper.fileToString

class AddCatTest : TestCase() {

    @get: Rule
    val prefs = PreferenceRuleKaspresso()

    @get: Rule
    val mock = WireMockRule(5000)

    @get: Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun addCatSuccess() = run {
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

        with(AddCatScreen()) {
            clickTabAddCat()
            enterName("Жужик")
            chooseGenderFemale()
            enterDescription("Котик")
            clickAddBtn()
            checkSuccessMessage()
        }

        verify(
            WireMock.postRequestedFor(urlEqualTo("/api/core/cats/add"))
                .withRequestBody(equalToJson(fileToString("add_cat_request.json")))
        )
    }
}