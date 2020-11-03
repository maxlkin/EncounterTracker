package encounter.tracker.models

data class CharacterModel(
        val id: Long?,
        val name: String?,
        val armorClass: Long?,
        val initiative: Long?,
        val maxHealth: Long?,
        val currentHealth: Long?
)