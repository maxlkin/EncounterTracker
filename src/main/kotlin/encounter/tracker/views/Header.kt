package encounter.tracker.views

import com.sun.prism.paint.Color
import tornadofx.*

class Header : View() {
    override val root = hbox() {
        label("Encounter Tracker")

        style {
            backgroundColor += c("#333333")

        }

        children.style {
            textFill = c("white")
        }
    }
}
