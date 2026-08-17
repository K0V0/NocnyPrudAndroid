package space.kovo.nocnyprud2.ui.activities.wizard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import space.kovo.nocnyprud2.R
import space.kovo.nocnyprud2.backend.services.httpService.requestObjectTemplates.cz.cez.CaptchaRejectedException
import space.kovo.nocnyprud2.backend.services.httpService.requestObjectTemplates.cz.cez.CezEanCodeLookup
import space.kovo.nocnyprud2.backend.services.httpService.requestObjectTemplates.cz.cez.NoCodeForEanException

/**
 *  Fallback for users who do not know the HDO code printed on their receiver: resolves it once
 *  from their EAN through the captcha guarded ČEZ portal and hands it back to the setup screen.
 *
 *  Nothing is persisted here - the resolved code is returned as an activity result and it is the
 *  setup screen that stores it, so an abandoned lookup leaves no trace.
 */
class CezEanLookupActivity : AppCompatActivity() {

    companion object {
        const val RESULT_HDO_CODE = "hdo_code"
    }

    private val lookup: CezEanCodeLookup by lazy { CezEanCodeLookup.getInstance() }

    private lateinit var eanField: EditText
    private lateinit var captchaField: EditText
    private lateinit var captchaImage: ImageView
    private lateinit var searchButton: Button
    private lateinit var refreshButton: Button
    private lateinit var message: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wizard_cz_cez_ean_lookup)

        eanField = findViewById(R.id.eanLookupEanField)
        captchaField = findViewById(R.id.eanLookupCaptchaField)
        captchaImage = findViewById(R.id.eanLookupCaptchaImage)
        searchButton = findViewById(R.id.eanLookupSearchButton)
        refreshButton = findViewById(R.id.eanLookupRefreshCaptchaButton)
        message = findViewById(R.id.eanLookupMessage)

        searchButton.setOnClickListener { this.search() }
        refreshButton.setOnClickListener { this.loadCaptcha() }

        this.loadCaptcha()
    }

    private fun loadCaptcha() {
        captchaField.setText("")

        lifecycleScope.launch {
            setBusy(true)
            try {
                val bitmap = withContext(Dispatchers.IO) { lookup.fetchCaptcha() }
                captchaImage.setImageBitmap(bitmap)
                showMessage(null)
            } catch (e: Exception) {
                Logger.e(e, "Could not load captcha")
                showMessage(getString(R.string.cz_cez_ean_lookup_error_captcha_load))
            } finally {
                setBusy(false)
            }
        }
    }

    private fun search() {
        val ean = eanField.text.toString().trim()
        val captcha = captchaField.text.toString().trim()

        if (ean.isEmpty()) {
            showMessage(getString(R.string.cz_cez_ean_lookup_error_empty_ean))
            return
        }
        if (captcha.isEmpty()) {
            showMessage(getString(R.string.cz_cez_ean_lookup_error_empty_captcha))
            return
        }

        lifecycleScope.launch {
            setBusy(true)
            showMessage(getString(R.string.cz_cez_ean_lookup_searching))
            try {
                val code = withContext(Dispatchers.IO) { lookup.resolveCode(ean, captcha) }
                finishWithCode(code)
            } catch (e: CaptchaRejectedException) {
                // a used up challenge is never accepted again, so always offer a fresh one
                showMessage(getString(R.string.cz_cez_ean_lookup_error_captcha))
                loadCaptcha()
            } catch (e: NoCodeForEanException) {
                showMessage(getString(R.string.cz_cez_ean_lookup_error_not_found))
                loadCaptcha()
            } catch (e: Exception) {
                Logger.e(e, "EAN lookup failed")
                showMessage(getString(R.string.cz_cez_ean_lookup_error_general))
                loadCaptcha()
            } finally {
                setBusy(false)
            }
        }
    }

    private fun finishWithCode(code: String) {
        setResult(Activity.RESULT_OK, Intent().putExtra(RESULT_HDO_CODE, code))
        finish()
    }

    private fun setBusy(busy: Boolean) {
        searchButton.isEnabled = !busy
        refreshButton.isEnabled = !busy
    }

    private fun showMessage(text: String?) {
        message.text = text.orEmpty()
        message.visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
    }
}
