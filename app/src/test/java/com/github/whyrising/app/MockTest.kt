package com.github.whyrising.app

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.ints.shouldBeExactly

class MockTest : FreeSpec({
    "nothing" {
        2 + 2 shouldBeExactly 4
    }
})
