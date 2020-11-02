package encounter.tracker.views

import javafx.scene.paint.Color
import tornadofx.*

class Style: Stylesheet() {
    companion object {
        val sidebar by cssclass()
    }
    init {
        sidebar {
            backgroundColor += c("#dddddd")
            minWidth = 150.px

            button {
                backgroundColor += c("#dddddd")
                backgroundRadius = multi(box(0.percent))
                prefWidth = 150.px

                and(hover) {
                    backgroundColor += c("#aaaaaa")
                }

                and(pressed) {
                    backgroundColor += c("#888888")
                }
            }
        }
    }
}