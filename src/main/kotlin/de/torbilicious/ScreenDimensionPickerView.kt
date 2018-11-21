package de.torbilicious

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import jfxtras.labs.util.event.MouseControlUtil
import tornadofx.*
import java.awt.Robot
import java.awt.Toolkit
import java.io.File
import javax.imageio.ImageIO
import javafx.scene.shape.Rectangle as JfxRectangle
import java.awt.Rectangle as AwtRectangle

class ScreenDimensionPickerView : View() {
    override val root: BorderPane = BorderPane()
    private var screenShotView: ImageView by singleAssign()

    private val rectangle = javafx.scene.shape.Rectangle()
    private val buttonDisabledProperty = SimpleBooleanProperty(true)

    private val detectedTextProperty = SimpleStringProperty("")

    private val shownImageWidth = 1270.0
    private val shownImageHeigth = 720.0

    init {
        val screenshotFile = captureScreenshot()

        detectedTextProperty.onChange {
            println("New text: $it")
        }

        with(root) {
            top {
                label("Pick a part of your Screen to monitor.")
            }

            center {
                screenShotView = imageview(
                    Image(
                        screenshotFile.inputStream(),
                        shownImageWidth,
                        shownImageHeigth,
                        false,
                        true
                    )
                )
            }

            bottom {
                vbox {
                    button("Pick") {
                        disableProperty().bind(buttonDisabledProperty)

                        action {
                            println("Picked: $rectangle")

                            val screenshotOfSelectedArea = captureScreenshot(
                                rectangle.toAwtRectangle()
                            )

                            println(screenshotOfSelectedArea.canonicalPath)

                            detectedTextProperty.value = getTextFromImage(screenshotOfSelectedArea)
                        }
                    }
                    label("Detected text: ")

                    label(detectedTextProperty)
                }
            }
        }


        rectangle.fill = Color.TRANSPARENT
        rectangle.stroke = Color.DARKGRAY

        MouseControlUtil.addSelectionRectangleGesture(
            root,
            rectangle,
            {},
            {},
            { buttonDisabledProperty.value = false }
        )
    }

    private fun JfxRectangle.toAwtRectangle(): AwtRectangle {
        //FIXME still not the exactly correct ratio
        val originalWidth = Toolkit.getDefaultToolkit().screenSize.width
        val ratio = originalWidth / shownImageWidth

        return AwtRectangle(
            (this.x * ratio).toInt(),
            (this.y * ratio).toInt(),
            (this.width * ratio).toInt(),
            (this.height * ratio).toInt()
        )
    }

    private fun captureScreenshot(rectangle: AwtRectangle = AwtRectangle(Toolkit.getDefaultToolkit().screenSize)): File {
        val image = Robot().createScreenCapture(rectangle)
        val file = createTempFile(suffix = ".png")
        ImageIO.write(image, "png", file)

        return file
    }
}




