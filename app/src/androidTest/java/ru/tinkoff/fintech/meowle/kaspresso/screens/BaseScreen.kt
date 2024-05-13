package ru.tinkoff.fintech.meowle.kaspresso.screens

import com.kaspersky.kaspresso.screens.KScreen
import ru.tinkoff.fintech.meowle.R

abstract class BaseScreen : KScreen<BaseScreen>() {

    override val layoutId: Int = R.layout.search_fragment
    override val viewClass: Class<*>? = null
}