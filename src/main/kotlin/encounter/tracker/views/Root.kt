package encounter.tracker.views

import tornadofx.View
import tornadofx.button
import tornadofx.label
import tornadofx.vbox

class Root: View() {
    override val root = vbox {
        button("Press me")
        label("Waiting")
    }
}