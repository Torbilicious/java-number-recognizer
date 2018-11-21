package de.torbilicious

import net.sourceforge.tess4j.Tesseract
import net.sourceforge.tess4j.TesseractException
import java.io.File

private val instance = Tesseract()

fun getTextFromImage(screenshotFile: File): String? {
    //TODO only recognize numbers
//    instance.setTessVariable("tessedit_char_whitelist", "0123456789")
    instance.setDatapath("tessdata")

    return try {
        instance.doOCR(screenshotFile)
    } catch (e: TesseractException) {
        null
    }
}
