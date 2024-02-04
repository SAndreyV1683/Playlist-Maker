package a.sboev.ru.playlistmaker.settings.data.sharing

import a.sboev.ru.playlistmaker.App
import a.sboev.ru.playlistmaker.settings.domain.model.EmailData
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri

class ExternalNavigator {
    fun shareLink(link: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, link)
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        App.INSTANCE.startActivity(intent)
    }
    fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        App.INSTANCE.startActivity(intent)
    }
    fun openEmail(emailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.extraMail))
        intent.putExtra(Intent.EXTRA_SUBJECT,  emailData.extraSubject)
        intent.putExtra(Intent.EXTRA_TEXT, emailData.extraText)
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        App.INSTANCE.startActivity(intent)
    }
}