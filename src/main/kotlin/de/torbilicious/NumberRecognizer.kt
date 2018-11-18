package de.torbilicious

import net.sourceforge.tess4j.Tesseract
import net.sourceforge.tess4j.TesseractException
import java.awt.MouseInfo
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


fun main(args: Array<String>) {
    val mousePosition = MouseInfo.getPointerInfo().location
    val screenshotFile = captureScreenshot(
        Rectangle(
            mousePosition.x,
            mousePosition.y,
            100,
            100
        )
    )

    println("Captured Screenshot at '${screenshotFile.canonicalPath}'.")


    val instance = Tesseract()

    //TODO only recognize numbers
//    instance.setTessVariable("tessedit_char_whitelist", "0123456789")
    instance.setDatapath("tessdata")

    try {
        val result: String? = instance.doOCR(screenshotFile)
        println(result)
    } catch (e: TesseractException) {
        System.err.println(e.message)
    }
}

fun captureScreenshot(rectangle: Rectangle = Rectangle(Toolkit.getDefaultToolkit().screenSize)): File {
    val image = Robot().createScreenCapture(rectangle)
    val file = createTempFile(suffix = ".png")
    ImageIO.write(image, "png", file)

    return file
}

fun captureScreenshotBI(rectangle: Rectangle = Rectangle(Toolkit.getDefaultToolkit().screenSize)): BufferedImage {
    return Robot().createScreenCapture(rectangle)
}
