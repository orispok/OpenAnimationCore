package com.osg.openanimation.core.ui.color

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class ColorsExtractorTest {

    private val sampleLottieJson = """
        {
          "v": "5.5.2",
          "fr": 30,
          "ip": 0,
          "op": 90,
          "w": 1920,
          "h": 1080,
          "nm": "Test Animation",
          "layers": [
            {
              "nm": "Shape Layer 1",
              "ty": 4,
              "ks": {},
              "shapes": [
                {
                  "nm": "Group 1",
                  "it": [
                    {
                      "nm": "Fill 1",
                      "ty": "fl",
                      "c": {
                        "a": 0,
                        "k": [0.1, 0.2, 0.3, 1.0]
                      }
                    }
                  ]
                }
              ]
            },
            {
              "nm": "Shape Layer 2",
              "ty": 4,
              "ks": {},
              "shapes": [
                {
                  "nm": "Group 2",
                  "it": [
                    {
                      "nm": "Stroke 1",
                      "ty": "st",
                      "c": {
                        "a": 1,
                        "k": [
                          {
                            "s": [0.4, 0.5, 0.6, 1.0],
                            "e": [0.4, 0.5, 0.6, 1.0]
                          },
                          {
                            "s": [0.7, 0.8, 0.9, 1.0],
                            "e": [0.4, 0.5, 0.6, 1.0]
                          }
                        ]
                      }
                    }
                  ]
                }
              ]
            }
          ]
        }
    """.trimIndent()

    @Test
    fun `extractColorsFromJson test`() = runTest {
        val colors = extractColorsFromJson(sampleLottieJson)
        assertTrue { colors.isNotEmpty() }
    }

    @Test
    fun `updateColorsInJson should correctly replace a color`() = runTest {
//        val colors = extractColorsFromJson(sampleLottieJson)
//        val jsonElement = Json.encodeToString(Json.parseToJsonElement(sampleLottieJson))
//        val updatedJson = updateColorsInJson(sampleLottieJson, colors, TransformOptions())
//        assertEquals(jsonElement,updatedJson)
    }
}
