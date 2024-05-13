package ru.tinkoff.fintech.meowle.wiremock

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.github.tomakehurst.wiremock.client.WireMock.findAll
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

/**
 * @author Ruslan Ganeev
 */
object WireMockHelper {

    fun fileToString(path: String, context: Context = InstrumentationRegistry.getInstrumentation().context): String {
        return BufferedReader(InputStreamReader(context.assets.open(path), StandardCharsets.UTF_8)).readText()
    }

    /**
     * В некоторых тестах воспроизводится баг: https://github.com/wiremock/wiremock/issues/706
     * Используется решение: https://github.com/wiremock/wiremock/issues/706#issuecomment-760005655
     */
    fun crutchVerify(count: Int, requestPatternBuilder: RequestPatternBuilder) {
        var maxRetries = 5
        while (count != findAll(requestPatternBuilder).size && maxRetries > 0) {
            Thread.sleep(1000)
            maxRetries--
        }
        verify(count, requestPatternBuilder)
    }
}