package com.netanel.smartrip

import com.netanel.smartrip.model.PartyProfile
import com.netanel.smartrip.model.computeStopFractions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StopCalculatorTest {
    @Test
    fun infantsMoreStops() {
        val withInfant = PartyProfile(infants = true)
        val adult = PartyProfile()
        val s1 = computeStopFractions(300.0, withInfant, false)
        val s2 = computeStopFractions(300.0, adult, false)
        assertTrue(s1.size > s2.size)
    }

    @Test
    fun fuelMandatoryAddsStop() {
        val adult = PartyProfile()
        val s = computeStopFractions(10.0, adult, true)
        assertEquals(1, s.size)
        assertEquals(0.5, s[0], 0.01)
    }
}
