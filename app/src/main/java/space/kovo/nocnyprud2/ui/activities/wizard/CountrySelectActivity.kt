package space.kovo.nocnyprud2.ui.activities.wizard

import android.widget.AdapterView
import android.widget.ListView
import space.kovo.nocnyprud2.R

class CountrySelectActivity : WizardActivityBase<ProviderSelectActivity>(
    R.string.country_select_title,
    R.string.country_select_text,
    R.string.country_select_button_next,
    R.layout.wizard_country_select_fragment,
    ProviderSelectActivity::class.java
) {

    override fun onStart() {
        super.onStart()
        this.loadSupportedCountriesToSelectMenu()
    }

    private fun loadSupportedCountriesToSelectMenu() {
        val listView: ListView = findViewById(R.id.wizardCountrySelectListView)
        listView.setAdapter(CountrySelectSelectAdapter(
            this

        ) {
            println("clicked on country $it")
        })
        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            println(position)
        }

    }

    fun test(i: Int): Unit {
        println(i)
    }
}
