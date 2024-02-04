package a.sboev.ru.playlistmaker.settings.domain.model

data class EmailData(
    val extraMail: Array<String>,
    val extraSubject: String,
    val extraText: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EmailData

        if (!extraMail.contentEquals(other.extraMail)) return false
        if (extraSubject != other.extraSubject) return false
        return extraText == other.extraText
    }

    override fun hashCode(): Int {
        var result = extraMail.contentHashCode()
        result = 31 * result + extraSubject.hashCode()
        result = 31 * result + extraText.hashCode()
        return result
    }

}