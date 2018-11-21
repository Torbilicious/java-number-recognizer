package de.torbilicious

import javafx.stage.Stage
import tornadofx.*

class NumberRecognizerApp : App(ScreenDimensionPickerView::class) {
    override fun start(stage: Stage) {
        super.start(stage)

        stage.maxWidth = 1920.0
        stage.maxHeight = 1080.0
    }

}

fun main(args: Array<String>) {
    launch<NumberRecognizerApp>(args)
}