package com.netanel.smartrip

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class RoutePlanTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun typingAndPlanningShowsPlan() {
        composeTestRule.onNodeWithContentDescription("origin").performTextInput("ירושלים")
        composeTestRule.onNodeWithContentDescription("destination").performTextInput("תל אביב")
        composeTestRule.onNodeWithText("תכנן").performClick()
        composeTestRule.onNodeWithText("ירושלים → תל אביב").assertExists()
        composeTestRule.onNodeWithText("קפה").performClick()
    }
}
